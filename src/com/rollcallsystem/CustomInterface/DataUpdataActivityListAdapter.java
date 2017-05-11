package com.rollcallsystem.CustomInterface;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DataUpdataActivityListAdapter  extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<RollCall_DateVO> RollList;
	private CurriculumManager CurriculumManager;
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
	public DataUpdataActivityListAdapter(Context context,List<RollCall_DateVO> List)
	{
		Log.i("SetUpActivityListAdapter","SetUpActivityListAdapter Start>>");
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.RollList = List;
		CurriculumManager = new CurriculumManager(context);
		Log.i("SetUpActivityListAdapter","SetUpActivityListAdapter End>>");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return RollList.size();
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return RollList.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.dataupdate_activity_listadapter, viewGroup, false);
		Log.i("SetUpActivityListAdapter","1");
		CheckBox CB =(CheckBox) view.findViewById(R.id.CB);
		TextView TV =(TextView) view.findViewById(R.id.TV);
		TextView TV_1 =(TextView) view.findViewById(R.id.TV_1);
		TextView TV1 =(TextView) view.findViewById(R.id.TV1);
		TextView TV2 =(TextView) view.findViewById(R.id.TV2);
		
		Log.i("SetUpActivityListAdapter","2");
		String cuName = CurriculumManager.getCurriculumByCuId(RollList.get(i).getRollCall_DateColumn_Curriculum_ID()).get(0).getCurriculum_NAME();
		String cuClass = CurriculumManager.getCurriculumByCuId(RollList.get(i).getRollCall_DateColumn_Curriculum_ID()).get(0).getCurriculum_CLASS();
		TV.setText(cuName);
		TV_1.setText(cuClass);
		TV1.setText(RollList.get(i).getRollCall_DateColumn_StartDate());
		TV2.setText(RollList.get(i).isISUPDATE()==false?"未上傳":"以上傳");
		if(RollList.get(i).isISUPDATE()==false){TV2.setTextColor(Color.RED);}
		else{TV2.setTextColor(Color.GREEN);}
		Log.i("SetUpActivityListAdapter","3");
		return view;
	}

}
