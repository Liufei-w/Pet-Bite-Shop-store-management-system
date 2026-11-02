package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class LichHen {
    private String maLichHen;
    private LocalDateTime ngayGioHen;
    private String trangThai;
    private LocalDateTime thoiDiemHoanThanh;

    private KhachHang khachHang;
    private ThuCung thuCung;
    private ArrayList<ChiTietLichHen> dsChiTietLichHen;

    public LichHen() {
        this.dsChiTietLichHen = new ArrayList<>();
    }

    public LichHen(String maLichHen) {
        this.maLichHen = maLichHen;
        this.dsChiTietLichHen = new ArrayList<>();
    }

    public LichHen(String maLichHen, LocalDateTime ngayGioHen, String trangThai, 
                   LocalDateTime thoiDiemHoanThanh, KhachHang khachHang, ThuCung thuCung) {
        this.maLichHen = maLichHen;
        this.ngayGioHen = ngayGioHen;
        this.trangThai = trangThai;
        this.thoiDiemHoanThanh = thoiDiemHoanThanh;
        this.khachHang = khachHang;
        this.thuCung = thuCung;
        this.dsChiTietLichHen = new ArrayList<>();
    }

    // Getter và Setter
    public String getMaLichHen() { return maLichHen; }
    public void setMaLichHen(String maLichHen) { this.maLichHen = maLichHen; }

    public LocalDateTime getNgayGioHen() { return ngayGioHen; }
    public void setNgayGioHen(LocalDateTime ngayGioHen) { this.ngayGioHen = ngayGioHen; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LocalDateTime getThoiDiemHoanThanh() { return thoiDiemHoanThanh; }
    public void setThoiDiemHoanThanh(LocalDateTime thoiDiemHoanThanh) { this.thoiDiemHoanThanh = thoiDiemHoanThanh; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public ThuCung getThuCung() { return thuCung; }
    public void setThuCung(ThuCung thuCung) { this.thuCung = thuCung; }

    public ArrayList<ChiTietLichHen> getDsChiTietLichHen() { return dsChiTietLichHen; }

    public void addChiTietLichHen(ChiTietLichHen chiTiet) {
        if (chiTiet != null) {
            this.dsChiTietLichHen.add(chiTiet);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLichHen);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LichHen other = (LichHen) obj;
        return Objects.equals(maLichHen, other.maLichHen);
    }

    @Override
    public String toString() {
        return "LichHen [maLichHen=" + maLichHen + ", ngayGioHen=" + ngayGioHen + ", trangThai=" + trangThai + "]";
    }
}