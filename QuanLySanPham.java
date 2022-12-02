package vn.edu.stu.quanlycuahang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import vn.edu.stu.quanlycuahang.model.LoaiSanPham;
import vn.edu.stu.quanlycuahang.model.SanPham;
import vn.edu.stu.quanlycuahang.model.Thongtin;

public class QuanLySanPham extends CapNhatSanPham {
    String url = "http://192.168.1.201/WebService/updatedata.php";
    ListView lvSP;
    Button btnAll, btnPL, btnThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnAll = findViewById(R.id.btnAll);
        btnThem = findViewById(R.id.btnThem);
        btnPL = findViewById(R.id.btnPL);
        lvSP = (ListView) findViewById(R.id.lvSP);
        if (Thongtin.firstv == -1) {
            lvSP.setAdapter(Thongtin.adapterSanPham);
        } else if (Thongtin.firstv == 0) {
            lvSP.setAdapter(Thongtin.adapterSanPhamTheoloai);
        }
    }

   

    private void addEvents() {
        lvSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog = new Dialog(QuanLySanPham.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.thongtinsp_dialog);
                Button btnXoa = (Button) dialog.findViewById(R.id.btnXoa);
                Button btnCs = (Button) dialog.findViewById(R.id.btnChinhsua);
                Button btnHuy = (Button) dialog.findViewById(R.id.btnBack);
                ImageView img = (ImageView) dialog.findViewById(R.id.imgsanpham);
                TextView tvma = (TextView) dialog.findViewById(R.id.tvma);
                TextView tvten = (TextView) dialog.findViewById(R.id.tvten);
                TextView tvloai = (TextView) dialog.findViewById(R.id.tvmaloai);
                TextView tvtt = (TextView) dialog.findViewById(R.id.tvtt);
                TextView tvgia = (TextView) dialog.findViewById(R.id.tvgia);
                if (Thongtin.firstv == -1) {
                    //Kiem tra chuyen trang ?
                    if (Thongtin.swappage == false) {
                        //Dung tai trang chu
                        if (Thongtin.function == false) {
                            tvma.setText(Thongtin.arrayMoHinh.get(i).getMasp().toString());
                            tvten.setText(Thongtin.arrayMoHinh.get(i).getTensp().toString());
                            tvloai.setText(Thongtin.arrayMoHinh.get(i).getMaloaisp().toString());
                            tvtt.setText(Thongtin.arrayMoHinh.get(i).getThongtin().toString());
                            tvgia.setText(String.valueOf(Thongtin.arrayMoHinh.get(i).getGia()) + "đ");
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
                                    img.setImageBitmap(bitmap);
                                }
                            }
                            new loadImage().execute(Thongtin.arrayMoHinh.get(i).getAnh().toString());
                            btnHuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            btnCs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    xulySua();
                                    dialog.cancel();
                                }
                            });
                            btnXoa.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Thongtin.arrayMoHinh.remove(i);
                                    Thongtin.adapterSanPham.notifyDataSetChanged();
                                    Thongtin.chon = -2;
                                    dialog.cancel();
                                }
                            });
                        }
                        //Trang phan loai
                        else {
                            tvma.setText(Thongtin.arrayLoaiSP.get(i).getMasp().toString());
                            tvten.setText(Thongtin.arrayLoaiSP.get(i).getTensp().toString());
                            tvloai.setText(Thongtin.arrayLoaiSP.get(i).getMaloaisp().toString());
                            tvtt.setText(Thongtin.arrayLoaiSP.get(i).getThongtin().toString());
                            tvgia.setText(String.valueOf(Thongtin.arrayLoaiSP.get(i).getGia()) + "đ");
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
                                    img.setImageBitmap(bitmap);
                                }
                            }
                            new loadImage().execute(Thongtin.arrayLoaiSP.get(i).getAnh().toString());
                            btnHuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            btnCs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    xulySua();
                                    dialog.cancel();
                                }
                            });
                            btnXoa.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Thongtin.arrayMoHinh.remove(i);
                                    Thongtin.arrayLoaiSP.remove(i);
                                    Thongtin.adapterSanPham.notifyDataSetChanged();
                                    Thongtin.adapterSanPhamTheoloai.notifyDataSetChanged();
                                    Thongtin.chon = -2;
                                    dialog.cancel();
                                }
                            });
                        }
                    }
                }
                //Update
                else {
                    if (Thongtin.function == false) {
                        tvma.setText(Thongtin.arrayMoHinh.get(i).getMasp().toString());
                        tvten.setText(Thongtin.arrayMoHinh.get(i).getTensp().toString());
                        tvloai.setText(Thongtin.arrayMoHinh.get(i).getMaloaisp().toString());
                        tvtt.setText(Thongtin.arrayMoHinh.get(i).getThongtin().toString());
                        tvgia.setText(String.valueOf(Thongtin.arrayMoHinh.get(i).getGia()) + "đ");
                        //Kiem tra anh da thay doi o trang chu
                        if (Thongtin.arrayMoHinh.get(i).getAnh().equals("")) {
                            for (int j = 0; j < Thongtin.arrayUpdate.size(); j++) {
                                if (Thongtin.arrayMoHinh.get(i).getMasp().toString().equals(Thongtin.arrayUpdate.get(j).getMasp().toString())) {
                                    img.setImageBitmap(Thongtin.arrayUpdate.get(j).getAnh());
                                }
                            }
                        }//Neu khong thay doi giu nguyen ban dau
                        else {
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
                                    img.setImageBitmap(bitmap);
                                }
                            }
                            new loadImage().execute(Thongtin.arrayMoHinh.get(i).getAnh().toString());
                        }
                        btnHuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        btnCs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                xulySua();
                                dialog.cancel();
                            }
                        });
                        btnXoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Thongtin.arrayMoHinh.remove(i);
                                Thongtin.adapterSanPham.notifyDataSetChanged();
                                Thongtin.chon = -2;
                                dialog.cancel();
                            }
                        });
                    }
                    //View Phan Loai
                    else {
                        tvma.setText(Thongtin.arrayLoaiSP.get(i).getMasp().toString());
                        tvten.setText(Thongtin.arrayLoaiSP.get(i).getTensp().toString());
                        tvloai.setText(Thongtin.arrayLoaiSP.get(i).getMaloaisp().toString());
                        tvtt.setText(Thongtin.arrayLoaiSP.get(i).getThongtin().toString());
                        tvgia.setText(String.valueOf(Thongtin.arrayLoaiSP.get(i).getGia()) + "đ");
                        if (Thongtin.arrayLoaiSP.get(i).getAnh().equals("")) {
                            for (int j = 0; j < Thongtin.arrayUpdate.size(); j++) {
                                if (Thongtin.arrayLoaiSP.get(i).getMasp().toString().equals(Thongtin.arrayUpdate.get(j).getMasp().toString())) {
                                    img.setImageBitmap(Thongtin.arrayUpdate.get(j).getAnh());
                                }
                            }
                        }//Neu khong thay doi giu nguyen ban dau
                        else {
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
                                    img.setImageBitmap(bitmap);
                                }
                            }
                            new loadImage().execute(Thongtin.arrayLoaiSP.get(i).getAnh().toString());
                        }
                        btnHuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        btnCs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                xulySua();
                                dialog.cancel();
                            }
                        });
                        btnXoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Thongtin.arrayMoHinh.remove(i);
                                Thongtin.arrayLoaiSP.remove(i);
                                Thongtin.adapterSanPham.notifyDataSetChanged();
                                Thongtin.adapterSanPhamTheoloai.notifyDataSetChanged();
                                Thongtin.chon = -2;
                                dialog.cancel();
                            }
                        });
                    }
                }
                Thongtin.chon = i;
                dialog.show();
            }
        });
    }

}