package entity;

import java.math.BigDecimal;

/**
 * Một dòng sản phẩm trong hóa đơn.
 * donGia là giá tại thời điểm bán (có thể khác giá bán hiện tại của sản phẩm).
 * thanhTien không lưu, luôn tính từ soLuong * donGia
 * (trong CSDL đây là cột tính toán nên không được INSERT).
 */
public class ChiTietHoaDon_SanPham {

    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietHoaDon_SanPham() {
    }

    public ChiTietHoaDon_SanPham(SanPham sanPham, int soLuong, BigDecimal donGia) {
        setSanPham(sanPham);
        setSoLuong(soLuong);
        setDonGia(donGia);
    }

    public ChiTietHoaDon_SanPham(ChiTietHoaDon_SanPham ct) {
        this.sanPham = ct.sanPham;
        this.soLuong = ct.soLuong;
        this.donGia = ct.donGia;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        if (sanPham == null) {
            throw new IllegalArgumentException("Sản phẩm trong hóa đơn không được để trống.");
        }
        this.sanPham = sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0.");
        }
        this.donGia = donGia;
    }

    //Nghiệp vụ
    /** Thành tiền = số lượng * đơn giá. */
    public BigDecimal tinhThanhTien() {
        if (donGia == null) {
            return BigDecimal.ZERO;
        }
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    public BigDecimal getThanhTien() {
        return tinhThanhTien();
    }

    @Override
    public String toString() {
        String ten = (sanPham == null) ? "?" : sanPham.getTenSP();
        return "ChiTietHoaDon_SanPham [sanPham=" + ten + ", soLuong=" + soLuong
                + ", donGia=" + donGia + ", thanhTien=" + tinhThanhTien() + "]";
    }
}