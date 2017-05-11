package com.rollcallsystem.DB.DAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.rollcallsystem.DB.Column.StudentColumn;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class StudentDAO extends StudentDBDAO {

	public StudentDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private static final String TB_NAME =  StudentColumn.TABLE_NAME;
	private static final String _ID = StudentColumn._ID;
	private static final String LOG_ID = "StudentDAO";
	
	/**
	 * 新增學生
	 * @param Student
	 * @return
	 */
	public long addNewStudent(StudentVO Student) {
		Log.i(LOG_ID, "addNewStudent is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(StudentColumn.Student_NAME, Student.getStudent_NAME());
		values.put(StudentColumn.Student_ID, Student.getStudent_ID());
		values.put(StudentColumn.Student_CARD_ID, Student.getStudent_CARD_ID());
		return database.insert(TB_NAME, null, values);
	}
	
	/**
	 * 取得所有資料
	 * 
	 * @return
	 * @throws ParseException
	 */
	public List<StudentVO> getAllUsers() throws ParseException {
		List<StudentVO> StudentList = new ArrayList<StudentVO>();

		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
		Log.i(LOG_ID, "cursorSize>>"+cursor.getCount());
		while (cursor.moveToNext()) {
			StudentVO Student = new StudentVO();
			Student.set_id(cursor.getInt(cursor.getColumnIndex(StudentColumn._ID)));
			Student.setStudent_NAME(cursor.getString(cursor.getColumnIndex(StudentColumn.Student_NAME)));
			Student.setStudent_ID(cursor.getString(cursor.getColumnIndex(StudentColumn.Student_ID)));
			Student.setStudent_CARD_ID(cursor.getString(cursor.getColumnIndex(StudentColumn.Student_CARD_ID)));
			StudentList.add(Student);
		}
		cursor.close();
		return StudentList;
	}
}
