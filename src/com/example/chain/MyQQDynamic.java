package com.example.chain;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//鍔ㄦ��
public class MyQQDynamic extends Activity{
	
	private TextView tv;
	/**
	 * 1.鍒涘缓涓�涓秷鎭鐞嗗櫒    杩愯鍦ㄤ富绾跨▼涓�
	 */
	private Handler handler = new Handler() {
		// 3.loop璋冪敤鐢ㄦ潵澶勭悊娑堟伅鐨勬柟娉曪紝杩愯鍦ㄤ富绾跨▼
		public void handleMessage(Message msg) {
			int i = (Integer) msg.obj;
			tv.setText("进度"+i+"%");
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_item_dynamic);
		tv = (TextView) findViewById(R.id.tv);
	}
	public void click(View view) {

		new Thread() {
			public void run() {
			
				// 2.通閫氳繃娑堟伅澶勭悊鍣ㄦ妸娑堟伅鍙戦�佸埌娑堟伅闃熷垪
				for (int i = 0; i < 100; i++) {
					Message msg = new Message();
					msg.obj = i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();

	}

}
