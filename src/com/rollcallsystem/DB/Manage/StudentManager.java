package com.rollcallsystem.DB.Manage;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.DAO.StudentDAO;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;

public class StudentManager {
	private static final String LOG_ID = "StudentManager";
	private  StudentDAO studentDAO;
	public StudentManager()
	{
		super();
	}
	public StudentManager(Context context) {
		super();
		try {
			this.studentDAO = new StudentDAO(context);
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}
	public StudentManager(StudentDAO studentDAO)
	{
		super();
		this.studentDAO = studentDAO;
	}
	
	/**
	 * 新增學生
	 * 
	 * @param StudentVo
	 * @return
	 */
	public boolean add(StudentVO StudentVo) {
		Log.i(LOG_ID, "add is start >>>");
		boolean status = false;
		
			long id = studentDAO.addNewStudent(StudentVo);
			Log.i(LOG_ID, "id=" + id);
			if (id > 0) {
				status = true;
			}
			
		return status;
	}
	
	/**
	 * 取得使用者  by StudentName
	 * 
	 * @param StudentName
	 * @return
	 */
	public StudentVO getStudentByName(String StudentName) {
		StudentVO student = new StudentVO();
		try {
			List<StudentVO> List = studentDAO.getAllUsers();
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).getStudent_CARD_ID().equals(StudentName)) {
					student = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}
	
	/**
	 * 取得使用者  by StudentID
	 * 
	 * @param StudentID
	 * @return
	 */
	public StudentVO getStudentByID(String StudentID) {
		StudentVO student = new StudentVO();
		try {
			List<StudentVO> List = studentDAO.getAllUsers();
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).getStudent_ID().equals(StudentID)) {
					student = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}
	
	
	/**
	 * 取得使用者  by StudentCard_ID
	 * 
	 * @param StudentCard_ID
	 * @return
	 */
	public StudentVO getStudentByCard_ID(String StudentCard_ID) {
		StudentVO student = new StudentVO();
		try {
			List<StudentVO> List = studentDAO.getAllUsers();
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).getStudent_CARD_ID().equals(StudentCard_ID)) {
					student = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}
	
}
