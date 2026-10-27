package entity;

/**
 * Khách hàng.
 * Ràng buộc theo: mã KH + 9 chữ số, số điện thoại 10 số bắt đầu 03/05/07/08/09,
 * giới tính và địa chỉ có thể để trống.
 */
public class KhachHang {

    private String maKH;
    private String hoTen;
    private String soDienThoai;
    private String gioiTinh;   
    private String diaChi;  
    private boolean trangThai;    // true: Đang hoạt động, false: Ngừng hoạt động

    public KhachHang() {
        this.trangThai = true;    // mặc định: đang hoạt động
    }

    public KhachHang(String maKH, String hoTen, String soDienThoai,
                     String gioiTinh, String diaChi, boolean trangThai) {
        setMaKH(maKH);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setGioiTinh(gioiTinh);
        setDiaChi(diaChi);
        setTrangThai(trangThai);
    }

    public KhachHang(KhachHang kh) {
        this.maKH = kh.maKH;
        this.hoTen = kh.hoTen;
        this.soDienThoai = kh.soDienThoai;
        this.gioiTinh = kh.gioiTinh;
        this.diaChi = kh.diaChi;
        this.trangThai = kh.trangThai;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        if (maKH == null || !maKH.matches("KH\\d{9}")) {
            throw new IllegalArgumentException("Mã khách hàng phải có dạng KHYYMMDDXXX (KH + 9 chữ số).");
        }
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên khách hàng không được để trống.");
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
        if (gioiTinh != null && !gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ") && !gioiTinh.equals("Khác")) {
            throw new IllegalArgumentException("Giới tính chỉ nhận: Nam, Nữ hoặc Khác.");
        }
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", hoTen=" + hoTen + ", soDienThoai=" + soDienThoai
                + ", gioiTinh=" + gioiTinh + ", diaChi=" + diaChi
                + ", trangThai=" + (trangThai ? "Đang hoạt động" : "Ngừng hoạt động") + "]";
    }
}