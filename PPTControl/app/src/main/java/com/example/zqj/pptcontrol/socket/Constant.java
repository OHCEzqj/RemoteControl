package com.example.zqj.pptcontrol.socket;

import java.net.InetAddress;

/**
 * Created by zqj on 2017/4/18.
 */

public class Constant {
    //事件识别码：鼠标事件，键盘事件，滑动事件，状态事件，反馈事件,Media码,电脑码
    public static final int RULE_MOUSE_EVENT = 1;
    public static final int RULE_KEY_EVENT = 2;
    public static final int RULE_SCREEN_PIX = 3;
    public static final int RULE_CONDITION=4;
    public static final int RULE_FEEDBACK=5;
    public static final int RULE_MEDIA=6;
    public static final int RULE_SCreen=7;
    //以下鼠标事件码，由系统提供
    public static final int MOUSEEVENT_LEFTDOWN = 0x2;
    public static final int MOUSEEVENT_LEFTUP = 0x4;
    public static final int MOUSEEVENT_MIDDLEDOWN = 0x20;
    public static final int MOUSEEVENT_MIDDLEUP = 0x40;
    public static final int MOUSEEVENT_MOVE = 0x1;
    public static final int MOUSEEVENT_ABSOLUTE = 0x8000+1;
    public static final int MOUSEEVENT_RIGHTDOWN = 0x8;
    public static final int MOUSEEVENT_RIGHTUP = 0x10;


    //以下为键盘键值，由系统提供
    public static final int KEY_ARROW_LEFT = 37;
    public static final int KEY_ARROW_RIGHT = 39;
    public static final int KEY_ARROW_UP = 38;
    public static final int KEY_ARROW_DOWN = 40;
    public static final int KEY_ESC = 27;
    public static final int KEY_F5 = 116;
    //后加键盘键值，用于MediaPlayer



    //鼠标移动方向指令值，这个为自定义
    public static final int MOVE_LEFT = 'L';
    public static final int MOVE_RIGHT = 'R';
    public static final int MOVE_UP = 'U';
    public static final int MOVE_DOWN = 'D';

    //鼠标按键指令值，这个为自定义
    public static final int MOUSE_LEFT = 0x11;//17
    public static final int MOUSE_RIGHT = 0x22;//34
    public static final int MOUSE_CENTER = 0x33;//51
    public static final int MOUSE_WHELL_UP=0x44;
    public static final int MOUSE_WHELL_DOWN=0x55;

    //状态事件码,自己定义
    public static final int CONDITION_CONNECT='A';
    public static final int CONDITION_BREAKE='B';

    //反馈事件码
    public static final int FEEDBACK_INIT='Z';

    //MediaPlayer码
    //mediaplayer 码
    public static final int MEDIA_VOLUME_UP='E';
    public static final int MEDIA_VOLUME_SLIENCE='F';
    public static final int MEDIA_VOLUME_DOWN='G';
    public static final int MEDIA_PLAY='H';
    public static final int MEDIA_FF='I';
    public static final int MEDIA_REW='J';
    public static final int MEDIA_NEXT='K';
    public static final int MEDIA_PREVIOUS='L';
    public static final int MEDIA_SCREEN='M';

    //其它
    public static final int MOUSE_GESTURE = 'C';
    public static final int bufferSize = 12;
    public static String ConIp=null;
    public static int Port = 8600;
    public static InetAddress IpAddress = null;

    //电脑码
    public  static  final  int Screen_init='O';
    public  static  final  int Screen_win='P';
    public  static  final  int Screen_shutdown='Q';
    public  static  final  int Screen_ctrlalt='R';
    public  static  final  int Screen_cmd='S';
    public  static  final  int Screen_logoff='T';
    public  static  final  int Screen_explorer='U';
    public  static  final  int Screen_nptepad='V';
    public  static  final  int Screen_mspaint='W';
    public  static  final  int Screen_taskmgr='X';
    public  static  final  int Screen_Nslookup='Y';



}
