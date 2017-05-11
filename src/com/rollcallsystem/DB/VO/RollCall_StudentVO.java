package com.rollcallsystem.DB.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class RollCall_StudentVO implements Parcelable{
	
	private int _id;
	private String RollCall_DateColumn_Curriculum_ID;
	private String RollCall_DateColumn_StudentID;
	private String RollCall_DateColumn_DateID;
	private String RollCall_DateColumn_StudentRollCallDate;
	private String RollCall_DateColumn_AttendanceRate;

	

	public RollCall_StudentVO(){
		super();
	}
	
	public RollCall_StudentVO(int _id,String rollCall_DateColumn_Curriculum_ID,String rollCall_DateColumn_StudentID,String rollCall_DateColumn_DateID,String rollCall_DateColumn_StudentRollCallDate,String rollCall_DateColumn_AttendanceRate){
		super();
		this._id=_id;
		this.RollCall_DateColumn_Curriculum_ID=rollCall_DateColumn_Curriculum_ID;
		this.RollCall_DateColumn_StudentID=rollCall_DateColumn_StudentID;
		this.RollCall_DateColumn_DateID=rollCall_DateColumn_DateID;
		this.RollCall_DateColumn_StudentRollCallDate=rollCall_DateColumn_StudentRollCallDate;
		this.RollCall_DateColumn_AttendanceRate = rollCall_DateColumn_AttendanceRate;
	}
	
	private RollCall_StudentVO(Parcel in)
	{
		super();
		this._id=in.readInt();
		this.RollCall_DateColumn_Curriculum_ID=in.readString();
		this.RollCall_DateColumn_StudentID=in.readString();
		this.RollCall_DateColumn_DateID=in.readString();
		this.RollCall_DateColumn_StudentRollCallDate=in.readString();
		this.RollCall_DateColumn_AttendanceRate=in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getRollCall_DateColumn_Curriculum_ID());
		parcel.writeString(getRollCall_DateColumn_DateID());
		parcel.writeString(getRollCall_DateColumn_StudentID());
		parcel.writeString(getRollCall_DateColumn_StudentRollCallDate());
		parcel.writeString(getRollCall_DateColumn_AttendanceRate());
	}
	
	public static final Parcelable.Creator<RollCall_StudentVO> CREATOR = new Parcelable.Creator<RollCall_StudentVO>() {
		public RollCall_StudentVO createFromParcel(Parcel in) {
			return new RollCall_StudentVO(in);
		}

		public RollCall_StudentVO[] newArray(int size) {
			return new RollCall_StudentVO[size];
		}
	};
	
	@Override
	public String toString() {
		return "RollCall_StudentVO [_id=" + _id + ", RollCall_DateColumn_Curriculum_ID=" + RollCall_DateColumn_Curriculum_ID + ", RollCall_DateColumn_StudentID=" + RollCall_DateColumn_StudentID + ", RollCall_DateColumn_DateID="
				+ RollCall_DateColumn_DateID + ", RollCall_DateColumn_StudentRollCallDate=" + RollCall_DateColumn_StudentRollCallDate + ", RollCall_DateColumn_AttendanceRate=" + RollCall_DateColumn_AttendanceRate + "]";
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getRollCall_DateColumn_Curriculum_ID() {
		return RollCall_DateColumn_Curriculum_ID;
	}

	public void setRollCall_DateColumn_Curriculum_ID(String rollCall_DateColumn_Curriculum_ID) {
		RollCall_DateColumn_Curriculum_ID = rollCall_DateColumn_Curriculum_ID;
	}

	public String getRollCall_DateColumn_StudentID() {
		return RollCall_DateColumn_StudentID;
	}

	public void setRollCall_DateColumn_StudentID(String rollCall_DateColumn_StudentID) {
		RollCall_DateColumn_StudentID = rollCall_DateColumn_StudentID;
	}

	public String getRollCall_DateColumn_DateID() {
		return RollCall_DateColumn_DateID;
	}

	public void setRollCall_DateColumn_DateID(String rollCall_DateColumn_DateID) {
		RollCall_DateColumn_DateID = rollCall_DateColumn_DateID;
	}

	public String getRollCall_DateColumn_StudentRollCallDate() {
		return RollCall_DateColumn_StudentRollCallDate;
	}

	public void setRollCall_DateColumn_StudentRollCallDate(String rollCall_DateColumn_StudentRollCallDate) {
		RollCall_DateColumn_StudentRollCallDate = rollCall_DateColumn_StudentRollCallDate;
	}
	
	public String getRollCall_DateColumn_AttendanceRate() {
		return RollCall_DateColumn_AttendanceRate;
	}

	public void setRollCall_DateColumn_AttendanceRate(String rollCall_DateColumn_AttendanceRate) {
		RollCall_DateColumn_AttendanceRate = rollCall_DateColumn_AttendanceRate;
	}

}
