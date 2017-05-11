package com.rollcallsystem.CustomInterface;

import java.util.ArrayList;
import java.util.List;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.VO.RollCall_DateVO;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DiaLogClassDataListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<String> StdName,StdId,StdRecord;
	private DiaLogAskForLeave DLAFL;
	private String Curriculum_ID,DateID;
	public DiaLogClassDataListAdapter(Context context,List<String> stname,List<String> stid , List<String> strecord,String curriculum_ID,String dateID)
	{
		Log.i("DiaLogClassDataListAdapter","DiaLogClassDataListAdapter Start>>");
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.StdName = stname;
		this.StdId = stid;
		this.StdRecord = strecord;
		this.Curriculum_ID = curriculum_ID;
		this.DateID = dateID;
		Log.i("DiaLogClassDataListAdapter","DiaLogClassDataListAdapter End>>");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return StdName.size();
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return StdName.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.dialogclassdatalist_adapter, viewGroup, false);
		TextView TV =(TextView) view.findViewById(R.id.TV1);
		TextView TV1 =(TextView) view.findViewById(R.id.TV2);
		TextView TV2 =(TextView) view.findViewById(R.id.TV3);
		Button BT =(Button) view.findViewById(R.id.BT_DayOff);
		BT.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Log.i("DiaLogClassDataListAdapter","bt"+StdName.get(i).toString());
				DLAFL = new DiaLogAskForLeave(context,Curriculum_ID, DateID, StdId.get(i).toString());
				DLAFL.show();
			}
		});
		TV.setText(StdName.get(i).toString());
		TV1.setText(StdId.get(i).toString());
		TV2.setText(StdRecord.get(i).toString());
		if(StdRecord.get(i).toString()=="已簽到")
		{
			TV2.setTextColor(Color.GREEN);
		}else if(StdRecord.get(i).toString()=="尚未簽到")
		{
			TV2.setTextColor(Color.RED);
		}
		return view;
	}
	
	

}
