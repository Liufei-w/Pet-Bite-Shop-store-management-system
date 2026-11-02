package dao;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import entity.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Chỉ đọc và ghi SanPham. Không kiểm tra quyền, không quyết định được kinh doanh hay không.
 * Hàm nhận Connection dùng chung kết nối của lời gọi service để nằm trong cùng một transaction.
 * Ba hàm bán hàng giữ nguyên chữ ký: timSanPhamTheoMaHoacTen, getAllSanPham, getSanPhamTheoMa.
 */
public class SanPham_DAO {

    private static final String COT_SAN_PHAM =
            "sp.maSP, sp.tenSP, sp.donViTinh, sp.giaNhap, sp.tienLoi, sp.soLuongTon, sp.trangThai, "
                    + "lsp.maLoaiSP, lsp.tenLoaiSP, lsp.moTa, lsp.trangThai AS trangThaiLoai";

    public SanPham_DAO() {
    }

    public ArrayList<SanPham> getAllSanPhamDangKinhDoanh() {
        return timSanPhamTheoMaHoacTen("");
    }

    public SanPham getSanPhamTheoMa(String ma) {
        if (ma == null || ma.isBlank()) {
            return null;
        }
        Connection con = ketNoi();
        if (con == null) {
            return null;
        }
        try {
            return getSanPhamTheoMa(con, ma);
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SanPham getSanPhamTheoMa(Connection con, String ma) throws SQLException {
        String sql = "SELECT " + COT_SAN_PHAM
                + " FROM SanPham sp INNER JOIN LoaiSanPham lsp ON sp.maLoaiSP = lsp.maLoaiSP WHERE sp.maSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, ma);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return docSanPham(rs);
                }
            }
        }
        return null;
    }

    public ArrayList<SanPham> timSanPhamTheoMaHoacTen(String keyword) {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        Connection con = ketNoi();
        if (con == null) {
            return dsSP;
        }
        String tuKhoa = keyword == null ? "" : keyword.trim();
        String sql = "SELECT " + COT_SAN_PHAM
                + " FROM SanPham sp INNER JOIN LoaiSanPham lsp ON sp.maLoaiSP = lsp.maLoaiSP "
                + "WHERE (sp.maSP LIKE ? OR sp.tenSP LIKE ?) AND sp.trangThai = 1 AND sp.soLuongTon > 0 "
                + "ORDER BY sp.tenSP";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            String mau = "%" + tuKhoa + "%";
            statement.setString(1, mau);
            statement.setNString(2, mau);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    dsSP.add(docSanPham(rs));
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return dsSP;
    }

    public ArrayList<SanPham> getAllSanPham() {
        return timSanPhamTheoMaHoacTen("");
    }

    public ArrayList<SanPham> getDanhSachSanPhamQuanLy() {
        return locDanhSachQuanLy(null, null, null, "CU_NHAT");
    }

    /**
     * Danh sách quản lý: gồm hết hàng và ngừng kinh doanh.
     * maLoai null là mọi loại, trangThai null là mọi trạng thái.
     * kieuSapXep: MOI_NHAT, CU_NHAT, TEN_AZ, TEN_ZA, GIA_TANG, GIA_GIAM.
     * Trả về null khi không kết nối hoặc câu lệnh lỗi.
     */
    public ArrayList<SanPham> locDanhSachQuanLy(String tuKhoa, String maLoai, Boolean trangThai, String kieuSapXep) {
        Connection con = ketNoi();
        if (con == null) {
            return null;
        }
        try {
            return locDanhSachQuanLy(con, tuKhoa, maLoai, trangThai, kieuSapXep);
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<SanPham> locDanhSachQuanLy(Connection con, String tuKhoa, String maLoai,
            Boolean trangThai, String kieuSapXep) throws SQLException {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        StringBuilder sql = new StringBuilder("SELECT " + COT_SAN_PHAM
                + " FROM SanPham sp INNER JOIN LoaiSanPham lsp ON sp.maLoaiSP = lsp.maLoaiSP WHERE 1 = 1 ");
        if (coNoiDung(tuKhoa)) {
            sql.append("AND (sp.maSP LIKE ? OR sp.tenSP LIKE ?) ");
        }
        if (coNoiDung(maLoai)) {
            sql.append("AND sp.maLoaiSP = ? ");
        }
        if (trangThai != null) {
            sql.append("AND sp.trangThai = ? ");
        }
        sql.append("ORDER BY ").append(menhDeSapXepSanPham(kieuSapXep));

        try (PreparedStatement statement = con.prepareStatement(sql.toString())) {
            int chiSo = 1;
            if (coNoiDung(tuKhoa)) {
                String mau = "%" + tuKhoa.trim() + "%";
                statement.setString(chiSo++, mau);
                statement.setNString(chiSo++, mau);
            }
            if (coNoiDung(maLoai)) {
                statement.setString(chiSo++, maLoai.trim());
            }
            if (trangThai != null) {
                statement.setBoolean(chiSo++, trangThai);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    dsSP.add(docSanPham(rs));
                }
            }
        }
        return dsSP;
    }

    public SanPham getSanPhamTheoMaKhoa(Connection con, String maSP) throws SQLException {
        String sql = "SELECT " + COT_SAN_PHAM
                + " FROM SanPham sp WITH (UPDLOCK, HOLDLOCK) "
                + "INNER JOIN LoaiSanPham lsp ON sp.maLoaiSP = lsp.maLoaiSP WHERE sp.maSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, maSP);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return docSanPham(rs);
                }
            }
        }
        return null;
    }

    public String sinhMa(Connection con) throws SQLException {
        String prefix = "SP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String sql = "SELECT MAX(maSP) FROM SanPham WITH (UPDLOCK, HOLDLOCK) WHERE maSP LIKE ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, prefix + "%");
            try (ResultSet rs = statement.executeQuery()) {
                int soThuTu = 1;
                if (rs.next() && rs.getString(1) != null) {
                    String maLonNhat = rs.getString(1);
                    soThuTu = Integer.parseInt(maLonNhat.substring(maLonNhat.length() - 3)) + 1;
                }
                if (soThuTu > 999) {
                    throw new SQLException("Đã hết số thứ tự sản phẩm trong ngày.");
                }
                return prefix + String.format("%03d", soThuTu);
            }
        }
    }

    public void them(Connection con, SanPham sanPham) throws SQLException {
        String sql = "INSERT INTO SanPham (maSP, tenSP, donViTinh, giaNhap, tienLoi, soLuongTon, trangThai, maLoaiSP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, sanPham.getMaSP());
            statement.setNString(2, sanPham.getTenSP());
            statement.setNString(3, sanPham.getDonViTinh());
            statement.setBigDecimal(4, sanPham.getGiaNhap());
            statement.setBigDecimal(5, sanPham.getTienLoi());
            statement.setInt(6, sanPham.getSoLuongTon());
            statement.setBoolean(7, sanPham.isTrangThai());
            statement.setString(8, sanPham.getLoaiSanPham().getMaLoaiSanPham());
            statement.executeUpdate();
        }
    }

    /** Sinh mã rồi ghi. Trả về null khi thành công. Mã được gán vào sản phẩm. */
    public String themSanPham(SanPham sanPham) {
        Connection con = ketNoi();
        if (con == null) {
            return "Không kết nối được cơ sở dữ liệu.";
        }
        boolean daBatDau = false;
        try {
            con.setAutoCommit(false);
            daBatDau = true;
            sanPham.setMaSP(sinhMa(con));
            them(con, sanPham);
            con.commit();
            return null;
        } catch (SQLException ex) {
            rollback(con, daBatDau);
            if (ex.getMessage() != null && ex.getMessage().contains("số thứ tự")) {
                return ex.getMessage();
            }
            ex.printStackTrace();
            return "Không lưu được sản phẩm.";
        } catch (IllegalArgumentException ex) {
            rollback(con, daBatDau);
            return ex.getMessage();
        } finally {
            khoiPhucAutoCommit(con, daBatDau);
        }
    }

    /** Chỉ cập nhật bảng SanPham. donGia đã chốt trên hóa đơn không bị sửa. Trả về null khi thành công. */
    public String capNhatSanPham(SanPham sanPham) {
        Connection con = ketNoi();
        if (con == null) {
            return "Không kết nối được cơ sở dữ liệu.";
        }
        boolean daBatDau = false;
        try {
            con.setAutoCommit(false);
            daBatDau = true;
            if (capNhat(con, sanPham) == 0) {
                con.rollback();
                return "Không cập nhật được sản phẩm.";
            }
            con.commit();
            return null;
        } catch (SQLException ex) {
            rollback(con, daBatDau);
            ex.printStackTrace();
            return "Không cập nhật được sản phẩm.";
        } finally {
            khoiPhucAutoCommit(con, daBatDau);
        }
    }

    /** Chỉ cập nhật bảng SanPham. donGia đã chốt trên hóa đơn không nằm trong câu lệnh này. */
    public int capNhat(Connection con, SanPham sanPham) throws SQLException {
        String sql = "UPDATE SanPham SET tenSP = ?, donViTinh = ?, giaNhap = ?, tienLoi = ?, "
                + "soLuongTon = ?, trangThai = ?, maLoaiSP = ? WHERE maSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setNString(1, sanPham.getTenSP());
            statement.setNString(2, sanPham.getDonViTinh());
            statement.setBigDecimal(3, sanPham.getGiaNhap());
            statement.setBigDecimal(4, sanPham.getTienLoi());
            statement.setInt(5, sanPham.getSoLuongTon());
            statement.setBoolean(6, sanPham.isTrangThai());
            statement.setString(7, sanPham.getLoaiSanPham().getMaLoaiSanPham());
            statement.setString(8, sanPham.getMaSP());
            return statement.executeUpdate();
        }
    }

    private SanPham docSanPham(ResultSet rs) throws SQLException {
        SanPham sanPham = new SanPham(
                rs.getString("maSP"),
                rs.getString("tenSP"),
                rs.getString("donViTinh"),
                rs.getBigDecimal("giaNhap"),
                rs.getBigDecimal("tienLoi"),
                rs.getInt("soLuongTon"),
                rs.getBoolean("trangThai"));
        LoaiSanPham loai = new LoaiSanPham(
                rs.getString("maLoaiSP"),
                rs.getString("tenLoaiSP"),
                rs.getString("moTa"),
                rs.getBoolean("trangThaiLoai"));
        sanPham.setLoaiSanPham(loai);
        return sanPham;
    }

    private String menhDeSapXepSanPham(String kieuSapXep) {
        if ("CU_NHAT".equals(kieuSapXep)) {
            return "sp.maSP ASC";
        }
        if ("TEN_AZ".equals(kieuSapXep)) {
            return "sp.tenSP ASC";
        }
        if ("TEN_ZA".equals(kieuSapXep)) {
            return "sp.tenSP DESC";
        }
        if ("GIA_TANG".equals(kieuSapXep)) {
            return "sp.giaBan ASC";
        }
        if ("GIA_GIAM".equals(kieuSapXep)) {
            return "sp.giaBan DESC";
        }
        return "sp.maSP DESC";
    }

    private boolean coNoiDung(String giaTri) {
        return giaTri != null && !giaTri.trim().isEmpty();
    }

    private Connection ketNoi() {
        ConnectDB.getInstance();
        return ConnectDB.getConnection();
    }

    private void rollback(Connection con, boolean daBatDau) {
        if (!daBatDau) {
            return;
        }
        try {
            con.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void khoiPhucAutoCommit(Connection con, boolean daBatDau) {
        if (!daBatDau) {
            return;
        }
        try {
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
