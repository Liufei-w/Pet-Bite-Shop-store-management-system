package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class PhieuTraHang {
    private String maPhieuTra;
    private LocalDate ngayLap;
    private String trangThai;
    private BigDecimal tongTienDuKien;
    private BigDecimal soTienDuocDuyet;
    private BigDecimal soTienThucTra;
    private String lyDoTuChoi;
    private LocalDateTime thoiDiemHoanTat;
    private String lyDoGiamHoan;

    private HoaDon hoaDon;
    private NhanVien nguoiLap;
    private NhanVien nguoiDuyet;
    private ArrayList<ChiTietPhieuTraHang> dsChiTietPhieuTra;

    public PhieuTraHang() {
        this.dsChiTietPhieuTra = new ArrayList<>();
    }

    public PhieuTraHang(String maPhieuTra) {
        this.maPhieuTra = maPhieuTra;
        this.dsChiTietPhieuTra = new ArrayList<>();
    }

    public PhieuTraHang(String maPhieuTra, LocalDate ngayLap, String trangThai, 
                        BigDecimal tongTienDuKien, BigDecimal soTienDuocDuyet, 
                        BigDecimal soTienThucTra, String lyDoTuChoi, 
                        LocalDateTime thoiDiemHoanTat, String lyDoGiamHoan,
                        HoaDon hoaDon, NhanVien nguoiLap, NhanVien nguoiDuyet) {
        this.maPhieuTra = maPhieuTra;
        this.ngayLap = ngayLap;
        this.trangThai = trangThai;
        this.tongTienDuKien = tongTienDuKien;
        this.soTienDuocDuyet = soTienDuocDuyet;
        this.soTienThucTra = soTienThucTra;
        this.lyDoTuChoi = lyDoTuChoi;
        this.thoiDiemHoanTat = thoiDiemHoanTat;
        this.lyDoGiamHoan = lyDoGiamHoan;
        this.hoaDon = hoaDon;
        this.nguoiLap = nguoiLap;
        this.nguoiDuyet = nguoiDuyet;
        this.dsChiTietPhieuTra = new ArrayList<>();
    }

    // Getter và Setter
    public String getMaPhieuTra() { return maPhieuTra; }
    public void setMaPhieuTra(String maPhieuTra) { this.maPhieuTra = maPhieuTra; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public BigDecimal getTongTienDuKien() { return tongTienDuKien; }
    public void setTongTienDuKien(BigDecimal tongTienDuKien) { this.tongTienDuKien = tongTienDuKien; }

    public BigDecimal getSoTienDuocDuyet() { return soTienDuocDuyet; }
    public void setSoTienDuocDuyet(BigDecimal soTienDuocDuyet) { this.soTienDuocDuyet = soTienDuocDuyet; }

    public BigDecimal getSoTienThucTra() { return soTienThucTra; }
    public void setSoTienThucTra(BigDecimal soTienThucTra) { this.soTienThucTra = soTienThucTra; }

    public String getLyDoTuChoi() { return lyDoTuChoi; }
    public void setLyDoTuChoi(String lyDoTuChoi) { this.lyDoTuChoi = lyDoTuChoi; }

    public LocalDateTime getThoiDiemHoanTat() { return thoiDiemHoanTat; }
    public void setThoiDiemHoanTat(LocalDateTime thoiDiemHoanTat) { this.thoiDiemHoanTat = thoiDiemHoanTat; }

    public String getLyDoGiamHoan() { return lyDoGiamHoan; }
    public void setLyDoGiamHoan(String lyDoGiamHoan) { this.lyDoGiamHoan = lyDoGiamHoan; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    public NhanVien getNguoiLap() { return nguoiLap; }
    public void setNguoiLap(NhanVien nguoiLap) { this.nguoiLap = nguoiLap; }

    public NhanVien getNguoiDuyet() { return nguoiDuyet; }
    public void setNguoiDuyet(NhanVien nguoiDuyet) { this.nguoiDuyet = nguoiDuyet; }

    public ArrayList<ChiTietPhieuTraHang> getDsChiTietPhieuTra() { return dsChiTietPhieuTra; }

    public void addChiTietPhieuTra(ChiTietPhieuTraHang chiTiet) {
        if (chiTiet != null) {
            this.dsChiTietPhieuTra.add(chiTiet);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieuTra);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhieuTraHang other = (PhieuTraHang) obj;
        return Objects.equals(maPhieuTra, other.maPhieuTra);
    }

    @Override
    public String toString() {
        return "PhieuTraHang [maPhieuTra=" + maPhieuTra + ", ngayLap=" + ngayLap + 
               ", trangThai=" + trangThai + ", soTienThucTra=" + soTienThucTra + "]";
    }
}