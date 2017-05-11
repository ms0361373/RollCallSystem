package com.rollcallsystem.CustomInterface;

import java.util.HashMap;
import java.util.List;

import com.example.rollcallsystem.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> listDataHeader; // 標題
	private HashMap<String, List<String>> listDataChild; // 內容

	/**
	 * @param listDataHeader
	 *            -標題
	 * @param listChildData
	 *            -內容
	 */
	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listChildData;
	}

	/* -------------------- 內容 -------------------- */
	public Object getChild(int groupPosition, int childPosititon) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	public int getChildrenCount(int groupPosition) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition))
				.size();
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/* 內容View */
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition); // 取得內容
	// processing views

			/* 設置內容layout */
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandablelistadapter, null);
		}

		/* 設置內容 */
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		// txtListChild.setTextColor(Color.rgb(230, 230, 230));
		return convertView;
	}

	/* -------------------- 標題 -------------------- */
	public Object getGroup(int groupPosition) {
		return this.listDataHeader.get(groupPosition);
	}

	public int getGroupCount() {
		return this.listDataHeader.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/* 標題View */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String headerTitle = (String) getGroup(groupPosition); // 取得標題
		/* 設置標題layout */
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandablelistgroup, null);
		}

		/* 設置標題 */
		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		lblListHeader.setTextSize(25);
		return convertView;
	}

	/* ------------------------------------------------ */
	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
