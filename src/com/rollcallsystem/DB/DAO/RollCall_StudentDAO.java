package com.rollcallsystem.DB.DAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.rollcallsystem.DB.Column.RollCall_DateColumn;
import com.rollcallsystem.DB.Column.RollCall_StudentColumn;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class RollCall_StudentDAO extends RollCall_StudentDBDAO {

	public RollCall_StudentDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static final String TB_NAME = RollCall_StudentColumn.TABLE_NAME;
	private static final String _ID = RollCall_StudentColumn._ID;
	private static final String RollCall_DateColumn_DateID = RollCall_StudentColumn.RollCall_DateColumn_DateID;
	private static final String LOG_ID = "RollCall_StudentDAO";

	/**
	 * 新增學生靠卡紀錄
	 * 
	 * @param RollCall_StudentVO
	 * @return
	 */
	public long addNewRollCall_Student(RollCall_StudentVO RollCall_Student) {
		// Log.i(LOG_ID, "addNewCurriculum is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID, RollCall_Student.getRollCall_DateColumn_Curriculum_ID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_DateID, RollCall_Student.getRollCall_DateColumn_DateID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentID, RollCall_Student.getRollCall_DateColumn_StudentID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentRollCallDate, RollCall_Student.getRollCall_DateColumn_StudentRollCallDate());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_AttendanceRate, RollCall_Student.getRollCall_DateColumn_AttendanceRate());
		return database.insert(TB_NAME, null, values);
	}

	/**
	 * 更新學生點名時間紀錄
	 * 
	 * @param RollCall_StudentVO
	 * @return
	 */
	public long updateRollCall_Student(RollCall_StudentVO RollCall_Student) {
		//Log.i(LOG_ID, "updateRollCall_Student is start >>>");
		ContentValues values = new ContentValues();
		//Log.i(LOG_ID, "values =" + values.toString());

		int updateCount = 0;
		//Log.d(LOG_ID, "RollCall_StudentId:" + RollCall_Student.get_id());

		values.put(RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID, RollCall_Student.getRollCall_DateColumn_Curriculum_ID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_DateID, RollCall_Student.getRollCall_DateColumn_DateID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentID, RollCall_Student.getRollCall_DateColumn_StudentID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentRollCallDate, RollCall_Student.getRollCall_DateColumn_StudentRollCallDate());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_AttendanceRate, RollCall_Student.getRollCall_DateColumn_AttendanceRate());
		try {
			//updateCount = database.update(TB_NAME, values, RollCall_StudentColumn.RollCall_DateColumn_StudentID + "="+ RollCall_Student.get_id(), null);
			updateCount = database.update(TB_NAME, values,
					RollCall_StudentColumn.RollCall_DateColumn_DateID + "='" + 
			RollCall_Student.getRollCall_DateColumn_DateID()+"' AND "+ 
					RollCall_StudentColumn.RollCall_DateColumn_StudentID+"='" + 
			RollCall_Student.getRollCall_DateColumn_StudentID()+"'", null);
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
	
	public long updateALLRollCall_Student(RollCall_StudentVO RollCall_Student) {
		//Log.i(LOG_ID, "updateRollCall_Student is start >>>");
		ContentValues values = new ContentValues();
		//Log.i(LOG_ID, "values =" + values.toString());

		int updateCount = 0;
		//Log.d(LOG_ID, "RollCall_StudentId:" + RollCall_Student.get_id());

		values.put(RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID, RollCall_Student.getRollCall_DateColumn_Curriculum_ID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_DateID, RollCall_Student.getRollCall_DateColumn_DateID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentID, RollCall_Student.getRollCall_DateColumn_StudentID());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_StudentRollCallDate, RollCall_Student.getRollCall_DateColumn_StudentRollCallDate());
		values.put(RollCall_StudentColumn.RollCall_DateColumn_AttendanceRate, RollCall_Student.getRollCall_DateColumn_AttendanceRate());
		try {
			//updateCount = database.update(TB_NAME, values, RollCall_StudentColumn.RollCall_DateColumn_StudentID + "="+ RollCall_Student.get_id(), null);
			updateCount = database.update(TB_NAME, values,
					RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID + "='" + 
							RollCall_Student.getRollCall_DateColumn_Curriculum_ID()+"' AND "+
					RollCall_StudentColumn.RollCall_DateColumn_DateID + "='" + 
			RollCall_Student.getRollCall_DateColumn_DateID()+"' AND "+ 
					RollCall_StudentColumn.RollCall_DateColumn_StudentID+"='" + 
			RollCall_Student.getRollCall_DateColumn_StudentID()+"'", null);
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
	public List<RollCall_StudentVO> getAllRollCall_Student() throws ParseException {
		List<RollCall_StudentVO> RollCall_DateList = new ArrayList<RollCall_StudentVO>();

		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
		Log.i(LOG_ID, "cursorSize>>" + cursor.getCount());
		while (cursor.moveToNext()) {
			RollCall_StudentVO RollCall_Student = new RollCall_StudentVO();
			RollCall_Student.set_id(cursor.getInt(cursor.getColumnIndex(RollCall_DateColumn._ID)));
			RollCall_Student.setRollCall_DateColumn_Curriculum_ID(cursor.getString(cursor.getColumnIndex(RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID)));
			RollCall_Student.setRollCall_DateColumn_DateID(cursor.getString(cursor.getColumnIndex(RollCall_StudentColumn.RollCall_DateColumn_DateID)));
			RollCall_Student.setRollCall_DateColumn_StudentID(cursor.getString(cursor.getColumnIndex(RollCall_StudentColumn.RollCall_DateColumn_StudentID)));
			RollCall_Student.setRollCall_DateColumn_StudentRollCallDate(cursor.getString(cursor.getColumnIndex(RollCall_StudentColumn.RollCall_DateColumn_StudentRollCallDate)));
			RollCall_Student.setRollCall_DateColumn_AttendanceRate(cursor.getString(cursor.getColumnIndex(RollCall_StudentColumn.RollCall_DateColumn_AttendanceRate)));
			RollCall_DateList.add(RollCall_Student);
		}
		cursor.close();
		return RollCall_DateList;
	}

	public int deleteRollCall_Studen(String CuId) {
		Log.i(LOG_ID, "delete is start >>>");
		int deleteCount = 0;
		try {
			// deleteCount = database.delete(TB_NAME, _ID, new String[] {
			// String.valueOf(RollCall_Date.get_id()) });
			deleteCount = database.delete(TB_NAME, RollCall_DateColumn_DateID + "=" + CuId, null);
			if (deleteCount <= 0) {
				Log.d(LOG_ID, "no data delete, values= " + CuId.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "delete fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "delete end >>>");
		}
		return deleteCount;
	}
}
