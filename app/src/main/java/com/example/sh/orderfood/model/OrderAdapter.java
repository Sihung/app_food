package com.example.sh.orderfood.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sh.orderfood.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder>{

    List<Order> listOrder;

    public OrderAdapter(List<Order> listdata) {
        this.listOrder = listdata;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }


    public void onBindViewHolder(MyHolder holder, int position) {
        Order order = listOrder.get(position);
//        holder.txtId.setText(order);
        holder.txtTenMonAn.setText(order.getTenMonAn());
        holder.txtTenKhachHang.setText(order.getGia());
        holder.txtSoDienThoai.setText(order.soLuong);
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        TextView txtId;
        TextView txtTenMonAn,txtTenKhachHang,txtSoDienThoai;

        public MyHolder(View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtIdFood);
            txtTenMonAn = itemView.findViewById(R.id.txtTenMonAn);
            txtTenKhachHang = itemView.findViewById(R.id.txtTenKhachHang);
            txtSoDienThoai = itemView.findViewById(R.id.txtSoDienThoai);
        }
    }


}