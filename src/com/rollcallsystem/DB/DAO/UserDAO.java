package com.rollcallsystem.DB.DAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.VO.UserVO;

public class UserDAO extends UserDBDAO {
	public UserDAO(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static final String TB_NAME = UserColumn.TABLE_NAME;
	private static final String _ID = UserColumn._ID;
	private static final String LOG_ID = "UserDAO";

	/**
	 * 新增使用者
	 * @param user
	 * @return
	 */
	public long addNewUser(UserVO user) {
		Log.i(LOG_ID, "addNewUser is start >>>");
		// long id = 0;
		ContentValues values = new ContentValues();
		values.put(UserColumn.USER_NAME, user.getUSER_NAME());
		values.put(UserColumn.USER_ID, user.getUSER_ID());
		values.put(UserColumn.USER_CARD_ID, user.getUSER_CARD_ID());
		values.put(UserColumn.USER_EMAIL, user.getUSER_EMAIL());
		return database.insert(TB_NAME, null, values);
	}
	
	/**
	 * 更新使用者
	 * @param user
	 * @return
	 */
	public long updateUser(UserVO user) {
		Log.i(LOG_ID, "updateUser is start >>>");
		ContentValues values = new ContentValues();
		Log.i(LOG_ID, "values =" + values.toString());

		int updateCount = 0;
		Log.d(LOG_ID, "UserId:" + user.get_id());
		
		values.put(UserColumn.USER_NAME, user.getUSER_NAME());
		values.put(UserColumn.USER_ID, user.getUSER_ID());
		values.put(UserColumn.USER_CARD_ID, user.getUSER_CARD_ID());
		values.put(UserColumn.USER_EMAIL, user.getUSER_EMAIL());
		try {
			//updateCount = database.update(TB_NAME, values, UserColumn.USER_NAME + "="+ user.getUSER_NAME(), null);
			updateCount = database.update(TB_NAME, values, UserColumn.USER_NAME + "='" + user.getUSER_NAME()+"'", null);
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
	 * 刪除使用者
	 * @param user
	 * @return
	 */
	public int deleteUser(UserVO user) {
		Log.i(LOG_ID, "delete is start >>>");
		int deleteCount = 0;
		try {
			deleteCount = database.delete(TB_NAME, _ID,
					new String[] { String.valueOf(user.get_id()) });
			if (deleteCount <= 0) {
				Log.d(LOG_ID, "no data delete, values= " + user.toString());
			}
		} catch (Exception e) {
			Log.e(LOG_ID, "delete fail , " + e.getMessage());
		} finally {
			Log.i(LOG_ID, "delete end >>>");
		}
		return deleteCount;
	}
	
	/**
	 * 取得所有資料
	 * 
	 * @return
	 * @throws ParseException
	 */
	public List<UserVO> getAllUsers() throws ParseException {
		List<UserVO> userList = new ArrayList<UserVO>();
		Cursor cursor = database.query(TB_NAME, null, null, null, null, null,
				null);
//		Log.i(LOG_ID, "cursorSize>>"+cursor.getCount());
		while (cursor.moveToNext()) {
			UserVO user = new UserVO();
			user.set_id(cursor.getInt(cursor.getColumnIndex(UserColumn._ID)));
			user.setUSER_NAME(cursor.getString(cursor.getColumnIndex(UserColumn.USER_NAME)));
			user.setUSER_ID(cursor.getString(cursor.getColumnIndex(UserColumn.USER_ID)));
			user.setUSER_EMAIL(cursor.getString(cursor.getColumnIndex(UserColumn.USER_EMAIL)));
			user.setUSER_CARD_ID(cursor.getString(cursor.getColumnIndex(UserColumn.USER_CARD_ID)));
			userList.add(user);
		}
		cursor.close();
		return userList;
	}
	
	/**
	 * 檢查是否有相同使用者
	 * 
	 * @param userID
	 *            使用者ID
	 * @param userCardID
	 *            CardID
	 * @return 資料筆數
	 */
	public int getCount(String userID,String userCardID) {
		int result = 0;
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + UserColumn.TABLE_NAME + " WHERE "
		+ UserColumn.USER_ID+ "='" + userID + "' AND " 
				+ UserColumn.USER_CARD_ID+ "='"
				+ userCardID + "'", null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		Log.i(LOG_ID, "getCount = " + result);
		cursor.close();
		return result;
	}

}
