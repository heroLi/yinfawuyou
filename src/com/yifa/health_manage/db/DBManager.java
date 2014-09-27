package com.yifa.health_manage.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yifa.health_manage.model.UserInfo;

public class DBManager {

	private Context mContext;

	private SQLDataHelper hander;

	private SQLiteDatabase db = null;

	private static DBManager dbManager = null;

	public DBManager(Context c) {
		hander = SQLDataHelper.getSqlHelper(c);
		db = hander.getWritableDatabase();
	}

	public static DBManager getManager(Context c) {
		if (dbManager == null) {
			dbManager = new DBManager(c);
		}
		return dbManager;
	}

	public void insert(String type, String sn, String id, UserInfo info) {
		if (db.isOpen()) {
			db.execSQL(
					"INSERT INTO Image_User values(null,?,?,?,?,?,?)",
					new Object[] { type, sn, id, info.getName(),
							info.getImageUrl(), info.getLayoutId() });
		}
	}

	public void insertAll(List<UserInfo> mList) {
		try {
			db.beginTransaction();
			for (UserInfo info : mList) {
				db.execSQL(
						"INSERT INTO Image_User values(null,?,?,?,?,?,?)",
						new Object[] { info.getType(), info.getDevice_sn(),
								info.getFriend_id(), info.getName(),
								info.getImageUrl(), info.getLayoutId() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public UserInfo quaryId(String sn, String id) {
		UserInfo info = null;
		Cursor cursor = null;
		if (db.isOpen()) {
			cursor = db.query("Image_User", null,
					"device_sn=? and friend_id=?", new String[] { sn, id },
					null, null, null);
		}
		if (cursor != null) {

			if (cursor.moveToFirst()) {
				info = new UserInfo();
				info.setDevice_sn(cursor.getString(cursor
						.getColumnIndex("device_sn")));
				info.setFriend_id(cursor.getString(cursor
						.getColumnIndex("friend_id")));
				cursor.getString(cursor.getColumnIndex("friend_name"));
				info.setImageUrl(cursor.getString(cursor
						.getColumnIndex("image_url")));
				info.setName(cursor.getString(cursor
						.getColumnIndex("friend_name")));
				info.setLayoutId(cursor.getString(cursor
						.getColumnIndex("layout_id")));
			}
		}
		return info;
	}

	public List<UserInfo> quaryAll(String type) {
		List<UserInfo> mList = new ArrayList<UserInfo>();

		Cursor cursor = null;
		if (db.isOpen()) {
			cursor = db.query("Image_User", null, "device_type = ? ",
					new String[] { type }, null, null, null);
		}
		if (cursor != null) {

			while (cursor.moveToNext()) {
				UserInfo info = new UserInfo();
				info.setDevice_sn(cursor.getString(cursor
						.getColumnIndex("device_sn")));
				info.setFriend_id(cursor.getString(cursor
						.getColumnIndex("friend_id")));
				cursor.getString(cursor.getColumnIndex("friend_name"));
				info.setImageUrl(cursor.getString(cursor
						.getColumnIndex("image_url")));
				info.setName(cursor.getString(cursor
						.getColumnIndex("friend_name")));
				info.setType(cursor.getString(cursor
						.getColumnIndex("device_type")));
				info.setLayoutId(cursor.getString(cursor
						.getColumnIndex("layout_id")));
				mList.add(info);
			}
			cursor.close();
		}
		return mList;
	}

	public void update(UserInfo info) {
		if (db.isOpen()) {

			ContentValues values = new ContentValues();
			values.put("image_url", info.getImageUrl());
			values.put("friend_name", info.getName());

			db.update("Image_User", values, "device_sn=? and friend_id=?",
					new String[] { info.getDevice_sn(), info.getFriend_id() });
		}
	}

	public void updateName(List<UserInfo> infos) {
		if (db.isOpen()) {
			try {

				db.beginTransaction();
				for (UserInfo info : infos) {
					ContentValues values = new ContentValues();
					values.put("friend_name", info.getName());

					db.update(
							"Image_User",
							values,
							"device_sn=? and friend_id=?",
							new String[] { info.getDevice_sn(),
									info.getFriend_id() });
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}

		}
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}
}