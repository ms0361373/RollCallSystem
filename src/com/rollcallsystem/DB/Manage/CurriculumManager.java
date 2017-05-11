package com.rollcallsystem.DB.Manage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.DAO.CurriculumDAO;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;



public class CurriculumManager {
	
	private static final String LOG_ID = "CurriculumManager";
	private CurriculumDAO curriculumDAO;

	public CurriculumManager()
	{
		super();
	}
	
	public CurriculumManager(Context context)
	{
		super();
		try {
			this.curriculumDAO = new CurriculumDAO(context);
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}
	
	public CurriculumManager(CurriculumDAO curriculumDAO)
	{
		super();
		this.curriculumDAO = curriculumDAO;
	}
	
	/**
	 * 新增課程
	 * 
	 * @param StudentVo
	 * @return
	 */
	public boolean add(CurriculumVO CurriculumVo) {
		boolean status = false;
		
			long id = curriculumDAO.addNewCurriculum(CurriculumVo);
			if (id > 0) {
				status = true;
			}
			
		return status;
	}
	
	public boolean delete(CurriculumVO CurriculumVo) {
		boolean status = false;
		
			long id = curriculumDAO.deleteCurriculum(CurriculumVo);
			if (id > 0) {
				status = true;
			}
			
		return status;
	}
	
	/**
	 * 更新使用者
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean update(CurriculumVO CurriculumVo) {
		boolean status = false;
		long id = curriculumDAO.updateUser(CurriculumVo);
		if (id > 0) {
			status = true;
		}
		return status;
	}
	
	/**
	 * 取得所有課程資料
	 * 
	 * @return
	 */
	public List<CurriculumVO> getAllCurriculum() {
		List<CurriculumVO> Curriculum = new ArrayList<CurriculumVO>();
		try {
			Curriculum = curriculumDAO.getAllCurriculums();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Curriculum;
	}
	
	/**
	 * 取得課程資料  by UserName
	 * 
	 * @param User_CardID
	 * @return
	 */
	public List<CurriculumVO> getCurriculumByUserName(String UserName) {
		//CurriculumVO Curriculum = new CurriculumVO();
		List<CurriculumVO> CurriculumList = new ArrayList<CurriculumVO>();
		List<CurriculumVO> CurriculumLists = new ArrayList<CurriculumVO>();
		try {
			 CurriculumList = curriculumDAO.getAllCurriculums();
			int size =CurriculumList.size();
			for (int i = 0; i < size; i++) 
				if (CurriculumList.get(i).getCurriculum_INSTRUCTORS().equals(UserName)) {
				//	Log.i(LOG_ID, "CurriculumLists getCurriculum_SEASON >>" +CurriculumList.get(i).getCurriculum_STD_Data());
					CurriculumLists.add(CurriculumList.get(i));
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.size());
		return CurriculumLists;
	}
	
	/**
	 * 取得課程資料  by ClassName
	 * @param ClassName
	 * @param Class
	 * @return
	 */
	public List<CurriculumVO> getCurriculumByClassName(String ClassName,String Class,String Year) {
		//CurriculumVO Curriculum = new CurriculumVO();
		List<CurriculumVO> CurriculumList = new ArrayList<CurriculumVO>();
		List<CurriculumVO> CurriculumLists = new ArrayList<CurriculumVO>();
		try {
			 CurriculumList = curriculumDAO.getAllCurriculums();
			int size =CurriculumList.size();
			for (int i = 0; i < size; i++) 
				if (CurriculumList.get(i).getCurriculum_NAME().equals(ClassName) && CurriculumList.get(i).getCurriculum_CLASS().equals(Class)&& CurriculumList.get(i).getCurriculum_SEASON().equals(Year)) {
					CurriculumLists.add(CurriculumList.get(i));
				
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.size());
		//Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.get(1).getCurriculum_STD_Data());
		return CurriculumLists;
	}
	
	
	
	/**
	 * 取得課程資料  by ClassName
	 * @param ClassName
	 * @param Class
	 * @return
	 */
	public List<CurriculumVO> getCurriculumByClassName2(String ClassName,String Class,String Year) {
		//CurriculumVO Curriculum = new CurriculumVO();
		List<CurriculumVO> CurriculumList = new ArrayList<CurriculumVO>();
		List<CurriculumVO> CurriculumLists = new ArrayList<CurriculumVO>();
		try {
			 CurriculumList = curriculumDAO.getAllCurriculums();
			int size =CurriculumList.size();
			for (int i = 0; i < size; i++) 
				if (CurriculumList.get(i).getCurriculum_NAME().equals(ClassName) && CurriculumList.get(i).getCurriculum_CLASS().equals(Class)&& CurriculumList.get(i).getCurriculum_SEASON().equals(Year)) {
					CurriculumLists.add(CurriculumList.get(i));
					break;
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.size());
		//Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.get(1).getCurriculum_STD_Data());
		return CurriculumLists;
	}
	
	
	/**
	 * 確認有無課程資料  by Name
	 * @param UserName
	 * @return
	 */
	public boolean getCurriculumCheck (String UserName) {
		boolean status = false;
		//Log.i(LOG_ID, "CARD_ID>>"+UserName);
		try {
			List<CurriculumVO> List = curriculumDAO.getAllCurriculums();
			//Log.i(LOG_ID, "ListSize>>"+List.size());
			for (int i = 0; i < List.size(); i++) {
				//Log.i(LOG_ID, "cardid>>>"+List.get(i).getCurriculum_INSTRUCTORS());
				if (List.get(i).getCurriculum_INSTRUCTORS().equals(UserName)) {
						status = true;
				}
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "getCount error");
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 確認有無課程資料  by CuId
	 * @param CuId
	 * @return
	 */
	public List<CurriculumVO> getCurriculumByCuId (String CuID) {
	//CurriculumVO Curriculum = new CurriculumVO();
			List<CurriculumVO> CurriculumList = new ArrayList<CurriculumVO>();
			List<CurriculumVO> CurriculumLists = new ArrayList<CurriculumVO>();
			try {
				 CurriculumList = curriculumDAO.getAllCurriculums();
				int size =CurriculumList.size();
				int id =Integer.valueOf(CuID);
				for (int i = 0; i < size; i++) 
					if (CurriculumList.get(i).get_id()==id) {
						CurriculumLists.add(CurriculumList.get(i));
					}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.size());
			//Log.i(LOG_ID, "CurriculumLists Size >>" +CurriculumLists.get(1).getCurriculum_STD_Data());
			return CurriculumLists;
	}
}
