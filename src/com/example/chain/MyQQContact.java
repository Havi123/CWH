package com.example.chain;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.studentsystem.db.dao.StudentDao;

public class MyQQContact extends Activity {
	private EditText et_id;
	private EditText et_name;
	private EditText et_phone;
	private StudentDao dao;
	private ListView lv;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_item_contact);
	//	et_id = (EditText) this.findViewById(R.id.et_id);
	//	et_name = (EditText) this.findViewById(R.id.et_name);
	//	et_phone = (EditText) findViewById(R.id.et_phone);
		lv = (ListView) findViewById(R.id.lv);
		dao = new StudentDao(this);
		lv.setAdapter(new MyAdapter());
	}
	
	//锟斤拷锟窖э拷锟�
	public void addStudent(View view){
		String id = et_id.getText().toString().trim();
		String name = et_name.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(id)||TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)){
			Toast.makeText(this, "杈撳叆涓嶈兘涓虹┖", 0).show();
		}else{
			//锟斤拷锟斤拷锟斤拷锟捷碉拷锟斤拷锟捷匡拷,锟斤拷锟斤拷同锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷.
			boolean result = dao.add(id, name, phone);//锟斤拷锟捷憋拷锟斤拷锟诫到锟斤拷锟捷匡拷锟斤拷.
			if(result){
				Toast.makeText(this, "娣诲姞鎴愬姛",0).show();
				lv.setAdapter(new MyAdapter());
			}
		}
		
	}
	
	private class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return dao.getTotalCount();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item, null);
			//锟斤拷锟斤拷锟斤拷锟絭iew锟斤拷锟斤拷募锟斤拷锟絫extview,锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
			TextView tv_item_id = (TextView) view.findViewById(R.id.tv_item_id);
			TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
			TextView tv_item_phone = (TextView) view.findViewById(R.id.tv_item_phone);
			final Map<String, String>  map = dao.getStudentInfo(position);
			view.findViewById(R.id.iv_delete).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean result = dao.delete(map.get("studentid"));
					if(result){
						Toast.makeText(MyQQContact.this, "鍒犻櫎鎴愬姛",0).show();
						lv.setAdapter(new MyAdapter());
					}
				}
			});
			tv_item_phone.setText(map.get("phone"));
			tv_item_id.setText(map.get("studentid"));
			tv_item_name.setText(map.get("name"));
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}

