package entity;

import java.math.BigDecimal;

/**
 * Sản phẩm. soLuongTon là số lượng tồn duy nhất của sản phẩm:
 * bán hàng thì trừ, nhận lại hàng trả nguyên vẹn thì cộng.
 */
public class SanPham {

    private String maSP;
    private String tenSP;
    private String donViTinh;
    private BigDecimal giaBan;
    private int soLuongTon;
    private boolean trangThai;    // true: Đang kinh doanh, false: Ngừng kinh doanh

    public SanPham() {
        this.trangThai = true;    // mặc định: đang kinh doanh
    }

    public SanPham(String maSP, String tenSP, String donViTinh, BigDecimal giaBan,
                   int soLuongTon, boolean trangThai) {
        setMaSP(maSP);
        setTenSP(tenSP);
        setDonViTinh(donViTinh);
        setGiaBan(giaBan);
        setSoLuongTon(soLuongTon);
        setTrangThai(trangThai);
    }

    public SanPham(SanPham sp) {
        this.maSP = sp.maSP;
        this.tenSP = sp.tenSP;
        this.donViTinh = sp.donViTinh;
        this.giaBan = sp.giaBan;
        this.soLuongTon = sp.soLuongTon;
        this.trangThai = sp.trangThai;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        if (maSP == null || !maSP.matches("SP\\d{9}")) {
            throw new IllegalArgumentException("Mã sản phẩm phải có dạng SPYYMMDDXXX (SP + 9 chữ số).");
        }
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        this.tenSP = tenSP.trim();
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Đơn vị tính không được để trống.");
        }
        this.donViTinh = donViTinh.trim();
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        if (giaBan == null || giaBan.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá bán phải lớn hơn 0.");
        }
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm.");
        }
        this.soLuongTon = soLuongTon;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    //Nghiệp vụ
    /**
     * Cộng (soLuong > 0) hoặc trừ (soLuong < 0) số lượng tồn.
     * Trả về false và không thay đổi gì nếu tồn kho sẽ bị âm.
     */
    public boolean capNhatSoLuongTon(int soLuong) {
        int tonMoi = this.soLuongTon + soLuong;
        if (tonMoi < 0) {
            return false;
        }
        this.soLuongTon = tonMoi;
        return true;
    }

    /** Hết hàng khi số lượng tồn bằng 0. */
    public boolean kiemTraHetHang() {
        return soLuongTon == 0;
    }

    @Override
    public String toString() {
        return "SanPham [maSP=" + maSP + ", tenSP=" + tenSP + ", donViTinh=" + donViTinh
                + ", giaBan=" + giaBan + ", soLuongTon=" + soLuongTon
                + ", trangThai=" + (trangThai ? "Đang kinh doanh" : "Ngừng kinh doanh") + "]";
    }
}