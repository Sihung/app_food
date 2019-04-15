package com.example.sh.orderfood.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sh.orderfood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThongKeAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<MonAn> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ThongKeAdapter(Context context, List<MonAn> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    //tra ve vi tri con tai vi tri dataheader
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getTenMonAn())
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        Log.d("CPC-Deli", "childText: " + childText);

        String[] tmp = childText.split("\\|");
        String tenUser = tmp[0];
        String soLuong = tmp[1];

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.itemsthongke, null);
        }




        ImageView imgSoLuong =  convertView.findViewById(R.id.Cart_Item_Count);

        TextDrawable drawable = TextDrawable.builder().buildRound(soLuong,Color.RED);

        imgSoLuong.setImageDrawable(drawable);
        TextView txtListChild =  convertView
                .findViewById(R.id.txt_TenKhachHang);

        txtListChild.setText(tenUser);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getTenMonAn())
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        List<MonAn> monAnList = new ArrayList<>();
        MonAn monAn = (MonAn) getGroup(groupPosition);

        String headerTenMon = monAn.getTenMonAn();
        String headerGia = String.valueOf(monAn.getGia());
        int iSoLuong = monAn.getSoLuong();

        // Update logic

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroup, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        TextView Gia =  convertView.findViewById(R.id.txt_Gia);
        ImageView imgSoLuong =  convertView.findViewById(R.id.Cart_Item_Count);

        TextDrawable drawable = TextDrawable.builder().buildRound( String.valueOf(iSoLuong),Color.RED);



        // Update View
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTenMon);
        imgSoLuong.setImageDrawable(drawable);
        Gia.setText(headerGia);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
