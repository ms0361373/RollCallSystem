package com.rollcallsystem.DB;





import java.util.Date;
import com.rollcallsystem.DB.Column.BaseColumn;
import com.rollcallsystem.DB.Column.CurriculumColumn;
import com.rollcallsystem.DB.Column.RollCall_DateColumn;
import com.rollcallsystem.DB.Column.RollCall_StudentColumn;
import com.rollcallsystem.DB.Column.SeasonYearColumn;
import com.rollcallsystem.DB.Column.StudentColumn;
import com.rollcallsystem.DB.Column.UserColumn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RollCallDB extends SQLiteOpenHelper {
	private static String APP_ID = "RollCallDB";
	private static final String DATABASE_NAME = "RollCallDB";
	//private static final int DATABASE_VERSION = 1;
	//private static final int DATABASE_VERSION = 2;
	private static final int DATABASE_VERSION = 3;
	private static RollCallDB RC_DB;
	
	private static final String CREATE_SQL_BASE = BaseColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT";
	
	private static final String CREATE_SQL_TB_USER_MAIN = "CREATE TABLE IF NOT EXISTS " + UserColumn.TABLE_NAME + " ("
			+  CREATE_SQL_BASE + "," + UserColumn.USER_NAME + " TEXT," + UserColumn.USER_ID + " TEXT," + UserColumn.USER_EMAIL
			+ " TEXT," + UserColumn.USER_CARD_ID + " TEXT) ";
	
	private static final String CREATE_SQL_TB_STUDENT_MAIN = "CREATE TABLE IF NOT EXISTS " + StudentColumn.TABLE_NAME + " ("
			+ CREATE_SQL_BASE + "," + StudentColumn.Student_NAME + " TEXT," + StudentColumn.Student_ID + " TEXT NOT NULL UNIQUE," +  StudentColumn.Student_CARD_ID + " TEXT) ";
	
	private static final String CREATE_SQL_TB_CURRICULUM_MAIN = "CREATE TABLE IF NOT EXISTS " + CurriculumColumn.TABLE_NAME + " ("
			+ CREATE_SQL_BASE + "," + CurriculumColumn.Curriculum_NAME + " TEXT," + CurriculumColumn.Curriculum_INSTRUCTORS + " TEXT," +
			CurriculumColumn.Curriculum_ID + " TEXT," + CurriculumColumn.Curriculum_CLASS + " TEXT," + CurriculumColumn.Curriculum_SEASON + " TEXT," + 
			CurriculumColumn.Curriculum_HOURS + " TEXT," + CurriculumColumn.Curriculum_STD_Data + " TEXT) ";
	
	private static final String CREATE_SQL_TB_SEASON_MAIN = "CREATE TABLE IF NOT EXISTS " + SeasonYearColumn.TABLE_NAME + " ("
			+ CREATE_SQL_BASE + "," + SeasonYearColumn.SeasonYear_SEASON +  " TEXT ) ";
	
	private static final String CREATE_SQL_TB_RollCall_DateColumn_MAIN = "CREATE TABLE IF NOT EXISTS " + RollCall_DateColumn.TABLE_NAME + " ("
			+ CREATE_SQL_BASE + "," + RollCall_DateColumn.RollCall_DateColumn_Curriculum_ID +  
			" TEXT," + RollCall_DateColumn.RollCall_DateColumn_StartDate +  
			" TEXT," + RollCall_DateColumn.RollCall_DateColumn_EndDate +  
			" TEXT," + RollCall_DateColumn.ISUPDATE +  
			" INTEGER ) ";
	
	private static final String CREATE_SQL_TB_RollCall_StudentColumn_MAIN = "CREATE TABLE IF NOT EXISTS " + RollCall_StudentColumn.TABLE_NAME + " ("
			+ CREATE_SQL_BASE + "," + RollCall_StudentColumn.RollCall_DateColumn_Curriculum_ID +  
			" TEXT," + RollCall_StudentColumn.RollCall_DateColumn_StudentID + 
			" TEXT," + RollCall_StudentColumn.RollCall_DateColumn_DateID +  
			" TEXT," + RollCall_StudentColumn.RollCall_DateColumn_StudentRollCallDate + 
			" TEXT," + RollCall_StudentColumn.RollCall_DateColumn_AttendanceRate +  
			" TEXT ) ";
	
	public static synchronized RollCallDB getDB(Context context) {
		if (RC_DB == null)
			RC_DB = new RollCallDB(context);
		return RC_DB;
	}
	
	public RollCallDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_USER_MAIN);
		db.execSQL(CREATE_SQL_TB_USER_MAIN);
		
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_STUDENT_MAIN);
		db.execSQL(CREATE_SQL_TB_STUDENT_MAIN);
		
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_CURRICULUM_MAIN);
		db.execSQL(CREATE_SQL_TB_CURRICULUM_MAIN);
		
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_SEASON_MAIN);
		db.execSQL(CREATE_SQL_TB_SEASON_MAIN);
		
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_RollCall_DateColumn_MAIN);
		db.execSQL(CREATE_SQL_TB_RollCall_DateColumn_MAIN);
		
		Log.i(APP_ID, " SQL >>> " + CREATE_SQL_TB_RollCall_StudentColumn_MAIN);
		db.execSQL(CREATE_SQL_TB_RollCall_StudentColumn_MAIN);
		
//		ContentValues value = new ContentValues();
//		value.put(UserColumn.USER_NAME,"root");
//		value.put(UserColumn.USER_ID,"G030E008");
//		value.put(UserColumn.USER_EMAIL, "ms0361373@gmail.com");
//		value.put(UserColumn.USER_CARD_ID, "123456");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		onCreate(db);
	}

}
