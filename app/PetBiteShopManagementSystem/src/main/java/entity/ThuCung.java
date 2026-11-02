package entity;

import java.util.ArrayList;
import java.util.Objects;

public class ThuCung {
    private String maThuCung;
    private String tenThuCung;
    private String loai;
    private String giong;
    private String gioiTinh;
    private double canNang;
    private String trangThai;

    private KhachHang khachHang;
    private ArrayList<LichSuChamSoc> dsLichSuChamSoc;

    public ThuCung() {
        super();
        this.dsLichSuChamSoc = new ArrayList<>();
    }

    public ThuCung(String maThuCung) {
        super();
        this.maThuCung = maThuCung;
        this.dsLichSuChamSoc = new ArrayList<>();
    }

    public ThuCung(String maThuCung, String tenThuCung, String loai, String giong, 
                   String gioiTinh, double canNang, String trangThai, KhachHang khachHang) {
        super();
        setMaThuCung(maThuCung);
        setTenThuCung(tenThuCung);
        setLoai(loai);
        setGiong(giong);
        setGioiTinh(gioiTinh);
        setCanNang(canNang);
        setTrangThai(trangThai);
        this.khachHang = khachHang;
        this.dsLichSuChamSoc = new ArrayList<>();
    }

    public String getMaThuCung() {
        return maThuCung;
    }

    public void setMaThuCung(String maThuCung) {
        if (maThuCung == null || maThuCung.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thú cưng không được để trống.");
        }
        this.maThuCung = maThuCung;
    }

    public String getTenThuCung() {
        return tenThuCung;
    }

    public void setTenThuCung(String tenThuCung) {
        if (tenThuCung == null || tenThuCung.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thú cưng không được để trống.");
        }
        this.tenThuCung = tenThuCung.trim();
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getGiong() {
        return giong;
    }

    public void setGiong(String giong) {
        this.giong = giong;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        if (canNang < 0) {
            throw new IllegalArgumentException("Cân nặng thú cưng không được âm.");
        }
        this.canNang = canNang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public ArrayList<LichSuChamSoc> getDsLichSuChamSoc() {
        return dsLichSuChamSoc;
    }

    public void addLichSuChamSoc(LichSuChamSoc lichSu) {
        if (lichSu != null) {
            this.dsLichSuChamSoc.add(lichSu);
            lichSu.setThuCung(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThuCung);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThuCung other = (ThuCung) obj;
        return Objects.equals(maThuCung, other.maThuCung);
    }

    @Override
    public String toString() {
        String tenKhach = (khachHang == null) ? "?" : khachHang.getHoTen();
        return "ThuCung [maThuCung=" + maThuCung + ", tenThuCung=" + tenThuCung 
                + ", loai=" + loai + ", canNang=" + canNang + ", chuNhan=" + tenKhach + "]";
    }
}