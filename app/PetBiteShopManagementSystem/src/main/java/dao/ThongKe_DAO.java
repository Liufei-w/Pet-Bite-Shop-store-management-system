package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.SanPham;

public class ThongKe_DAO {

    /** 1. Thống kê doanh thu (UC033) */
    public ArrayList<Object[]> getThongKeDoanhThu(String tuNgay, String denNgay) {
        ArrayList<Object[]> data = new ArrayList<>();
        String sql = "SELECT h.maHD, h.ngayLap, N'Bán hàng' AS loai, " +
                     "       COALESCE(kh.hoTen, N'Khách lẻ') AS tenKH, " +
                     "       h.tongTienHang AS tienChuaVAT, h.tongTien AS thanhTien " +
                     "FROM HoaDon h " +
                     "LEFT JOIN KhachHang kh ON h.maKH = kh.maKH " +
                     "WHERE CAST(h.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "UNION ALL " +
                     "SELECT p.maPhieuTra AS maHD, p.ngayLap, N'Trả hàng' AS loai, " +
                     "       COALESCE(kh.hoTen, N'Khách lẻ') AS tenKH, " +
                     "       -COALESCE(p.soTienThucTra, 0) AS tienChuaVAT, -COALESCE(p.soTienThucTra, 0) AS thanhTien " +
                     "FROM PhieuTraHang p " +
                     "LEFT JOIN HoaDon h ON p.maHD = h.maHD " +
                     "LEFT JOIN KhachHang kh ON h.maKH = kh.maKH " +
                     "WHERE p.trangThai = N'Đã hoàn tất' AND CAST(p.ngayLap AS DATE) BETWEEN ? AND ?";

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, tuNgay);
            pst.setString(2, denNgay);
            pst.setString(3, tuNgay);
            pst.setString(4, denNgay);
            ResultSet rs = pst.executeQuery();

            int stt = 1;
            while (rs.next()) {
                Object[] row = new Object[] {
                    stt++,
                    rs.getString("maHD"),
                    rs.getString("ngayLap"),
                    rs.getString("loai"),
                    rs.getString("tenKH"),
                    rs.getDouble("tienChuaVAT"),
                    rs.getDouble("thanhTien")
                };
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /** 2. Thống kê số lượng sản phẩm bán thực tế  */
    public ArrayList<Object[]> getThongKeSanPham(String tuNgay, String denNgay) {
        ArrayList<Object[]> data = new ArrayList<>();
        String sql = "SELECT s.maSP, s.tenSP, s.donViTinh, " +
                     "       COALESCE(sub_ban.slBan, 0) AS slBan, " +
                     "       COALESCE(sub_tra.slTra, 0) AS slTra, " +
                     "       (COALESCE(sub_ban.slBan, 0) - COALESCE(sub_tra.slTra, 0)) AS slThucTe " +
                     "FROM SanPham s " +
                     "LEFT JOIN (" +
                     "    SELECT ct.maSP, SUM(ct.soLuong) AS slBan " +
                     "    FROM ChiTietHoaDon_SanPham ct " +
                     "    JOIN HoaDon h ON ct.maHD = h.maHD " +
                     "    WHERE CAST(h.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "    GROUP BY ct.maSP" +
                     ") sub_ban ON s.maSP = sub_ban.maSP " +
                     "LEFT JOIN (" +
                     "    SELECT ptr.maSP, SUM(ptr.soLuongTra) AS slTra " +
                     "    FROM ChiTietPhieuTraHang ptr " +
                     "    JOIN PhieuTraHang p ON ptr.maPhieuTra = p.maPhieuTra " +
                     "    WHERE p.trangThai = N'Đã hoàn tất' AND CAST(p.ngayLap AS DATE) BETWEEN ? AND ? " +
                     "    GROUP BY ptr.maSP" +
                     ") sub_tra ON s.maSP = sub_tra.maSP";

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
        
            pst.setString(1, tuNgay);
            pst.setString(2, denNgay);
            pst.setString(3, tuNgay);
            pst.setString(4, denNgay);
            
            ResultSet rs = pst.executeQuery();

            int stt = 1;
            while (rs.next()) {
                Object[] row = new Object[] {
                    stt++,
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getString("donViTinh"),
                    rs.getInt("slBan"),
                    rs.getInt("slTra"),
                    rs.getInt("slThucTe")
                };
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<Object[]> getThongKeDichVu(String tuNgay, String denNgay) {
        ArrayList<Object[]> data = new ArrayList<>();
        String sql = "SELECT dv.maDV, dv.tenDV, dv.giaDichVu, " +
                     "       COUNT(ct.maLichHen) AS soLuot, " +
                     "       (COUNT(ct.maLichHen) * dv.giaDichVu) AS tongDoanhThuDV " +
                     "FROM DichVu dv " +
                     "JOIN ChiTietLichHen ct ON dv.maDV = ct.maDV " +
                     "JOIN LichHen lh ON ct.maLichHen = lh.maLichHen " +
                     "WHERE lh.trangThai = N'Hoàn thành' AND CAST(lh.ngayGioHen AS DATE) BETWEEN ? AND ? " +
                     "GROUP BY dv.maDV, dv.tenDV, dv.giaDichVu";

        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, tuNgay);
            pst.setString(2, denNgay);
            ResultSet rs = pst.executeQuery();

            int stt = 1;
            while (rs.next()) {
                Object[] row = new Object[] {
                    stt++,
                    rs.getString("maDV"),
                    rs.getString("tenDV"),
                    rs.getDouble("giaDichVu"), 
                    rs.getInt("soLuot"),
                    rs.getDouble("tongDoanhThuDV")
                };
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /** 4. Báo cáo tồn kho */
    public ArrayList<SanPham> getBaoCaoTonKho() {
        ArrayList<SanPham> dsSanPham = new ArrayList<>();
        String sql = "SELECT s.maSP, s.tenSP, s.donViTinh, s.giaNhap, s.tienLoi, s.soLuongTon, s.trangThai " +
                     "FROM SanPham s";

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getString("donViTinh"),
                    rs.getBigDecimal("giaNhap"),
                    rs.getBigDecimal("tienLoi"),
                    rs.getInt("soLuongTon"),
                    rs.getBoolean("trangThai")
                );
                dsSanPham.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSanPham;
    }
}