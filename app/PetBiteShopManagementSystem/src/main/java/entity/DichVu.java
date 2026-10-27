package entity;

import java.math.BigDecimal;

/** Dịch vụ chăm sóc thú cưng. */
public class DichVu {

    private String maDV;
    private String tenDV;
    private BigDecimal giaDichVu;
    private int thoiGianThucHien;
    private String moTa;            // có thể null
    private boolean trangThai;      // true: Đang cung cấp, false: Ngừng cung cấp

    public DichVu() {
        this.trangThai = true;      //trạng thái mặc định: đang cung cấp
    }

    public DichVu(String maDV, String tenDV, BigDecimal giaDichVu, int thoiGianThucHien,
                  String moTa, boolean trangThai) {
        setMaDV(maDV);
        setTenDV(tenDV);
        setGiaDichVu(giaDichVu);
        setThoiGianThucHien(thoiGianThucHien);
        setMoTa(moTa);
        setTrangThai(trangThai);
    }

    public DichVu(DichVu dv) {
        this.maDV = dv.maDV;
        this.tenDV = dv.tenDV;
        this.giaDichVu = dv.giaDichVu;
        this.thoiGianThucHien = dv.thoiGianThucHien;
        this.moTa = dv.moTa;
        this.trangThai = dv.trangThai;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        if (maDV == null || !maDV.matches("DV\\d{9}")) {
            throw new IllegalArgumentException("Mã dịch vụ phải có dạng DVYYMMDDXXX (DV + 9 chữ số).");
        }
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        if (tenDV == null || tenDV.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được để trống.");
        }
        this.tenDV = tenDV.trim();
    }

    public BigDecimal getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(BigDecimal giaDichVu) {
        if (giaDichVu == null || giaDichVu.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá dịch vụ phải lớn hơn 0.");
        }
        this.giaDichVu = giaDichVu;
    }

    public int getThoiGianThucHien() {
        return thoiGianThucHien;
    }

    public void setThoiGianThucHien(int thoiGianThucHien) {
        if (thoiGianThucHien <= 0) {
            throw new IllegalArgumentException("Thời gian thực hiện phải lớn hơn 0 (phút).");
        }
        this.thoiGianThucHien = thoiGianThucHien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "DichVu [maDV=" + maDV + ", tenDV=" + tenDV + ", giaDichVu=" + giaDichVu
                + ", thoiGianThucHien=" + thoiGianThucHien + " phút, moTa=" + moTa
                + ", trangThai=" + (trangThai ? "Đang cung cấp" : "Ngừng cung cấp") + "]";
    }
}