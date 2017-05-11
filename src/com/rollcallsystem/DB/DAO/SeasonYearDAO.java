package com.rollcallsystem.DB.DAO;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.rollcallsystem.DB.Column.CurriculumColumn;
import com.rollcallsystem.DB.Column.SeasonYearColumn;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class SeasonYearDAO extends SeasonYearDBDAO{

	public SeasonYearDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private static final String TB_NAME =SeasonYearColumn.TABLE_NAME;
	private static final String _ID = SeasonYearColumn._ID;
	private static final String LOG_ID = "SeasonYearDAO";
	
	/**
	 * 新增學期年度
	 * @param SeasonYear
	 * @return
	 */
	public long addNewSeasonYear(SeasonYearVO SeasonYear) {
	//	Log.i(LOG_ID, "addNewSeasonYear is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(SeasonYearColumn.SeasonYear_SEASON, SeasonYear.getSeasonYear_SEASON());
		return database.insert(TB_NAME, null, values);
	}
	
	public long deleteSeasonYear(SeasonYearVO SeasonYear) {
		Log.i(LOG_ID, "delete is start >>>");
		int deleteCount = 0;
		try {
			deleteCount = database.delete(TB_NAME, SeasonYearColumn.SeasonYear_SEASON+"='"+SeasonYear.getSeasonYear_SEASON()+"'", null);
			if (deleteCount <= 0) {
				Log.d(LOG_ID, "no data delete, values= " + SeasonYear.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "delete fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "delete end >>>");
		}
		return deleteCount;
	}
	
	/**
	 * 取得所有資料
	 * 
	 * @return
	 * @throws ParseException
	 */
	public List<SeasonYearVO> getAllSeasonYears() throws ParseException {
		List<SeasonYearVO> SeasonYearList = new ArrayList<SeasonYearVO>();

		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
		Log.i(LOG_ID, "cursorSize>>"+cursor.getCount());
		while (cursor.moveToNext()) {
			SeasonYearVO  SeasonYear = new  SeasonYearVO();
			SeasonYear.setSeasonYear_SEASON(cursor.getString(cursor.getColumnIndex(SeasonYearColumn.SeasonYear_SEASON)));
			SeasonYearList.add(SeasonYear);
		}
		cursor.close();
		return SeasonYearList;
	}

}
