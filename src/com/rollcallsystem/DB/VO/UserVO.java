package com.rollcallsystem.DB.VO;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class UserVO implements Parcelable {
	private int _id;
	private String USER_NAME;
	private String USER_ID;
	private String USER_CARD_ID;
	private String USER_EMAIL;
	
	

	public UserVO() {
		super();
	}
	
	public UserVO(int _id,String uSER_NAME,String uSER_ID,String uSER_CARD_ID,String uSER_EMAIL) {
		super();
		this._id=_id;
		this.USER_NAME=uSER_NAME;
		this.USER_ID=uSER_ID;
		this.USER_CARD_ID=uSER_CARD_ID;
		this.USER_EMAIL=uSER_EMAIL;
	}

	private UserVO(Parcel in) {
		super();
		this._id = in.readInt();
		this.USER_NAME = in.readString();
		this.USER_ID = in.readString();
		this.USER_CARD_ID = in.readString();
		this.USER_EMAIL = in.readString();
	}
	
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getUSER_NAME());
		parcel.writeString(getUSER_ID());
		parcel.writeString(getUSER_CARD_ID());
		parcel.writeString(getUSER_EMAIL());
		
	}
	
	public static final Parcelable.Creator<UserVO> CREATOR = new Parcelable.Creator<UserVO>() {
		public UserVO createFromParcel(Parcel in) {
			return new UserVO(in);
		}

		public UserVO[] newArray(int size) {
			return new UserVO[size];
		}
	};
	
	@Override
	public String toString() {
		return "UserVO [_id=" + _id + ", USER_NAME=" + USER_NAME + ", USER_ID=" + USER_ID + ", USER_CARD_ID="
				+ USER_CARD_ID + ", USER_EMAIL=" + USER_EMAIL + "]";
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String i) {
		USER_ID = i;
	}

	public String getUSER_CARD_ID() {
		return USER_CARD_ID;
	}

	public void setUSER_CARD_ID(String uSER_CARD_ID) {
		USER_CARD_ID = uSER_CARD_ID;
	}

	public String getUSER_EMAIL() {
		return USER_EMAIL;
	}

	public void setUSER_EMAIL(String uSER_EMAIL) {
		USER_EMAIL = uSER_EMAIL;
	}

}
