package com.example.chain;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import com.itheima.studentsystem.db.dao.StudentDao;

public class MyQQNews extends Activity implements OnClickListener {

	private final static int REQUEST_CONNECT_DEVICE = 1; // �궨���ѯ�豸���
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP����UUID��
	StudentDao dao; 
	public EditText tiwen_shuru;
	public EditText wendu_shuru;
	public EditText shidu_shuru;
	public EditText sensor_shuru;
	public EditText xsudu_shuru;
	public Boolean bluetoothStatus = false;
	private MyReceiver receiver=null;
	private InputStream is; // ������������������������
	BluetoothDevice _device = null; // �����豸
	BluetoothSocket _socket = null; // ����ͨ��socket
	boolean bRun = true;

	@SuppressLint("NewApi")

	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter(); // ��ȡ�����������������������豸

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_item_news);
        dao = new StudentDao(this);
        tiwen_shuru=(EditText)findViewById(R.id.input_tiwen);
        wendu_shuru=(EditText)findViewById(R.id.input_wendu);
        shidu_shuru=(EditText)findViewById(R.id.input_shidu);
        sensor_shuru=(EditText)findViewById(R.id.input_sensor);
        xsudu_shuru=(EditText)findViewById(R.id.input_xsudu);
//		注册广播
		//注册广播接收器
		receiver=new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("dataforshow");
		MyQQNews.this.registerReceiver(receiver,filter);
//        连接蓝牙按钮及实现
        Button lianjieBtn=(Button)findViewById(R.id.lianjie_Button);
        lianjieBtn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				if(bluetoothStatus){
					Toast.makeText(getApplicationContext(), "蓝牙已连接", Toast.LENGTH_LONG).show();
				}
				else{
					if (_bluetooth.isEnabled() == false) {
						Toast.makeText(getApplicationContext(), " 连接蓝牙成功", Toast.LENGTH_LONG).show();
						return;
					}
					if (_socket == null) {
						Intent serverIntent = new Intent(getApplicationContext(),DeviceListActivity.class); // ��ת��������
						startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // ���÷��غ궨��
						 
					} else {
						
						// �ر�����socket
						try {
							//�����Ͽ����Ƶ�
							OutputStream os = _socket.getOutputStream(); // �������������
	
						} catch (IOException e) {
						}
						try {
							is.close();
							_socket.close();
							_socket = null;
							bRun = false;
						} catch (IOException e) {
						}
					}
				}
			}
		});

     		if (_bluetooth == null) {
     			Toast.makeText(this, "已连接蓝牙", Toast.LENGTH_LONG).show();
     			return;
     		}
     		

    		new Thread() {
    			@SuppressLint("NewApi")
    			public void run() {
    				if (_bluetooth.isEnabled() == false) {
    					_bluetooth.enable();
    				}
    			}
    		}.start();
    }
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			wendu_shuru.setText(bundle.getString(2+""));
			shidu_shuru.setText(bundle.getString(3+""));
			tiwen_shuru.setText(bundle.getString(4+""));
			sensor_shuru.setText(bundle.getString(5+""));
			xsudu_shuru.setText(bundle.getString(6+""));
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 		@TargetApi(Build.VERSION_CODES.ECLAIR)
 		@SuppressLint("NewApi")
 		public void onActivityResult(int requestCode, int resultCode, Intent data) {
 			switch (requestCode) {
 			case REQUEST_CONNECT_DEVICE: // ���ӽ������DeviceListActivity���÷���
 				// ��Ӧ���ؽ��
 				if (resultCode == Activity.RESULT_OK) { // ���ӳɹ�����DeviceListActivity���÷���
 					bluetoothStatus=!bluetoothStatus;   //转到service处理
					data.getExtras();
					Intent intent = new Intent(this,DataProcessing.class);
					intent.putExtras(data.getExtras());
					this.startService(intent);
 				}
 				break;
 			default:
 				break;
 			}
 		}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	/*System.out.println("MainActivity ����绰");
      	call();*/
	}

    }
