package com.rollcallsystem.DB.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class CurriculumVO implements Parcelable{
	
	
	private int _id;
	private String Curriculum_INSTRUCTORS;
	private String Curriculum_NAME;
	private String Curriculum_ID;
	private String Curriculum_CLASS;
	private String Curriculum_HOURS;
	private String Curriculum_SEASON;
	private String Curriculum_STD_Data;

	
	

	public CurriculumVO(){
		super();
	}
	
	public CurriculumVO(int _id,String curriculum_INSTRUCTORS,String curriculum_NAME,String curriculum_ID,String curriculum_CLASS
			,String curriculum_HOURS,String curriculum_SEASON,String curriculum_STD_Data){
		super();
		this._id=_id;
		this.Curriculum_INSTRUCTORS=curriculum_INSTRUCTORS;
		this.Curriculum_NAME=curriculum_NAME;
		this.Curriculum_ID=curriculum_ID;
		this.Curriculum_CLASS=curriculum_CLASS;
		this.Curriculum_HOURS=curriculum_HOURS;
		this.Curriculum_SEASON=curriculum_SEASON;
		this.Curriculum_STD_Data=curriculum_STD_Data;
	}
	
	private CurriculumVO(Parcel in)
	{
		super();
		this._id=in.readInt();
		this.Curriculum_INSTRUCTORS=in.readString();
		this.Curriculum_NAME=in.readString();
		this.Curriculum_ID=in.readString();
		this.Curriculum_CLASS=in.readString();
		this.Curriculum_HOURS=in.readString();
		this.Curriculum_SEASON=in.readString();
		this.Curriculum_STD_Data=in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getCurriculum_INSTRUCTORS());
		parcel.writeString(getCurriculum_NAME());
		parcel.writeString(getCurriculum_ID());
		parcel.writeString(getCurriculum_CLASS());
		parcel.writeString(getCurriculum_HOURS());
		parcel.writeString(getCurriculum_SEASON());
		parcel.writeString(getCurriculum_STD_Data());
		
	}
	
	public static final Parcelable.Creator<CurriculumVO> CREATOR = new Parcelable.Creator<CurriculumVO>() {
		public CurriculumVO createFromParcel(Parcel in) {
			return new CurriculumVO(in);
		}

		public CurriculumVO[] newArray(int size) {
			return new CurriculumVO[size];
		}
	};
	
	@Override
	public String toString() {
		return "CurriculumVO [_id=" + _id + ", Curriculum_INSTRUCTORS=" + Curriculum_INSTRUCTORS + ", Curriculum_NAME=" + Curriculum_NAME + ", Curriculum_ID="
				+ Curriculum_ID + ", Curriculum_CLASS=" + Curriculum_CLASS + ", Curriculum_HOURS=" + Curriculum_HOURS +", Curriculum_SEASON=" + Curriculum_SEASON +", Curriculum_STD_Data=" + Curriculum_STD_Data +"]";
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getCurriculum_INSTRUCTORS() {
		return Curriculum_INSTRUCTORS;
	}

	public void setCurriculum_INSTRUCTORS(String curriculum_INSTRUCTORS) {
		Curriculum_INSTRUCTORS = curriculum_INSTRUCTORS;
	}

	public String getCurriculum_NAME() {
		return Curriculum_NAME;
	}

	public void setCurriculum_NAME(String curriculum_NAME) {
		Curriculum_NAME = curriculum_NAME;
	}

	public String getCurriculum_ID() {
		return Curriculum_ID;
	}

	public void setCurriculum_ID(String curriculum_ID) {
		Curriculum_ID = curriculum_ID;
	}

	public String getCurriculum_CLASS() {
		return Curriculum_CLASS;
	}

	public void setCurriculum_CLASS(String curriculum_CLASS) {
		Curriculum_CLASS = curriculum_CLASS;
	}

	public String getCurriculum_HOURS() {
		return Curriculum_HOURS;
	}

	public void setCurriculum_HOURS(String curriculum_HOURS) {
		Curriculum_HOURS = curriculum_HOURS;
	}

	public String getCurriculum_SEASON() {
		return Curriculum_SEASON;
	}

	public void setCurriculum_SEASON(String curriculum_SEASON) {
		Curriculum_SEASON = curriculum_SEASON;
	}
	public String getCurriculum_STD_Data() {
		return Curriculum_STD_Data;
	}

	public void setCurriculum_STD_Data(String curriculum_STD_Data) {
		Curriculum_STD_Data = curriculum_STD_Data;
	}

}
