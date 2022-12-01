package vn.edu.stu.quanlycuahang.model;

public class ByteAnh {
    private int stt;
    private byte[] anh;

    public ByteAnh() {
    }

    public ByteAnh(int stt, byte[] anh) {
        this.stt = stt;
        this.anh = anh;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }
}
