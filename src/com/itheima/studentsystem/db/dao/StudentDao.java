package com.itheima.studentsystem.db.dao;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itheima.studentsystem.db.StudentDBOpenHelper;

/**
 * 学生数据库的增删改查工具类
 * 
 * @author Administrator
 * 
 */
public class StudentDao {
	private StudentDBOpenHelper helper;

	public StudentDao(Context context) {
		helper = new StudentDBOpenHelper(context);
	}

	/**
	 * 添加一条记录
	 * 
	 * @param studentid
	 * @param name
	 * @param phone
	 * @return
	 */
	public boolean add(String studentid, String name, String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("studentid", studentid);
		values.put("name", name);
		values.put("phone", phone);
		long row = db.insert("info", null, values);
		db.close();
		if (row == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除一条记录
	 * 
	 * @param studentid
	 * @param name
	 * @param phone
	 * @return
	 */
	public boolean delete(String studentid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db
				.delete("info", "studentid=?", new String[] { studentid });
		db.close();
		if (count <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 查询的逻辑
	 * 
	 * @param position
	 *            数据在数据库表里面的位置
	 * @return Map<数据库的列名,值>
	 */
	public Map<String, String> getStudentInfo(int position) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", new String[] { "studentid", "name",
				"phone" }, null, null, null, null, null);
		cursor.moveToPosition(position);
		String studentid = cursor.getString(0);
		String name = cursor.getString(1);
		String phone = cursor.getString(2);
		cursor.close();
		db.close();
		Map<String, String> result = new HashMap<String, String>();
		result.put("studentid", studentid);
		result.put("name", name);
		result.put("phone", phone);
		return result;
	}

	/**
	 * 查询数据库里面一共有多少条记录
	 */
	public int getTotalCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", null, null, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
}
