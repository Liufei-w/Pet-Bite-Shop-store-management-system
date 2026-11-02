package app;

import entity.NhanVien;
import entity.TaiKhoan;

/**
 * Quản lý phiên đăng nhập hiện tại của người dùng trong toàn ứng dụng.
 */
public class UserSession {
    private static UserSession instance;
    private TaiKhoan taiKhoanHienTai;

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoanHienTai = taiKhoan;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoanHienTai;
    }

    public NhanVien getNhanVien() {
        return taiKhoanHienTai != null ? taiKhoanHienTai.getNhanVien() : null;
    }

    public String getTenNguoiDung() {
        if (taiKhoanHienTai != null && taiKhoanHienTai.getNhanVien() != null) {
            return taiKhoanHienTai.getNhanVien().getHoTen();
        }
        if (taiKhoanHienTai != null) {
            return taiKhoanHienTai.getTenDangNhap();
        }
        return "Khách";
    }

    public String getVaiTro() {
        if (taiKhoanHienTai != null && taiKhoanHienTai.getNhanVien() != null) {
            return taiKhoanHienTai.getNhanVien().getVaiTro();
        }
        return "Nhân viên";
    }

    public boolean isLoggedIn() {
        return taiKhoanHienTai != null;
    }

    public void clear() {
        this.taiKhoanHienTai = null;
    }
}
