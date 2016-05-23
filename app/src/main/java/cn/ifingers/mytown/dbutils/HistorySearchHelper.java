package cn.ifingers.mytown.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistorySearchHelper extends SQLiteOpenHelper {

	public HistorySearchHelper(Context context) {
		super(context, "searchhistory.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table historytable(" +
				"_id integer primary key autoincrement," +
				"content varchar(40)," +
				"time varchar(40))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}