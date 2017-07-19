package com.example.zqj.pptcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.Constant;
import com.example.zqj.pptcontrol.socket.MySocket;
import com.example.zqj.pptcontrol.view.ByteAndInt;

import java.net.DatagramSocket;

public class MouseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private byte cmdBuffer[] = new byte[Constant.bufferSize];
    private ImageButton left = null;
    private ImageButton right = null;
    private Button whell_up;
    private Button whell_down;
    //socket
    private DatagramSocket socket = null;
    private MySocket myApplication=null;
    //滑动
    private GestureDetector mGestureDetector = null;
    //滚轮
    private boolean up_flag=false;
    private boolean down_flag=false;
    WhellThread whellThread=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        //获得socket对象
        myApplication = (MySocket) MouseActivity.this.getApplication();
        //构造方法new GestureDetector(GestureListener listener);
        mGestureDetector = new GestureDetector(this);
        //绑定
        left=(ImageButton)findViewById(R.id.left);
        right=(ImageButton)findViewById(R.id.right);
        whell_up=(Button)findViewById(R.id.whell_up);
        whell_down=(Button)findViewById(R.id.whell_down);

        whellThread=new WhellThread();
        whellThread.start();

        //监听函数
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
                cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
                cmdBuffer[1]=Constant.MOUSE_LEFT;
                myApplication.sendCMD(cmdBuffer);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
                cmdBuffer[1]=Constant.MOUSE_RIGHT;
                myApplication.sendCMD(cmdBuffer);
            }
        });
        //滚轮由点击事件变为按下可以一直滑动
//        whell_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
//                cmdBuffer[1]=Constant.MOUSE_WHELL_UP;
//                myApplication.sendCMD(cmdBuffer);
//            }
//        });
//        whell_down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
//                cmdBuffer[1]=Constant.MOUSE_WHELL_DOWN;
//                myApplication.sendCMD(cmdBuffer);
//            }
//        });
        whell_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    up_flag=false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                   up_flag=true;
                }
                return true;
            }
        });
        whell_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    down_flag=false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    down_flag=true;
                }
                return true;
            }
        });

    }

    class WhellThread extends Thread{
        @Override
        public void run () {
            while (true) {
                try {
                    Thread.sleep(100);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(up_flag){
                        cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
                        cmdBuffer[1]=Constant.MOUSE_WHELL_UP;
                        myApplication.sendCMD(cmdBuffer);
                    }else if (down_flag){
                        cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
                        cmdBuffer[1]=Constant.MOUSE_WHELL_DOWN;
                        myApplication.sendCMD(cmdBuffer);
                    }
                    break;
            }
        }
    };
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
                Intent it=new Intent(MouseActivity.this,MainActivity.class);
                startActivity(it);
                break;
            case R.id.id_2:
                //PPT事件
                Toast.makeText(this,"前往PPT控制界面",Toast.LENGTH_LONG).show();
                Intent it2=new Intent(MouseActivity.this,PPTActivity.class);
                startActivity(it2);
                break;
            case R.id.id_3:
                //media
                Toast.makeText(this,"前往MediaPlayer界面",Toast.LENGTH_LONG).show();
                Intent it3=new Intent(MouseActivity.this,MediaActivity.class);
                startActivity(it3);
                break;
            case R.id.id_4:
                Toast.makeText(this,"已经在鼠标控制界面界面了",Toast.LENGTH_LONG).show();
                break;
            case R.id.id_5:
                Toast.makeText(this,"前往M电脑控制界面",Toast.LENGTH_LONG).show();
                Intent it4=new Intent(MouseActivity.this,ScreenShow.class);
                startActivity(it4);
                break;

        }
        return true;
    }


    //下面是侦测屏幕滑动事件
    /**
     * 我们调用GestureDetector的onTouchEvent()方法，将捕捉到的滑动事件交给GestureDetector进行分析
     * 是否有合适的callback函数来处理用户的手势
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 接口函数
     * 触发条件：用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
     */
    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("tag", "onDown: ");
        return false;
    }

    /**
     * 接口函数
     * 触发条件：用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
     * 和onDown()的区别：强调的是没有松开或者拖动的状态
     */
    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("tag", "onShowPress: ");
    }

    /**
     * 接口函数
     * 触发条件： 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //轻触，发送鼠标点击指令
        cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
        cmdBuffer[1]=Constant.MOUSE_LEFT;
        myApplication.sendCMD(cmdBuffer);
        Log.i("tag","onSingleTapUp: ");

        return false;
    }

    /**
     * 接口函数
     * 触发条件：用户按下触摸屏，并拖动，由1个MotionEvent.ACTION_DOWN, 多个MotionEvent.ACTION_MOVE触发
     * 参数解释：downEvent为MotionEvent.ACTION_DOWN触发的事件
     * movEvent为MotionEvent.ACTION_MOVE触发的事件
     * distance_X表示在X方向上滑动的距离,该值由上一个坐标值减去当前坐标值p1.x-p2.x,因此当正向滑动时为负值
     * distance_Y表示在Y方向上滑动的距离,该值由上一个坐标值减去当前坐标值p1.y-p2.y,因此当正向滑动时为负值
     * 因我们需要当移动端正向滑动时电脑端鼠标也跟着正向移动，因此所要把这两个值反一下
     */
    @Override
    public boolean onScroll(MotionEvent downEvent, MotionEvent movEvent, float distance_X,float distance_Y) {
        Log.i("tag", "onScroll: ");

        //把本次移动的距离发送到服务端以此来控制服务端鼠标的移动
        try {
            cmdBuffer[0]=Constant.RULE_MOUSE_EVENT;
            cmdBuffer[1]=Constant.MOUSE_GESTURE;
            byte xValue[]= ByteAndInt.int2ByteArray((int)(-distance_X));//把滑动距离反一下，这样当正向滑动时电脑端也为正向移动
            byte yValue[]=ByteAndInt.int2ByteArray((int)(-distance_Y));
            System.arraycopy(xValue, 0, cmdBuffer, 4, 4);
            System.arraycopy(yValue, 0, cmdBuffer, 8, 4);
            myApplication.sendCMD(cmdBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(downEvent);
    }

    /**
     * 接口函数
     * 触发条件：用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
     */
    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("tag", "onLongPress: ");
    }

    /**
     * 接口函数
     * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
     * 触发条件：X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒，时会触发该事件
     * 参数解释：
     * downEvent当屏幕按下时触发的事件,由MotionEvent.ACTION_DOWN提供
     * upEvent当滑动停止时触发的事件，由滑动的最后一个MotionEvent.ACTION_MOVE提供，即弹起
     * xVelocity: X轴上的移动速度，像素/秒
     * yVelocity: Y轴上的移动速度，像素/秒
     */
    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent upEvent, float xVelocity,float yVelocity) {
        if((upEvent.getX()-downEvent.getX())>0){//向左翻页
            cmdBuffer[0]= Constant.RULE_KEY_EVENT;
            cmdBuffer[1]=Constant.KEY_ARROW_LEFT;
            myApplication.sendCMD(cmdBuffer);
        }else{//向右翻页
            cmdBuffer[0]=Constant.RULE_KEY_EVENT;
            cmdBuffer[1]=Constant.KEY_ARROW_RIGHT;
            myApplication.sendCMD(cmdBuffer);
        }
        Log.i("tag", "onFling: ");
        return false;
    }
}
