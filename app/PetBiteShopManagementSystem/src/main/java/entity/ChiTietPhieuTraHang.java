package entity;

import java.math.BigDecimal;

public class ChiTietPhieuTraHang {
    private int soLuongTra;
    private String tinhTrang;
    private String lyDoTra;
    private BigDecimal donGia;
    private int soThuTu;

    private PhieuTraHang phieuTraHang;
    private SanPham sanPham;

    public ChiTietPhieuTraHang() {
    }

    public ChiTietPhieuTraHang(int soLuongTra, String tinhTrang, String lyDoTra, 
                               BigDecimal donGia, int soThuTu, PhieuTraHang phieuTraHang, SanPham sanPham) {
        setSoLuongTra(soLuongTra);
        setTinhTrang(tinhTrang);
        setLyDoTra(lyDoTra);
        setDonGia(donGia);
        setSoThuTu(soThuTu);
        this.phieuTraHang = phieuTraHang;
        this.sanPham = sanPham;
    }

    public int getSoLuongTra() {
        return soLuongTra;
    }

    public void setSoLuongTra(int soLuongTra) {
        if (soLuongTra <= 0) {
            throw new IllegalArgumentException("Số lượng trả phải lớn hơn 0.");
        }
        this.soLuongTra = soLuongTra;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getLyDoTra() {
        return lyDoTra;
    }

    public void setLyDoTra(String lyDoTra) {
        this.lyDoTra = lyDoTra;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm.");
        }
        this.donGia = donGia;
    }

    public int getSoThuTu() {
        return soThuTu;
    }

    public void setSoThuTu(int soThuTu) {
        this.soThuTu = soThuTu;
    }

    public PhieuTraHang getPhieuTraHang() {
        return phieuTraHang;
    }

    public void setPhieuTraHang(PhieuTraHang phieuTraHang) {
        this.phieuTraHang = phieuTraHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public BigDecimal getTinhThanhTien() {
        if (donGia == null) {
            return BigDecimal.ZERO;
        }
        return donGia.multiply(BigDecimal.valueOf(soLuongTra));
    }
}