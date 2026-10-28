package dao;

import connectDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import entity.HoaDon; 

public class HoaDon_DAO {

    public HoaDon_DAO() {
    }

    // Lấy toàn bộ danh sách hóa đơn từ cơ sở dữ liệu
    public ArrayList<Object[]> getAllHDBody() {
        ArrayList<Object[]> dshd = new ArrayList<Object[]>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        Statement statement = null;
        try {
            String sql = "SELECT h.maHD, h.ngayLap, kh.hoTen AS tenKH, nv.hoTen AS tenNV, " +
                         "h.phuongThucTT, h.tongTien, h.trangThai " +
                         "FROM HoaDon h " +
                         "JOIN KhachHang kh ON h.maKH = kh.maKH " +
                         "JOIN NhanVien nv ON h.maNV = nv.maNV";
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            
            while (rs.next()) {
                String maHD = rs.getString(1);
                String ngayLap = rs.getString(2);
                String tenKH = rs.getString(3);
                String tenNV = rs.getString(4);
                String ptTT = rs.getString(5);
                double tongTien = rs.getDouble(6);
                String trangThai = rs.getString(7);
                
                Object[] row = {maHD, ngayLap, tenKH, tenNV, ptTT, tongTien, trangThai, "Chi tiết"};
                dshd.add(row);
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
        return dshd;
    }
}