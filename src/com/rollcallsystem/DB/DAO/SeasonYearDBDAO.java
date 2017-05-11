package com.rollcallsystem.DB.DAO;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rollcallsystem.DB.RollCallDB;

public class SeasonYearDBDAO {
	protected SQLiteDatabase database;
	private RollCallDB RCDB;
	private Context mContext;
	
	public SeasonYearDBDAO(Context context) {
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
