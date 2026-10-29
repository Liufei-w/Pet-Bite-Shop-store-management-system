package dao;

import connectDB.ConnectDB;
import entity.SanPham;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SanPham_DAO {

    public SanPham_DAO() {
    }

    // Lấy danh sách toàn bộ sản phẩm đang kinh doanh và còn tồn kho
    public ArrayList<SanPham> getAllSanPhamDangKinhDoanh() {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        Statement statement = null;
        try {
            String sql = "SELECT * FROM SanPham WHERE trangThai = 1 AND soLuongTon > 0";
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                String donViTinh = rs.getString("donViTinh");
                BigDecimal giaBan = rs.getBigDecimal("giaBan"); 
                int soLuongTon = rs.getInt("soLuongTon");
                boolean trangThai = rs.getBoolean("trangThai");
                
                SanPham sp = new SanPham(maSP, tenSP, donViTinh, giaBan, soLuongTon, trangThai);
                dsSP.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsSP;
    }

    // Tìm sản phẩm theo mã sản phẩm
    public SanPham getSanPhamTheoMa(String ma) {
        SanPham sp = null;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM SanPham WHERE maSP = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, ma);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                String donViTinh = rs.getString("donViTinh");
                BigDecimal giaBan = rs.getBigDecimal("giaBan"); 
                int soLuongTon = rs.getInt("soLuongTon");
                boolean trangThai = rs.getBoolean("trangThai");
                
                sp = new SanPham(maSP, tenSP, donViTinh, giaBan, soLuongTon, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sp;
    }
 // Tìm danh sách sản phẩm theo mã hoặc tên gần đúng (Hỗ trợ tốt tiếng Việt NVARCHAR)
    public ArrayList<SanPham> timSanPhamTheoMaHoacTen(String keyword) {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            // Sử dụng N? để tương thích hoàn toàn với kiểu NVARCHAR (tiếng Việt có dấu) trong SQL Server
            String sql = "SELECT * FROM SanPham WHERE (maSP LIKE ? OR tenSP LIKE N'%'+?+'%') AND trangThai = 1 AND soLuongTon > 0";
            statement = con.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, keyword);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                String donViTinh = rs.getString("donViTinh");
                java.math.BigDecimal giaBan = rs.getBigDecimal("giaBan");
                int soLuongTon = rs.getInt("soLuongTon");
                boolean trangThai = rs.getBoolean("trangThai");
                
                SanPham sp = new SanPham(maSP, tenSP, donViTinh, giaBan, soLuongTon, trangThai);
                dsSP.add(sp);
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
        return dsSP;
    }
 // Lấy toàn bộ danh sách sản phẩm (hỗ trợ hiển thị danh sách gợi ý nhanh)
    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        Statement statement = null;
        try {
            String sql = "SELECT * FROM SanPham WHERE trangThai = 1 AND soLuongTon > 0";
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                String donViTinh = rs.getString("donViTinh");
                java.math.BigDecimal giaBan = rs.getBigDecimal("giaBan");
                int soLuongTon = rs.getInt("soLuongTon");
                boolean trangThai = rs.getBoolean("trangThai");
                
                SanPham sp = new SanPham(maSP, tenSP, donViTinh, giaBan, soLuongTon, trangThai);
                dsSP.add(sp);
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
        return dsSP;
    }
}