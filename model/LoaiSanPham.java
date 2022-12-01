package vn.edu.stu.quanlycuahang.model;

public class LoaiSanPham {
    private String maloaisp;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String maloaisp) {
        this.maloaisp = maloaisp;
    }

    public String getMaloaisp() {
        return maloaisp;
    }

    public void setMaloaisp(String maloaisp) {
        this.maloaisp = maloaisp;
    }

    @Override
    public String toString() {
        return maloaisp;
    }
}
