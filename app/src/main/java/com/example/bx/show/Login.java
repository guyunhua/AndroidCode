package com.example.bx.show;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity  implements View.OnClickListener {

    private Button loginbtn, registerBtn;
    private TextView infotv, regtv;

    EditText username, password;

    private ProgressDialog dialog;

    private String info;

    private static Handler handle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.username);
        loginbtn = (Button)findViewById(R.id.login);
        infotv = (TextView) findViewById(R.id.infotv);
        registerBtn = (Button)findViewById(R.id.torigister);


        loginbtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    public  void onClick(View v){
        switch (v.getId()){
            case R.id.login:
            {
                if (!checkNetwork()){
                    Toast toast = Toast.makeText(Login.this, "Net unconnected", Toast.LENGTH_SHORT);
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
            case R.id.torigister:
            {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
            }
        }
    }

    public  class  MyThread implements Runnable{
        public  void  run(){

            info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
            handle.post(new Runnable() {
                @Override
                public void run() {
                    infotv.setText(info);
                    dialog.dismiss();

                    if (info.contains("Success")){
                        Intent intent = new Intent(Login.this, Hello.class);
                        startActivity(intent);
                        Login.this.finish();
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
