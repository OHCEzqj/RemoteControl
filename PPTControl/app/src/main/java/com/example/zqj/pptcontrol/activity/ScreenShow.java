package com.example.zqj.pptcontrol.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.Constant;
import com.example.zqj.pptcontrol.socket.MySocket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by zqj on 2017/5/18.
 */

public class ScreenShow extends AppCompatActivity {
    private byte cmdBuffer[] = new byte[Constant.bufferSize];
    DatagramSocket datagramSocket = null;
    ImageView imageView;
    byte[] buf = new byte[2048];
    ByteArrayOutputStream baos;
    private MySocket myApplication=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//        //设置成全屏模式
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        setContentView(R.layout.screenshow);
        baos = new ByteArrayOutputStream();
        //获得socket对象
        myApplication = (MySocket) ScreenShow.this.getApplication();
        try {
            datagramSocket = new DatagramSocket(8776);
            Log.i("tag", "Socket创建成功: ");
        } catch (SocketException e) {
            Log.i("tag", "Socket创建失败: ");
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.imageView);


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_init;
                myApplication.sendCMD(cmdBuffer);
                //show();
                Thread  t = new Thread(new MyThread());
                    t.start();


            }
        });

        //win

        Button win = (Button) findViewById(R.id.win);
        win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_win;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //shutdown

        Button shutdown = (Button) findViewById(R.id.shutdown);
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_shutdown;
                myApplication.sendCMD(cmdBuffer);




            }
        });
        //cmd

        Button cmd = (Button) findViewById(R.id.cmd);
        cmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_cmd;
                myApplication.sendCMD(cmdBuffer);




            }
        });
        //ctrlalt

        Button ctrlalt = (Button) findViewById(R.id.ctrlalt);
        ctrlalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_ctrlalt;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //logoff

        final Button logoff = (Button) findViewById(R.id.logoff);
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_logoff;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //explorer

        Button explorer = (Button) findViewById(R.id.explorer);
        explorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_explorer;
                myApplication.sendCMD(cmdBuffer);

                Log.i("tag", "explorer: onClick");


            }
        });
        //nptepad

        Button nptepad = (Button) findViewById(R.id.nptepad);
        nptepad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_nptepad;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //mspaint

        Button mspaint = (Button) findViewById(R.id.mspaint);
        mspaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_mspaint;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //taskmgr

        Button taskmgr = (Button) findViewById(R.id.taskmgr);
        taskmgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_taskmgr;
                myApplication.sendCMD(cmdBuffer);



            }
        });
        //Nslookup

        Button Nslookup = (Button) findViewById(R.id.Nslookup);
        Nslookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]= Constant.RULE_SCreen;
                cmdBuffer[1]=Constant.Screen_Nslookup;
                myApplication.sendCMD(cmdBuffer);



            }
        });

    }


    class MyThread implements Runnable {

        @Override
        public void run() {

            Log.i("tag", "run: ");
            while (true) {
                DatagramPacket datagramPacketn = new DatagramPacket(buf, buf.length);


                Log.i("tag", "即将receive: ");

                DataOutputStream out = null;
                //  InputStream in=new DataInputStream();

                while (true) {

                    //3,使用socket对象的receive方法将接收到的数据都存储到数据包的对象中。
                    try {

                        datagramSocket.receive(datagramPacketn);//阻塞式方法。
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //获取数据内容。
                    byte[] data = datagramPacketn.getData();

                    baos.write(datagramPacketn.getData(), 0, datagramPacketn.getLength());
                    if (data.length > datagramPacketn.getLength()) {//结束条件，当数据的长度小于接受数组的长度时，说明已是最后一次，故结束循环
                        break;
                    }
                }//while
                Log.i("tag", "receive: Success");


                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;//inJustDecodeBounds 需要设置为false，如果设置为true，那么将返回null

                // final Bitmap bitmap = BitmapFactory.decodeStream(in);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
                // final Bitmap bitmap = BitmapFactory.decodeStream(in);
                baos.reset();
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //菜单设置
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //根据不同的id点击不同按钮控制activity需要做的事件
        switch (item.getItemId())
        {
            case R.id. id_1:
                Toast.makeText(this,"返回连接界面",Toast.LENGTH_LONG).show();
                Intent it=new Intent(ScreenShow.this,MainActivity.class);
                startActivity(it);
                break;
            case R.id.id_2:
                //PPT事件
                Toast.makeText(this,"前往PPT控制界面",Toast.LENGTH_LONG).show();
                Intent it2=new Intent(ScreenShow.this,PPTActivity.class);
                startActivity(it2);
                break;
            case R.id.id_3:
                //media
                Toast.makeText(this,"前往MediaPlayer界面",Toast.LENGTH_LONG).show();
                Intent it3=new Intent(ScreenShow.this,MediaActivity.class);
                startActivity(it3);
                break;
            case R.id.id_4:
                Toast.makeText(this,"前往鼠标控制界面",Toast.LENGTH_LONG).show();
                Intent it4=new Intent(ScreenShow.this,MouseActivity.class);
                startActivity(it4);

                break;
            case R.id.id_5:
                Toast.makeText(this,"已经在鼠标控制界面界面了",Toast.LENGTH_LONG).show();
                break;

        }
        return true;
    }
}