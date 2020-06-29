### Native层热修复方式学习，手写一个简易的热修复框架



**目的：**

​		基于AndFix修复原理，手写一个简易的热修复框架。学习这种基于Native层的修复方式原理。



**原理：**

​		AndFix执行的原理是实现方法体的替换（**基于Native层的修复方式**）



**优劣势：**

​		优势是不需要重启app就可以实现bug修复，使用在紧急或比较小的bug轻量级的修复效果好；

​		劣势就是只能针对局部一两个方法做修复，同时兼容性也是个问题，因为每次新的Android版本，谷歌对于虚拟机里的代码会做修改，导致要对不同版本去做兼容。

（学习andfix的意义在于它代表的一种修复思路，和其他的热修复方式都不一样，很有创造性的一种方式）



**方法替换流程：**

AndFix通过Java的自定义注解来判断一个方法是否应该被替换，如果可以就会hook该方法并进行替换。AndFix在ART架构上的Native方法是`art_replaceMethod` 、在X86架构上的Native方法是`dalvik_replaceMethod`。他们的实现方式是不同的。对于Dalvik，它将改变目标方法的类型为`Native`同时hook方法的实现至AndFix自己的`Native`方法，这个方法称为 `dalvik_dispatcher`,这个方法将会唤醒已经注册的回调，这就是我们通常说的`hooked`（挂钩）。对于ART来说，我们仅仅改变目标方法的属性来替代它。



**Native实现**

具体实现见代码

加载修复包，得到里面的修复后的方法，同时根据修复包里获取的待修复的方法名去获取到程序出现bug的方法，将两个方法和sdk版本(为了做兼容)传入native方法中。

利用方法区中方法表里ArtMethod结构体（每个方法都有一个ArtMethod结构体）。 用新方法的去替换旧方法的结构体成员变量来实现修复。其实就是达到，Java层创建该类去调用bug方法时候，链接到方法区时通过方法表将修复过的方法加载到栈中，从而实现修复。

ArtMethod结构体： 参考源码 art/runtime/ 下有art_method.cpp、art_file.cpp、dex_file.h、jni_internal.h、class_linker.cc  找不到再去mirror目录下找

```c++
//ArtMethod  Android6.0（为例） 系统源码中
art::mirror::ArtMethod *wrong = (art::mirror::ArtMethod *) env->FromReflectedMethod(
    wrong_method);
art::mirror::ArtMethod *right = (art::mirror::ArtMethod *) env->FromReflectedMethod(
    right_method);
//method   --->class  --->ClassLoader
wrong->declaring_class_ = right->declaring_class_;
wrong->dex_cache_resolved_methods_ = right->dex_cache_resolved_methods_;

wrong->access_flags_ = right->access_flags_;
wrong->dex_cache_resolved_types_ = right->dex_cache_resolved_types_;
wrong->dex_code_item_offset_ = right->dex_code_item_offset_;
//这里   方法索引的替换
wrong->method_index_ = right->method_index_;
wrong->dex_method_index_ = right->dex_method_index_;
....
```



由于每次Android系统升级，官方对于ArtMethod结构体都有较大的改动，所以每个版本的ArtMethod会有不同，通过对比源码可以发现，

7.0以前 uint32_t method_index_;  ...等等

7.0及以后 uint16_t method_index_;  ...等等

如果用6.0的art_method.h头文件，当运行在8.0的设备时候，因为两者的art_method结构体大小不一样，方法表里会造成内存溢出的，因此要对每个版本都做适配兼容处理。每个版本要对应的art_method.h去适配。

**追及： 同一个方法，不管是空方法还是几千行代码，最终在虚拟机内art_method结构体大小都是一样的。**



**修复包的生成**

修复包的生成，build代码后找到要解决好bug类的class文件  利用Android sdk中dx工具，在终端控制台进行转换得到dex文件（即修复包，不管是什么后缀名，本质上就是个dex文件） （需要配置好环境变量）

