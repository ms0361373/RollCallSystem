package com.rollcallsystem.DB.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentVO implements Parcelable{
	
	private int _id;
	private String Student_NAME;
	private String Student_ID;
	private String Student_CARD_ID;

	public StudentVO(){
	super();
	}
	
	public StudentVO(int _id,String student_NAME,String student_ID,String student_CARD_ID){
		super();
		this._id=_id;
		this.Student_NAME=student_NAME;
		this.Student_ID=student_ID;
		this.Student_CARD_ID=student_CARD_ID;
	}
	
	private StudentVO(Parcel in) {
		super();
		this._id = in.readInt();
		this.Student_NAME = in.readString();
		this.Student_ID = in.readString();
		this.Student_CARD_ID = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getStudent_NAME());
		parcel.writeString(getStudent_ID());
		parcel.writeString(getStudent_CARD_ID());
		
	}
	
	public static final Parcelable.Creator<StudentVO> CREATOR = new Parcelable.Creator<StudentVO>() {
		public StudentVO createFromParcel(Parcel in) {
			return new StudentVO(in);
		}

		public StudentVO[] newArray(int size) {
			return new StudentVO[size];
		}
	};
	
	@Override
	public String toString() {
		return "StudentVO [_id=" + _id + ", Student_NAME=" + Student_NAME + ", Student_ID=" + Student_ID + ", Student_CARD_ID="
				+ Student_CARD_ID + "]";
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getStudent_NAME() {
		return Student_NAME;
	}

	public void setStudent_NAME(String student_NAME) {
		Student_NAME = student_NAME;
	}

	public String getStudent_ID() {
		return Student_ID;
	}

	public void setStudent_ID(String student_ID) {
		Student_ID = student_ID;
	}

	public String getStudent_CARD_ID() {
		return Student_CARD_ID;
	}

	public void setStudent_CARD_ID(String student_CARD_ID) {
		Student_CARD_ID = student_CARD_ID;
	}

}
