package cn.ifingers.mytown.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ifingers.mytown.entities.SearchHistoryBean;

public class HistorySearchDao {
	private Context context;

	public HistorySearchDao(Context context) {
		this.context = context;
	}

	public void insertAndUpdate(SearchHistoryBean bean) {
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		ContentValues values = new ContentValues();
		if (isInserted(bean) != -1) {
			values.put("time", bean.getTime());
			db.update("historytable", values, "content=?",
					new String[] { bean.getTitle() });
		} else {
			values.put("time", bean.getTime());
			values.put("content", bean.getTitle());
			db.insert("historytable", null, values);
		}
		db.close();
	}

	public int isInserted(SearchHistoryBean bean) {
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from historytable where content=?",
				new String[] { bean.getTitle() });
		int rowNumber = -1;
		if (cursor.moveToFirst()) {
			rowNumber = cursor.getInt(cursor.getColumnIndex("_id"));
		}
		cursor.close();
		db.close();
		return rowNumber;
	}

	public List<SearchHistoryBean> queryAll() {
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		List<SearchHistoryBean> list = new ArrayList<SearchHistoryBean>();
		Cursor cursor = db.rawQuery(
				"select * from historytable order by _id desc", null);
		while (cursor.moveToNext()) {
			SearchHistoryBean bean = new SearchHistoryBean();
			bean.setTitle(cursor.getString(cursor.getColumnIndex("content")));
			bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
			list.add(bean);
		}
		cursor.close();
		db.close();
		return list;
	}

	public List<SearchHistoryBean> queryByCount(int count) {
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		List<SearchHistoryBean> list = new ArrayList<SearchHistoryBean>();
		Cursor cursor = db.rawQuery(
				"select * from historytable order by _id desc", null);
		if (count < cursor.getCount()) {
			for (int i = 0; i < count; i++) {
				cursor.moveToNext();
				SearchHistoryBean bean = new SearchHistoryBean();
				bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
				bean.setTitle(cursor.getString(cursor.getColumnIndex("content")));
				list.add(bean);
			}
		} else {
			while (cursor.moveToNext()) {
				SearchHistoryBean bean = new SearchHistoryBean();
				bean.setTitle(cursor.getString(cursor.getColumnIndex("content")));
				bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
				list.add(bean);
			}
		}
		return list;
	}
	
	public int deleteByTime(String time){
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		int count = db.delete("historytable", "time=?", new String[]{time});
		return count;
	}
	
	public void deleteAll(){
		SQLiteDatabase db = new HistorySearchHelper(context).getReadableDatabase();
		db.delete("historytable", null, null);
	}
}