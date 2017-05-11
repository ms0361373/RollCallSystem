package com.rollcallsystem.DB.Manage;

import java.util.ArrayList;
import java.util.List;

import com.rollcallsystem.DB.DAO.SeasonYearDAO;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;

import android.content.Context;
import android.util.Log;

public class SeasonYearManager {

	private static final String LOG_ID = "SeasonYearManager";
	private SeasonYearDAO seasonYearDAO;

	public SeasonYearManager() {
		super();
	}

	public SeasonYearManager(Context context) {
		super();
		try {
			this.seasonYearDAO = new SeasonYearDAO(context);
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}

	public SeasonYearManager(SeasonYearDAO seasonYearDAO) {
		super();
		this.seasonYearDAO = seasonYearDAO;
	}

	/**
	 * 新增學期學年度
	 * 
	 * @param StudentVo
	 * @return
	 */
	public boolean add(SeasonYearVO SeasonYearVO) {
		// Log.i(LOG_ID, "add is start >>>");
		boolean status = false;

		long id = seasonYearDAO.addNewSeasonYear(SeasonYearVO);
		// Log.i(LOG_ID, "id=" + id);
		if (id > 0) {
			status = true;
		}

		return status;
	}

	public boolean delete(SeasonYearVO SeasonYearVO) {
		// Log.i(LOG_ID, "add is start >>>");
		boolean status = false;

		long id = seasonYearDAO.deleteSeasonYear(SeasonYearVO);
		// Log.i(LOG_ID, "id=" + id);
		if (id > 0) {
			status = true;
		}

		return status;
	}

	/**
	 * 取得所有學期學年度
	 * 
	 * @return
	 */
	public List<SeasonYearVO> getAllSeasonYear() {
		List<SeasonYearVO> SeasonYear = new ArrayList<SeasonYearVO>();
		try {
			SeasonYear = seasonYearDAO.getAllSeasonYears();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SeasonYear;
	}
}
