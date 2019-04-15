package com.example.sh.orderfood.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sh.orderfood.MainAddToCartActivity;
import com.example.sh.orderfood.MainHomeActivity;
import com.example.sh.orderfood.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;



public class Adapter extends ArrayAdapter<MonAn> {
    @SuppressWarnings("deprecation")
    public static final String TAG = MainHomeActivity.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private int resouce;
    ElegantNumberButton numberButton;
    ScaleGestureDetector detector;
    List<MonAn> lsMonAn;
    public int soLuong;
//    CacheImage cacheImage = new CacheImage();
    public Adapter(Context context, int resource, List<MonAn> lsMonAn) {
        super(context, resource, lsMonAn);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.resouce = resource;
        this.lsMonAn = lsMonAn;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        //lay du lieu item cho position
        final MonAn monan = getItem(position);
        View result;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.items, null);
            holder.imageview = convertView.findViewById(R.id.imgMonAn);
            holder.tvGia = (TextView) convertView.findViewById(R.id.txtGia);
            holder.tvTenMon = (TextView) convertView.findViewById(R.id.txtTen);
            numberButton = (ElegantNumberButton) convertView.findViewById(R.id.number_button);

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Inflater inflater = new Inflater();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                    View mView = inflater.inflate(R.layout.dialog_custom_layout, null);

                    PhotoView photoView = mView.findViewById(R.id.photoview);
                    //photoView.setImageResource(R.drawable.a);

                    final String url = monan.getImageUrl();
                    Log.d("Deli-CPC","--------------" + url);
//                    Picasso picasso = Picasso.with(mContext);
                    Picasso.with(mContext).setIndicatorsEnabled(true);
//                    picasso.setDebugging(true);
//                    cacheImage.onCreate();
                    Picasso.with(mContext).load(url).into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //holder.pb.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });

            result = convertView;
            convertView.setTag(holder);
            numberButton.setTag(monan);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        numberButton.setRange(0, 10);

        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.d(TAG, String.format("oldValue: %d   newValue: %d", oldValue, newValue));
                soLuong = newValue;
                ((MainHomeActivity)mContext).XuLy((MonAn) view.getTag());
                MonAn monAn = (MonAn) view.getTag();
                monAn.soLuong = soLuong;
                monAn.tongtien = monAn.gia* monAn.soLuong;
                Log.d("Deli-CPC","Tong: " + monAn.tongtien);
                for (int i = 0;i < lsMonAn.size(); i++){
                    if(lsMonAn.get(i).tenMonAn.equalsIgnoreCase(monAn.tenMonAn)){
                        lsMonAn.set(i, monAn);
                    }
                }
            }
        });

        holder.tvTenMon.setText(monan.getTenMonAn());
        holder.tvGia.setText(monan.getGia() + " đ");
        final String url = monan.getImageUrl();
        Log.d("Deli-CPC","--------------" + url);
        Picasso.with(mContext).setIndicatorsEnabled(true);
//        cacheImage.onCreate();.networkPolicy(NetworkPolicy.OFFLINE)
        Picasso.with(mContext).load(url).into(holder.imageview, new Callback() {

            @Override
            public void onSuccess() {
                //holder.pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });

        return convertView;
    }


    public static class ViewHolder {
        ImageView imageview;
        TextView tvTenMon, tvGia;
    }


}



