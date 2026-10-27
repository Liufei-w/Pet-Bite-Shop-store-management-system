package entity;

import java.time.LocalDate;
import java.time.Period;

/**
 * Nhân viên (có 3 chức vụ: Chủ cửa hàng, Thu ngân, Chăm sóc).
 */
public class NhanVien {

    private String maNV;
    private String hoTen;
    private String soDienThoai;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String diaChi;
    private String vaiTro;
    private boolean trangThai;    // true: Đang làm việc, false: Ngừng làm việc

    public NhanVien() {
        this.trangThai = true;    // mặc định: đang làm việc
    }

    public NhanVien(String maNV, String hoTen, String soDienThoai, String gioiTinh,
                    LocalDate ngaySinh, String diaChi, String vaiTro, boolean trangThai) {
        setMaNV(maNV);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setGioiTinh(gioiTinh);
        setNgaySinh(ngaySinh);
        setDiaChi(diaChi);
        setVaiTro(vaiTro);
        setTrangThai(trangThai);
    }

    public NhanVien(NhanVien nv) {
        this.maNV = nv.maNV;
        this.hoTen = nv.hoTen;
        this.soDienThoai = nv.soDienThoai;
        this.gioiTinh = nv.gioiTinh;
        this.ngaySinh = nv.ngaySinh;
        this.diaChi = nv.diaChi;
        this.vaiTro = nv.vaiTro;
        this.trangThai = nv.trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        if (maNV == null || !maNV.matches("NV\\d{9}")) {
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NVYYMMDDXXX (NV + 9 chữ số).");
        }
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên nhân viên không được để trống.");
        }
        this.hoTen = hoTen.trim();
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches("0[35789]\\d{8}")) {
            throw new IllegalArgumentException("Số điện thoại gồm 10 chữ số, bắt đầu bằng 03, 05, 07, 08 hoặc 09.");
        }
        this.soDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        if (gioiTinh == null || (!gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ") && !gioiTinh.equals("Khác"))) {
            throw new IllegalArgumentException("Giới tính chỉ nhận: Nam, Nữ hoặc Khác.");
        }
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null || ngaySinh.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không được để trống hoặc lớn hơn ngày hiện tại.");
        }
        if (Period.between(ngaySinh, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Nhân viên phải đủ 18 tuổi trở lên.");
        }
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ nhân viên không được để trống.");
        }
        this.diaChi = diaChi.trim();
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        if (vaiTro == null || (!vaiTro.equals("Thu ngân") && !vaiTro.equals("Chăm sóc")
                && !vaiTro.equals("Chủ cửa hàng"))) {
            throw new IllegalArgumentException("Vai trò chỉ nhận: Thu ngân, Chăm sóc hoặc Chủ cửa hàng.");
        }
        this.vaiTro = vaiTro;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    //Nghiệp vụ
    /** Tính tuổi tính đến ngày hiện tại. */
    public int tinhTuoi() {
        return Period.between(ngaySinh, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "NhanVien [maNV=" + maNV + ", hoTen=" + hoTen + ", soDienThoai=" + soDienThoai
                + ", gioiTinh=" + gioiTinh + ", ngaySinh=" + ngaySinh + ", diaChi=" + diaChi
                + ", vaiTro=" + vaiTro + ", trangThai=" + (trangThai ? "Đang làm việc" : "Ngừng làm việc") + "]";
    }
}