
#指定cmake最小支持版本
cmake_minimum_required(VERSION 3.4.1)

#添加一个库，根据native-lib.cpp源文件编译一个native-lib的动态库  native-lib.cpp
add_library( 
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

target_link_libraries(
        native-lib
        log)

