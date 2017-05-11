/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.rollcallsystem.spf.manage;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SPFManager implements SPFInterface {

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;

	private SPFConfig config = new SPFConfig();
	private Context context;

	public SPFManager(Context context) {
		this.context = context;
		sharedPref = context.getSharedPreferences(SPFConfig.SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	@Override
	public boolean save(String Key, String Value) {
		boolean status = false;
		try {
			status = editor.putString(Key, Value).commit();
			Log.i("SPF", "save success , "+Key+" = " + Value);
		} catch (Exception e) {
			Log.i("SPF", "save fail = " + e.getMessage());
		}
		return status;
	}
	
	public boolean save(String Key, int Value) {
		return save(Key,String.valueOf(Value));
	}

	/**
	 * get Data By Key
	 */
	@Override
	public String load(String Key) {
		String value = "";
		try {
			value = sharedPref.getString(Key, "");//就是卡這行
			Log.i("SPF", "load success , "+Key+" = " + value);
		} catch (Exception e) {
			Log.i("SPF", "load fail = " + e.getMessage());
		}
		return value;
	}

	@Override
	public boolean remove(String Key) {

		boolean status = false;
		try {
			status = editor.remove(Key).commit();
			Log.i("SPF", "remove success , Key ="+Key);
			
		} catch (Exception e) {
			Log.i("SPF", "save fail = " + e.getMessage());
		}
		return status;
	}

	@Override
	public void removeAll() {
		try {
			editor.clear().commit();
			Log.i("SPF", "remove all success !");
		} catch (Exception e) {
			Log.i("SPF", "save fail = " + e.getMessage());
		}
	}
}
