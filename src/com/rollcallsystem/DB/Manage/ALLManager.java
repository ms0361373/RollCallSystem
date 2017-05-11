package com.rollcallsystem.DB.Manage;


import com.rollcallsystem.DB.DAO.UserDAO;

import android.content.Context;



public class ALLManager {
	private UserManager userManager;
	
	public ALLManager(Context context) throws Exception{
		userManager=new UserManager(context);
	}
	public UserManager getUserManager() {
		return userManager;
	}
	
}
