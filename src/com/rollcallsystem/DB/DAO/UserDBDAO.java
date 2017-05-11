package com.rollcallsystem.DB.DAO;

import com.rollcallsystem.DB.RollCallDB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class UserDBDAO {

	protected SQLiteDatabase database;
	private RollCallDB RCDB;
	private Context mContext;

	public UserDBDAO(Context context) {
		this.mContext = context;
		RCDB = RollCallDB.getDB(mContext);
		open();
	}

	public void open() throws SQLException {
		if (RCDB == null)
			RCDB = RollCallDB.getDB(mContext);
		database = RCDB.getWritableDatabase();
	}

}
