package com.example.zqj.pptcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zqj.pptcontrol.R;
import com.example.zqj.pptcontrol.socket.Constant;
import com.example.zqj.pptcontrol.socket.MySocket;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener{

    //socket
    private MySocket myApplication=null;
    private byte cmdBuffer[] = new byte[Constant.bufferSize];

    private ImageButton volume_up;
    private ImageButton volume_slience;
    private ImageButton volume_down;

    private ImageButton media_play;
    private ImageButton media_ff;
    private ImageButton media_rew;
    private ImageButton media_next;
    private ImageButton media_previous;

    private ImageButton screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        myApplication = (MySocket) MediaActivity.this.getApplication();

        volume_up=(ImageButton)findViewById(R.id.volume_up);
        volume_up.setOnClickListener(this);
        volume_down=(ImageButton)findViewById(R.id.volume_down);
        volume_down.setOnClickListener(this);
        volume_slience=(ImageButton)findViewById(R.id.volume_slience);
        volume_slience.setOnClickListener(this);
        media_play=(ImageButton)findViewById(R.id.media_play);
        media_play.setOnClickListener(this);
        media_ff=(ImageButton)findViewById(R.id.media_ff);
        media_ff.setOnClickListener(this);
        media_rew=(ImageButton)findViewById(R.id.media_rew);
        media_rew.setOnClickListener(this);
        media_next=(ImageButton)findViewById(R.id.media_next);
        media_next.setOnClickListener(this);
        media_previous=(ImageButton)findViewById(R.id.media_previous);
        media_previous.setOnClickListener(this);
        screen=(ImageButton)findViewById(R.id.screen);
        screen.setOnClickListener(this);
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
                Intent it=new Intent(MediaActivity.this,MainActivity.class);
                startActivity(it);
                break;
            case R.id.id_2:
                //PPT事件
                Toast.makeText(this,"前往PPT控制界面",Toast.LENGTH_LONG).show();
                Intent it2=new Intent(MediaActivity.this,PPTActivity.class);
                startActivity(it2);
                break;
            case R.id.id_3:
                //media
                Toast.makeText(this,"已经在MediaPlayer界面了",Toast.LENGTH_LONG).show();
                break;
            case R.id.id_4:
                Toast.makeText(this,"前往鼠标控制界面",Toast.LENGTH_LONG).show();
                Intent it3=new Intent(MediaActivity.this,MouseActivity.class);
                startActivity(it3);
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Log.i("tag", "media按键 ");
        switch (v.getId())
        {
            case R.id.volume_up:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_VOLUME_UP;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.volume_down:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_VOLUME_DOWN;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.volume_slience:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_VOLUME_SLIENCE;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.media_play:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_PLAY;
                myApplication.sendCMD(cmdBuffer);
                Log.i("tag", "media_play: ");
                break;
            case R.id.media_ff:
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_FF;
                myApplication.sendCMD(cmdBuffer);
                //事件
                break;
            case R.id.media_rew:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_REW;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.media_next:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_NEXT;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.media_previous:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_PREVIOUS;
                myApplication.sendCMD(cmdBuffer);
                break;
            case R.id.screen:
                //事件
                cmdBuffer[0]= Constant.RULE_MEDIA;
                cmdBuffer[1]=Constant.MEDIA_SCREEN;
                myApplication.sendCMD(cmdBuffer);
                break;
        }
    }
}
