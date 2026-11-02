package dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TaiKhoan_DAO {

    public enum LoginStatus {
        SUCCESS,
        INVALID_CREDENTIALS,
        ACCOUNT_LOCKED,
        EMPLOYEE_INACTIVE,
        DATABASE_ERROR
    }

    public static class LoginResult {
        private final LoginStatus status;
        private final TaiKhoan taiKhoan;
        private final String message;

        public LoginResult(LoginStatus status, TaiKhoan taiKhoan, String message) {
            this.status = status;
            this.taiKhoan = taiKhoan;
            this.message = message;
        }

        public LoginStatus getStatus() {
            return status;
        }

        public TaiKhoan getTaiKhoan() {
            return taiKhoan;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return status == LoginStatus.SUCCESS;
        }
    }

    public TaiKhoan_DAO() {
    }

    /**
     * Băm mật khẩu theo chuẩn SHA-256 (cho ra chuỗi hex 64 ký tự thường).
     */
    public static String hashPassword(String password) {
        if (password == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

    /**
     * Thực hiện kiểm tra đăng nhập bằng tên đăng nhập và mật khẩu.
     */
    public LoginResult dangNhap(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Vui lòng nhập tên đăng nhập!");
        }
        if (matKhau == null || matKhau.trim().isEmpty()) {
            return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Vui lòng nhập mật khẩu!");
        }

        String username = tenDangNhap.trim();
        String hashedPassword = hashPassword(matKhau);

        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            if (con != null) {
                String sql = "SELECT t.maTK, t.tenDangNhap, t.matKhau, t.trangThai AS tk_trangThai, "
                           + "       n.maNV, n.hoTen, n.soDienThoai, n.gioiTinh, n.ngaySinh, n.diaChi, n.vaiTro, n.trangThai AS nv_trangThai "
                           + "FROM TaiKhoan t "
                           + "LEFT JOIN NhanVien n ON t.maNV = n.maNV "
                           + "WHERE t.tenDangNhap = ?";

                statement = con.prepareStatement(sql);
                statement.setString(1, username);
                rs = statement.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("matKhau");
                    boolean tkTrangThai = rs.getBoolean("tk_trangThai");

                    // Khớp mật khẩu (hỗ trợ cả hash SHA-256 và so khớp gốc nếu cần)
                    boolean passwordMatches = storedHash.equalsIgnoreCase(hashedPassword) || storedHash.equals(matKhau);
                    if (!passwordMatches) {
                        return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Tên đăng nhập hoặc mật khẩu không chính xác!");
                    }

                    if (!tkTrangThai) {
                        return new LoginResult(LoginStatus.ACCOUNT_LOCKED, null, "Tài khoản của bạn đã bị khóa! Vui lòng liên hệ quản trị viên.");
                    }

                    // Lấy thông tin nhân viên
                    NhanVien nv = null;
                    String maNV = rs.getString("maNV");
                    if (maNV != null) {
                        String hoTen = rs.getString("hoTen");
                        String soDienThoai = rs.getString("soDienThoai");
                        String gioiTinh = rs.getString("gioiTinh");
                        Date ngaySinhSql = rs.getDate("ngaySinh");
                        LocalDate ngaySinh = ngaySinhSql != null ? ngaySinhSql.toLocalDate() : LocalDate.of(1990, 1, 1);
                        String diaChi = rs.getString("diaChi");
                        String vaiTro = rs.getString("vaiTro");
                        boolean nvTrangThai = rs.getBoolean("nv_trangThai");

                        if (!nvTrangThai) {
                            return new LoginResult(LoginStatus.EMPLOYEE_INACTIVE, null, "Nhân viên đã ngừng làm việc tại hệ thống!");
                        }

                        nv = new NhanVien(maNV, hoTen, soDienThoai, gioiTinh, ngaySinh, diaChi, vaiTro, nvTrangThai);
                    }

                    String maTK = rs.getString("maTK");
                    TaiKhoan tk = new TaiKhoan(username, storedHash, tkTrangThai, maTK, nv);
                    return new LoginResult(LoginStatus.SUCCESS, tk, "Đăng nhập thành công!");
                } else {
                    return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Tên đăng nhập hoặc mật khẩu không chính xác!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Dự phòng (fallback) tài khoản mẫu seed data nếu DB ngắt kết nối đột xuất
        return kiemTraDangNhapDuPhong(username, matKhau);
    }

    /**
     * Dữ liệu mẫu dự phòng khi chưa khởi động SQL Server
     */
    private LoginResult kiemTraDangNhapDuPhong(String username, String matKhau) {
        String defaultPass = "PetBite@123";
        if (username.equalsIgnoreCase("thungan3")) {
            return new LoginResult(LoginStatus.ACCOUNT_LOCKED, null, "Tài khoản của bạn đã bị khóa! Vui lòng liên hệ quản trị viên.");
        }

        if (matKhau.equals(defaultPass) || hashPassword(matKhau).equals("f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc")) {
            NhanVien nv;
            switch (username.toLowerCase()) {
                case "chu":
                    nv = new NhanVien("NV251001001", "Nguyễn Văn Hùng", "0901000001", "Nam", LocalDate.of(1985, 4, 12), "25 Trần Hưng Đạo, Quận 1", "Chủ cửa hàng", true);
                    break;
                case "thungan1":
                    nv = new NhanVien("NV251001002", "Trần Thị Mai", "0901000002", "Nữ", LocalDate.of(1998, 8, 21), "14 Lê Văn Sỹ, Quận 3", "Thu ngân", true);
                    break;
                case "thungan2":
                    nv = new NhanVien("NV251001003", "Lê Hoàng Nam", "0901000003", "Nam", LocalDate.of(1999, 2, 14), "60 Nguyễn Thị Minh Khai, Quận 1", "Thu ngân", true);
                    break;
                case "chamsoc1":
                    nv = new NhanVien("NV251001004", "Phạm Thu Hà", "0901000004", "Nữ", LocalDate.of(1997, 11, 30), "8 Hai Bà Trưng, Quận 1", "Chăm sóc", true);
                    break;
                case "chamsoc2":
                    nv = new NhanVien("NV251001005", "Võ Minh Tuấn", "0901000005", "Nam", LocalDate.of(1996, 6, 5), "102 Pasteur, Quận 3", "Chăm sóc", true);
                    break;
                default:
                    return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Tên đăng nhập hoặc mật khẩu không chính xác!");
            }
            TaiKhoan tk = new TaiKhoan(username, hashPassword(matKhau), true, "TK_FALLBACK", nv);
            return new LoginResult(LoginStatus.SUCCESS, tk, "Đăng nhập thành công (Dự phòng)!");
        }

        return new LoginResult(LoginStatus.INVALID_CREDENTIALS, null, "Tên đăng nhập hoặc mật khẩu không chính xác!");
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        if (con == null) return list;

        String sql = "SELECT t.maTK, t.tenDangNhap, t.matKhau, t.trangThai, t.maNV, n.hoTen "
                   + "FROM TaiKhoan t LEFT JOIN NhanVien n ON t.maNV = n.maNV";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String maTK = rs.getString("maTK");
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                boolean trangThai = rs.getBoolean("trangThai");
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");

                NhanVien nv = new NhanVien();
                nv.setMaNV(maNV);
                nv.setHoTen(hoTen);

                list.add(new TaiKhoan(tenDangNhap, matKhau, trangThai, maTK, nv));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            System.err.println("[themTaiKhoan] ConnectDB.getConnection() trả về null!");
            return false;
        }

        String sql = "INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, trangThai, maNV) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTaiKhoan());
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMatKhau());
            ps.setBoolean(4, tk.isTrangThai());
            ps.setString(5, tk.getNhanVien() != null ? tk.getNhanVien().getMaNV() : null);

            int rows = ps.executeUpdate();
            System.out.println("[themTaiKhoan] Đã thêm thành công: " + tk.getMaTaiKhoan() + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[themTaiKhoan] Lỗi SQL khi thêm tài khoản " + tk.getMaTaiKhoan() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean capNhatTaiKhoan(String maNV, String matKhauMoi, boolean trangThai) {
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            System.err.println("[capNhatTaiKhoan] ConnectDB.getConnection() trả về null!");
            return false;
        }

        String sql;
        boolean doiMatKhau = matKhauMoi != null && !matKhauMoi.trim().isEmpty();
        if (doiMatKhau) {
            sql = "UPDATE TaiKhoan SET matKhau = ?, trangThai = ? WHERE maNV = ?";
        } else {
            sql = "UPDATE TaiKhoan SET trangThai = ? WHERE maNV = ?";
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (doiMatKhau) {
                ps.setString(1, matKhauMoi);
                ps.setBoolean(2, trangThai);
                ps.setString(3, maNV);
            } else {
                ps.setBoolean(1, trangThai);
                ps.setString(2, maNV);
            }
            int rows = ps.executeUpdate();
            System.out.println("[capNhatTaiKhoan] Đã cập nhật thành công cho maNV=" + maNV + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[capNhatTaiKhoan] Lỗi SQL khi cập nhật tài khoản: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public String generateNextMaTK() {
        Connection con = ConnectDB.getConnection();
        String prefix = "TK" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
        if (con == null) {
            return prefix + "001";
        }
        String sql = "SELECT MAX(maTK) AS maxMa FROM TaiKhoan WHERE maTK LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maxMa = rs.getString("maxMa");
                    if (maxMa != null && maxMa.length() >= prefix.length() + 3) {
                        int stt = Integer.parseInt(maxMa.substring(prefix.length())) + 1;
                        return prefix + String.format("%03d", stt);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001";
    }
}
