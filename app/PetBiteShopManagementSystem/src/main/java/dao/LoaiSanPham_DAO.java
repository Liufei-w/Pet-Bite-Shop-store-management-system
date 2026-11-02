package dao;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Chỉ đọc và ghi LoaiSanPham. Không kiểm tra quyền, không quyết định được ngừng hoạt động hay không.
 * Hàm nhận Connection dùng chung kết nối của lời gọi service để nằm trong cùng một transaction.
 */
public class LoaiSanPham_DAO {

    public LoaiSanPham_DAO() {
    }

    public ArrayList<LoaiSanPham> getAllLoaiSanPham() {
        return locLoaiSanPham(null, null, "CU_NHAT");
    }

    public ArrayList<LoaiSanPham> getLoaiSanPhamDangHoatDong() {
        return locLoaiSanPham(null, Boolean.TRUE, "TEN_AZ");
    }

    /**
     * tuKhoa tìm theo mã hoặc tên. trangThai null là mọi trạng thái.
     * kieuSapXep: MOI_NHAT, CU_NHAT, TEN_AZ, TEN_ZA.
     * Trả về null khi không kết nối hoặc câu lệnh lỗi.
     */
    public ArrayList<LoaiSanPham> locLoaiSanPham(String tuKhoa, Boolean trangThai, String kieuSapXep) {
        Connection con = ketNoi();
        if (con == null) {
            return null;
        }
        try {
            return locLoaiSanPham(con, tuKhoa, trangThai, kieuSapXep);
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<LoaiSanPham> locLoaiSanPham(Connection con, String tuKhoa, Boolean trangThai, String kieuSapXep)
            throws SQLException {
        ArrayList<LoaiSanPham> dsLoai = new ArrayList<LoaiSanPham>();
        StringBuilder sql = new StringBuilder(
                "SELECT maLoaiSP, tenLoaiSP, moTa, trangThai FROM LoaiSanPham WHERE 1 = 1 ");
        if (coNoiDung(tuKhoa)) {
            sql.append("AND (maLoaiSP LIKE ? OR tenLoaiSP LIKE ?) ");
        }
        if (trangThai != null) {
            sql.append("AND trangThai = ? ");
        }
        sql.append("ORDER BY ").append(menhDeSapXep(kieuSapXep));

        try (PreparedStatement statement = con.prepareStatement(sql.toString())) {
            int chiSo = 1;
            if (coNoiDung(tuKhoa)) {
                String mau = "%" + tuKhoa.trim() + "%";
                statement.setString(chiSo++, mau);
                statement.setNString(chiSo++, mau);
            }
            if (trangThai != null) {
                statement.setBoolean(chiSo++, trangThai);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    dsLoai.add(docLoai(rs));
                }
            }
        }
        return dsLoai;
    }

    public LoaiSanPham getLoaiSanPhamTheoMa(String maLoaiSP) {
        Connection con = ketNoi();
        if (con == null || maLoaiSP == null) {
            return null;
        }
        try {
            return getLoaiSanPhamTheoMa(con, maLoaiSP);
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LoaiSanPham getLoaiSanPhamTheoMa(Connection con, String maLoaiSP) throws SQLException {
        String sql = "SELECT maLoaiSP, tenLoaiSP, moTa, trangThai FROM LoaiSanPham WHERE maLoaiSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, maLoaiSP);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return docLoai(rs);
                }
            }
        }
        return null;
    }

    public LoaiSanPham getLoaiSanPhamTheoMaKhoa(Connection con, String maLoaiSP) throws SQLException {
        String sql = "SELECT maLoaiSP, tenLoaiSP, moTa, trangThai "
                + "FROM LoaiSanPham WITH (UPDLOCK, HOLDLOCK) WHERE maLoaiSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, maLoaiSP);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return docLoai(rs);
                }
            }
        }
        return null;
    }

    public boolean tenLoaiDaTonTai(Connection con, String tenLoai, String maLoaiBoQua) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LoaiSanPham WHERE tenLoaiSP = ? AND (? IS NULL OR maLoaiSP <> ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setNString(1, tenLoai.trim());
            if (maLoaiBoQua == null || maLoaiBoQua.isBlank()) {
                statement.setNull(2, Types.VARCHAR);
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(2, maLoaiBoQua);
                statement.setString(3, maLoaiBoQua);
            }
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public int demSanPhamLienKet(Connection con, String maLoaiSP) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SanPham WITH (HOLDLOCK) WHERE maLoaiSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, maLoaiSP);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public String sinhMaLoaiSanPham(Connection con) throws SQLException {
        String prefix = "LSP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String sql = "SELECT MAX(maLoaiSP) FROM LoaiSanPham WITH (UPDLOCK, HOLDLOCK) WHERE maLoaiSP LIKE ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, prefix + "%");
            try (ResultSet rs = statement.executeQuery()) {
                int soThuTu = 1;
                if (rs.next() && rs.getString(1) != null) {
                    String maLonNhat = rs.getString(1);
                    soThuTu = Integer.parseInt(maLonNhat.substring(maLonNhat.length() - 3)) + 1;
                }
                if (soThuTu > 999) {
                    throw new SQLException("Đã hết số thứ tự loại sản phẩm trong ngày.");
                }
                return prefix + String.format("%03d", soThuTu);
            }
        }
    }

    public void them(Connection con, LoaiSanPham loai) throws SQLException {
        String sql = "INSERT INTO LoaiSanPham (maLoaiSP, tenLoaiSP, moTa, trangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, loai.getMaLoaiSanPham());
            statement.setNString(2, loai.getTenLoai());
            ganMoTa(statement, 3, loai.getMoTa());
            statement.setBoolean(4, loai.isTrangThai());
            statement.executeUpdate();
        }
    }

    public int capNhat(Connection con, LoaiSanPham loai) throws SQLException {
        String sql = "UPDATE LoaiSanPham SET tenLoaiSP = ?, moTa = ?, trangThai = ? WHERE maLoaiSP = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setNString(1, loai.getTenLoai());
            ganMoTa(statement, 2, loai.getMoTa());
            statement.setBoolean(3, loai.isTrangThai());
            statement.setString(4, loai.getMaLoaiSanPham());
            return statement.executeUpdate();
        }
    }

    private void ganMoTa(PreparedStatement statement, int chiSo, String moTa) throws SQLException {
        if (moTa == null) {
            statement.setNull(chiSo, Types.NVARCHAR);
        } else {
            statement.setNString(chiSo, moTa);
        }
    }

    private LoaiSanPham docLoai(ResultSet rs) throws SQLException {
        return new LoaiSanPham(
                rs.getString("maLoaiSP"),
                rs.getString("tenLoaiSP"),
                rs.getString("moTa"),
                rs.getBoolean("trangThai"));
    }

    private String menhDeSapXep(String kieuSapXep) {
        if ("CU_NHAT".equals(kieuSapXep)) {
            return "maLoaiSP ASC";
        }
        if ("TEN_AZ".equals(kieuSapXep)) {
            return "tenLoaiSP ASC";
        }
        if ("TEN_ZA".equals(kieuSapXep)) {
            return "tenLoaiSP DESC";
        }
        return "maLoaiSP DESC";
    }

    private boolean coNoiDung(String giaTri) {
        return giaTri != null && !giaTri.trim().isEmpty();
    }

    private Connection ketNoi() {
        ConnectDB.getInstance();
        return ConnectDB.getConnection();
    }
}
