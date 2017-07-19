package com.example.zqj.pptcontrol.activity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.MySocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 显示蓝牙界面
 * Created by zqj on 2017/5/2.
 */

public class BlueToothConnectActivity extends ListActivity {
    // 获取手机默认上的蓝牙适配器
    private BluetoothAdapter blueToothAdapter = BluetoothAdapter.getDefaultAdapter();
    // 用于存放搜索到的蓝牙设备
    private List<BluetoothDevice> _devices = new ArrayList<BluetoothDevice>();
    // 把蓝牙设备信息存在list中并显示出来
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private BluetoothSocket bluetoothSocket=null;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect);
        context=this;
        // 在listview中展示蓝牙设备
        showDevide();
        //switch开关
        Switch switchbutton=(Switch)findViewById(R.id.switchbutton);
        if ( blueToothAdapter.enable()){
            switchbutton.setChecked(true);
        }else{
            switchbutton.setChecked(false);
        }

        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    blueToothAdapter.enable();
                    //打开蓝牙
                } else {
                    blueToothAdapter.disable();
                    // 关闭蓝牙
                }
            }
        });

    }
    public void showDevide(){
        // 获取所有已配对的蓝牙设备
        Set<BluetoothDevice> devices = blueToothAdapter.getBondedDevices();
        //设备存在
        if (devices.size() > 0) {
            Iterator<BluetoothDevice> it = devices.iterator();
            BluetoothDevice bluetoothDevice = null;
            HashMap<String, String> map = new HashMap<String, String>();
            while (it.hasNext()) {
                bluetoothDevice = it.next();
                // 把每一个获取到的蓝牙设备的名称和地址存放到HashMap数组中，比如：xx:xx:xx:xx:xx: royal
                map.put("address", bluetoothDevice.getAddress());
                map.put("name", bluetoothDevice.getName());
                // 该list用于存放呈现的蓝牙设备，存放的是每个设备的map
                list.add(map);
                // 该list用于存放的是真正的每一个蓝牙设备对象
                _devices.add(bluetoothDevice);
            }

            // 构造一个简单的自定义布局风格
            SimpleAdapter listAdapter = new SimpleAdapter(this, list,
                    R.layout.device, new String[] { "address", "name" },
                    new int[] { R.id.address, R.id.name });
            this.setListAdapter(listAdapter);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("tag", "onListItemClick: ");
        //根据ListView按的位置得到地址
        String address = _devices.get(position).getAddress();
        //此处连接蓝牙
        //获得socket对象
          MySocket myApplication = (MySocket) BlueToothConnectActivity.this.getApplication();

          myApplication.initSocket(address,context);
//          if (myApplication.initSocket(address)){
//              Intent intent = new Intent(BlueToothConnectActivity.this, MouseActivity.class);
//              startActivity(intent);
//          }else{
//              Toast.makeText(this,"创建失败，请关闭重试,",Toast.LENGTH_LONG).show();
//          }

//        SharedPreferences preferences= getSharedPreferences("connectMode", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=preferences.edit();
//        editor.putString("connectMode", "BlueTooth");
//
//        editor.commit();

    }

  public static class  MyReceiver extends BroadcastReceiver{

     public MyReceiver(){

     }
      @Override
      public void onReceive(Context context, Intent intent) {
          String msg=intent.getStringExtra("msg");
          System.out.println(intent.getAction());
          Log.i("tag", "接收到广播");
          System.out.println(msg);
         if (msg.equals("fail")){
             Toast.makeText(context,"创建失败，请关闭重试,",Toast.LENGTH_LONG).show();
         }
         else if (msg.equals("success")){
             Intent it = new Intent(context, MouseActivity.class);
             it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(it);
         }

         // Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
      }
  }
}
