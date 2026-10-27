-- Tạo data base

CREATE DATABASE PetBiteDB;
GO

USE PetBiteDB;
GO

-- 1. Tạo bảng NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(20) NOT NULL,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
    ngaySinh DATE NOT NULL,
    gioiTinh NVARCHAR(10) NOT NULL,
    diaChi NVARCHAR(255) NULL,
    vaiTro NVARCHAR(20) NOT NULL,
    trangThai BIT NOT NULL,
    CONSTRAINT PK_NhanVien PRIMARY KEY (maNV)
);
GO

-- 2. Tạo bảng TaiKhoan; một tài khoản tham chiếu đến một nhân viên
CREATE TABLE TaiKhoan (
    maTK VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    tenDangNhap VARCHAR(50) NOT NULL,
    maKhau VARCHAR(255) NOT NULL, -- Lưu mã băm mật khẩu, không lưu mật khẩu gốc
    trangThai BIT NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (maTK),
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
GO

-- 3. Tạo bảng KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(20) NOT NULL,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
    gioiTinh NVARCHAR(10) NULL,
    diaChi NVARCHAR(255) NULL,
    trangThai NVARCHAR(20) NOT NULL,
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKH)
);
GO

-- 4. Tạo bảng ThuCung; mỗi thú cưng tham chiếu đến một khách hàng
CREATE TABLE ThuCung (
    maThuCung VARCHAR(20) NOT NULL,
    maKH VARCHAR(20) NOT NULL,
    tenThuCung NVARCHAR(100) NOT NULL,
    loai NVARCHAR(50) NOT NULL,
    giong NVARCHAR(100) NULL,
    gioiTinh NVARCHAR(10) NOT NULL,
    ngaySinh DATE NULL,
    canNang DECIMAL(8,2) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL,
    CONSTRAINT PK_ThuCung PRIMARY KEY (maThuCung),
    CONSTRAINT FK_ThuCung_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
GO

-- 5. Tạo bảng LoaiSanPham
CREATE TABLE LoaiSanPham (
    maLoaiSP VARCHAR(20) NOT NULL,
    tenLoai NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(255) NULL,
    trangThai BIT NOT NULL,
    CONSTRAINT PK_LoaiSanPham PRIMARY KEY (maLoaiSP)
);
GO

-- 6. Tạo bảng SanPham; sản phẩm thuộc một loại sản phẩm
CREATE TABLE SanPham (
    maSP VARCHAR(20) NOT NULL,
    maLoaiSP VARCHAR(20) NOT NULL,
    tenSP NVARCHAR(150) NOT NULL,
    donViTinh NVARCHAR(30) NOT NULL,
    giaBan DECIMAL(18,2) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL,
    soLuongTon INT NOT NULL,
    CONSTRAINT PK_SanPham PRIMARY KEY (maSP),
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY (maLoaiSP) REFERENCES LoaiSanPham(maLoaiSP)
);
GO

-- 7. Tạo bảng DichVu
CREATE TABLE DichVu (
    maDV VARCHAR(20) NOT NULL,
    tenDV NVARCHAR(150) NOT NULL,
    loaiDV NVARCHAR(100) NULL,
    giaDichVu DECIMAL(18,2) NOT NULL,
    thoiGianThucHien INT NOT NULL,
    moTa NVARCHAR(500) NULL,
    trangThai NVARCHAR(20) NOT NULL,
    CONSTRAINT PK_DichVu PRIMARY KEY (maDV)
);
GO

-- 8. Tạo bảng LichHen; liên kết khách hàng, thú cưng và nhân viên
CREATE TABLE LichHen (
    maLichHen VARCHAR(20) NOT NULL,
    maKH VARCHAR(20) NOT NULL,
    maThuCung VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    ngayGioHen DATETIME2(0) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL,
    tongThoiGianDuKien INT NOT NULL,
    CONSTRAINT PK_LichHen PRIMARY KEY (maLichHen),
    CONSTRAINT FK_LichHen_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_LichHen_ThuCung FOREIGN KEY (maThuCung) REFERENCES ThuCung(maThuCung),
    CONSTRAINT FK_LichHen_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
GO

-- 9. Tạo bảng ChiTietLichHen; khóa chính ghép từ mã lịch hẹn và mã dịch vụ
CREATE TABLE ChiTietLichHen (
    maLichHen VARCHAR(20) NOT NULL,
    maDV VARCHAR(20) NOT NULL,
    ghiChuRieng NVARCHAR(500) NULL,
    thoiGianDuKien INT NOT NULL,
    trangThaiThanhToan BIT NOT NULL,
    CONSTRAINT PK_ChiTietLichHen PRIMARY KEY (maLichHen, maDV),
    CONSTRAINT FK_CTLH_LichHen FOREIGN KEY (maLichHen) REFERENCES LichHen(maLichHen),
    CONSTRAINT FK_CTLH_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV)
);
GO

-- 10. Tạo bảng LichSuChamSoc; tham chiếu lịch hẹn
CREATE TABLE LichSuChamSoc (
    maLS VARCHAR(20) NOT NULL,
    maLichHen VARCHAR(20) NOT NULL,
    canNang DECIMAL(8,2) NOT NULL,
    ghiChu NVARCHAR(1000) NULL,
    CONSTRAINT PK_LichSuChamSoc PRIMARY KEY (maLS),
    CONSTRAINT FK_LSCS_LichHen FOREIGN KEY (maLichHen) REFERENCES LichHen(maLichHen)
);
GO

-- 11. Tạo bảng HoaDon; liên kết khách hàng và nhân viên lập
CREATE TABLE HoaDon (
    maHD VARCHAR(20) NOT NULL,
    maKH VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    ngayLap DATETIME2(0) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL,
    phuongThucTT NVARCHAR(20) NOT NULL,
    tienKhachDua DECIMAL(18,2) NOT NULL,
    tienThua DECIMAL(18,2) NOT NULL,
    tongTien DECIMAL(18,2) NOT NULL,
    CONSTRAINT PK_HoaDon PRIMARY KEY (maHD),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
GO

-- 12. Tạo bảng ChiTietHoaDonSanPham; khóa chính ghép từ mã hóa đơn và mã sản phẩm
CREATE TABLE ChiTietHoaDonSanPham (
    maHD VARCHAR(20) NOT NULL,
    maSP VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    thanhTien DECIMAL(18,2) NOT NULL,
    CONSTRAINT PK_CTHDSP PRIMARY KEY (maHD, maSP),
    CONSTRAINT FK_CTHDSP_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHDSP_SanPham FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);
GO

-- 13. Tạo bảng ChiTietHoaDonDichVu; liên kết dịch vụ thuộc lịch hẹn và hóa đơn
CREATE TABLE ChiTietHoaDonDichVu (
    maHD VARCHAR(20) NOT NULL,
    maLichHen VARCHAR(20) NOT NULL,
    maDV VARCHAR(20) NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    thanhTien DECIMAL(18,2) NOT NULL,
    CONSTRAINT PK_CTHDDV PRIMARY KEY (maHD, maLichHen, maDV),
    CONSTRAINT FK_CTHDDV_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHDDV_ChiTietLichHen FOREIGN KEY (maLichHen, maDV)
        REFERENCES ChiTietLichHen(maLichHen, maDV)
);
GO

-- 14. Tạo bảng PhieuTraHang; tham chiếu hóa đơn, người lập, người duyệt (nếu có)
CREATE TABLE PhieuTraHang (
    maPhieuTra VARCHAR(20) NOT NULL,
    maHD VARCHAR(20) NOT NULL,
    maNVLap VARCHAR(20) NOT NULL,
    maNVDuyet VARCHAR(20) NULL,
    ngayLap DATE NOT NULL,
    trangThai NVARCHAR(30) NOT NULL,
    tongTienDuKien DECIMAL(18,2) NOT NULL,
    soTienDuocDuyet DECIMAL(18,2) NULL,
    soTienThucTra DECIMAL(18,2) NULL,
    lyDoTuChoi NVARCHAR(500) NULL,
    thoiDiemHoanTat DATETIME2(0) NULL,
    lyDoGiamHoan NVARCHAR(500) NULL,
    CONSTRAINT PK_PhieuTraHang PRIMARY KEY (maPhieuTra),
    CONSTRAINT FK_PTH_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_PTH_NguoiLap FOREIGN KEY (maNVLap) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_PTH_NguoiDuyet FOREIGN KEY (maNVDuyet) REFERENCES NhanVien(maNV)
);
GO

-- 15. Tạo bảng ChiTietPhieuTraHang; mỗi dòng tham chiếu phiếu trả và sản phẩm đã mua
CREATE TABLE ChiTietPhieuTraHang (
    maPhieuTra VARCHAR(20) NOT NULL,
    maHD VARCHAR(20) NOT NULL,
    maSP VARCHAR(20) NOT NULL,
    soLuongTra INT NOT NULL,
    tinhTrang NVARCHAR(20) NOT NULL,
    lyDoTra NVARCHAR(500) NULL,
    donGia DECIMAL(18,2) NOT NULL,
    CONSTRAINT PK_ChiTietPhieuTraHang PRIMARY KEY (maPhieuTra, maSP),
    CONSTRAINT FK_CTPTH_PhieuTra FOREIGN KEY (maPhieuTra) REFERENCES PhieuTraHang(maPhieuTra),
    CONSTRAINT FK_CTPTH_CTHDSP FOREIGN KEY (maHD, maSP)
        REFERENCES ChiTietHoaDonSanPham(maHD, maSP)
);
GO