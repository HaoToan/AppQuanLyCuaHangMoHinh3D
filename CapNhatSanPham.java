package vn.edu.stu.quanlycuahang;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import vn.edu.stu.quanlycuahang.model.ByteAnh;
import vn.edu.stu.quanlycuahang.model.Thongtin;
import vn.edu.stu.quanlycuahang.model.UpDateData;

public class CapNhatSanPham extends AppCompatActivity {
    public static final String TAG = CapNhatSanPham.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    EditText etma, etten, etloai, ettt, etgia;
    ImageView img;
    ImageButton btnimg;
    Button btnXN, btnHuy;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_san_pham);
        addCtrls();
        addEvs();
    }

    private void addCtrls() {
        etma = findViewById(R.id.etMa);
        etten = findViewById(R.id.etTen);
        etloai = findViewById(R.id.etLoai);
        ettt = findViewById(R.id.ettt);
        etgia = findViewById(R.id.etGia);
        img = findViewById(R.id.imgsanpham);
        btnimg = findViewById(R.id.btnIMG);
        btnXN = findViewById(R.id.btnXN);
        btnHuy = findViewById(R.id.btnHuy);
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (result -> {
                Log.e(TAG, "onActivityResult");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));

    private byte[] ImageView_To_Byte(ImageView img) {
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
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
            Dialog dialog2 = new Dialog(CapNhatSanPham.this);
            dialog2.setCancelable(false);
            dialog2.setContentView(R.layout.thongtinapp_dialog);
            Button btnOke = (Button) dialog2.findViewById(R.id.btnxacnhan);
            btnOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.cancel();
                }
            });
            dialog2.show();
        } else {
            Intent intent = new Intent(CapNhatSanPham.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    int index = Thongtin.chon;

    private void addEvs() {
        if (Thongtin.module.toString().equals("Sua")) {
            if (Thongtin.function == false) {
                etma.setText(Thongtin.arrayMoHinh.get(index).getMasp().toString());
                etten.setText(Thongtin.arrayMoHinh.get(index).getTensp().toString());
                etloai.setText(Thongtin.arrayMoHinh.get(index).getMaloaisp().toString());
                ettt.setText(Thongtin.arrayMoHinh.get(index).getThongtin().toString());
                etgia.setText(String.valueOf(Thongtin.arrayMoHinh.get(index).getGia()));
                if (Thongtin.arrayMoHinh.get(index).getAnh().toString().equals("")) {
                    for (int i = 0; i < Thongtin.arrayUpdate.size(); i++) {
                        if (Thongtin.arrayMoHinh.get(index).getMasp().toString().equals(Thongtin.arrayUpdate.get(i).getMasp().toString())) {
                            img.setImageBitmap(Thongtin.arrayUpdate.get(i).getAnh());
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
                            img.setImageBitmap(bitmap);
                        }
                    }
                    new loadImage().execute(Thongtin.arrayMoHinh.get(index).getAnh().toString());
                }
            } else {
                etma.setText(Thongtin.arrayLoaiSP.get(index).getMasp().toString());
                etten.setText(Thongtin.arrayLoaiSP.get(index).getTensp().toString());
                etloai.setText(Thongtin.arrayLoaiSP.get(index).getMaloaisp().toString());
                ettt.setText(Thongtin.arrayLoaiSP.get(index).getThongtin().toString());
                etgia.setText(String.valueOf(Thongtin.arrayLoaiSP.get(index).getGia()));
                if (Thongtin.arrayLoaiSP.get(index).getAnh().toString().equals("")) {
                    for (int i = 0; i < Thongtin.arrayUpdate.size(); i++) {
                        if (Thongtin.arrayLoaiSP.get(index).getMasp().toString().equals(Thongtin.arrayUpdate.get(i).getMasp().toString())) {
                            img.setImageBitmap(Thongtin.arrayUpdate.get(i).getAnh());
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
                            img.setImageBitmap(bitmap);
                        }
                    }
                    new loadImage().execute(Thongtin.arrayLoaiSP.get(index).getAnh().toString());
                }
            }
        }

        btnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
                Thongtin.checkcam = true;
            }
        });
        btnXN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemtra();
                if (Thongtin.kt == true) {
                    if (Thongtin.checkma == true) {
                        Toast.makeText(CapNhatSanPham.this, "Mã SP tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Thongtin.checkloai == false) {
                            Toast.makeText(CapNhatSanPham.this, "Cửa hàng k có loại này", Toast.LENGTH_SHORT).show();
                        } else {
                            xulyCapNhat();
                            finish();
                        }
                    }
                }
                Thongtin.checkcam = false;
                Thongtin.checkma = false;
                Thongtin.checkloai = false;
                Thongtin.kt = true;
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void xulyCapNhat() {
        Thongtin.firstv = 0;
        Thongtin.swappage = true;
        String ma = etma.getText().toString();
        String ten = etten.getText().toString();
        String loai = etloai.getText().toString();
        String tt = ettt.getText().toString();
        int gia = Integer.parseInt(etgia.getText().toString());
        if (Thongtin.module == "Them") {
            Thongtin.arrayUpdate.add(new UpDateData(Thongtin.sp, ma, ten, loai, tt, gia, bitmap));
            Thongtin.sp += 1;
            Toast.makeText(CapNhatSanPham.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
        } else {
            if (Thongtin.checkcam == false) {
                if (Thongtin.function == false) {
                    Thongtin.arrayMoHinh.get(index).setMasp(ma);
                    Thongtin.arrayUpdate.add(new UpDateData(Thongtin.sp, ma, ten, loai, tt, gia, bitmap));
                    Thongtin.sp += 1;
                } else {
                    for (int i = 0; i < Thongtin.arrayMoHinh.size(); i++) {
                        if (Thongtin.arrayLoaiSP.get(index).getMasp().toString().equals(Thongtin.arrayMoHinh.get(i).getMasp().toString())) {
                            Thongtin.arrayMoHinh.get(i).setMasp(ma);
                            Thongtin.arrayUpdate.add(new UpDateData(Thongtin.sp, ma, ten, loai, tt, gia, bitmap));
                            Thongtin.sp += 1;
                        }
                    }
                }
                Toast.makeText(CapNhatSanPham.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            } else {
                if (Thongtin.function == false) {
                    Thongtin.arrayMoHinh.get(index).setMasp(ma);
                    Thongtin.arrayMoHinh.get(index).setAnh("");
                    Thongtin.arrayUpdate.add(new UpDateData(Thongtin.sp, ma, ten, loai, tt, gia, bitmap));
                    Thongtin.sp += 1;
                } else {
                    for (int i = 0; i < Thongtin.arrayMoHinh.size(); i++) {
                        if (Thongtin.arrayLoaiSP.get(index).getMasp().toString().equals(Thongtin.arrayMoHinh.get(i).getMasp().toString())) {
                            Thongtin.arrayLoaiSP.get(index).setMasp(ma);
                            Thongtin.arrayMoHinh.get(i).setMasp(ma);
                            Thongtin.arrayUpdate.add(new UpDateData(Thongtin.sp, ma, ten, loai, tt, gia, bitmap));
                            Thongtin.sp += 1;
                        }
                    }
                }
                Thongtin.checkdoianh = true;
                Toast.makeText(CapNhatSanPham.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
        }
        Thongtin.checkcam = false;
        Thongtin.checkma = false;
        Thongtin.checkloai = false;
    }

    public void kiemtra() {
        String ma = etma.getText().toString();
        String ten = etten.getText().toString();
        String loai = etloai.getText().toString();
        String tt = ettt.getText().toString();
        int gia = Integer.parseInt(etgia.getText().toString());
        if (ma.length() == 0 || ten.length() == 0 || loai.length() == 0 || tt.length() == 0) {
            Thongtin.kt = false;
            Thongtin.checkcam = false;
            Thongtin.checkma = false;
            Thongtin.checkloai = false;
            Toast.makeText(CapNhatSanPham.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < Thongtin.arrayMoHinh.size(); i++) {
                if (ma.equals(Thongtin.arrayMoHinh.get(i).getMasp().toString())) {
                    if (Thongtin.module.equals("Them")) {
                        Thongtin.checkma = true;
                    } else {
                        if (Thongtin.function == true) {
                            if (Thongtin.arrayLoaiSP.get(index).getMasp().equals(ma)) {
                                Thongtin.checkma = false;
                            } else {
                                Thongtin.checkma = true;
                            }
                        } else {
                            if (Thongtin.arrayMoHinh.get(index).getMasp().equals(ma)) {
                                Thongtin.checkma = false;
                            } else {
                                Thongtin.checkma = true;
                            }
                        }
                    }
                }
            }
            if (Thongtin.checkma == false) {
                for (int i = 0; i < Thongtin.listLoai.size(); i++) {
                    if (loai.equals(Thongtin.listLoai.get(i).getMaloaisp())) {
                        Thongtin.checkloai = true;
                    }
                }
            }
        }
    }


}