package com.yifa.health_manage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDataHelper extends SQLiteOpenHelper {

	private static String table = "Image_User";

	private static SQLDataHelper dataHelper = null;

	public SQLDataHelper(Context context) {
		super(context, table, null, 1);
	}

	public static SQLDataHelper getSqlHelper(Context context) {
		if (dataHelper == null) {
			dataHelper = new SQLDataHelper(context);
		}
		return dataHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS Image_User"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, device_type VARCHAR, device_sn VARCHAR, friend_id VARCHAR,friend_name VARCHAR, image_url TEXT, layout_id VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
