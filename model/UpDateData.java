package vn.edu.stu.quanlycuahang.model;

import android.graphics.Bitmap;

public class UpDateData {
    private int ind;
    private String masp;
    private String tensp;
    private String maloaisp;
    private String thongtin;
    private int gia;
    private Bitmap anh;

    public UpDateData() {
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getMaloaisp() {
        return maloaisp;
    }

    public void setMaloaisp(String maloaisp) {
        this.maloaisp = maloaisp;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Bitmap getAnh() {
        return anh;
    }

    public void setAnh(Bitmap anh) {
        this.anh = anh;
    }

    public UpDateData(int ind, String masp, String tensp, String maloaisp, String thongtin, int gia, Bitmap anh) {
        this.ind = ind;
        this.masp = masp;
        this.tensp = tensp;
        this.maloaisp = maloaisp;
        this.thongtin = thongtin;
        this.gia = gia;
        this.anh = anh;
    }

}
