package com.example.zqj.pptcontrol.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.Constant;
import com.example.zqj.pptcontrol.socket.MySocket;
import com.example.zqj.pptcontrol.view.IPEditText;
import com.example.zqj.pptcontrol.zxing.android.CaptureActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.example.zqj.pptcontrol.socket.Constant.IpAddress;


public class MainActivity extends AppCompatActivity {
    //界面
    private IPEditText edittext;
    private Button connect;
    private Button saoyisao;
    private Button bluetootn;
    //Socket
    private DatagramSocket socket = null;
    private byte cmdBuffer[] = new byte[Constant.bufferSize];
    //接受数组定义
    private byte recvBuffer[] = new byte[Constant.bufferSize];
    //网络界面延时线程
    private  NetworkThread networkthread=new NetworkThread();
    private boolean show;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //绑定
        edittext = (IPEditText) findViewById(R.id.iptext);
        connect = (Button) findViewById(R.id.button);
        saoyisao=(Button)findViewById(R.id.saoyisao);
        bluetootn=(Button)findViewById(R.id.bluetooth);
        bluetootn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlueToothConnectActivity.class);
                startActivity(intent);

            }
        });
        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saoyisao();
            }
        });

        //监听
        connect.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            con();
        }
    });
}

    private void saoyisao() {
        Intent intent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(MainActivity.this);
                    normalDialog.setTitle("连接失败！");
                    normalDialog.setMessage("请确保网络连通以及输入正确的IP");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    onRestart();
                                }
                            });
                    // 显示
                    normalDialog.show();
                    super.handleMessage(msg);
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"正在连接IP"+Constant.ConIp,Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    MainActivity.this.finish();
                    break;
            }
        }
    };

    public void con() {
        //获得输入的IP
        if (edittext.getText()==null&&Constant.ConIp==null){
            AlertDialog.Builder NullDialog =
                    new AlertDialog.Builder(MainActivity.this);
            NullDialog.setTitle("Warnning！");
            NullDialog.setMessage("请输入正确的IP");
            NullDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                            onRestart();
                        }
                    });
            // 显示
            NullDialog.show();
        }else {
            if (Constant.ConIp==null){Constant.ConIp = edittext.getText().toString();}
            Log.i("tag", "得到的IP：" + Constant.ConIp);
            try {
                IpAddress = InetAddress.getByName(Constant.ConIp);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            //获得socket对象
            MySocket myApplication = (MySocket) MainActivity.this.getApplication();
            socket = myApplication.init();

            //进行连接
            for (int i = 0; i < 4; i++) cmdBuffer[i] = 0;
            cmdBuffer[0] = Constant.RULE_CONDITION;
            cmdBuffer[1] = Constant.CONDITION_CONNECT;
            Log.i("tag", "调用sendCMD() ");
            myApplication.sendCMD(cmdBuffer);
            //feedback();
            MyThread thread = new MyThread();
            thread.start();
            show=true;
            networkthread.start();
        }//else if
    }

    class  NetworkThread extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(2999);
                Log.i("tag", "again!!");
                if (show){
                    mHandler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 接受反馈信息
     */
   class MyThread extends Thread{
        @Override
        public void run() {
            DatagramPacket rdp = new DatagramPacket(recvBuffer, recvBuffer.length);
            while(recvBuffer[0] != Constant.RULE_FEEDBACK) {
                try {
                    mHandler.sendEmptyMessage(1);
                    Log.i("tag", "正在接受");
                    socket.receive(rdp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("tag", "找到feedback");
            show=false;
//
//            SharedPreferences preferences= getSharedPreferences("connectMode", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor=preferences.edit();
//            editor.putString("connectMode", "NetWork");
//            editor.commit();
            Intent intent = new Intent(MainActivity.this, MouseActivity.class);
            startActivity(intent);

            //发送Handle 关闭本界面
           // mHandler.sendEmptyMessage(2);
        }
    }


    /*
    //监听服务器端反馈信息
    //socket初始化类
     class StartSocket implements Runnable{
        private byte recvBuffer[] = new byte[Constant.bufferSize];
        //打开socket以侦听数据
        @Override
        public void run() {
                Log.i("tag", "监听打开");
                while (!socket.isClosed()) {
                    Log.i("tag", "正在进行监听");
                    for (int i=0;i<Constant.bufferSize;i++){recvBuffer[i]=0;}
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatagramPacket rdp = new DatagramPacket(recvBuffer, recvBuffer.length);
                            try {
                                Log.i("tag", "进入线程");
                                socket.receive(rdp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    if (recvBuffer[0]==Constant.RULE_FEEDBACK) {
                        Log.i("tag", "feedback!!!");
                        break;
                    }
                }

        }

    }
*/
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult =data.getStringExtra("codedContent");
            if(IPCheck(scanResult)){
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("扫描成功！");
                normalDialog.setMessage("目标IP: "+ scanResult);
                normalDialog.setPositiveButton("连接",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                                Constant.ConIp=scanResult;
                                con();
                            }
                        });
                normalDialog.setNegativeButton("取消",null);
                // 显示
                normalDialog.show();
            }else{
                //不合法
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("扫描成功！");
                normalDialog.setMessage("IP不合法： "+scanResult);
                normalDialog.setPositiveButton("确定",null);
                // 显示
                normalDialog.show();
            }

        }
    }
    public static boolean IPCheck(String ip){
        String Ip = ip.replaceAll(" ","");
        if(Ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
            String st[]= Ip.split("\\.");
            if(Integer.parseInt(st[0])<250)
                if(Integer.parseInt(st[1])<250)
                    if(Integer.parseInt(st[2])<250)
                        if(Integer.parseInt(st[3])<250)
                            return true;
        }
        return false;
    }
}