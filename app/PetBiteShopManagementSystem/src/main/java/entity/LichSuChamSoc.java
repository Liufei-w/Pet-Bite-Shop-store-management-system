package entity;

import java.util.Objects;

public class LichSuChamSoc {
    private String maLichSuChamSoc;
    private double canNang;
    private String ghiChu;
    
    private ThuCung thuCung;

    public LichSuChamSoc() {
        super();
    }

    public LichSuChamSoc(String maLichSuChamSoc, double canNang, String ghiChu, ThuCung thuCung) {
        super();
        setMaLichSuChamSoc(maLichSuChamSoc);
        setCanNang(canNang);
        this.ghiChu = ghiChu;
        this.thuCung = thuCung;
    }

    public String getMaLichSuChamSoc() {
        return maLichSuChamSoc;
    }

    public void setMaLichSuChamSoc(String maLichSuChamSoc) {
        if (maLichSuChamSoc == null || maLichSuChamSoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch sử chăm sóc không được để trống.");
        }
        this.maLichSuChamSoc = maLichSuChamSoc;
    }

    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        if (canNang <= 0) {
            throw new IllegalArgumentException("Cân nặng phải lớn hơn 0.");
        }
        this.canNang = canNang;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public ThuCung getThuCung() {
        return thuCung;
    }

    public void setThuCung(ThuCung thuCung) {
        this.thuCung = thuCung;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLichSuChamSoc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LichSuChamSoc other = (LichSuChamSoc) obj;
        return Objects.equals(maLichSuChamSoc, other.maLichSuChamSoc);
    }

    @Override
    public String toString() {
        return "LichSuChamSoc [maLichSuChamSoc=" + maLichSuChamSoc + ", canNang=" + canNang 
                + ", ghiChu=" + ghiChu + "]";
    }
}