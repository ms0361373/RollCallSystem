package com.rollcallsystem.DB.DAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.rollcallsystem.DB.Column.CurriculumColumn;
import com.rollcallsystem.DB.Column.StudentColumn;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CurriculumDAO extends CurriculumDBDAO{

	public CurriculumDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private static final String TB_NAME = CurriculumColumn.TABLE_NAME;
	private static final String _ID = CurriculumColumn._ID;
	private static final String LOG_ID = "CurriculumDAO";
	
	/**
	 * 新增課程
	 * @param Curriculum
	 * @return
	 */
	public long addNewCurriculum(CurriculumVO Curriculum) {
	//Log.i(LOG_ID, "addNewCurriculum is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(CurriculumColumn.Curriculum_INSTRUCTORS, Curriculum.getCurriculum_INSTRUCTORS());
		values.put(CurriculumColumn.Curriculum_NAME, Curriculum.getCurriculum_NAME());
		values.put(CurriculumColumn.Curriculum_ID, Curriculum.getCurriculum_ID());
		values.put(CurriculumColumn.Curriculum_CLASS, Curriculum.getCurriculum_CLASS());
		values.put(CurriculumColumn.Curriculum_HOURS, Curriculum.getCurriculum_HOURS());
		values.put(CurriculumColumn.Curriculum_SEASON, Curriculum.getCurriculum_SEASON());
		values.put(CurriculumColumn.Curriculum_STD_Data, Curriculum.getCurriculum_STD_Data());
		return database.insert(TB_NAME, null, values);
	}
	public long deleteCurriculum(CurriculumVO Curriculum) {
		Log.i(LOG_ID, "delete is start >>>");
		int deleteCount = 0;
		try {
			deleteCount = database.delete(TB_NAME, CurriculumColumn.Curriculum_ID+"='"+Curriculum.getCurriculum_ID()+"'", null);
			if (deleteCount <= 0) {
				Log.d(LOG_ID, "no data delete, values= " + Curriculum.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "delete fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "delete end >>>");
		}
		return deleteCount;
	}
	
	/**
	 * 更新使用者
	 * @param user
	 * @return
	 */
	public long updateUser(CurriculumVO Curriculum) {
		Log.i(LOG_ID, "updateUser is start >>>");
		ContentValues values = new ContentValues();
		Log.i(LOG_ID, "values =" + values.toString());

		int updateCount = 0;
		Log.d(LOG_ID, "CurriculumId:" + Curriculum.get_id());
		
		values.put(CurriculumColumn.Curriculum_INSTRUCTORS, Curriculum.getCurriculum_INSTRUCTORS());
		values.put(CurriculumColumn.Curriculum_NAME, Curriculum.getCurriculum_NAME());
		values.put(CurriculumColumn.Curriculum_ID, Curriculum.getCurriculum_ID());
		values.put(CurriculumColumn.Curriculum_CLASS, Curriculum.getCurriculum_CLASS());
		values.put(CurriculumColumn.Curriculum_HOURS, Curriculum.getCurriculum_HOURS());
		values.put(CurriculumColumn.Curriculum_SEASON, Curriculum.getCurriculum_SEASON());
		values.put(CurriculumColumn.Curriculum_STD_Data, Curriculum.getCurriculum_STD_Data());
		try {
			updateCount = database.update(TB_NAME, values, CurriculumColumn._ID + "="
					+ Curriculum.get_id(), null);
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
	public List<CurriculumVO> getAllCurriculums() throws ParseException {
		List<CurriculumVO> CurriculumList = new ArrayList<CurriculumVO>();

		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
		Log.i(LOG_ID, "cursorSize>>"+cursor.getCount());
		while (cursor.moveToNext()) {
			 CurriculumVO  Curriculum = new  CurriculumVO();
			 Curriculum.set_id(cursor.getInt(cursor.getColumnIndex(CurriculumColumn._ID)));
			 Curriculum.setCurriculum_NAME(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_NAME)));
			 Curriculum.setCurriculum_INSTRUCTORS(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_INSTRUCTORS)));
			 Curriculum.setCurriculum_CLASS(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_CLASS)));
			 Curriculum.setCurriculum_ID(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_ID)));
			 Curriculum.setCurriculum_HOURS(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_HOURS)));
			 Curriculum.setCurriculum_SEASON(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_SEASON)));
			 Curriculum.setCurriculum_STD_Data(cursor.getString(cursor.getColumnIndex(CurriculumColumn.Curriculum_STD_Data)));
			CurriculumList.add(Curriculum);
		}
		cursor.close();
		return CurriculumList;
	}
}
