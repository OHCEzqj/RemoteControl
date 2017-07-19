package com.example.zqj.pptcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.Constant;
import com.example.zqj.pptcontrol.socket.MySocket;

public class PPTActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button stop;
    private Button previous;
    private Button next;
    //socket
    private MySocket myApplication=null;
    private byte cmdBuffer[] = new byte[Constant.bufferSize];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt);
        myApplication = (MySocket) PPTActivity.this.getApplication();
        //绑定
        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(this);
        stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        previous=(Button)findViewById(R.id.previous);
        previous.setOnClickListener(this);
        next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        boolean opr = false;
        switch (view.getId()){
            case R.id.start:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_F5;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.stop:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_ESC;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.previous:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_ARROW_UP;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.next:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_ARROW_DOWN;
                myApplication.sendCMD(cmdBuffer);
                break;
        }
    }
    //音量控制
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_ARROW_DOWN;
                myApplication.sendCMD(cmdBuffer);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cmdBuffer[0]=Constant.RULE_KEY_EVENT;
                cmdBuffer[1]=Constant.KEY_ARROW_UP;
                myApplication.sendCMD(cmdBuffer);
                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                return true;
        }
        return super.onKeyDown(keyCode, event);
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
                Intent it=new Intent(PPTActivity.this,MainActivity.class);
                startActivity(it);
                break;
            case R.id.id_2:
                //PPT事件
                Toast.makeText(this,"已经在PPT界面了",Toast.LENGTH_LONG).show();
                break;
            case R.id.id_3:
                //Media
                Toast.makeText(this,"前往MediaPlayer界面",Toast.LENGTH_LONG).show();
                Intent it2=new Intent(PPTActivity.this,MediaActivity.class);
                startActivity(it2);
                break;
            case R.id.id_4:
                Toast.makeText(this,"前往鼠标控制界面",Toast.LENGTH_LONG).show();
                Intent it3=new Intent(PPTActivity.this,MouseActivity.class);
                startActivity(it3);
                break;

        }
        return true;
    }

}
