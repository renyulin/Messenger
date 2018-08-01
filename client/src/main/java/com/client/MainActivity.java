package com.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final int MSG_SAY_HELLO = 1;
    Messenger mService = null;
    boolean mBound;
    private LinearLayout mainLL;
    private static final int MSG_SUM = 0x110;
    private int mA;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //接收onBind()传回来的IBinder，并用它构造Messenger
            mService = new Messenger(service);
            mBound = true;
            TextView btn1 = findViewById(R.id.btn1);
            btn1.setText("connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
            TextView btn1 = findViewById(R.id.btn1);
            btn1.setText("disconnected");
        }
    };
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromServer) {
            switch (msgFromServer.what) {
                case MSG_SUM:
                    TextView tv = (TextView) mainLL.findViewById(msgFromServer.arg1);
                    tv.setText(tv.getText() + "=>" + msgFromServer.arg2);
                    break;
            }
            super.handleMessage(msgFromServer);
        }
    });

    //调用此方法时会发送信息给服务端
    public void sayHello(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (!mBound) return;
                //发送一条信息给服务端
                Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn2:
                try {
                    int a = mA++;
                    int b = (int) (Math.random() * 100);

                    //创建一个tv,添加到LinearLayout中
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(a + " + " + b + " = caculating ...");
                    tv.setId(a);
                    mainLL.addView(tv);
                    Message msgFromClient = Message.obtain(null, MSG_SUM, a, b);
                    msgFromClient.replyTo = mMessenger;
                    if (mBound) {
                        //往服务端发送消息
                        mService.send(msgFromClient);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLL = findViewById(R.id.mainLL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //绑定服务端的服务，此处的action是service在Manifests文件里面声明的
        Intent intent = new Intent();
        intent.setAction("com.largeimg.messenger");
        //不要忘记了包名，不写会报错
        intent.setPackage("com.largeimg");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
