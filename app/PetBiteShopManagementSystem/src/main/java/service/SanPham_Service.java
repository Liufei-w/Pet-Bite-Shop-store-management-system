package service;

import entity.SanPham;
import java.util.ArrayList;

/**
 * Nghiệp vụ sản phẩm. Màn quản lý và hộp thoại gọi lớp này, không gọi DAO.
 * Hàm ghi trả về null khi thành công, ngược lại là câu thông báo cho người dùng.
 */
public interface SanPham_Service {

    ArrayList<SanPham> getAllSanPhamDangKinhDoanh();

    SanPham getSanPhamTheoMa(String ma);

    ArrayList<SanPham> timSanPhamTheoMaHoacTen(String keyword);

    ArrayList<SanPham> getAllSanPham();

    ArrayList<SanPham> getDanhSachSanPhamQuanLy();

    ArrayList<SanPham> locDanhSachQuanLy(String tuKhoa, String maLoai, Boolean trangThai, String kieuSapXep);

    String themSanPham(SanPham sanPham);

    String capNhatSanPham(SanPham sanPham);

    String ngungKinhDoanh(String maSP);
}
