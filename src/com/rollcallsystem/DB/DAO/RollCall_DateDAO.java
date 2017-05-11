package com.rollcallsystem.DB.DAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.rollcallsystem.DB.Column.CurriculumColumn;
import com.rollcallsystem.DB.Column.RollCall_DateColumn;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;

public class RollCall_DateDAO extends RollCall_DateDBDAO{
	
	public RollCall_DateDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private static final String TB_NAME = RollCall_DateColumn.TABLE_NAME;
	private static final String _ID = RollCall_DateColumn._ID;
	private static final String LOG_ID = "RollCall_DateColumnDAO";
	
	/**
	 * 新增點名時間紀錄
	 * @param Curriculum
	 * @return
	 */
	public long addNewRollCall_Date(RollCall_DateVO RollCall_Date) {
	//Log.i(LOG_ID, "addNewCurriculum is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(RollCall_DateColumn.RollCall_DateColumn_Curriculum_ID, RollCall_Date.getRollCall_DateColumn_Curriculum_ID());
		values.put(RollCall_DateColumn.RollCall_DateColumn_StartDate, RollCall_Date.getRollCall_DateColumn_StartDate());
		values.put(RollCall_DateColumn.RollCall_DateColumn_EndDate,RollCall_Date.getRollCall_DateColumn_EndDate());
		values.put(RollCall_DateColumn.ISUPDATE,RollCall_Date.isISUPDATE());
		return database.insert(TB_NAME, null, values);
	}
	
	/**
	 * 更新點名時間紀錄
	 * @param RollCall_Date
	 * @return
	 */
	public long updateRollCall_Date(RollCall_DateVO RollCall_Date) {
		//Log.i(LOG_ID, "updateRollCall_Date is start >>>");
		ContentValues values = new ContentValues();
		//Log.i(LOG_ID, "values =" + values.toString());

		int updateCount = 0;
		//Log.d(LOG_ID, "RollCall_DateId:" + RollCall_Date.get_id());
		
		values.put(RollCall_DateColumn.RollCall_DateColumn_Curriculum_ID, RollCall_Date.getRollCall_DateColumn_Curriculum_ID());
		values.put(RollCall_DateColumn.RollCall_DateColumn_StartDate, RollCall_Date.getRollCall_DateColumn_StartDate());
		values.put(RollCall_DateColumn.RollCall_DateColumn_EndDate, RollCall_Date.getRollCall_DateColumn_EndDate());
		values.put(RollCall_DateColumn.ISUPDATE, RollCall_Date.isISUPDATE());
		try {
			updateCount = database.update(TB_NAME, values, RollCall_DateColumn._ID + "="
					+ RollCall_Date.get_id(), null);
			if (updateCount <= 0) {
				Log.d(LOG_ID, "no data update, values= " + values.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "update fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "update end >>>");
		}
		return updateCount;
	}
	
	/**
	 * 取得所有資料
	 * 
	 * @return
	 * @throws ParseException
	 */
	public List<RollCall_DateVO> getAllRollCall_Date() throws ParseException {
		List<RollCall_DateVO> RollCall_DateList = new ArrayList<RollCall_DateVO>();

		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
		//Log.i(LOG_ID, "cursorSize>>"+cursor.getCount());
		while (cursor.moveToNext()) {
			RollCall_DateVO  RollCall_Date = new  RollCall_DateVO();
			RollCall_Date.set_id(cursor.getInt(cursor.getColumnIndex(RollCall_DateColumn._ID)));
			RollCall_Date.setRollCall_DateColumn_Curriculum_ID(cursor.getString(cursor.getColumnIndex(RollCall_DateColumn.RollCall_DateColumn_Curriculum_ID)));
			RollCall_Date.setRollCall_DateColumn_StartDate(cursor.getString(cursor.getColumnIndex(RollCall_DateColumn.RollCall_DateColumn_StartDate)));
			RollCall_Date.setRollCall_DateColumn_EndDate(cursor.getString(cursor.getColumnIndex(RollCall_DateColumn.RollCall_DateColumn_EndDate)));
			RollCall_Date.setISUPDATE(cursor.getInt(cursor.getColumnIndex(RollCall_DateColumn.ISUPDATE)) == 1 ? true : false);
			RollCall_DateList.add(RollCall_Date);
		}
		cursor.close();
		//database.close();
		return RollCall_DateList;
	}
	
	
	
	public int deleteRollCall_Date(RollCall_DateVO RollCall_Date) {
		Log.i(LOG_ID, "delete is start >>>");
		int deleteCount = 0;
		try {
			//deleteCount = database.delete(TB_NAME, _ID, new String[] { String.valueOf(RollCall_Date.get_id()) });
			deleteCount = database.delete(TB_NAME, _ID + "=" + String.valueOf(RollCall_Date.get_id()), null);
			if (deleteCount <= 0) {
				Log.d(LOG_ID, "no data delete, values= " + RollCall_Date.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "delete fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "delete end >>>");
		}
		return deleteCount;
	}
}


