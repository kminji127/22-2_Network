package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "192.168.35.15";
    private int port = 8080;

    String username;
    Button exitbutton;
    Button chatbutton;
    EditText message;
    ScrollView scroll;
    LinearLayout container;
    String sendmsg;
    String read;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        message = (EditText) findViewById(R.id.message);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        exitbutton = (Button) findViewById(R.id.exitbutton);
        chatbutton = (Button) findViewById(R.id.chatbutton);
        scroll = (ScrollView) findViewById(R.id.scroll);
        container = (LinearLayout) findViewById(R.id.container);

        new Thread() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    sendWriter.println("[ " + username + " 님이 입장했습니다 ]");
                    sendWriter.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(true){
                        read = input.readLine();

                        System.out.println("TTTTTTTT"+read);
                        if(read!=null) {
                            mHandler.post(new msgUpdate(read));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sendWriter.println("[ " + username + " 님이 퇴장했습니다 ]");
                            sendWriter.flush();
                            sendWriter.close();
                            Intent intent = new Intent(getApplicationContext(), EnterActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg = message.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        scroll.fullScroll(View.FOCUS_DOWN);
                        try {
                            sendWriter.println("[" + username + "] " + sendmsg);
                            sendWriter.flush();
                            message.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    class msgUpdate implements Runnable{
        private String msg;
        public msgUpdate(String str) {this.msg=str;}

        @Override
        public void run() {
            scroll.fullScroll(View.FOCUS_DOWN);
            TextView tv = new TextView(MainActivity.this);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if(msg.startsWith("["+username+"]")){
                tv.setTextColor(Color.parseColor("#7C481F"));
            }else{
                tv.setTextColor(Color.BLACK);
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setPadding(20, 10, 10, 10);
            tv.setText(msg);
            container.addView(tv);
        }
    }
}