package com.rollcallsystem.CustomInterface;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_DateManage;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

public class AttendanceRateCalculationTool
{
	private static final String LOG = "AttendanceRateCalculationTool";
	private Context context;
	private ArrayList<String> StartTime = new ArrayList<String>();
	private ArrayList<String> EndTime = new ArrayList<String>();
	private ArrayList<String> StudentTime = new ArrayList<String>();
	private ArrayList<Long>[] Total;
	private CurriculumManager curriculummanager;
	private RollCall_DateManage rollCall_DateManage;
	private RollCall_StudentManager rollCall_StudentManager;
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//	public void AttendanceRateCalculationTool(Context context, String Date, RollCall_StudentManager rollCall_StudentManager)
//	{
//		this.context = context;
//		curriculummanager = new CurriculumManager(context);
//		rollCall_DateManage = new RollCall_DateManage(context);
//		this.rollCall_StudentManager = rollCall_StudentManager;
//		
//		String Dates = rollCall_DateManage.getRollCall_DateByDate(Date);
//		
//		List<RollCall_DateVO> rollCall_Date = rollCall_DateManage.getRollCall_DateByID(Dates); // 取得當日
//		List<RollCall_DateVO> roll = rollCall_DateManage.getRollCall_DateByCu(curriculummanager.getCurriculumByCuId(rollCall_Date.get(0).getRollCall_DateColumn_Curriculum_ID()).get(0).getCurriculum_NAME(), curriculummanager.getCurriculumByCuId(rollCall_Date.get(0).getRollCall_DateColumn_Curriculum_ID()).get(0).getCurriculum_CLASS()); // 出席資料
//		int DateSize = roll.size();
//		Log.i(LOG, "DateSize >>" + DateSize);
//		List<RollCall_StudentVO> rollCall_Student = rollCall_StudentManager.getRollCall_StudentByDateId(Dates); // 取得當日
//																												// 學生出席資料
//		
//		Calculation(DateSize, rollCall_Date, rollCall_Student);
//		
//	}
	
//	private void Calculation(int DateSize, List<RollCall_DateVO> rollCall_Date, List<RollCall_StudentVO> rollCall_Student)
//	{
//		Log.i(LOG, "當日學生點名資料 Size >>" + rollCall_Student.size());
//		String StartTime = rollCall_Date.get(0).getRollCall_DateColumn_StartDate();
//		String EndTime = rollCall_Date.get(0).getRollCall_DateColumn_EndDate();
//		for (int i = 0; i < rollCall_Student.size(); i++)
//		{
//			String StuTime = rollCall_Student.get(i).getRollCall_DateColumn_StudentRollCallDate();
//			double Rate = a(StartTime, EndTime, StuTime);
//			RollCall_StudentVO rollCall_StudentVO = new RollCall_StudentVO();
//			rollCall_StudentVO.setRollCall_DateColumn_AttendanceRate(String.valueOf(Rate));
//			rollCall_StudentVO.setRollCall_DateColumn_Curriculum_ID(rollCall_Student.get(i).getRollCall_DateColumn_Curriculum_ID());
//			rollCall_StudentVO.setRollCall_DateColumn_DateID(rollCall_Student.get(i).getRollCall_DateColumn_DateID());
//			rollCall_StudentVO.setRollCall_DateColumn_StudentID(rollCall_Student.get(i).getRollCall_DateColumn_StudentID());
//			rollCall_StudentVO.setRollCall_DateColumn_StudentRollCallDate(rollCall_Student.get(i).getRollCall_DateColumn_StudentRollCallDate());
//			Log.i(LOG, "Staus >>" + rollCall_StudentManager.update(rollCall_StudentVO));
//		}
//		
//	}
	
