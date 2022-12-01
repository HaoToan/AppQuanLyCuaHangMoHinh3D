package vn.edu.stu.quanlycuahang.model;

public class SanPham {
    private String masp;
    private String tensp;
    private String maloaisp;
    private String thongtin;
    private int gia;
    private String anh;
    public SanPham() {
    }

    public SanPham(String masp, String tensp, String maloaisp, String thongtin, int gia, String anh) {
        this.masp = masp;
        this.tensp = tensp;
        this.maloaisp = maloaisp;
        this.thongtin = thongtin;
        this.gia = gia;
        this.anh = anh;
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

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
