package com.rollcallsystem.DB.Manage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.DAO.CurriculumDAO;
import com.rollcallsystem.DB.DAO.RollCall_DateDAO;
import com.rollcallsystem.DB.DAO.RollCall_StudentDAO;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

public class RollCall_StudentManager {
	private static final String LOG_ID = "RollCall_DateManage";
	private RollCall_StudentDAO rollCall_StudentDAO;
	private Context context;
	public RollCall_StudentManager()
	{
		super();
	}
	
	public RollCall_StudentManager(Context context)
	{
		super();
		try {
			this.rollCall_StudentDAO = new RollCall_StudentDAO(context);
			this.context =context;
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}
	
	/**
	 * 刪除出席資料
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean delete(String CuID) {
		boolean status = false;
		long id = rollCall_StudentDAO.deleteRollCall_Studen(CuID);
		status = (id > 0) ? true : false;
		return status;
	}
	
	/**
	 * 新增學生出席資料
	 * 
	 * @param RollCall_StudentVO
	 * @return
	 */
	public boolean add(RollCall_StudentVO RollCall_StudentVO) {
		boolean status = false;
		
			long id = rollCall_StudentDAO.addNewRollCall_Student(RollCall_StudentVO);
			if (id > 0) {
				status = true;
			}
			
		return status;
	}
	
	/**
	 * 更新學生出席資料
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean update(RollCall_StudentVO RollCall_StudentVO) {
		boolean status = false;
		long id = rollCall_StudentDAO.updateRollCall_Student(RollCall_StudentVO);
		if (id > 0) {
			status = true;
		}
		return status;
	}
	
	/**
	 * 更新學生出席資料
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean updateAll(RollCall_StudentVO RollCall_StudentVO) {
		boolean status = false;
		long id = rollCall_StudentDAO.updateALLRollCall_Student(RollCall_StudentVO);
		if (id > 0) {
			status = true;
		}
		return status;
	}
	
	/**
	 * 取得所有學生出席資料
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getAllRollCall_Student() {
		List<RollCall_StudentVO> RollCall_Student = new ArrayList<RollCall_StudentVO>();
		try {
			RollCall_Student = rollCall_StudentDAO.getAllRollCall_Student();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RollCall_Student;
	}
	
	
	/**
	 * 取得學生出席資料by出席資料ID
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getRollCall_StudentByDateId(String ID) {
		List<RollCall_StudentVO> RS = new ArrayList<RollCall_StudentVO>();
		List<RollCall_StudentVO> RS2 = new ArrayList<RollCall_StudentVO>();
		int id = 0;
		try {
			//RollCall_DateDAO RD = new RollCall_DateDAO(context);
			//List<RollCall_DateVO> rollCall_Date = RD.getAllRollCall_Date();
			RS = rollCall_StudentDAO.getAllRollCall_Student();
			for(int i = 0; i <RS.size();i++)
			{
				if(RS.get(i).getRollCall_DateColumn_DateID().equals(ID))
					{
					RS2.add(RS.get(i));
					}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return RS2;
	}
	
	
	
	/**
	 * 取得學生出席資料by課程與學生ID
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getRollCall_StudentByCu(String CuName,String cuClass,String cuYear,String stdId) {
		List<RollCall_StudentVO> rollCall_Student = new ArrayList<RollCall_StudentVO>();
		List<RollCall_StudentVO> rollCall_Students = new ArrayList<RollCall_StudentVO>();
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		int id = 0;
		try {
			rollCall_Student = rollCall_StudentDAO.getAllRollCall_Student();
			CurriculumDAO cdao = new CurriculumDAO(context);
			Curriculum = cdao.getAllCurriculums();
			
			int size = Curriculum.size();
			for (int i = 0; i < size; i++) 
			{
				if (Curriculum.get(i).getCurriculum_NAME().equals(CuName) && Curriculum.get(i).getCurriculum_CLASS().equals(cuClass)&& Curriculum.get(i).getCurriculum_SEASON().equals(cuYear)) {
					 id = Curriculum.get(i).get_id();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int Rollsize = rollCall_Student.size();
		for (int i = 0; i < Rollsize; i++) 
		{
			if (rollCall_Student.get(i).getRollCall_DateColumn_Curriculum_ID().equals(String.valueOf(id)) && rollCall_Student.get(i).getRollCall_DateColumn_StudentID().equals(stdId)) {
			rollCall_Students.add(rollCall_Student.get(i));
			}
		}
		Log.i(LOG_ID, "rollCall_Students Size >>" + rollCall_Students.size());
		return rollCall_Students;
	}
	
	/**
	 * 取得學生出席資料by課程與學生ID
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getRollCall_StudentByCuStd(String CuName,String cuClass,String cuYear,String stdId,String DateID) {
		List<RollCall_StudentVO> rollCall_Student = new ArrayList<RollCall_StudentVO>();
		List<RollCall_StudentVO> rollCall_Students = new ArrayList<RollCall_StudentVO>();
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		int id = 0;
		try {
			rollCall_Student = rollCall_StudentDAO.getAllRollCall_Student();
			CurriculumDAO cdao = new CurriculumDAO(context);
			Curriculum = cdao.getAllCurriculums();
			
			int size = Curriculum.size();
			for (int i = 0; i < size; i++) 
			{
				if (Curriculum.get(i).getCurriculum_NAME().equals(CuName) && Curriculum.get(i).getCurriculum_CLASS().equals(cuClass)&& Curriculum.get(i).getCurriculum_SEASON().equals(cuYear)) {
					 id = Curriculum.get(i).get_id();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int Rollsize = rollCall_Student.size();
		for (int i = 0; i < Rollsize; i++) 
		{
			if (rollCall_Student.get(i).getRollCall_DateColumn_Curriculum_ID().equals(String.valueOf(id)) && rollCall_Student.get(i).getRollCall_DateColumn_StudentID().equals(stdId) &&  rollCall_Student.get(i).getRollCall_DateColumn_DateID().equals(DateID)) {
			rollCall_Students.add(rollCall_Student.get(i));
			}
		}
		Log.i(LOG_ID, "rollCall_Students Size >>" + rollCall_Students.size());
		return rollCall_Students;
	}
	
	
	/**
	 * 取得學生出席資料by課程與
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getRollCall_StudentByClass(String CuName,String cuClass,String CuYear) {
		List<RollCall_StudentVO> rollCall_Student = new ArrayList<RollCall_StudentVO>();
		List<RollCall_StudentVO> rollCall_Students = new ArrayList<RollCall_StudentVO>();
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		int id = 0;
		try {
			rollCall_Student = rollCall_StudentDAO.getAllRollCall_Student();
			CurriculumDAO cdao = new CurriculumDAO(context);
			Curriculum = cdao.getAllCurriculums();
			
			int size = Curriculum.size();
			for (int i = 0; i < size; i++) 
			{
				if (Curriculum.get(i).getCurriculum_NAME().equals(CuName) && Curriculum.get(i).getCurriculum_CLASS().equals(cuClass)&& Curriculum.get(i).getCurriculum_SEASON().equals(CuYear)) {
					 id = Curriculum.get(i).get_id();
					 break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int Rollsize = rollCall_Student.size();
		for (int i = 0; i < Rollsize; i++) 
		{
			if (rollCall_Student.get(i).getRollCall_DateColumn_Curriculum_ID().equals(String.valueOf(id))) {
			rollCall_Students.add(rollCall_Student.get(i));
			}
		}
		Log.i(LOG_ID, "rollCall_Students Size >>" + rollCall_Students.size());
		return rollCall_Students;
	}
	
	/**
	 * 取得學生出席資料by課程與日期
	 * 
	 * @return
	 */
	public List<RollCall_StudentVO> getRollCall_StudentByClassAndDate(String CuName,String cuClass,String CuYear,String date) {
		List<RollCall_StudentVO> rollCall_Student = new ArrayList<RollCall_StudentVO>();
		List<RollCall_StudentVO> rollCall_Students = new ArrayList<RollCall_StudentVO>();
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		int id = 0;
		try {
			rollCall_Student = rollCall_StudentDAO.getAllRollCall_Student();
			CurriculumDAO cdao = new CurriculumDAO(context);
			Curriculum = cdao.getAllCurriculums();
			
			int size = Curriculum.size();
			for (int i = 0; i < size; i++) 
			{
				if (Curriculum.get(i).getCurriculum_NAME().equals(CuName) && Curriculum.get(i).getCurriculum_CLASS().equals(cuClass)&& Curriculum.get(i).getCurriculum_SEASON().equals(CuYear)) {
					 id = Curriculum.get(i).get_id();
					 break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int Rollsize = rollCall_Student.size();
		for (int i = 0; i < Rollsize; i++) 
		{
			if (rollCall_Student.get(i).getRollCall_DateColumn_Curriculum_ID().equals(String.valueOf(id)) && rollCall_Student.get(i).getRollCall_DateColumn_DateID().equals(date)) {
			rollCall_Students.add(rollCall_Student.get(i));
			}
		}
		Log.i(LOG_ID, "rollCall_Students Size >>" + rollCall_Students.size());
		return rollCall_Students;
	}
}
