package com.rollcallsystem.DB.DAO;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rollcallsystem.DB.RollCallDB;

public class RollCall_DateDBDAO {
	protected SQLiteDatabase database;
	private RollCallDB RCDB;
	private Context mContext;
	public RollCall_DateDBDAO(Context context) {
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
