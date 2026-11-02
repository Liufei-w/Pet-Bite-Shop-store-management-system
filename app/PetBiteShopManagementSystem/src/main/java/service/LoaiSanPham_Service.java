package service;

import entity.LoaiSanPham;
import java.util.ArrayList;

/**
 * Nghiệp vụ loại sản phẩm. Giao diện gọi lớp này, không gọi DAO.
 * Hàm ghi trả về null khi thành công, ngược lại là câu thông báo cho người dùng.
 */
public interface LoaiSanPham_Service {

    ArrayList<LoaiSanPham> getAllLoaiSanPham();

    ArrayList<LoaiSanPham> getLoaiSanPhamDangHoatDong();

    ArrayList<LoaiSanPham> locLoaiSanPham(String tuKhoa, Boolean trangThai, String kieuSapXep);

    LoaiSanPham getLoaiSanPhamTheoMa(String maLoaiSP);

    String themLoaiSanPham(LoaiSanPham loai);

    String capNhatLoaiSanPham(LoaiSanPham loai);

    String ngungHoatDongLoaiSanPham(String maLoaiSP);
}
