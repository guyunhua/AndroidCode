package com.example.bx.show;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText username, password;
    Button register;
    TextView tv;

    ProgressDialog dialog;

    String info;

    private static Handler handle = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.username);
        register = (Button)findViewById(R.id.register);
        tv = (TextView)findViewById(R.id.serverReturn);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register: {
                if (!checkNetwork()) {
                    Toast toast = Toast.makeText(Register.this, "Net unconnected", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }

                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("正在登陆，请稍后。。。");
                dialog.setCancelable(false);
                dialog.show();

                new Thread(new MyThread()).start();
                break;
            }
        }
    }

    public  class  MyThread implements Runnable{
        public  void  run(){

            //info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
            info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
            handle.post(new Runnable() {
                @Override
                public void run() {

                    tv.setText(info);
                    dialog.dismiss();
                    if(info.contains("Success")){
                        Register.this.finish();

                    }
                }
            });
        }
    }

    private  boolean checkNetwork(){
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null){
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
