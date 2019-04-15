package com.example.sh.orderfood.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.sh.orderfood.model.GlobalVariable.databaseReference;
import static com.example.sh.orderfood.model.GlobalVariable.database;
import com.example.sh.orderfood.MainQuanLyMonAnActivity;
import com.example.sh.orderfood.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterQuanLyMonAn extends ArrayAdapter<MonAn>{

    public static final String TAG = MainQuanLyMonAnActivity.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private int resouce1;
    ScaleGestureDetector detector;
    List<MonAn> lsMonAn;

    public AdapterQuanLyMonAn(@NonNull Context context, int resource, @NonNull List<MonAn> lsMonAn) {
        super(context, resource, lsMonAn);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.resouce1 = resource;
        this.lsMonAn = lsMonAn;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        //lay du lieu item cho position
        final MonAn monan = getItem(position);
        View result;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.itemquanlymon, null);
            holder.imageview = convertView.findViewById(R.id.imgMonAn);
            holder.tvGia = (TextView) convertView.findViewById(R.id.txtGia);
            holder.tvTenMon = (TextView) convertView.findViewById(R.id.txtTen);
            holder.btnXoa = convertView.findViewById(R.id.btnXoa);

            result = convertView;
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = holder.tvTenMon.getText().toString();
                Log.d("------------------","btn xoa: ===============:" + key);
                databaseReference = database.getReference("DanhMuc");
                databaseReference.child(key).removeValue();

            }
        });

        holder.tvTenMon.setText(monan.getTenMonAn());
        holder.tvGia.setText(monan.getGia() + " đ");
        final String url = monan.getImageUrl();
        Log.d("Deli-CPC","--------------" + url);
        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageview, new Callback() {

            @Override
            public void onSuccess() {
                //holder.pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                Picasso.with(mContext)
                        .load(url)
                        .into(holder.imageview, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Log.v("Picasso","Could not fetch image");
                            }
                        });
            }
        });

        return convertView;
    }


    public static class ViewHolder {
        ImageView imageview;
        Button btnXoa;
        TextView tvTenMon, tvGia;
    }

}
