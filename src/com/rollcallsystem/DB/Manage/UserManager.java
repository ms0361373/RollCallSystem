package com.rollcallsystem.DB.Manage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.DAO.UserDAO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.spf.manage.SPFManager;

public class UserManager {
	private static final String LOG_ID = "UserManager";
	private UserDAO userDAO;
	private SPFManager spfManager;
	public UserManager() {
		super();
	}
	public UserManager(Context context) {
		super();
		try {
			this.userDAO = new UserDAO(context);
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}
	
	public UserManager(Context context, SPFManager spfManager) {
		super();
		try {
			this.userDAO = new UserDAO(context);
			this.spfManager = spfManager;
		} catch (Exception e) {
			Log.e(LOG_ID, e.getMessage());
		}
	}
	
	public UserManager(UserDAO userDAO) {
		super();
		this.userDAO = userDAO;
	}

	/**
	 * 取得使用者  by cardID
	 * 
	 * @param User_CardID
	 * @return
	 */
	public UserVO getUserByCardID(String User_CardID) {
		UserVO user = new UserVO();
		try {
			List<UserVO> List = userDAO.getAllUsers();
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).getUSER_CARD_ID().equals(User_CardID)) {
					user = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * 取得使用者  by cardID
	 * 
	 * @param User_CardID
	 * @return
	 */
	public UserVO getUserByUserID(String UserID) {
		UserVO user = new UserVO();
		try {
			List<UserVO> List = userDAO.getAllUsers();
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).getUSER_ID().equals(UserID)) {
					user = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 取得所有使用者
	 * 
	 * @return
	 */
	public List<UserVO> getAllUser() {
		List<UserVO> user = new ArrayList<UserVO>();
		try {
			user = userDAO.getAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 取得使用者 by id
	 * 
	 * @param id
	 * @return
	 */
	public UserVO getUserById(int id) {
		UserVO user = new UserVO();
		try {
			List<UserVO> List = userDAO.getAllUsers();
//			Log.i(LOG_ID, "ListSize >>>"+List.size());
			for (int i = 0; i < List.size(); i++) {
				if (List.get(i).get_id()==id) {
//					Log.i(LOG_ID, "user = List.get(i)");
					user = List.get(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 取得使用者 by Name
	 * 
	 * @param Name
	 * @return
	 */
	public List<UserVO> getUserByName(String Name) {
		List<UserVO> user = new ArrayList<UserVO>();
		try {
			user = userDAO.getAllUsers();
			int size = user.size();
			int count = 0;
			for (int i = 0; i < size; i++) {
				if (Name != null && !Name.equals("")) {
					if (!user.get(i - count).getUSER_NAME().equals(Name)) {
						user.remove(i - count);
						count++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 新增使用者
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean add(UserVO userVo) {
		Log.i(LOG_ID, "add is start >>>");
		boolean status = false;
		int usercount = userDAO.getCount(userVo.getUSER_ID(), userVo.getUSER_CARD_ID());
		if(usercount==0){
			long id = userDAO.addNewUser(userVo);
			Log.i(LOG_ID, "id=" + id);
			if (id > 0) {
				status = true;
			}
			
		}else{status = false;}
		return status;
	}

	/**
	 * 更新使用者
	 * 
	 * @param userVo
	 * @return
	 */
	public boolean update(UserVO userVo) {
		boolean status = false;
		long id = userDAO.updateUser(userVo);
		if (id > 0) {
			status = true;
		}
		return status;
	}
	
	
	public boolean login (String CARD_ID) {
		boolean status = false;
		//Log.i(LOG_ID, "CARD_ID>>"+CARD_ID);
		try {
			List<UserVO> List = userDAO.getAllUsers();
			//Log.i(LOG_ID, "ListSize>>"+List.size());
			for (int i = 0; i < List.size(); i++) {
				//Log.i(LOG_ID, "cardid>>>"+List.get(i).getUSER_CARD_ID());
				if (List.get(i).getUSER_CARD_ID().equals(CARD_ID)) {
						status = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean loginByUserID (String UserID,String Email) {
		boolean status = false;
		//Log.i(LOG_ID, "CARD_ID>>"+CARD_ID);
		try {
			List<UserVO> List = userDAO.getAllUsers();
			//Log.i(LOG_ID, "ListSize>>"+List.size());
			for (int i = 0; i < List.size(); i++) {
				//Log.i(LOG_ID, "cardid>>>"+List.get(i).getUSER_CARD_ID());
				if (List.get(i).getUSER_ID().equals(UserID) && List.get(i).getUSER_EMAIL().equals(Email)) {
						status = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public UserVO getLoginUser() {
		int id = Integer.parseInt(spfManager.load(UserColumn.TABLE_NAME+UserColumn._ID));
		Log.i(LOG_ID, "id >>>"+id);
		return getUserById(id);
	}

}
