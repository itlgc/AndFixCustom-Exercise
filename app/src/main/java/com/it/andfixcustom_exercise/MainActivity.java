package com.it.andfixcustom_exercise;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import com.it.andfixcustom_exercise.web.CopyUtils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 仿写 andfix热修复
 */
public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }
    TextView textView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result);
        //模拟下载修复包，将修复包从assets拷入手机内部存储内
        file= CopyUtils.copyAssetsAndWrite(getApplicationContext(), "fix.dex");
    }


    /**
     * 调用异常方法
     * @param view
     */
    public void caculator(View view) {
        Caclutor caclutor = new Caclutor();
        textView.setText("  "+caclutor.caculator());
        Caclutor caclutor1 = new Caclutor();
        textView.setText("  "+caclutor1.caculator());
        Caclutor caclutor2 = new Caclutor();

        textView.setText("  "+caclutor2.caculator());
    }

    //模拟修复
    public void fix(View view) {
//        File file = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        DexManager dexManager = new DexManager(this);
        dexManager.load(file);
    }
}