	public String AttendanceRateCalculationTool(Context context, RollCall_StudentManager rollCall_StudentManager, String CuName, String CuClass, String StdId,String CuYear)
	{
		this.context = context;
		curriculummanager = new CurriculumManager(context);
		rollCall_DateManage = new RollCall_DateManage(context);
		this.rollCall_StudentManager = rollCall_StudentManager;
		List<RollCall_DateVO> rollCall_Date = rollCall_DateManage.getRollCall_DateByCu(CuName, CuClass,CuYear);
		List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCu(CuName, CuClass,CuYear, StdId);
		
		List<String> StartTime = new ArrayList<String>();
		List<String> EndTime = new ArrayList<String>();
		List<String> StuTime = new ArrayList<String>(); //該學生總次數
		
//		Log.i(LOG, "Rollcall_size"+rollCall_Date.size());
//		Log.i(LOG, "RollCall_Student_size"+RollCall_Student.size());
		
		for (int i = 0; i < rollCall_Date.size(); i++)
		{
			StartTime.add(rollCall_Date.get(i).getRollCall_DateColumn_StartDate());
			EndTime.add(rollCall_Date.get(i).getRollCall_DateColumn_EndDate());
		}
		
		for (int x = 0; x < RollCall_Student.size(); x++)
		{
			if (RollCall_Student.get(x).getRollCall_DateColumn_AttendanceRate().equals("0"))
			{
				StuTime.add("0");
			} else if (RollCall_Student.get(x).getRollCall_DateColumn_AttendanceRate().equals("請假"))
			{
				StuTime.add("請假");
			} else
			{
				StuTime.add(RollCall_Student.get(x).getRollCall_DateColumn_StudentRollCallDate());
			}
		}
		
		Log.i(LOG,"StuTime Size >>"+StuTime.size());
		
		double[] Rates = new double[StuTime.size()];
		for (int i = 0; i < StuTime.size(); i++)
		{
			double Rate = a(StartTime.get(i), EndTime.get(i), StuTime.get(i));
			Rates[i] = Rate;
		}
		double R = 0.0;
		for (int i = 0; i < Rates.length; i++)
		{
			R = R + Rates[i];
		}
		int StuSize = StuTime.size();
		double mRate = R / StuSize;
		Log.i(LOG,"Rate >>"+mRate);
		for (int x = 0; x < RollCall_Student.size(); x++)
		{
			RollCall_StudentVO rollCall_StudentVO = new RollCall_StudentVO();
			rollCall_StudentVO.setRollCall_DateColumn_AttendanceRate(String.valueOf(mRate));
			rollCall_StudentVO.setRollCall_DateColumn_Curriculum_ID(RollCall_Student.get(x).getRollCall_DateColumn_Curriculum_ID());
			rollCall_StudentVO.setRollCall_DateColumn_DateID(RollCall_Student.get(x).getRollCall_DateColumn_DateID());
			rollCall_StudentVO.setRollCall_DateColumn_StudentID(RollCall_Student.get(x).getRollCall_DateColumn_StudentID());
			rollCall_StudentVO.setRollCall_DateColumn_StudentRollCallDate(RollCall_Student.get(x).getRollCall_DateColumn_StudentRollCallDate());
			Log.i(LOG, "Staus >>" + rollCall_StudentManager.update(rollCall_StudentVO));
		}
		return String.valueOf(mRate);
	}
	
//	private String cal(int DateSize, double ToDay, String Lasttime)
//	{
//		Double TotalRate = null;
//		if (DateSize > 1)
//		{
//			Log.i(LOG, "ToDay >>" + ToDay);
//			Log.i(LOG, "Lasttime >>" + Lasttime);
//			Double lastTime = Double.valueOf(Lasttime);
//			Log.i(LOG, "lastTime >>" + lastTime);
//			TotalRate = (ToDay + lastTime) / 2;
//			Log.i(LOG, "1TotalRate >>" + TotalRate);
//		}
//		else
//		{
//			TotalRate = Double.valueOf(ToDay);
//			Log.i(LOG, "2TotalRate >>" + TotalRate);
//		}
//		Log.i(LOG, "String.valueOf(TotalRate) >>" + String.valueOf(TotalRate));
//		return String.valueOf(TotalRate);
//	}
	
	private double a(String Start, String End, String stu)
	{
		Date StartT = new Date();
		Date EndT = new Date();
		Date StuT = new Date();
		double Rate = 0;
		try
		{
			StartT = dateTimeFormat.parse(Start);
			EndT = dateTimeFormat.parse(End);
			
			Calendar CStartT = Calendar.getInstance();
			CStartT.setTime(StartT);
			Date S = CStartT.getTime();
			
			Calendar CEndT = Calendar.getInstance();
			CEndT.setTime(EndT);
			Date E = CEndT.getTime();
			
			long Total = E.getTime() - S.getTime();
			Log.i(LOG, "Total >>" + Total);
			Log.i(LOG, "stu >>" + stu);
		try{
			String[] AFL =  stu.split(","); // 字串 > 請假,分數:xx
			String[] F = AFL[1].split(":");
			Log.i(LOG, "s.length >>" + F.length);
			Rate = Double.parseDouble(F[1]);
		}catch(Exception e){
			Log.i(LOG, "catch >");
			if (!"0".equals(stu))
			{
				Log.i(LOG, "stu >>" + stu);
//				if ("0000-00-00 00:00:00".equals(stu))
//				{
//					Rate = 0;
//				}else{
				StuT = dateTimeFormat.parse(stu);
				Log.i(LOG, "stu >>" + StuT);
				Calendar CStuT = Calendar.getInstance();
				CStuT.setTime(StuT);
				Date STu = CStuT.getTime();
				long StTotal = E.getTime() - STu.getTime();
				
				DecimalFormat df = new DecimalFormat("##.00");
				Rate = ((double) StTotal / Total) * 100;
				Rate = Double.parseDouble(df.format(Rate));
				//}
			} else
			{
				Rate = 0;
			}
		}
		
			
			
			
			// long StTotal = E.getTime() - E.getTime();
			
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("tool", "Rate >>"+Rate);
		return Rate;
	}
}
