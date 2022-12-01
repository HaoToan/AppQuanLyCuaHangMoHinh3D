package vn.edu.stu.quanlycuahang.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import vn.edu.stu.quanlycuahang.QuanLySanPham;
import vn.edu.stu.quanlycuahang.R;

public class AdapterLoaiSP extends BaseAdapter {
    private Activity context;
    private int layout;
    private List<LoaiSanPham> listLoai;
    public AdapterLoaiSP(Activity context, int layout, List<LoaiSanPham> listLoai) {
        this.context = context;
        this.layout = layout;
        this.listLoai = listLoai;
    }
    @Override
    public int getCount() {
        return listLoai.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class viewHolder {
        TextView tvma;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AdapterLoaiSP.viewHolder holder;
        if (view == null) {
            holder = new AdapterLoaiSP.viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.tvma = (TextView) view.findViewById(R.id.tvmaloaisp);
            view.setTag(holder);
        } else {
            holder = (AdapterLoaiSP.viewHolder) view.getTag();
        }
        LoaiSanPham loai = listLoai.get(i);
        holder.tvma.setText(loai.getMaloaisp().toString());
        return view;
    }
}
