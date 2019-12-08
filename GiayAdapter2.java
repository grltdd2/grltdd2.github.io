package com.example.shoesshop;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangsach.R;

public class GiayAdapter2 extends ArrayAdapter<Giay> {
    Activity context;
    int resource;
    int position=-1;
    public GiayAdapter2(Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View customView=this.context.getLayoutInflater().inflate(this.resource,null);

        ImageView imgHinhAnh1=(ImageView)customView.findViewById(R.id.imgHinhAnh);
        TextView txtTenSP1=(TextView)customView.findViewById(R.id.txtTenSP);
        TextView txtGiaGiay1=(TextView)customView.findViewById(R.id.txtGiaGiay);
        TextView txtNhaXuatBan1=(TextView)customView.findViewById(R.id.txtNhaSanXuat);

        Giay sot=getItem(position);
        imgHinhAnh1.setImageResource(sot.getHinhAnh());
        txtTenSP1.setText(sot.getTenSP());
        txtGiaGiay1.setText(sot.getGiaGiay()+"VND");
        txtNhaXuatBan1.setText(sot.getNhaSanXuat());
        return customView;
    }
}
