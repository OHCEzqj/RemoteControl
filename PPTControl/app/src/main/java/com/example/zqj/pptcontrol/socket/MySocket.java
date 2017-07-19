package com.example.zqj.pptcontrol.socket;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

/**
 * 三个函数：
 * 初始函数：创建Socket以及BlueToothSocket两个函数，二选一
 * 发送函数：根据connectMode，发送数据
 * Created by zqj on 2017/4/18.
 */

public class MySocket extends Application {
    //连接方式
    private String connectMode=null;
    boolean flagblue;

    //是否发送成功
    private  boolean flag;
    private DatagramSocket socket;
    private DatagramPacket pack;

    public DatagramSocket init(){

        try {
            socket=new DatagramSocket(Constant.Port);
            connectMode="NetWork";
            Log.i("tag", "socket成功创建 ");
        } catch (SocketException e) {
            e.printStackTrace();
            Log.i("tag", "socket创建失败 ");
        }
        return socket;
    }



    //蓝牙
    // 建立蓝牙通信的UUID
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 自带的蓝牙适配器
    private BluetoothAdapter bluetoothAdapter = null;
    // 扫描得到的蓝牙设备
    private BluetoothDevice device = null;
    // 蓝牙通信socket，客户端使用单独的BlueToothSocket类去初始化一个外界连接和管理该链接
    private BluetoothSocket bluetoothSocket = null;
    // 手机输出流
    private OutputStream outStream = null;


    public boolean initSocket(String address, final Context context){
        // 获取手机默认上的蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 根据蓝牙设备地址得到该蓝牙设备对象
        device = bluetoothAdapter.getRemoteDevice(address);
        /**
         * 创建一个BlurToothSocket去连接一个设备，
         * 使用BluetoothDevice.createRfcommSocketToServiceRecord()方法
         */
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Log.i("tag", "准备创建btSocket");
                    //根据UUID创建通信套接字
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                    //因为connect()方法是阻塞调用，这个连接过程始终应该在独立与主Activity线程之外的线程中被执行。
                    bluetoothSocket.connect();
                    connectMode="BlueTooth";
                    flagblue=true;
                    Log.i("tag", "创建btSocket成功 ");
                    //Toast.makeText(context,"创建成功，正在进入,",Toast.LENGTH_LONG).show();
                    //广播
                    Intent intent=new Intent();
//                  action需要与注册广播的action一样
                    intent.setAction("MY_ACTION");
//                  传递的消息
                    intent.putExtra("msg","success");
//                  发送
                    sendBroadcast(intent);
                    Log.i("tag", "准备发送广播 ");
                } catch (Exception e) {
                    Intent intent=new Intent();
//                  action需要与注册广播的action一样
                    intent.setAction("MY_ACTION");
//                  传递的消息
                    intent.putExtra("msg","fail");
//                  发送
                    sendBroadcast(intent);
                    Log.i("tag", "创建btSocket失败："+e);
                    flagblue=false;
                }
            }
        }).start();
        return flagblue;
    }
    public  boolean sendBluetooth(final byte[] cmdBuffer) {
        Log.i("tag", "即将发送蓝牙数据包 ");

        //创建多线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //网络发送数据
                try {
                    outStream = bluetoothSocket.getOutputStream();
                    outStream.write(cmdBuffer);
                    Log.i("tag", "数据包发送成功 ");
                    flag=true;
                } catch (IOException e) {
                    Log.i("tag", "数据包发送失败 ");
                    e.printStackTrace();
                    flag=false;
                }
            }
        }).start();
        return flag;
    }
    public  boolean sendCMD(final byte[] cmdBuffer) {
//        SharedPreferences sharedPreferences= getSharedPreferences("connectMode", Context.MODE_PRIVATE);
//        // 使用getString方法获得value，注意第2个参数是value的默认值
//        connectMode =sharedPreferences.getString("connectMode", "");
        if (connectMode.equals("NetWork")) {
            pack = new DatagramPacket(cmdBuffer, Constant.bufferSize, Constant.IpAddress, Constant.Port);
            Log.i("tag", "即将发送Socket数据包 ");

            //创建多线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //网络发送数据
                    try {
                        socket.send(pack);
                        Log.i("tag", "数据包发送成功 ");
                        flag = true;
                    } catch (IOException e) {
                        Log.i("tag", "数据包发送失败 ");
                        e.printStackTrace();
                        flag = false;
                    }
                }
            }).start();

        }//if
        else if (connectMode.equals("BlueTooth")){
            Log.i("tag", "即将发送蓝牙数据包 ");

            //创建多线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //网络发送数据
                    try {
                        outStream = bluetoothSocket.getOutputStream();
                        outStream.write(cmdBuffer);
                        Log.i("tag", "数据包发送成功 ");
                        flag=true;
                    } catch (IOException e) {
                        Log.i("tag", "数据包发送失败 ");
                        e.printStackTrace();
                        flag=false;
                    }
                }
            }).start();
        }
        return flag;
    }
}
