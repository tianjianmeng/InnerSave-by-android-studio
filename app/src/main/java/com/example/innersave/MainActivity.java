package com.example.innersave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    static final String MyFileName = "myinnerSave";
    static final String TAG = "innerSave";
    private Button write;
    private Button read;
    private EditText xuehao;
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkNeedPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        write = findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream out = null;
                xuehao = findViewById(R.id.xuehao);
                name = findViewById(R.id.name);
                String xue = xuehao.getText().toString();
                String na = name.getText().toString();
                String save = "id:" + xue + "name:" + na;
                Log.v(TAG,save);
                try{
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        File file = Environment.getExternalStorageDirectory();
                        File myfile = new File(file.getCanonicalPath() + "/" + MyFileName);

                        FileOutputStream fileOutputStream = new FileOutputStream(myfile);
                        out = new BufferedOutputStream(fileOutputStream);
                        try{
                            out.write(save.getBytes(StandardCharsets.UTF_8));
                            Log.v(TAG,"写入成功");
                        }finally {
                            if(out != null){

                                out.close();
                            }
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v(TAG,"环境错误");
                }
            }
        });
        read = findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputStream in = null;
                try{
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        File file = Environment.getExternalStorageDirectory();
                        File myfile = new File(file.getCanonicalPath() + "/" +MyFileName);
                        FileInputStream fileInputStream = new FileInputStream(myfile);
                        in = new BufferedInputStream(fileInputStream);
                        int c;
                        StringBuilder sb = new StringBuilder("");
                        try{
                            while ((c = in.read()) != -1){
                                sb.append((char)c);
                            }
                            Toast.makeText(MainActivity.this,sb.toString(),Toast.LENGTH_LONG).show();

                        }finally {
                            if(in != null){
                                in.close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void checkNeedPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //多个权限一起申请
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }
}