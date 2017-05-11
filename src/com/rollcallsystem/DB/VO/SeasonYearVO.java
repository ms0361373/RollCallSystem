package com.rollcallsystem.DB.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class SeasonYearVO  implements Parcelable{
	
	private int _id;
	private String SeasonYear_SEASON;

	

	public SeasonYearVO(){
		super();
		}
	
	public SeasonYearVO(int _id,String seasonYear_SEASON){
		super();
		this._id=_id;
		this.SeasonYear_SEASON=seasonYear_SEASON;
	}
	
	private SeasonYearVO(Parcel in) {
		super();
		this._id = in.readInt();
		this.SeasonYear_SEASON = in.readString();
	}
	
	public static final Parcelable.Creator<SeasonYearVO> CREATOR = new Parcelable.Creator<SeasonYearVO>() {
		public SeasonYearVO createFromParcel(Parcel in) {
			return new SeasonYearVO(in);
		}

		public SeasonYearVO[] newArray(int size) {
			return new SeasonYearVO[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getSeasonYear_SEASON());
		
	}
	@Override
	public String toString() {
		return "SeasonYearVO [_id=" + _id + ", SeasonYear_SEASON=" + SeasonYear_SEASON +"]";
	}
	
	public String getSeasonYear_SEASON() {
		return SeasonYear_SEASON;
	}

	public void setSeasonYear_SEASON(String seasonYear_SEASON) {
		SeasonYear_SEASON = seasonYear_SEASON;
	}

}
