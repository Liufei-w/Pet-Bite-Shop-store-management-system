package dao;

import connectDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import entity.HoaDon;

public class HoaDon_DAO {

    public HoaDon_DAO() {
    }

    // ==========================================
    // 1. PHẦN DÀNH CHO TRA CỨU HÓA ĐƠN
    // ==========================================
    
    // Lấy toàn bộ danh sách hóa đơn từ cơ sở dữ liệu để hiển thị lên JTable
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

    // ==========================================
    // 2. PHẦN DÀNH CHO BÁN HÀNG & THANH TOÁN
    // ==========================================

    /**
     * Thực hiện thêm hóa đơn mới vào cơ sở dữ liệu kèm theo chi tiết sản phẩm và dịch vụ.
     * Sử dụng cơ chế Transaction (Giao dịch) để bảo đảm tính toàn vẹn dữ liệu.
     * 
     * @param hd Đối tượng Hóa đơn chính
     * @param dsChiTietSP Danh sách chi tiết sản phẩm gồm: [maSP (String), soLuong (int), donGia (BigDecimal), thanhTien (BigDecimal)]
     * @param dsChiTietDV Danh sách chi tiết dịch vụ gồm: [maDV (String), maLichHen (String), donGia (BigDecimal), thanhTien (BigDecimal)]
     * @return true nếu thanh toán và lưu thành công, false nếu có lỗi xảy ra.
     */
    public boolean themHoaDon(HoaDon hd, ArrayList<Object[]> dsChiTietSP, ArrayList<Object[]> dsChiTietDV) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmtHD = null;
        PreparedStatement stmtCTSP = null;
        PreparedStatement stmtUpdateTon = null;
        PreparedStatement stmtCTDV = null;
        PreparedStatement stmtUpdateDV = null;

        try {
            // Tắt chế độ commit tự động để bắt đầu Transaction
            con.setAutoCommit(false);

            String sqlHD = "INSERT INTO HoaDon (maHD, ngayLap, trangThai, phuongThucTT, tienKhachDua, tienThua, tongTien, maKH, maNV) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmtHD = con.prepareStatement(sqlHD);
            stmtHD.setString(1, hd.getMaHD());
            stmtHD.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            stmtHD.setString(3, hd.getTrangThai());
            stmtHD.setString(4, hd.getPhuongThucTT());
            
            if (hd.getTienKhachDua() != null) {
                stmtHD.setBigDecimal(5, hd.getTienKhachDua());
            } else {
                stmtHD.setNull(5, java.sql.Types.DECIMAL);
            }
            
            if (hd.getTienThua() != null) {
                stmtHD.setBigDecimal(6, hd.getTienThua());
            } else {
                stmtHD.setNull(6, java.sql.Types.DECIMAL);
            }
            
            stmtHD.setBigDecimal(7, hd.getTongTien());
            stmtHD.setString(8, hd.getKhachHang().getMaKH()); 
            stmtHD.setString(9, hd.getNhanVien().getMaNV()); 
            stmtHD.executeUpdate();

            // Insert chi tiết sản phẩm và cập nhật giảm số lượng tồn kho tương ứng
            if (dsChiTietSP != null && !dsChiTietSP.isEmpty()) {
                String sqlCTSP = "INSERT INTO ChiTietHoaDon_SanPham (maHD, maSP, soLuong, donGia) VALUES (?, ?, ?, ?)";
                String sqlUpdateTon = "UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ?";
                stmtCTSP = con.prepareStatement(sqlCTSP);
                stmtUpdateTon = con.prepareStatement(sqlUpdateTon);

                for (Object[] ct : dsChiTietSP) {
                    String maSP = (String) ct[0];
                    int soLuong = (int) ct[1];
                    java.math.BigDecimal donGia = (java.math.BigDecimal) ct[2];
                    // Lưu ý: Không lấy thanhTien để insert vì CSDL tự tính toán (computed column)

                    // Lưu chi tiết sản phẩm
                    stmtCTSP.setString(1, hd.getMaHD());
                    stmtCTSP.setString(2, maSP);
                    stmtCTSP.setInt(3, soLuong);
                    stmtCTSP.setBigDecimal(4, donGia);
                    stmtCTSP.executeUpdate();

                    // Giảm tồn kho sản phẩm
                    stmtUpdateTon.setInt(1, soLuong);
                    stmtUpdateTon.setString(2, maSP);
                    stmtUpdateTon.executeUpdate();
                }
            }

            // Insert chi tiết dịch vụ và đánh dấu dịch vụ trong lịch hẹn là đã thanh toán
            if (dsChiTietDV != null && !dsChiTietDV.isEmpty()) {
                String sqlCTDV = "INSERT INTO ChiTietHoaDon_DichVu (maHD, maDV, maLichHen, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)";
                String sqlUpdateDV = "UPDATE ChiTietLichHen SET trangThaiThanhToan = 1 WHERE maLichHen = ? AND maDV = ?";
                stmtCTDV = con.prepareStatement(sqlCTDV);
                stmtUpdateDV = con.prepareStatement(sqlUpdateDV);

                for (Object[] dv : dsChiTietDV) {
                    String maDV = (String) dv[0];
                    String maLichHen = (String) dv[1];
                    java.math.BigDecimal donGia = (java.math.BigDecimal) dv[2];
                    java.math.BigDecimal thanhTien = (java.math.BigDecimal) dv[3];

                    // Lưu chi tiết dịch vụ vào hóa đơn
                    stmtCTDV.setString(1, hd.getMaHD());
                    stmtCTDV.setString(2, maDV);
                    stmtCTDV.setString(3, maLichHen);
                    stmtCTDV.setBigDecimal(4, donGia);
                    stmtCTDV.setBigDecimal(5, thanhTien);
                    stmtCTDV.executeUpdate();

                    // Cập nhật trạng thái thanh toán của dịch vụ trong lịch hẹn thành đã thanh toán (1)
                    stmtUpdateDV.setString(1, maLichHen);
                    stmtUpdateDV.setString(2, maDV);
                    stmtUpdateDV.executeUpdate();
                }
            }

            // Commit toàn bộ giao dịch khi thành công
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hoàn tác nếu có lỗi xảy ra
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (stmtHD != null) stmtHD.close();
                if (stmtCTSP != null) stmtCTSP.close();
                if (stmtUpdateTon != null) stmtUpdateTon.close();
                if (stmtCTDV != null) stmtCTDV.close();
                if (stmtUpdateDV != null) stmtUpdateDV.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Đếm số lượng hóa đơn trong ngày để sinh mã STT (001, 002,...)
    public int demSoLuongHoaDonTrongNgay(String yymmdd) {
        int count = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT COUNT(*) FROM HoaDon WHERE maHD LIKE ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, "HD" + yymmdd + "%");
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