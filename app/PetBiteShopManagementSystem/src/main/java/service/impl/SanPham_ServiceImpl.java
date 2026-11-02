package service.impl;

import app.UserSession;
import connectDB.ConnectDB;
import dao.LoaiSanPham_DAO;
import dao.SanPham_DAO;
import entity.LoaiSanPham;
import entity.SanPham;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import service.SanPham_Service;

public class SanPham_ServiceImpl implements SanPham_Service {

    private static final String KHONG_CO_QUYEN =
            "Chỉ chủ cửa hàng được thêm, cập nhật hoặc đổi trạng thái sản phẩm.";
    private static final String KHONG_KET_NOI = "Không kết nối được cơ sở dữ liệu.";

    private final SanPham_DAO sanPhamDAO = new SanPham_DAO();
    private final LoaiSanPham_DAO loaiSanPhamDAO = new LoaiSanPham_DAO();

    @Override
    public ArrayList<SanPham> getAllSanPhamDangKinhDoanh() {
        return sanPhamDAO.getAllSanPhamDangKinhDoanh();
    }

    @Override
    public SanPham getSanPhamTheoMa(String ma) {
        return sanPhamDAO.getSanPhamTheoMa(ma);
    }

    @Override
    public ArrayList<SanPham> timSanPhamTheoMaHoacTen(String keyword) {
        return sanPhamDAO.timSanPhamTheoMaHoacTen(keyword);
    }

    @Override
    public ArrayList<SanPham> getAllSanPham() {
        return sanPhamDAO.getAllSanPham();
    }

    @Override
    public ArrayList<SanPham> getDanhSachSanPhamQuanLy() {
        return sanPhamDAO.getDanhSachSanPhamQuanLy();
    }

    @Override
    public ArrayList<SanPham> locDanhSachQuanLy(String tuKhoa, String maLoai, Boolean trangThai, String kieuSapXep) {
        return sanPhamDAO.locDanhSachQuanLy(tuKhoa, maLoai, trangThai, kieuSapXep);
    }

    @Override
    public String themSanPham(SanPham sanPham) {
        if (!UserSession.getInstance().laChuCuaHang()) {
            return KHONG_CO_QUYEN;
        }
        String loiKiemTra = chuanHoa(sanPham, true);
        if (loiKiemTra != null) {
            return loiKiemTra;
        }
        return trongGiaoDich("Không lưu được sản phẩm.", con -> {
            if (!loaiDangHoatDong(con, sanPham.getLoaiSanPham().getMaLoaiSanPham())) {
                return "Chỉ được thêm sản phẩm vào loại đang hoạt động.";
            }
            sanPham.setMaSP(sanPhamDAO.sinhMa(con));
            sanPhamDAO.them(con, sanPham);
            return null;
        });
    }

    @Override
    public String capNhatSanPham(SanPham sanPham) {
        if (!UserSession.getInstance().laChuCuaHang()) {
            return KHONG_CO_QUYEN;
        }
        String loiKiemTra = chuanHoa(sanPham, false);
        if (loiKiemTra != null) {
            return loiKiemTra;
        }
        return trongGiaoDich("Không cập nhật được sản phẩm.", con -> {
            SanPham hienTai = sanPhamDAO.getSanPhamTheoMaKhoa(con, sanPham.getMaSP());
            if (hienTai == null) {
                return "Sản phẩm không còn tồn tại.";
            }
            String maLoaiMoi = sanPham.getLoaiSanPham().getMaLoaiSanPham();
            boolean doiLoai = hienTai.getLoaiSanPham() == null
                    || !maLoaiMoi.equals(hienTai.getLoaiSanPham().getMaLoaiSanPham());
            boolean loaiMoiDangHoatDong = loaiDangHoatDong(con, maLoaiMoi);
            if (doiLoai && !loaiMoiDangHoatDong) {
                return "Không được chuyển sản phẩm sang loại đang ngừng hoạt động.";
            }
            if (sanPham.isTrangThai() && !loaiMoiDangHoatDong) {
                return "Không thể chuyển sang đang kinh doanh khi loại sản phẩm đã ngừng hoạt động.";
            }
            if (sanPhamDAO.capNhat(con, sanPham) == 0) {
                return "Không cập nhật được sản phẩm.";
            }
            return null;
        });
    }

    @Override
    public String ngungKinhDoanh(String maSP) {
        SanPham sanPham = getSanPhamTheoMa(maSP);
        if (sanPham == null) {
            return "Không tìm thấy sản phẩm.";
        }
        sanPham.setTrangThai(false);
        return capNhatSanPham(sanPham);
    }

    private String chuanHoa(SanPham sanPham, boolean taoMoi) {
        if (sanPham == null) {
            return "Thiếu thông tin sản phẩm.";
        }
        try {
            sanPham.setTenSP(sanPham.getTenSP());
            sanPham.setDonViTinh(sanPham.getDonViTinh());
            sanPham.setGiaNhap(sanPham.getGiaNhap());
            sanPham.setTienLoi(sanPham.getTienLoi());
            sanPham.setSoLuongTon(sanPham.getSoLuongTon());
            if (!taoMoi) {
                sanPham.setMaSP(sanPham.getMaSP());
            }
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
        if (sanPham.getLoaiSanPham() == null || sanPham.getLoaiSanPham().getMaLoaiSanPham() == null) {
            return "Phải chọn loại sản phẩm.";
        }
        return null;
    }

    private boolean loaiDangHoatDong(Connection con, String maLoaiSP) throws SQLException {
        LoaiSanPham loai = loaiSanPhamDAO.getLoaiSanPhamTheoMa(con, maLoaiSP);
        return loai != null && loai.isTrangThai();
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
