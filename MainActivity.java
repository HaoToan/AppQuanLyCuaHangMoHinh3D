package vn.edu.stu.quanlycuahang;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import vn.edu.stu.quanlycuahang.model.AdapterLoaiSP;
import vn.edu.stu.quanlycuahang.model.AdapterSanPham;
import vn.edu.stu.quanlycuahang.model.LoaiSanPham;
import vn.edu.stu.quanlycuahang.model.SanPham;
import vn.edu.stu.quanlycuahang.model.Thongtin;
import vn.edu.stu.quanlycuahang.model.UpDateData;

public class MainActivity extends AppCompatActivity {
    String SERVER = "http:/172.20.44.183/WebService/getdata.php";
    EditText ettk, etmk;
    Button btnDangnhap;
    private ImageView img;
    ArrayList<SanPham> listSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        getData(SERVER);
        addEvents();
    }
    private void addEvents() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(ettk.getText().toString().trim().equals("admin")&&etmk.getText().toString().trim().equals("admin")){
                    Intent intent = new Intent(MainActivity.this, QuanLySanPham.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }else{
		            Toast.makeText(MainActivity.this, "Thông tin đăng  nhập không chính xác!", Toast.LENGTH_SHORT).show();
	            }
            }
        });
    }
    private void addControls() {
        ettk = findViewById(R.id.etTenDangNhap);
        etmk = findViewById(R.id.etMatKhau);
        btnDangnhap = findViewById(R.id.btnDangNhap);
        img = findViewById(R.id.imghome);
        if (Thongtin.chon == -1) {
            Thongtin.arrayUpdate = new ArrayList<>();
            Thongtin.arrayLoaiSP = new ArrayList<>();
            Thongtin.listLoai = new ArrayList<>();
            Thongtin.arrayMoHinh = new ArrayList<>();
            Thongtin.adapterLoaiSP=new AdapterLoaiSP(MainActivity.this,R.layout.listviewloai_row,Thongtin.listLoai);
            Thongtin.adapterSanPhamTheoloai = new AdapterSanPham(MainActivity.this, R.layout.listviewsp_row, Thongtin.arrayLoaiSP);
            Thongtin.adapterSanPham = new AdapterSanPham(MainActivity.this, R.layout.listviewsp_row, Thongtin.arrayMoHinh);

        }
    }

    private byte[] ImageView_To_Byte(ImageView img) {
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void getData(String server) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                if (Thongtin.chon == -1) {
                                    Thongtin.arrayMoHinh.add(new SanPham(
                                                    object.getString("masp"),
                                                    object.getString("tensp"),
                                                    object.getString("maloaisp"),
                                                    object.getString("thongtin"),
                                                    object.getInt("gia"),
                                                    object.getString("hinhanh")
                                            )
                                    );
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
                                    boolean check = false;
                                    if (Thongtin.listLoai.size() == 0) {
                                        Thongtin.listLoai.add(new LoaiSanPham(object.getString("maloaisp")));
                                    } else {
                                        for (int j = 0; j < Thongtin.listLoai.size(); j++) {
                                            if (Thongtin.listLoai.get(j).getMaloaisp().toString().equals(object.getString("maloaisp"))) {
                                                check = true;
                                            }
                                        }
                                        if (check == false) {
                                            Thongtin.listLoai.add(new LoaiSanPham(object.getString("maloaisp")));
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Thongtin.adapterLoaiSP.notifyDataSetChanged();
                        Thongtin.adapterSanPham.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}
