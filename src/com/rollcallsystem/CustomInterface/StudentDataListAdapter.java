package com.rollcallsystem.CustomInterface;



import java.util.List;

import com.example.rollcallsystem.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StudentDataListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<String> stdName=null;
	private List<String> stdId=null;
	private List<String> stdRate=null;
	public StudentDataListAdapter(Context context , List<String> StdNmae,List<String> StdId,List<String> stdrate)
	{
		Log.i("StudentDataListAdapter","StudentDataListAdapter Start>>");
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.stdName =StdNmae;
		this.stdId =StdId;
		this.stdRate =stdrate;
		Log.i("StudentDataListAdapter","StudentDataListAdapter End>>");
	}
	
	@Override
	public int getCount() {
		return stdName.size();
	}

	@Override
	public Object getItem(int position) {
		
		return stdName.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView( int i, View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.studentdata_lsit_adapter, viewGroup, false);
		TextView TV1, TV2, TV3;
		TV1 = (TextView) view.findViewById(R.id.TV_Item1);
		TV2 = (TextView) view.findViewById(R.id.TV_Item2);
		TV3 = (TextView) view.findViewById(R.id.TV_Item3);
		TV1.setText(stdName.get(i).toString());
		TV2.setText(stdId.get(i).toString());
		TV3.setText(stdRate.get(i).toString());
		return view;
	}

}
