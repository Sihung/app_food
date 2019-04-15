package com.example.sh.orderfood.model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sh.orderfood.MainHomeActivity;
import com.example.sh.orderfood.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txtItemName,txtItemGia;
    ImageView imgCartCount;
    public ItemclickListener itemClickListener;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtItemName = itemView.findViewById(R.id.txtItemName);
        txtItemGia = itemView.findViewById(R.id.txtItemGia);
        imgCartCount = itemView.findViewById(R.id.Cart_Item_Count);
    }

    public void setTxtItemName(TextView txtItemName) {
        this.txtItemName = txtItemName;
    }

    @Override
    public void onClick(View view) {

        }
}
public class CartAdapter  extends RecyclerView.Adapter<CartViewHolder>{
    List<MonAn> listMonAn;
//    MonAn monAn;

    Context context;


    public CartAdapter(List<MonAn> listMonAn, Context context) {
        this.listMonAn = listMonAn;
        this.context = context;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_layout,viewGroup,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        int sl = listMonAn.get(i).soLuong;

        TextDrawable drawable = TextDrawable.builder().buildRound(""+sl,Color.RED);
        cartViewHolder.imgCartCount.setImageDrawable(drawable);

        int gia = listMonAn.get(i).getGia();
        String name = listMonAn.get(i).getTenMonAn();
        cartViewHolder.txtItemGia.setText(gia + " đ");
        cartViewHolder.txtItemName.setText(name);
    }

    @Override
    public int getItemCount() {
        return listMonAn.size();
    }

}
