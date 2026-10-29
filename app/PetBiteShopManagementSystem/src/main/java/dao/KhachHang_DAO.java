package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KhachHang_DAO {

    public KhachHang_DAO() {
    }
    
    public KhachHang getKhachHangTheoSDT(String sdt) {
        KhachHang kh = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ? OR maKH = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, sdt);
            statement.setString(2, sdt);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                String maKH = rs.getString("maKH");
                String hoTen = rs.getString("hoTen");
                String soDienThoai = rs.getString("soDienThoai");
                String gioiTinh = rs.getString("gioiTinh");
                String diaChi = rs.getString("diaChi");
                boolean trangThai = rs.getBoolean("trangThai");
                
                kh = new KhachHang(maKH, hoTen, soDienThoai, gioiTinh, diaChi, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return kh;
    }

    // Thêm mới khách hàng khi chưa có trên hệ thống
    public boolean createKhachHang(KhachHang kh) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        int n = 0;
        try {
            String sql = "INSERT INTO KhachHang (maKH, hoTen, soDienThoai, gioiTinh, diaChi, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
            statement = con.prepareStatement(sql);
            statement.setString(1, kh.getMaKH());
            statement.setString(2, kh.getHoTen());
            statement.setString(3, kh.getSoDienThoai());
            statement.setString(4, kh.getGioiTinh());
            statement.setString(5, kh.getDiaChi());
            statement.setBoolean(6, kh.isTrangThai());
            
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }
 // Đếm số lượng khách hàng trong ngày để sinh STT tự động (001, 002,...)
    public int demSoLuongKhachHangTrongNgay(String yymmdd) {
        int count = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT COUNT(*) FROM KhachHang WHERE maKH LIKE ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, "KH" + yymmdd + "%");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}