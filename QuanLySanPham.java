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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Dialog dialog2 = new Dialog(QuanLySanPham.this);
            dialog2.setCancelable(false);
            dialog2.setContentView(R.layout.thongtinapp_dialog);
            Button btnOke = (Button) dialog2.findViewById(R.id.btnxacnhan);
            ImageButton btncall =(ImageButton) dialog2.findViewById(R.id.btncall);
            TextView tvsdt = (TextView) dialog2.findViewById(R.id.tvsdt);
            btncall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvsdt.getText().toString().trim())));
                    dialog2.cancel();
                }
            });
            btnOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.cancel();
                }
            });
            dialog2.show();
        } else {
            Intent intent = new Intent(QuanLySanPham.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        btnPL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyPL();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyThem();
            }
        });
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyALL();
            }
        });
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
                    // Kiem tra chuyen trang ?
                    if (Thongtin.swappage == false) {
                        // Dung tai trang chu
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
                        // Trang phan loai
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
                // Update
                else {
                    if (Thongtin.function == false) {
                        tvma.setText(Thongtin.arrayMoHinh.get(i).getMasp().toString());
                        tvten.setText(Thongtin.arrayMoHinh.get(i).getTensp().toString());
                        tvloai.setText(Thongtin.arrayMoHinh.get(i).getMaloaisp().toString());
                        tvtt.setText(Thongtin.arrayMoHinh.get(i).getThongtin().toString());
                        tvgia.setText(String.valueOf(Thongtin.arrayMoHinh.get(i).getGia()) + "đ");
                        // Kiem tra anh da thay doi o trang chu
                        if (Thongtin.arrayMoHinh.get(i).getAnh().equals("")) {
                            for (int j = 0; j < Thongtin.arrayUpdate.size(); j++) {
                                if (Thongtin.arrayMoHinh.get(i).getMasp().toString()
                                        .equals(Thongtin.arrayUpdate.get(j).getMasp().toString())) {
                                    img.setImageBitmap(Thongtin.arrayUpdate.get(j).getAnh());
                                }
                            }
                        } // Neu khong thay doi giu nguyen ban dau
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
                    // View Phan Loai
                    else {
                        tvma.setText(Thongtin.arrayLoaiSP.get(i).getMasp().toString());
                        tvten.setText(Thongtin.arrayLoaiSP.get(i).getTensp().toString());
                        tvloai.setText(Thongtin.arrayLoaiSP.get(i).getMaloaisp().toString());
                        tvtt.setText(Thongtin.arrayLoaiSP.get(i).getThongtin().toString());
                        tvgia.setText(String.valueOf(Thongtin.arrayLoaiSP.get(i).getGia()) + "đ");
                        if (Thongtin.arrayLoaiSP.get(i).getAnh().equals("")) {
                            for (int j = 0; j < Thongtin.arrayUpdate.size(); j++) {
                                if (Thongtin.arrayLoaiSP.get(i).getMasp().toString()
                                        .equals(Thongtin.arrayUpdate.get(j).getMasp().toString())) {
                                    img.setImageBitmap(Thongtin.arrayUpdate.get(j).getAnh());
                                }
                            }
                        } // Neu khong thay doi giu nguyen ban dau
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

    private void xulyALL() {
        String ma = "";
        String ten = "";
        String loai = "";
        String tt = "";
        int gia = 0;
        if (Thongtin.swappage == true) {
            if (Thongtin.function == true) {
                if (Thongtin.module.equals("Them")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.add(new SanPham(ma, ten, loai, tt, gia, ""));
                    updateDatatoSever(url, ma, ten, loai, tt, String.valueOf(gia), "img");
                    Thongtin.module = "";
                } else if (Thongtin.module.equals("Sua")) {
                    String madl = Thongtin.arrayLoaiSP.get(Thongtin.chon).getMasp().toString();
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    for (int k = 0; k < Thongtin.arrayMoHinh.size(); k++) {
                        if (Thongtin.arrayMoHinh.get(k).getMasp().equals(madl)) {
                            Thongtin.arrayMoHinh.get(k).setMasp(ma);
                            Thongtin.arrayMoHinh.get(k).setTensp(ten);
                            Thongtin.arrayMoHinh.get(k).setMaloaisp(loai);
                            Thongtin.arrayMoHinh.get(k).setThongtin(tt);
                            Thongtin.arrayMoHinh.get(k).setGia(gia);
                            if (Thongtin.checkdoianh == true) {
                                Thongtin.arrayMoHinh.get(k).setAnh("");
                            }
                        }
                    }
                    Thongtin.module = "";
                } else {
                    lvSP.setAdapter(Thongtin.adapterSanPham);
                }
            } else {
                if (Thongtin.module.equals("Them")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.add(new SanPham(ma, ten, loai, tt, gia, ""));
                    updateDatatoSever(url, ma, ten, loai, tt, String.valueOf(gia), "img");
                    Thongtin.adapterSanPham.notifyDataSetChanged();
                    Thongtin.module = "";
                } else if (Thongtin.module.equals("Sua")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setMasp(ma);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setTensp(ten);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setMaloaisp(loai);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setThongtin(tt);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setGia(gia);
                    Thongtin.adapterSanPham.notifyDataSetChanged();
                    Thongtin.module = "";
                } else {
                    lvSP.setAdapter(Thongtin.adapterSanPham);
                }
            }
            lvSP.setAdapter(Thongtin.adapterSanPham);
        } else {
            lvSP.setAdapter(Thongtin.adapterSanPham);
        }

        Thongtin.checkdoianh = false;
        Thongtin.module = "";
        Thongtin.function = false;
        Toast.makeText(QuanLySanPham.this, "Tất cả SP", Toast.LENGTH_SHORT).show();
    }

    private void xulySua() {
        Thongtin.module = "Sua";
        Intent intent = new Intent(QuanLySanPham.this, CapNhatSanPham.class);
        startActivity(intent);
    }

    private void xulyPL() {
        Thongtin.function = true;
        ListView lvLoaiSP;
        Dialog dialog = new Dialog(QuanLySanPham.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.listviewloaisanpham);
        lvLoaiSP = (ListView) dialog.findViewById(R.id.lvloai);
        lvLoaiSP.setAdapter(Thongtin.adapterLoaiSP);
        lvLoaiSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog1 = new Dialog(dialog.getContext());
                dialog1.setCancelable(true);
                dialog1.setContentView(R.layout.xulyloaisp_dialog);
                ImageButton btnXem = (ImageButton) dialog1.findViewById(R.id.btnXem);
                ImageButton btnXoa = (ImageButton) dialog1.findViewById(R.id.btnXoa);
                btnXem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Thongtin.arrayLoaiSP.clear();
                        String ml = Thongtin.listLoai.get(i).toString();
                        for (int a = 0; a < Thongtin.arrayMoHinh.size(); a++) {
                            if (ml.equals(Thongtin.arrayMoHinh.get(a).getMaloaisp())) {
                                String ma = Thongtin.arrayMoHinh.get(a).getMasp();
                                String ten = Thongtin.arrayMoHinh.get(a).getTensp();
                                String loai = Thongtin.arrayMoHinh.get(a).getMaloaisp();
                                String tt = Thongtin.arrayMoHinh.get(a).getThongtin();
                                int gia = Thongtin.arrayMoHinh.get(a).getGia();
                                String anh = Thongtin.arrayMoHinh.get(a).getAnh();
                                Thongtin.arrayLoaiSP.add(new SanPham(ma, ten, loai, tt, gia, anh));
                            }
                        }
                        Thongtin.adapterSanPhamTheoloai.notifyDataSetChanged();
                        lvSP.setAdapter(Thongtin.adapterSanPhamTheoloai);
                        dialog1.cancel();
                        dialog.cancel();
                        Toast.makeText(QuanLySanPham.this, "Loại SP: " + ml, Toast.LENGTH_SHORT).show();
                    }
                });
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ml = Thongtin.listLoai.get(i).getMaloaisp().toString();
                        Dialog dialog2 = new Dialog(dialog1.getContext());
                        dialog2.setCancelable(false);
                        dialog2.setContentView(R.layout.yesno_dialog);
                        Button btnyes = (Button) dialog2.findViewById(R.id.btnyes);
                        Button btnno = (Button) dialog2.findViewById(R.id.btnno);
                        btnyes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Thongtin.listLoai.remove(i);
                                for (int j = 0; j < Thongtin.arrayMoHinh.size(); j++) {
                                    if (Thongtin.arrayMoHinh.get(j).getMaloaisp().toString().equals(ml)) {
                                        Thongtin.arrayMoHinh.remove(j);
                                        j--;
                                    }
                                }
                                Thongtin.adapterLoaiSP.notifyDataSetChanged();
                                Thongtin.adapterSanPham.notifyDataSetChanged();
                                dialog2.cancel();
                                dialog1.cancel();
                                lvLoaiSP.setAdapter(Thongtin.adapterLoaiSP);
                                Toast.makeText(QuanLySanPham.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.cancel();
                            }
                        });
                        dialog2.show();
                    }
                });
                dialog1.show();
            }
        });
        ImageButton btnnew = (ImageButton) dialog.findViewById(R.id.btnnew);
        ImageButton btnback = (ImageButton) dialog.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(QuanLySanPham.this);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.newloai_dialog);
                EditText etmaloai = (EditText) dialog1.findViewById(R.id.etMaloaisp);
                ImageButton btnimgback = (ImageButton) dialog1.findViewById(R.id.btnimghuy);
                ImageButton btndone = (ImageButton) dialog1.findViewById(R.id.btnimgthem);
                btnimgback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.cancel();
                    }
                });
                btndone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean done = true;
                        if (etmaloai.getText().length() == 0) {
                            Toast.makeText(QuanLySanPham.this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int j = 0; j < Thongtin.listLoai.size(); j++) {
                                if (etmaloai.getText().toString()
                                        .equals(Thongtin.listLoai.get(j).getMaloaisp().toString())) {
                                    done = false;
                                }
                            }
                        }
                        if (done == false) {
                            Toast.makeText(QuanLySanPham.this, "Loại SP đã có!", Toast.LENGTH_SHORT).show();
                        } else {
                            Thongtin.listLoai.add(new LoaiSanPham(etmaloai.getText().toString()));
                            Thongtin.adapterLoaiSP.notifyDataSetChanged();
                            Toast.makeText(QuanLySanPham.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            dialog1.cancel();
                            dialog.cancel();
                        }
                    }
                });
                dialog1.show();
            }
        });
        dialog.show();
    }

    private void xulyALL() {
        String ma = "";
        String ten = "";
        String loai = "";
        String tt = "";
        int gia = 0;
        if (Thongtin.swappage == true) {
            if (Thongtin.function == true) {
                if (Thongtin.module.equals("Them")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.add(new SanPham(ma, ten, loai, tt, gia, ""));
                    updateDatatoSever(url, ma, ten, loai, tt, String.valueOf(gia), "img");
                    Thongtin.module = "";
                } else if (Thongtin.module.equals("Sua")) {
                    String madl = Thongtin.arrayLoaiSP.get(Thongtin.chon).getMasp().toString();
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    for (int k = 0; k < Thongtin.arrayMoHinh.size(); k++) {
                        if (Thongtin.arrayMoHinh.get(k).getMasp().equals(madl)) {
                            Thongtin.arrayMoHinh.get(k).setMasp(ma);
                            Thongtin.arrayMoHinh.get(k).setTensp(ten);
                            Thongtin.arrayMoHinh.get(k).setMaloaisp(loai);
                            Thongtin.arrayMoHinh.get(k).setThongtin(tt);
                            Thongtin.arrayMoHinh.get(k).setGia(gia);
                            if (Thongtin.checkdoianh == true) {
                                Thongtin.arrayMoHinh.get(k).setAnh("");
                            }
                        }
                    }
                    Thongtin.module = "";
                } else {
                    lvSP.setAdapter(Thongtin.adapterSanPham);
                }
            } else {
                if (Thongtin.module.equals("Them")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.add(new SanPham(ma, ten, loai, tt, gia, ""));
                    updateDatatoSever(url, ma, ten, loai, tt, String.valueOf(gia), "img");
                    Thongtin.adapterSanPham.notifyDataSetChanged();
                    Thongtin.module = "";
                } else if (Thongtin.module.equals("Sua")) {
                    ma = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMasp().toString();
                    ten = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getTensp().toString();
                    loai = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getMaloaisp().toString();
                    tt = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getThongtin().toString();
                    gia = Thongtin.arrayUpdate.get(Thongtin.sp - 1).getGia();
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setMasp(ma);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setTensp(ten);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setMaloaisp(loai);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setThongtin(tt);
                    Thongtin.arrayMoHinh.get(Thongtin.chon).setGia(gia);
                    Thongtin.adapterSanPham.notifyDataSetChanged();
                    Thongtin.module = "";
                } else {
                    lvSP.setAdapter(Thongtin.adapterSanPham);
                }
            }
            lvSP.setAdapter(Thongtin.adapterSanPham);
        } else {
            lvSP.setAdapter(Thongtin.adapterSanPham);
        }

        Thongtin.checkdoianh = false;
        Thongtin.module = "";
        Thongtin.function = false;
        Toast.makeText(QuanLySanPham.this, "Tất cả SP", Toast.LENGTH_SHORT).show();
    }

}
