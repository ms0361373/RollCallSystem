package com.rollcallsystem.CustomInterface;

import java.util.List;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StudentRecordDialogListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<RollCall_DateVO> DateList;
	private List<String> StudentList;
	public StudentRecordDialogListAdapter(Context context,List<RollCall_DateVO> dateList,List<String> studentList)
	{
		Log.i("StudentRecordDialogListAdapter","StudentRecordDialogListAdapter Start>>");
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.DateList = dateList;
		this.StudentList = studentList;
		Log.i("StudentRecordDialogListAdapter","StudentRecordDialogListAdapter End>>");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DateList.size();
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return DateList.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.dialogstudentrecord_adapter, viewGroup, false);
		TextView TV_Start =(TextView) view.findViewById(R.id.TV_Start);
		TextView TV_End =(TextView) view.findViewById(R.id.TV_End);
		TextView TV_StdDate =(TextView) view.findViewById(R.id.TV_StdDate);
		TV_Start.setText(DateList.get(i).getRollCall_DateColumn_StartDate());
		TV_End.setText(DateList.get(i).getRollCall_DateColumn_EndDate());
		TV_StdDate.setText(StudentList.get(i));
		if(StudentList.get(i).equals("未簽到"))
		{
			TV_StdDate.setTextColor(Color.RED);
		}
		return view;
	}

}
