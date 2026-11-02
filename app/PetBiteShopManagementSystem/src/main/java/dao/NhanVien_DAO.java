package dao;

import connectDB.ConnectDB;
import entity.NhanVien;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        if (con == null) return list;

        String sql = "SELECT maNV, hoTen, soDienThoai, gioiTinh, ngaySinh, diaChi, vaiTro, trangThai FROM NhanVien";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                String sdt = rs.getString("soDienThoai");
                String gioiTinh = rs.getString("gioiTinh");
                Date nsDate = rs.getDate("ngaySinh");
                LocalDate ngaySinh = nsDate != null ? nsDate.toLocalDate() : LocalDate.of(2000, 1, 1);
                String diaChi = rs.getString("diaChi");
                String vaiTro = rs.getString("vaiTro");
                boolean trangThai = rs.getBoolean("trangThai");

                list.add(new NhanVien(maNV, hoTen, sdt, gioiTinh, ngaySinh, diaChi, vaiTro, trangThai));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themNhanVien(NhanVien nv) {
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            System.err.println("[themNhanVien] ConnectDB.getConnection() trả về null!");
            return false;
        }

        String sql = "INSERT INTO NhanVien (maNV, hoTen, soDienThoai, gioiTinh, ngaySinh, diaChi, vaiTro, trangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getGioiTinh());
            ps.setDate(5, Date.valueOf(nv.getNgaySinh()));
            ps.setString(6, nv.getDiaChi());
            ps.setString(7, nv.getVaiTro());
            ps.setBoolean(8, nv.isTrangThai());

            int rows = ps.executeUpdate();
            System.out.println("[themNhanVien] Đã thêm thành công: " + nv.getMaNV() + ", rows=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[themNhanVien] Lỗi SQL khi thêm nhân viên " + nv.getMaNV() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean capNhatNhanVien(NhanVien nv) {
        Connection con = ConnectDB.getConnection();
        if (con == null) return false;

        String sql = "UPDATE NhanVien SET hoTen = ?, soDienThoai = ?, gioiTinh = ?, ngaySinh = ?, diaChi = ?, vaiTro = ?, trangThai = ? "
                   + "WHERE maNV = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getSoDienThoai());
            ps.setString(3, nv.getGioiTinh());
            ps.setDate(4, Date.valueOf(nv.getNgaySinh()));
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getVaiTro());
            ps.setBoolean(7, nv.isTrangThai());
            ps.setString(8, nv.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateNextMaNV() {
        Connection con = ConnectDB.getConnection();
        String prefix = "NV" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
        if (con == null) {
            return prefix + "001";
        }
        String sql = "SELECT MAX(maNV) AS maxMa FROM NhanVien WHERE maNV LIKE ?";
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

