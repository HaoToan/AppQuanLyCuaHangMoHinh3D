package vn.edu.stu.quanlycuahang.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import vn.edu.stu.quanlycuahang.R;

public class AdapterSanPham extends BaseAdapter {
    private Context context;
    private int layout;
    private List<SanPham> listSP;

    public AdapterSanPham(Context context, int layout, List<SanPham> listSP) {
        this.context = context;
        this.layout = layout;
        this.listSP = listSP;
    }

    @Override
    public int getCount() {
        return listSP.size();
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
        TextView tvma, tvten, tvloai, tvthongtin, tvgia;
        ImageView imageView;
        ImageButton btnTT, btnCS, btnXoa;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.tvten = (TextView) view.findViewById(R.id.tvten);
            holder.tvloai = (TextView) view.findViewById(R.id.tvmaloai);
            holder.tvgia = (TextView) view.findViewById(R.id.tvgia);
            holder.imageView = (ImageView) view.findViewById(R.id.imgsp);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }

        SanPham sp = listSP.get(i);
        holder.tvten.setText(sp.getTensp().toString());
        holder.tvloai.setText(sp.getMaloaisp().toString());
        holder.tvgia.setText(String.valueOf(sp.getGia()) + "đ");

        if (sp.getAnh().equals("")) {
            for (int j = 0; j < Thongtin.arrayUpdate.size(); j++) {
                if (sp.getMasp().toString().equals(Thongtin.arrayUpdate.get(j).getMasp().toString())) {
                    holder.imageView.setImageBitmap(Thongtin.arrayUpdate.get(j).getAnh());
                }
            }
        } else {
            class loadImage extends AsyncTask<String, Void, Bitmap> {
                Bitmap bitmapimg = null;

                @Override
                protected Bitmap doInBackground(String... strings) {
                    try {
                        URL url = new URL(strings[0]);
                        InputStream inputStream = url.openConnection().getInputStream();
                        bitmapimg = BitmapFactory.decodeStream(inputStream);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bitmapimg;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    holder.imageView.setImageBitmap(bitmap);
                }
            }
            new loadImage().execute(sp.getAnh().toString());
        }
        return view;
    }
}
