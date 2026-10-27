package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon_DichVu {

    private String maLichHen;
    private DichVu dichVu;
    private BigDecimal donGia;

    public ChiTietHoaDon_DichVu() {
    }

    public ChiTietHoaDon_DichVu(String maLichHen, DichVu dichVu, BigDecimal donGia) {
        setMaLichHen(maLichHen);
        setDichVu(dichVu);
        setDonGia(donGia);
    }

    public ChiTietHoaDon_DichVu(ChiTietHoaDon_DichVu ct) {
        this.maLichHen = ct.maLichHen;
        this.dichVu = ct.dichVu;
        this.donGia = ct.donGia;
    }

    public String getMaLichHen() {
        return maLichHen;
    }

    public void setMaLichHen(String maLichHen) {
        if (maLichHen == null || !maLichHen.matches("LH\\d{9}")) {
            throw new IllegalArgumentException("Mã lịch hẹn phải có dạng LHYYMMDDXXX (LH + 9 chữ số).");
        }
        this.maLichHen = maLichHen;
    }

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        if (dichVu == null) {
            throw new IllegalArgumentException("Dịch vụ trong hóa đơn không được để trống.");
        }
        this.dichVu = dichVu;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Đơn giá dịch vụ phải lớn hơn 0.");
        }
        this.donGia = donGia;
    }

    //Nghiệp vụ
    /** Mỗi dịch vụ tính một lần: thành tiền = đơn giá. */
    public BigDecimal tinhThanhTien() {
        if (donGia == null) {
            return BigDecimal.ZERO;
        }
        return donGia;
    }

    public BigDecimal getThanhTien() {
        return tinhThanhTien();
    }

    @Override
    public String toString() {
        String ten = (dichVu == null) ? "?" : dichVu.getTenDV();
        return "ChiTietHoaDon_DichVu [maLichHen=" + maLichHen + ", dichVu=" + ten
                + ", donGia=" + donGia + ", thanhTien=" + tinhThanhTien() + "]";
    }
}