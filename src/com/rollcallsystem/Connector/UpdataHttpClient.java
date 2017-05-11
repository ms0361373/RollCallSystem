package com.rollcallsystem.Connector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnConnectionParamBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

public class UpdataHttpClient
{
	
	private static String[] HTPpost = { "http://172.20.174.140/",
			"http://192.168.0.2/", "http://120.114.136.54/","http://172.20.168.20/"};
	static String htpclient = HTPpost[2];
	private static final String LOG = "UpdataHttpClient";
	
	/**
	 * 
	 * @param parameter
	 * @param key
	 *            (UserName) 1 透過老師名稱更新課程資料,回傳學生資料(json)
	 * @param key
	 *            (UserName) 2 透過老師稱取得課程資料,回傳課程資料(json)
	 * @param key
	 *            (UserName) 3 取得所有學生資料(json)
	 * @param key
	 *            (UserName) 4 連線測試 ,0 ok, -1error
	 * @param key
	 *            (UserName) 5 取得使用者學期年度排序(json)
	 * @param key
	 *            ((String)JSON) 6 上傳學生資料
	 * @return
	 */
	public static String executeQuery(String parameter, String key)
	{
		// Log.i(LOG, "parameter>>"+parameter);
		String result = "";
		String htps = new String();
		try
		{
			switch (key)
			{
				case "1":
					htps = htpclient + "RollCallSystem/CurriculumUpdate.php";// 透過老師名稱更新課程資料,回傳學生資料(json)
					break;
				case "2":
					htps = htpclient + "RollCallSystem/getCurriculumByUserName.php";// 透過老師稱取得課程資料,回傳課程資料(json)
					break;
				case "3":
					htps = htpclient + "RollCallSystem/getAllStudent.php";// 取得所有學生資料(json)
					break;
				case "4":
					result = "-1";
					htps = htpclient + "RollCallSystem/ConnectionTest.php";// 連線測試
																			// ,0
																			// ok,
																			// -1error
					break;
				case "5":
					htps = htpclient + "RollCallSystem/getSeasonByUserName.php";// 取得使用者學期年度排序(json)
					break;
				case "6":
					htps = htpclient + "RollCallSystem/studentcardupdate.php";// 上傳學生Data
					break;
				case "7":
					htps = htpclient + "RollCallSystem/studentcarddownload.php";
					break;
				case "8":
					htps = htpclient + "RollCallSystem/StudentRollCallRateExport.php";// Excel匯出
					break;
				case "9":
					htps = htpclient + "RollCallSystem/command.php";// 命令
					break;
				case "10":
					htps = htpclient + "RollCallSystem/getRollCallData.php";// 下載課程點名紀錄
					break;
			}
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(htps);
			params.add(new BasicNameValuePair("Parameter", parameter));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			if (key == "4")
			{
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			}
			else
			{
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 100000);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 100000);
			}
			try
			{
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				BufferedReader bufReader = new BufferedReader(
						new InputStreamReader(inputStream, "utf-8"), 8);
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = bufReader.readLine()) != null)
				{
					builder.append(line + "\n");
				}
				
				inputStream.close();
				result = builder.toString();
			} catch (Exception e)
			{
				result = "-1";
			}
			
		} catch (Exception e)
		{
			Log.e(LOG, "Fail ! >>" + e);
			result = "-1";
		} finally
		{
			
		}
		
		return result;
	}
	
	public static void setServerIp(String Block0,String Block1,String Block2,String Block3 )
	{
		String Ip = "http://"+Block0+"."+Block1+"."+Block2+"."+Block3+"/";
		htpclient = new String();
		htpclient = Ip;
	}
	
	/**
	 * 確認網路狀態
	 * 
	 * @param CM
	 * @return
	 */
	
	public static boolean checkNetworkConnected(ConnectivityManager CM)
	{
		boolean result = false;
		if (CM == null)
		{
			result = false;
		} else
		{
			NetworkInfo info = CM.getActiveNetworkInfo();
			if (info != null && info.isConnected())
			{
				if (!info.isAvailable())
				{
					result = false;
				} else
				{
					result = true;
				}
				Log.d(LOG, "[目前連線方式]" + info.getTypeName());
				Log.d(LOG, "[目前連線狀態]" + info.getState());
				Log.d(LOG, "[目前網路是否可使用]" + info.isAvailable());
				Log.d(LOG, "[網路是否已連接]" + info.isConnected());
				Log.d(LOG, "[網路是否已連接 或 連線中]" + info.isConnectedOrConnecting());
				Log.d(LOG, "[網路目前是否有問題 ]" + info.isFailover());
				Log.d(LOG, "[網路目前是否在漫遊中]" + info.isRoaming());
			}
		}
		return result;
	}
}
