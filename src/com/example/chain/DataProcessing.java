package com.example.chain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.itheima.studentsystem.db.dao.StudentDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Thread.sleep;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataProcessing extends Service {
    BluetoothDevice _device = null;
    BluetoothSocket _socket = null;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP锟斤拷锟斤拷UUID锟斤拷
    private InputStream is;  //获取蓝牙输入流
    public Thread thread;
    StudentDao dao;
    String [] string ={
    		 "我好像生病了",
             "我发烧了，好难受",
             "温度过高，我不想出去",
             "需要给我降温",
             "太冷了，我想要温暖",
             "好潮湿，难受",
             "好干燥，难受",
             "我有点小兴奋",
             "注意：危险！！！",
             "我有点饿",
             "我有点口渴",
             "我有点兴奋",
             "惊恐（危险！！！）"
};


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            List<String> s = analyzeData(bundle);
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("通知消息");
            for (int i=0;i<s.size();i++){
                builder.setMessage(s.get(i));
                builder.setNegativeButton("oK", null);
                Dialog dialog = builder.create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
            }
            Vibrator vibrator=null;
            vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        dao = new StudentDao(this);
        thread = new Thread() {
            public void run() {
                byte [] reqs = {0x02,0x03,0x04,0x05,0x06};
                String result= null;
                String [] daoDate = new String[3];
                while(true){
                    Bundle bundle = new Bundle();
                    for (int i=0;i<reqs.length;i++){
                        result = syncGetDate(reqs[i]);
                        if(i==3){
                            daoDate[0]=result;
                        }else if(i==2){
                            daoDate[1]=result;
                        }else if(i == 4){
                            daoDate[2]=result;
                        }
                        bundle.putString(i+2+"",result );
                    }
                    dao.add(daoDate[0]+"",daoDate[1]+"",daoDate[2]+"");
                    Message msg = new Message();
                    msg.setData(bundle);
                  handler.sendMessage(msg);
                    //鍙戦�佸箍鎾� 缁� myqqnews鏄剧ず鍑烘潵
                    Intent intent=new Intent();
                    intent.putExtras(bundle);
                    intent.setAction("dataforshow");
                    sendBroadcast(intent);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        getBlueToothIntput(intent);
        return super.onStartCommand(intent, flags, startId);

    }

    public List<String> analyzeData(Bundle bundle){
        List<String> s = new LinkedList<String>();

        float tiwen,hujinwendu,shidu,sudu,fenbai;
        tiwen = Float.parseFloat(bundle.getString("4").trim());
        hujinwendu =  Float.parseFloat(bundle.getString("2").trim());
        shidu = Float.parseFloat(bundle.getString("3").trim());
        sudu =  Float.parseFloat(bundle.getString("6").trim());
        fenbai =  Float.parseFloat(bundle.getString("5").trim());
        if(tiwen>33.6&&tiwen<=33.7){
            s.add(string[0]);
        }else if(tiwen>39){
            s.add(string[1]);
            if(hujinwendu>32&&hujinwendu<35){
        }
            s.add(string[2]);
        }else if(hujinwendu>35){
            s.add(string[3]);
        }else if(hujinwendu<16){
            s.add(string[4]);
        }
        if(shidu>60){
            s.add(string[5]);
        }else if(shidu<20){
            s.add(string[6]);
        }
        if (sudu>1.3&&sudu<1.6){
            s.add(string[7]);
        }else if(sudu>1.6){
            s.add(string[8]);
        }
        if(fenbai>10&&fenbai<15){
            s.add(string[9]);
        }else if (fenbai>15&&fenbai<20){
            s.add(string[10]);
        }else if (fenbai>20&&fenbai<40){
            s.add(string[11]);
        }else if (fenbai>40){
           s.add(string[12]);
        }
        return s;

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void getBlueToothIntput(Intent data){

        String address = data.getExtras().getString(
                DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        _device = _bluetooth.getRemoteDevice(address);

        try {
            _socket = _device.createRfcommSocketToServiceRecord(UUID
                    .fromString(MY_UUID));

        } catch (IOException e) {
            Toast.makeText(this, "锟斤拷锟斤拷失锟杰ｏ拷", Toast.LENGTH_SHORT).show();
        }

        try {
            _socket.connect();
            Toast.makeText(this, "锟斤拷锟斤拷" + _device.getName() + "锟缴癸拷锟斤拷",Toast.LENGTH_SHORT).show();
            thread.start();
            //锟斤拷锟斤拷锟斤拷锟接成癸拷


        } catch (IOException e) {
            try {
                Toast.makeText(this, "锟斤拷锟斤拷失锟杰ｏ拷", Toast.LENGTH_SHORT)
                        .show();
                _socket.close();
                _socket = null;
            } catch (IOException ee) {
                Toast.makeText(this, "锟斤拷锟斤拷失锟杰ｏ拷", Toast.LENGTH_SHORT)
                        .show();
            }

            return;
        }
    }

    public String syncGetDate(byte s){

        try {
            OutputStream os =  _socket.getOutputStream(); // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
            os.write(s);
            is=_socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s1 = (String)reader.readLine();
            Log.w("ff",s1);
            return s1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
