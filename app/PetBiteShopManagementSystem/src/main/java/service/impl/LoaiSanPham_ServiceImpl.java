package service.impl;

import app.UserSession;
import connectDB.ConnectDB;
import dao.LoaiSanPham_DAO;
import entity.LoaiSanPham;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import service.LoaiSanPham_Service;

public class LoaiSanPham_ServiceImpl implements LoaiSanPham_Service {

    private static final String KHONG_CO_QUYEN =
            "Chỉ chủ cửa hàng được thêm, cập nhật hoặc đổi trạng thái loại sản phẩm.";
    private static final String KHONG_KET_NOI = "Không kết nối được cơ sở dữ liệu.";

    private final LoaiSanPham_DAO loaiSanPhamDAO = new LoaiSanPham_DAO();

    @Override
    public ArrayList<LoaiSanPham> getAllLoaiSanPham() {
        return loaiSanPhamDAO.getAllLoaiSanPham();
    }

    @Override
    public ArrayList<LoaiSanPham> getLoaiSanPhamDangHoatDong() {
        return loaiSanPhamDAO.getLoaiSanPhamDangHoatDong();
    }

    @Override
    public ArrayList<LoaiSanPham> locLoaiSanPham(String tuKhoa, Boolean trangThai, String kieuSapXep) {
        return loaiSanPhamDAO.locLoaiSanPham(tuKhoa, trangThai, kieuSapXep);
    }

    @Override
    public LoaiSanPham getLoaiSanPhamTheoMa(String maLoaiSP) {
        return loaiSanPhamDAO.getLoaiSanPhamTheoMa(maLoaiSP);
    }

    @Override
    public String themLoaiSanPham(LoaiSanPham loai) {
        if (!UserSession.getInstance().laChuCuaHang()) {
            return KHONG_CO_QUYEN;
        }
        if (loai == null) {
            return "Thiếu thông tin loại sản phẩm.";
        }
        String loiKiemTra = chuanHoa(loai);
        if (loiKiemTra != null) {
            return loiKiemTra;
        }
        return trongGiaoDich("Không lưu được loại sản phẩm.", con -> {
            if (loaiSanPhamDAO.tenLoaiDaTonTai(con, loai.getTenLoai(), null)) {
                return "Tên loại sản phẩm đã tồn tại.";
            }
            loai.setMaLoaiSanPham(loaiSanPhamDAO.sinhMaLoaiSanPham(con));
            loaiSanPhamDAO.them(con, loai);
            return null;
        });
    }

    @Override
    public String capNhatLoaiSanPham(LoaiSanPham loai) {
        if (!UserSession.getInstance().laChuCuaHang()) {
            return KHONG_CO_QUYEN;
        }
        if (loai == null || loai.getMaLoaiSanPham() == null) {
            return "Không xác định được loại sản phẩm cần cập nhật.";
        }
        String loiKiemTra = chuanHoa(loai);
        if (loiKiemTra != null) {
            return loiKiemTra;
        }
        try {
            loai.setMaLoaiSanPham(loai.getMaLoaiSanPham());
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }

        return trongGiaoDich("Không cập nhật được loại sản phẩm.", con -> {
            LoaiSanPham hienTai = loaiSanPhamDAO.getLoaiSanPhamTheoMaKhoa(con, loai.getMaLoaiSanPham());
            if (hienTai == null) {
                return "Loại sản phẩm không còn tồn tại.";
            }
            if (loaiSanPhamDAO.tenLoaiDaTonTai(con, loai.getTenLoai(), loai.getMaLoaiSanPham())) {
                return "Tên loại sản phẩm đã tồn tại.";
            }
            if (hienTai.isTrangThai() && !loai.isTrangThai()) {
                int soLienKet = loaiSanPhamDAO.demSanPhamLienKet(con, loai.getMaLoaiSanPham());
                if (soLienKet > 0) {
                    return "Loại này còn " + soLienKet
                            + " sản phẩm liên kết, kể cả sản phẩm đã ngừng kinh doanh. "
                            + "Hãy chuyển các sản phẩm sang loại khác trước khi ngừng hoạt động.";
                }
            }
            if (loaiSanPhamDAO.capNhat(con, loai) == 0) {
                return "Không cập nhật được loại sản phẩm.";
            }
            return null;
        });
    }

    @Override
    public String ngungHoatDongLoaiSanPham(String maLoaiSP) {
        LoaiSanPham loai = getLoaiSanPhamTheoMa(maLoaiSP);
        if (loai == null) {
            return "Không tìm thấy loại sản phẩm.";
        }
        loai.setTrangThai(false);
        return capNhatLoaiSanPham(loai);
    }

    private String chuanHoa(LoaiSanPham loai) {
        try {
            loai.setTenLoai(loai.getTenLoai());
            loai.setMoTa(loai.getMoTa());
            return null;
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    private String trongGiaoDich(String loiMacDinh, ThaoTacGhi viec) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            return KHONG_KET_NOI;
        }
        boolean daBatDau = false;
        try {
            con.setAutoCommit(false);
            daBatDau = true;
            String loi = viec.thucHien(con);
            if (loi != null) {
                con.rollback();
                return loi;
            }
            con.commit();
            return null;
        } catch (SQLException ex) {
            rollback(con, daBatDau);
            if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601) {
                return "Tên loại sản phẩm đã tồn tại.";
            }
            if (ex.getMessage() != null && ex.getMessage().contains("số thứ tự")) {
                return ex.getMessage();
            }
            ex.printStackTrace();
            return loiMacDinh;
        } catch (IllegalArgumentException ex) {
            rollback(con, daBatDau);
            return ex.getMessage();
        } finally {
            khoiPhucAutoCommit(con, daBatDau);
        }
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

    @FunctionalInterface
    private interface ThaoTacGhi {
        String thucHien(Connection con) throws SQLException;
    }
}
