package com.rollcallsystem.ClassRollCallActivity;

import java.util.List;

import com.example.rollcallsystem.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DiaLogPublishClassListAdapter extends BaseAdapter {
private Context context;
private LayoutInflater inflater;
private List<String> StdName,StdId;
public DiaLogPublishClassListAdapter(Context context,List<String> stname,List<String> stid)
{
	Log.i("DiaLogClassDataListAdapter","DiaLogClassDataListAdapter Start>>");
	this.inflater = LayoutInflater.from(context);
	this.context = context;
	this.StdName = stname;
	this.StdId = stid;
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
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.publisgclasslistdlialog_adapter, viewGroup, false);
		CheckBox CB =(CheckBox) view.findViewById(R.id.CB);
		TextView TV =(TextView) view.findViewById(R.id.TV1);
		TextView TV1 =(TextView) view.findViewById(R.id.TV2);
		TV.setText(StdName.get(i).toString());
		TV1.setText(StdId.get(i).toString());
		return view;
	}

	
}
