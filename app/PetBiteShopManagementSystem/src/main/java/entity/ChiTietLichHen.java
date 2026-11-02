package entity;

public class ChiTietLichHen {
    private String ghiChuRieng;
    private int thoiGianDuKien;
    
    private LichHen lichHen;
    private DichVu dichVu;

    public ChiTietLichHen() {
    }

    public ChiTietLichHen(String ghiChuRieng, int thoiGianDuKien, LichHen lichHen, DichVu dichVu) {
        this.ghiChuRieng = ghiChuRieng;
        this.thoiGianDuKien = thoiGianDuKien;
        this.lichHen = lichHen;
        this.dichVu = dichVu;
    }

    public String getGhiChuRieng() {
        return ghiChuRieng;
    }

    public void setGhiChuRieng(String ghiChuRieng) {
        this.ghiChuRieng = ghiChuRieng;
    }

    public int getThoiGianDuKien() {
        return thoiGianDuKien;
    }

    public void setThoiGianDuKien(int thoiGianDuKien) {
        this.thoiGianDuKien = thoiGianDuKien;
    }

    public LichHen getLichHen() {
        return lichHen;
    }

    public void setLichHen(LichHen lichHen) {
        this.lichHen = lichHen;
    }

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        this.dichVu = dichVu;
    }
}