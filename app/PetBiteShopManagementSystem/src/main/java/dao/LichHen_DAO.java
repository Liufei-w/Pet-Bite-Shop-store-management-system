package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.LichHen;
import entity.KhachHang;
import entity.ThuCung;
import entity.NhanVien;

public class LichHen_DAO {

    /** 1. Lấy toàn bộ danh sách lịch hẹn để hiển thị lên bảng */
    public ArrayList<LichHen> getAllLichHen() {
        ArrayList<LichHen> dsLichHen = new ArrayList<>();
        String sql = "SELECT lh.maLichHen, lh.ngayGioHen, lh.trangThai, lh.tongThoiGianDuKien, lh.thoiDiemHoanThanh, " +
                     "       kh.maKH, kh.hoTen AS tenKH, kh.soDienThoai, kh.gioiTinh AS gtKH, kh.diaChi, kh.trangThai AS ttKH, " +
                     "       tc.maThuCung, tc.tenThuCung, tc.loai, tc.giong, tc.gioiTinh AS gtTC, tc.ngaySinh, tc.canNang, tc.trangThai AS ttTC, " +
                     "       nv.maNV, nv.hoTen AS tenNV " +
                     "FROM LichHen lh " +
                     "JOIN KhachHang kh ON lh.maKH = kh.maKH " +
                     "JOIN ThuCung tc ON lh.maThuCung = tc.maThuCung AND lh.maKH = tc.maKH " +
                     "JOIN NhanVien nv ON lh.maNV = nv.maNV " +
                     "ORDER BY lh.ngayGioHen DESC";

        try {
            Connection con = ConnectDB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String maLH = rs.getString("maLichHen");
                LocalDateTime ngayGioHen = rs.getTimestamp("ngayGioHen").toLocalDateTime();
                String trangThai = rs.getString("trangThai");
                LocalDateTime thoiDiemHoanThanh = rs.getTimestamp("thoiDiemHoanThanh") != null 
                        ? rs.getTimestamp("thoiDiemHoanThanh").toLocalDateTime() : null;

                // Khởi tạo Khách hàng
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getString("gtKH"),
                    rs.getString("diaChi"),
                    rs.getBoolean("ttKH")
                );

                // Khởi tạo Thú cưng
                ThuCung tc = new ThuCung();
                tc.setMaThuCung(rs.getString("maThuCung"));
                tc.setTenThuCung(rs.getString("tenThuCung"));
                tc.setLoai(rs.getString("loai"));
                tc.setGiong(rs.getString("giong"));

                // Khởi tạo Lịch hẹn gắn kết thực thể
                LichHen lh = new LichHen(maLH, ngayGioHen, trangThai, thoiDiemHoanThanh, kh, tc);
                
                // (Mở rộng tùy chọn): Nếu bạn muốn lưu thêm mã nhân viên phụ trách hoặc dịch vụ vào đối tượng để hiển thị lên bảng
                // Bạn có thể lưu vào biến phụ hoặc mở rộng constructor của LichHenExt giống như panel đang dùng.

                dsLichHen.add(lh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLichHen;
    }

    /** 2. Thêm mới lịch hẹn và chi tiết lịch hẹn vào CSDL */
    public boolean createLichHen(String maLH, LocalDateTime ngayGioHen, String trangThai, 
                                 int tongTGDK, String maKH, String maThuCung, String maNV, String maDV, String ghiChu) {
        String sqlLichHen = "INSERT INTO LichHen (maLichHen, ngayGioHen, trangThai, tongThoiGianDuKien, maKH, maThuCung, maNV) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlChiTiet = "INSERT INTO ChiTietLichHen (maLichHen, maDV, ghiChuRieng, thoiGianDuKien, trangThaiThanhToan) VALUES (?, ?, ?, ?, 0)";
        
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false); // Bật giao dịch (Transaction) để đảm bảo tính toàn vẹn

            // Thêm bảng cha: LichHen
            PreparedStatement pst1 = con.prepareStatement(sqlLichHen);
            pst1.setString(1, maLH);
            pst1.setTimestamp(2, Timestamp.valueOf(ngayGioHen));
            pst1.setString(3, trangThai);
            pst1.setInt(4, tongTGDK);
            pst1.setString(5, maKH);
            pst1.setString(6, maThuCung);
            pst1.setString(7, maNV);
            pst1.executeUpdate();

            // Thêm bảng con: ChiTietLichHen
            PreparedStatement pst2 = con.prepareStatement(sqlChiTiet);
            pst2.setString(1, maLH);
            pst2.setString(2, maDV);
            pst2.setString(3, ghiChu);
            pst2.setInt(4, tongTGDK);
            pst2.executeUpdate();

            con.commit(); // Xác nhận lưu thành công
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Rollback nếu có lỗi xảy ra
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean updateTrangThai(String maLichHen, String trangThaiMoi) {
        String sql;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst;

            if (trangThaiMoi.equals("Hoàn thành")) {
                // Bắt buộc phải truyền thời điểm hoàn thành để thỏa mãn CHECK constraint trong CSDL
                sql = "UPDATE LichHen SET trangThai = ?, thoiDiemHoanThanh = SYSDATETIME() WHERE maLichHen = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, trangThaiMoi);
                pst.setString(2, maLichHen);
            } else {
                // Các trạng thái khác (Đã đặt, Đang thực hiện, Đã hủy) thì thoiDiemHoanThanh phải là NULL
                sql = "UPDATE LichHen SET trangThai = ?, thoiDiemHoanThanh = NULL WHERE maLichHen = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, trangThaiMoi);
                pst.setString(2, maLichHen);
            }

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 4. Xóa lịch hẹn */
    public boolean deleteLichHen(String maLichHen) {
        String sqlChiTiet = "DELETE FROM ChiTietLichHen WHERE maLichHen = ?";
        String sqlLichHen = "DELETE FROM LichHen WHERE maLichHen = ?";
        Connection con = null;

        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            PreparedStatement pst1 = con.prepareStatement(sqlChiTiet);
            pst1.setString(1, maLichHen);
            pst1.executeUpdate();

            PreparedStatement pst2 = con.prepareStatement(sqlLichHen);
            pst2.setString(1, maLichHen);
            pst2.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }
    /** Lấy danh sách toàn bộ khách hàng đang hoạt động từ CSDL */
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> dsKH = new ArrayList<>();
        String sql = "SELECT maKH, hoTen, soDienThoai, gioiTinh, diaChi, trangThai FROM KhachHang WHERE trangThai = 1";

        try {
            Connection con = ConnectDB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("hoTen"),
                    rs.getString("soDienThoai"),
                    rs.getString("gioiTinh"),
                    rs.getString("diaChi"),
                    rs.getBoolean("trangThai")
                );
                dsKH.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKH;
    }

    /** Lấy danh sách thú cưng dựa theo mã khách hàng */
    public ArrayList<ThuCung> getThuCungByKhachHang(String maKH) {
        ArrayList<ThuCung> dsTC = new ArrayList<>();
        String sql = "SELECT maThuCung, tenThuCung, loai, giong, gioiTinh, ngaySinh, canNang, trangThai, maKH " +
                     "FROM ThuCung WHERE maKH = ? AND trangThai = 1";

        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maKH);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ThuCung tc = new ThuCung();
                tc.setMaThuCung(rs.getString("maThuCung"));
                tc.setTenThuCung(rs.getString("tenThuCung"));
                tc.setLoai(rs.getString("loai"));
                tc.setGiong(rs.getString("giong"));
                tc.setCanNang(rs.getDouble("canNang"));
                dsTC.add(tc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsTC;
    }
}