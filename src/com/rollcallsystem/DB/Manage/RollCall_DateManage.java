package com.rollcallsystem.DB.Manage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.DAO.CurriculumDAO;
import com.rollcallsystem.DB.DAO.RollCall_DateDAO;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;

public class RollCall_DateManage {

	private static final String LOG_ID = "RollCall_DateManage";
	private RollCall_DateDAO rollCall_DateDAO;
	private Context context;
	public RollCall_DateManage()
	{
		super();
	}

	public RollCall_DateManage(Context context)
	{
		super();
		try {
			this.rollCall_DateDAO = new RollCall_DateDAO(context);
			this.context =context;
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}

	/**
	 * 新增出席資料
	 * 
	 * @param RollCall_DateVO
	 * @return
	 */
	public boolean add(RollCall_DateVO RollCall_DateVO) {
		boolean status = false;

		long id = rollCall_DateDAO.addNewRollCall_Date(RollCall_DateVO);
		if (id > 0) {
			status = true;
		}

		return status;
	}

	/**
	 * 更新出席資料
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean update(RollCall_DateVO RollCall_DateVO) {
		boolean status = false;
		long id = rollCall_DateDAO.updateRollCall_Date(RollCall_DateVO);
		if (id > 0) {
			status = true;
		}
		return status;
	}
	
	/**
	 * 刪除出席資料
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean delete(RollCall_DateVO RollCall_DateVO) {
		boolean status = false;
		long id = rollCall_DateDAO.deleteRollCall_Date(RollCall_DateVO);
		status = (id > 0) ? true : false;
		return status;
	}

	/**
	 * 取得所有出席資料
	 * 
	 * @return
	 */
	public List<RollCall_DateVO> getAllRollCall_Date() {
		List<RollCall_DateVO> RollCall_Date = new ArrayList<RollCall_DateVO>();
		try {
			RollCall_Date = rollCall_DateDAO.getAllRollCall_Date();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RollCall_Date;
	}
	
	/**
	 * 取得所有出席資料by UserName
	 * 
	 * @return
	 */
	public List<RollCall_DateVO> getAllRollCall_DateByUserName(String UserName) {
		Log.i(LOG_ID, "RollCall_Dates Size >>" + UserName);
		List<RollCall_DateVO> RollCall_Date = new ArrayList<RollCall_DateVO>();
		List<RollCall_DateVO> RollCall_Dates = new ArrayList<RollCall_DateVO>();
		CurriculumManager curriculumManager = new CurriculumManager(context);
		List<CurriculumVO> ClassName=curriculumManager.getCurriculumByUserName(UserName);
		try {
			RollCall_Date = rollCall_DateDAO.getAllRollCall_Date();
			int size = RollCall_Date.size();
			for (int i = 0; i < size; i++) {
				if (RollCall_Date.get(i).getRollCall_DateColumn_Curriculum_ID()==ClassName.get(0).getCurriculum_ID()) {
					RollCall_Dates.add(RollCall_Date.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RollCall_Dates;
	}
	
	/**
	 * 取得出席資料by ID
	 * 
	 * @return
	 */
	public List<RollCall_DateVO> getRollCall_DateByID(String ID) {
		Log.i(LOG_ID, "RollCall_Dates Size >>" + ID);
		List<RollCall_DateVO> RollCall_Date = new ArrayList<RollCall_DateVO>();
		List<RollCall_DateVO> RollCall_Dates = new ArrayList<RollCall_DateVO>();
		try {
			RollCall_Date = rollCall_DateDAO.getAllRollCall_Date();
			int size = RollCall_Date.size();
			for (int i = 0; i < size; i++) {
				if (RollCall_Date.get(i).get_id()==Integer.valueOf(ID)) {
					RollCall_Dates.add(RollCall_Date.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RollCall_Dates;
	}

	/**
	 * 取得出席資料by開始日期
	 * 
	 * @return
	 */
	public String getRollCall_DateByDate(String StartDate) {
		Log.i(LOG_ID, "RollCall_Dates Size >>" + StartDate);
		List<RollCall_DateVO> RollCall_Date = new ArrayList<RollCall_DateVO>();
		List<RollCall_DateVO> RollCall_Dates = new ArrayList<RollCall_DateVO>();
		try {
			RollCall_Date = rollCall_DateDAO.getAllRollCall_Date();
			int size = RollCall_Date.size();
			for (int i = 0; i < size; i++) {
		//Log.i(LOG_ID, "RollCall_Date.get(i)  >>"+RollCall_Date.get(i).getRollCall_DateColumn_StartDate());
				if (RollCall_Date.get(i).getRollCall_DateColumn_StartDate().equals(StartDate)) {
					// Log.i(LOG_ID, "CurriculumLists getCurriculum_SEASON >>"
					// +CurriculumList.get(i).getCurriculum_STD_Data());
					RollCall_Dates.add(RollCall_Date.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i(LOG_ID, "RollCall_Dates Size >>" + RollCall_Dates.size());
		return String.valueOf(RollCall_Dates.get(0).get_id());
	}
	
	/**
	 * 取得出席資料by課程
	 * 
	 * @return
	 */
	public List<RollCall_DateVO> getRollCall_DateByCu(String CuName,String cuClass,String cuYear) {
		Log.i("rollcall_Datemanager", "1");
		List<RollCall_DateVO> RollCall_Date = new ArrayList<RollCall_DateVO>();
		List<RollCall_DateVO> RollCall_Dates = new ArrayList<RollCall_DateVO>();
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		Log.i("rollcall_Datemanager", "2");
		int id = 0;
		try {
			RollCall_Date = rollCall_DateDAO.getAllRollCall_Date();
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
		Log.i("rollcall_Datemanager", "3");
		int Rollsize = RollCall_Date.size();
		for (int i = 0; i < Rollsize; i++) 
		{
			if (RollCall_Date.get(i).getRollCall_DateColumn_Curriculum_ID().equals(String.valueOf(id))) {
				RollCall_Dates.add(RollCall_Date.get(i));
			}
		}
		Log.i(LOG_ID, "RollCall_Dates Size >>" + RollCall_Dates.size());
		return RollCall_Dates;
	}

}
