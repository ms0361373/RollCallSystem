package com.rollcallsystem.DB.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class RollCall_DateVO implements Parcelable {

	private int _id;
	private String RollCall_DateColumn_Curriculum_ID;
	private String RollCall_DateColumn_StartDate;
	private String RollCall_DateColumn_EndDate;
	private boolean ISUPDATE;

	public RollCall_DateVO() {
		super();
	}

	public RollCall_DateVO(int _id, String rollCall_DateColumn_Curriculum_ID, String rollCall_DateColumn_StartDate, String rollCall_DateColumn_EndDate, boolean iSUPDATE) {
		super();
		this._id = _id;
		this.RollCall_DateColumn_Curriculum_ID = rollCall_DateColumn_Curriculum_ID;
		this.RollCall_DateColumn_StartDate = rollCall_DateColumn_StartDate;
		this.RollCall_DateColumn_EndDate = rollCall_DateColumn_EndDate;
		this.ISUPDATE = iSUPDATE;
	}

	private RollCall_DateVO(Parcel in)
	{
		super();
		this._id = in.readInt();
		this.RollCall_DateColumn_Curriculum_ID = in.readString();
		this.RollCall_DateColumn_StartDate = in.readString();
		this.RollCall_DateColumn_EndDate = in.readString();
		this.ISUPDATE = in.readByte() != 0;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getRollCall_DateColumn_Curriculum_ID());
		parcel.writeString(getRollCall_DateColumn_StartDate());
		parcel.writeString(getRollCall_DateColumn_EndDate());
		parcel.writeByte((byte) (isISUPDATE() ? 1 : 0));

	}

	public static final Parcelable.Creator<RollCall_DateVO> CREATOR = new Parcelable.Creator<RollCall_DateVO>() {
		public RollCall_DateVO createFromParcel(Parcel in) {
			return new RollCall_DateVO(in);
		}

		public RollCall_DateVO[] newArray(int size) {
			return new RollCall_DateVO[size];
		}
	};

	@Override
	public String toString() {
		return "RollCall_DateVO [_id=" + _id + ", RollCall_DateColumn_Curriculum_ID=" + RollCall_DateColumn_Curriculum_ID + ", RollCall_DateColumn_StartDate=" + RollCall_DateColumn_StartDate + ", RollCall_DateColumn_EndDate="
				+ RollCall_DateColumn_EndDate + ", ISUPDATE="
				+ ISUPDATE + "]";
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

	public String getRollCall_DateColumn_StartDate() {
		return RollCall_DateColumn_StartDate;
	}

	public void setRollCall_DateColumn_StartDate(String rollCall_DateColumn_StartDate) {
		RollCall_DateColumn_StartDate = rollCall_DateColumn_StartDate;
	}

	public String getRollCall_DateColumn_EndDate() {
		return RollCall_DateColumn_EndDate;
	}

	public void setRollCall_DateColumn_EndDate(String rollCall_DateColumn_EndDate) {
		RollCall_DateColumn_EndDate = rollCall_DateColumn_EndDate;
	}

	public boolean isISUPDATE() {
		return ISUPDATE;
	}

	public void setISUPDATE(boolean iSUPDATE) {
		ISUPDATE = iSUPDATE;
	}
}
