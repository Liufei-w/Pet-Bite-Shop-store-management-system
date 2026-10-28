/* ======================================================================
    TAO CO SO DU LIEU CHO SQL SERVER

   ====================================================================== */

SET NOCOUNT ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_NULLS ON;
GO

IF DB_ID(N'PetBite') IS NULL
    CREATE DATABASE PetBite COLLATE Vietnamese_CI_AS;
GO

USE PetBite;
GO

/* ======================================================================
   PHAN 1: XOA BANG CU (thu tu nguoc: bang con truoc, bang cha sau)
   ====================================================================== */
DROP TABLE IF EXISTS ChiTietPhieuTraHang;
DROP TABLE IF EXISTS PhieuTraHang;
DROP TABLE IF EXISTS ChiTietHoaDon_DichVu;
DROP TABLE IF EXISTS ChiTietHoaDon_SanPham;
DROP TABLE IF EXISTS HoaDon;
DROP TABLE IF EXISTS LichSuChamSoc;
DROP TABLE IF EXISTS ChiTietLichHen;
DROP TABLE IF EXISTS LichHen;
DROP TABLE IF EXISTS ThuCung;
DROP TABLE IF EXISTS SanPham;
DROP TABLE IF EXISTS TaiKhoan;
DROP TABLE IF EXISTS NhanVien;
DROP TABLE IF EXISTS DichVu;
DROP TABLE IF EXISTS KhachHang;
DROP TABLE IF EXISTS LoaiSanPham;
GO

/* ======================================================================
   PHAN 2: TAO BANG (bang doc lap truoc, bang co khoa ngoai sau)
   Ma tu sinh: tien to + YYMMDD + XXX  => tien to + 9 chu so
   ====================================================================== */

/* ---------- 1. LoaiSanPham ---------- */
CREATE TABLE LoaiSanPham (
    maLoaiSP   VARCHAR(15)   NOT NULL,
    tenLoaiSP  NVARCHAR(50)  NOT NULL,
    moTa       NVARCHAR(255) NULL,
    trangThai  BIT           NOT NULL CONSTRAINT DF_LoaiSanPham_trangThai DEFAULT (1),
    CONSTRAINT PK_LoaiSanPham PRIMARY KEY (maLoaiSP),
    CONSTRAINT UQ_LoaiSanPham_tenLoaiSP UNIQUE (tenLoaiSP),
    CONSTRAINT CK_LoaiSanPham_maLoaiSP CHECK (maLoaiSP LIKE 'LSP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_LoaiSanPham_tenLoaiSP CHECK (LEN(LTRIM(RTRIM(tenLoaiSP))) > 0)
);
GO

/* ---------- 2. KhachHang ---------- */
CREATE TABLE KhachHang (
    maKH         VARCHAR(15)   NOT NULL,
    hoTen        NVARCHAR(50)  NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL,
    gioiTinh     NVARCHAR(10)  NULL,
    diaChi       NVARCHAR(200) NULL,
    trangThai    BIT           NOT NULL CONSTRAINT DF_KhachHang_trangThai DEFAULT (1),
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKH),
    CONSTRAINT UQ_KhachHang_soDienThoai UNIQUE (soDienThoai),
    CONSTRAINT CK_KhachHang_maKH CHECK (maKH LIKE 'KH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_KhachHang_hoTen CHECK (LEN(LTRIM(RTRIM(hoTen))) > 0),
    CONSTRAINT CK_KhachHang_soDienThoai CHECK (soDienThoai LIKE '0[35789][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_KhachHang_gioiTinh CHECK (gioiTinh IS NULL OR gioiTinh IN (N'Nam', N'Nữ', N'Khác'))
);
GO

/* ---------- 3. DichVu ---------- */
CREATE TABLE DichVu (
    maDV              VARCHAR(15)    NOT NULL,
    tenDV             NVARCHAR(100)  NOT NULL,
    giaDichVu         DECIMAL(12,2)  NOT NULL,
    thoiGianThucHien  INT            NOT NULL,   -- don vi: phut
    moTa              NVARCHAR(255)  NULL,
    trangThai         BIT            NOT NULL CONSTRAINT DF_DichVu_trangThai DEFAULT (1),
    CONSTRAINT PK_DichVu PRIMARY KEY (maDV),
    CONSTRAINT CK_DichVu_maDV CHECK (maDV LIKE 'DV[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_DichVu_tenDV CHECK (LEN(LTRIM(RTRIM(tenDV))) > 0),
    CONSTRAINT CK_DichVu_giaDichVu CHECK (giaDichVu > 0),
    CONSTRAINT CK_DichVu_thoiGianThucHien CHECK (thoiGianThucHien > 0)
);
GO

/* ---------- 4. NhanVien ---------- */
CREATE TABLE NhanVien (
    maNV         VARCHAR(15)   NOT NULL,
    hoTen        NVARCHAR(50)  NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL,
    gioiTinh     NVARCHAR(10)  NOT NULL,
    ngaySinh     DATE          NOT NULL,
    diaChi       NVARCHAR(200) NOT NULL,
    vaiTro       NVARCHAR(20)  NOT NULL,
    trangThai    BIT           NOT NULL CONSTRAINT DF_NhanVien_trangThai DEFAULT (1),
    CONSTRAINT PK_NhanVien PRIMARY KEY (maNV),
    CONSTRAINT UQ_NhanVien_soDienThoai UNIQUE (soDienThoai),
    CONSTRAINT CK_NhanVien_maNV CHECK (maNV LIKE 'NV[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_NhanVien_hoTen CHECK (LEN(LTRIM(RTRIM(hoTen))) > 0),
    CONSTRAINT CK_NhanVien_soDienThoai CHECK (soDienThoai LIKE '0[35789][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_NhanVien_gioiTinh CHECK (gioiTinh IN (N'Nam', N'Nữ', N'Khác')),
    -- du 18 tuoi tro len (so sanh bang ngay, khong dung DATEDIFF(YEAR) vi bi lech)
    CONSTRAINT CK_NhanVien_ngaySinh CHECK (ngaySinh <= DATEADD(YEAR, -18, CAST(GETDATE() AS DATE))),
    CONSTRAINT CK_NhanVien_diaChi CHECK (LEN(LTRIM(RTRIM(diaChi))) > 0),
    CONSTRAINT CK_NhanVien_vaiTro CHECK (vaiTro IN (N'Thu ngân', N'Chăm sóc', N'Chủ cửa hàng'))
);
GO

-- (Tuy chon) Chi cho phep DUNG MOT Chu cua hang. Bo comment neu bao cao co quy dinh nay.
-- CREATE UNIQUE INDEX UX_NhanVien_ChuCuaHang ON NhanVien(vaiTro) WHERE vaiTro = N'Chủ cửa hàng';
-- GO

/* ---------- 5. TaiKhoan (moi tai khoan thuoc dung 1 nhan vien; moi nhan vien toi da 1 tai khoan) ---------- */
CREATE TABLE TaiKhoan (
    maTK         VARCHAR(15)   NOT NULL,
    tenDangNhap  VARCHAR(50)   NOT NULL,
    matKhau      VARCHAR(255)  NOT NULL,   -- luu CHUOI BAM (hash), khong luu mat khau goc
    trangThai    BIT           NOT NULL CONSTRAINT DF_TaiKhoan_trangThai DEFAULT (1),  -- 1: hoat dong, 0: bi khoa
    maNV         VARCHAR(15)   NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (maTK),
    CONSTRAINT UQ_TaiKhoan_tenDangNhap UNIQUE (tenDangNhap),
    CONSTRAINT UQ_TaiKhoan_maNV UNIQUE (maNV),
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT CK_TaiKhoan_maTK CHECK (maTK LIKE 'TK[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_TaiKhoan_tenDangNhap CHECK (LEN(LTRIM(RTRIM(tenDangNhap))) > 0),
    CONSTRAINT CK_TaiKhoan_matKhau CHECK (LEN(matKhau) > 0)
);
GO

/* ---------- 6. SanPham ---------- */
CREATE TABLE SanPham (
    maSP         VARCHAR(15)    NOT NULL,
    tenSP        NVARCHAR(100)  NOT NULL,
    donViTinh    NVARCHAR(20)   NOT NULL,
    giaBan       DECIMAL(12,2)  NOT NULL,
    soLuongTon   INT            NOT NULL CONSTRAINT DF_SanPham_soLuongTon DEFAULT (0),
    trangThai    BIT            NOT NULL CONSTRAINT DF_SanPham_trangThai DEFAULT (1),   -- 1: dang kinh doanh
    maLoaiSP     VARCHAR(15)    NOT NULL,
    CONSTRAINT PK_SanPham PRIMARY KEY (maSP),
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY (maLoaiSP) REFERENCES LoaiSanPham(maLoaiSP),
    CONSTRAINT CK_SanPham_maSP CHECK (maSP LIKE 'SP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_SanPham_tenSP CHECK (LEN(LTRIM(RTRIM(tenSP))) > 0),
    CONSTRAINT CK_SanPham_donViTinh CHECK (LEN(LTRIM(RTRIM(donViTinh))) > 0),
    CONSTRAINT CK_SanPham_giaBan CHECK (giaBan > 0),
    CONSTRAINT CK_SanPham_soLuongTon CHECK (soLuongTon >= 0)
);
GO

/* ---------- 7. ThuCung ---------- */
CREATE TABLE ThuCung (
    maThuCung   VARCHAR(15)    NOT NULL,
    tenThuCung  NVARCHAR(50)   NOT NULL,
    loai        NVARCHAR(50)   NOT NULL,
    giong       NVARCHAR(50)   NULL,
    gioiTinh    NVARCHAR(10)   NULL,
    ngaySinh    DATE           NULL,
    canNang     DECIMAL(5,2)   NULL,          -- Kg
    trangThai   BIT            NOT NULL CONSTRAINT DF_ThuCung_trangThai DEFAULT (1),   -- 1: dang quan ly
    maKH        VARCHAR(15)    NOT NULL,
    CONSTRAINT PK_ThuCung PRIMARY KEY (maThuCung),
    -- rang buoc phu de LichHen tham chieu (maThuCung, maKH) => thu cung phai dung la cua khach hang do
    CONSTRAINT UQ_ThuCung_maThuCung_maKH UNIQUE (maThuCung, maKH),
    CONSTRAINT FK_ThuCung_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT CK_ThuCung_maThuCung CHECK (maThuCung LIKE 'TC[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_ThuCung_tenThuCung CHECK (LEN(LTRIM(RTRIM(tenThuCung))) > 0),
    CONSTRAINT CK_ThuCung_loai CHECK (LEN(LTRIM(RTRIM(loai))) > 0),
    CONSTRAINT CK_ThuCung_gioiTinh CHECK (gioiTinh IS NULL OR gioiTinh IN (N'Đực', N'Cái', N'Khác')),
    CONSTRAINT CK_ThuCung_ngaySinh CHECK (ngaySinh IS NULL OR ngaySinh <= CAST(GETDATE() AS DATE)),
    CONSTRAINT CK_ThuCung_canNang CHECK (canNang IS NULL OR canNang > 0)
);
GO

/* ---------- 8. LichHen ---------- */
-- Luu y: KHONG dat CHECK "ngayGioHen >= hien tai" vi se lam UPDATE trang thai cac lich cu bi loi.
--        Quy tac nay kiem tra o tang ung dung khi TAO MOI lich hen.
CREATE TABLE LichHen (
    maLichHen           VARCHAR(15)   NOT NULL,
    ngayGioHen          DATETIME2(0)  NOT NULL,
    trangThai           NVARCHAR(20)  NOT NULL CONSTRAINT DF_LichHen_trangThai DEFAULT (N'Đã đặt'),
    tongThoiGianDuKien  INT           NOT NULL,   -- phut, tong thoi gian du kien cac dich vu
    thoiDiemHoanThanh   DATETIME2(0)  NULL,
    maKH                VARCHAR(15)   NOT NULL,
    maThuCung           VARCHAR(15)   NOT NULL,
    maNV                VARCHAR(15)   NOT NULL,   -- nhan vien cham soc phu trach
    CONSTRAINT PK_LichHen PRIMARY KEY (maLichHen),
    -- bao gom ca khoa ngoai toi KhachHang(maKH) thong qua khoa ghep nay
    CONSTRAINT FK_LichHen_ThuCung_KhachHang FOREIGN KEY (maThuCung, maKH) REFERENCES ThuCung(maThuCung, maKH),
    CONSTRAINT FK_LichHen_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT CK_LichHen_maLichHen CHECK (maLichHen LIKE 'LH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_LichHen_trangThai CHECK (trangThai IN (N'Đã đặt', N'Đang thực hiện', N'Hoàn thành', N'Đã hủy')),
    CONSTRAINT CK_LichHen_tongThoiGian CHECK (tongThoiGianDuKien > 0),
    -- thoiDiemHoanThanh chi co khi lich hen o trang thai Hoan thanh
    CONSTRAINT CK_LichHen_hoanThanh CHECK (
        (trangThai = N'Hoàn thành' AND thoiDiemHoanThanh IS NOT NULL)
        OR (trangThai <> N'Hoàn thành' AND thoiDiemHoanThanh IS NULL)
    )
);
GO

/* ---------- 9. ChiTietLichHen (khoa ghep: maLichHen + maDV) ---------- */
CREATE TABLE ChiTietLichHen (
    maLichHen           VARCHAR(15)   NOT NULL,
    maDV                VARCHAR(15)   NOT NULL,
    ghiChuRieng         NVARCHAR(255) NULL,
    thoiGianDuKien      INT           NOT NULL,   -- phut, ghi nhan luc dang ky
    trangThaiThanhToan  BIT           NOT NULL CONSTRAINT DF_ChiTietLichHen_ttt DEFAULT (0),   -- 0: chua thanh toan
    CONSTRAINT PK_ChiTietLichHen PRIMARY KEY (maLichHen, maDV),
    CONSTRAINT FK_ChiTietLichHen_LichHen FOREIGN KEY (maLichHen) REFERENCES LichHen(maLichHen),
    CONSTRAINT FK_ChiTietLichHen_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV),
    CONSTRAINT CK_ChiTietLichHen_thoiGian CHECK (thoiGianDuKien > 0)
);
GO

/* ---------- 10. LichSuChamSoc (1-1 voi lich hen Hoan thanh) ---------- */
CREATE TABLE LichSuChamSoc (
    maLS       VARCHAR(15)    NOT NULL,
    canNang    DECIMAL(5,2)   NOT NULL,   -- Kg tai thoi diem cham soc
    ghiChu     NVARCHAR(255)  NULL,
    maLichHen  VARCHAR(15)    NOT NULL,
    CONSTRAINT PK_LichSuChamSoc PRIMARY KEY (maLS),
    CONSTRAINT UQ_LichSuChamSoc_maLichHen UNIQUE (maLichHen),
    CONSTRAINT FK_LichSuChamSoc_LichHen FOREIGN KEY (maLichHen) REFERENCES LichHen(maLichHen),
    CONSTRAINT CK_LichSuChamSoc_maLS CHECK (maLS LIKE 'LS[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_LichSuChamSoc_canNang CHECK (canNang > 0)
);
GO

/* ---------- 11. HoaDon ---------- */
-- Hoa don chi duoc luu SAU KHI thanh toan thanh cong => ngayLap chinh la thoi diem thanh toan.
CREATE TABLE HoaDon (
    maHD          VARCHAR(15)    NOT NULL,
    ngayLap       DATETIME2(0)   NOT NULL,
    trangThai     NVARCHAR(20)   NOT NULL CONSTRAINT DF_HoaDon_trangThai DEFAULT (N'Đã thanh toán'),
    phuongThucTT  NVARCHAR(20)   NOT NULL,
    tienKhachDua  DECIMAL(12,2)  NULL,    -- chi bat buoc voi tien mat
    tongTien      DECIMAL(12,2)  NOT NULL,
    tienThua      DECIMAL(12,2)  NULL,    -- tien mat: tienKhachDua - tongTien
    maKH          VARCHAR(15)    NOT NULL,
    maNV          VARCHAR(15)    NOT NULL,   -- thu ngan lap hoa don
    CONSTRAINT PK_HoaDon PRIMARY KEY (maHD),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT CK_HoaDon_maHD CHECK (maHD LIKE 'HD[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_HoaDon_ngayLap CHECK (ngayLap <= DATEADD(MINUTE, 1, SYSDATETIME())),   -- du 1 phut cho lech gio / lam tron giay
    CONSTRAINT CK_HoaDon_trangThai CHECK (trangThai = N'Đã thanh toán'),
    CONSTRAINT CK_HoaDon_phuongThucTT CHECK (phuongThucTT IN (N'Tiền mặt', N'Chuyển khoản')),
    CONSTRAINT CK_HoaDon_tongTien CHECK (tongTien > 0),
    CONSTRAINT CK_HoaDon_tienKhachDua CHECK (tienKhachDua IS NULL OR tienKhachDua >= 0),
    CONSTRAINT CK_HoaDon_tienThua CHECK (tienThua IS NULL OR tienThua >= 0),
    CONSTRAINT CK_HoaDon_thanhToan CHECK (
        (phuongThucTT = N'Tiền mặt'
            AND tienKhachDua IS NOT NULL AND tienKhachDua >= tongTien
            AND tienThua IS NOT NULL AND tienThua = tienKhachDua - tongTien)
        OR
        (phuongThucTT = N'Chuyển khoản'
            AND tienKhachDua IS NULL AND (tienThua IS NULL OR tienThua = 0))
    )
);
GO

/* ---------- 12. ChiTietHoaDon_SanPham (khoa ghep: maHD + maSP) ---------- */
CREATE TABLE ChiTietHoaDon_SanPham (
    maHD      VARCHAR(15)    NOT NULL,
    maSP      VARCHAR(15)    NOT NULL,
    soLuong   INT            NOT NULL,
    donGia    DECIMAL(12,2)  NOT NULL,   -- don gia tai thoi diem lap hoa don
    thanhTien AS (CAST(soLuong * donGia AS DECIMAL(12,2))) PERSISTED,   -- cot tinh toan: KHONG INSERT
    CONSTRAINT PK_ChiTietHoaDon_SanPham PRIMARY KEY (maHD, maSP),
    CONSTRAINT FK_CTHDSP_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHDSP_SanPham FOREIGN KEY (maSP) REFERENCES SanPham(maSP),
    CONSTRAINT CK_CTHDSP_soLuong CHECK (soLuong > 0),
    CONSTRAINT CK_CTHDSP_donGia CHECK (donGia > 0)
);
GO

/* ---------- 13. ChiTietHoaDon_DichVu ----------
   Mot dong = viec thanh toan MOT dich vu cua MOT lich hen (tham chieu ChiTietLichHen).
   Dich vu duoc xac dinh qua ChiTietLichHen, nen khong co khoa ngoai truc tiep toi DichVu.
   UNIQUE (maLichHen, maDV): mot dich vu cua lich hen chi thanh toan mot lan. */
CREATE TABLE ChiTietHoaDon_DichVu (
    maHD       VARCHAR(15)    NOT NULL,
    maLichHen  VARCHAR(15)    NOT NULL,
    maDV       VARCHAR(15)    NOT NULL,
    donGia     DECIMAL(12,2)  NOT NULL,   -- don gia dich vu tai thoi diem thanh toan
    thanhTien  AS (donGia) PERSISTED,     -- moi dich vu tinh mot lan => thanhTien = donGia. KHONG INSERT
    CONSTRAINT PK_ChiTietHoaDon_DichVu PRIMARY KEY (maHD, maLichHen, maDV),
    CONSTRAINT UQ_CTHDDV_maLichHen_maDV UNIQUE (maLichHen, maDV),
    CONSTRAINT FK_CTHDDV_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHDDV_ChiTietLichHen FOREIGN KEY (maLichHen, maDV) REFERENCES ChiTietLichHen(maLichHen, maDV),
    CONSTRAINT CK_CTHDDV_donGia CHECK (donGia > 0)
);
GO

/* ---------- 14. PhieuTraHang ---------- */
CREATE TABLE PhieuTraHang (
    maPhieuTra       VARCHAR(15)    NOT NULL,
    ngayLap          DATETIME2(0)   NOT NULL,
    trangThai        NVARCHAR(20)   NOT NULL CONSTRAINT DF_PhieuTraHang_trangThai DEFAULT (N'Chờ xác nhận'),
    tongTienDuKien   DECIMAL(12,2)  NOT NULL,
    soTienDuocDuyet  DECIMAL(12,2)  NULL,    -- Chu cua hang phe duyet
    soTienThucTra    DECIMAL(12,2)  NULL,    -- co khi phieu Da hoan tat
    lyDoTuChoi       NVARCHAR(255)  NULL,
    thoiDiemHoanTat  DATETIME2(0)   NULL,
    lyDoGiamHoan     NVARCHAR(255)  NULL,
    maHD             VARCHAR(15)    NOT NULL,
    nguoiLap         VARCHAR(15)    NOT NULL,   -- maNV cua thu ngan lap phieu
    nguoiDuyet       VARCHAR(15)    NULL,       -- maNV cua chu cua hang, trong khi phieu Cho xac nhan
    CONSTRAINT PK_PhieuTraHang PRIMARY KEY (maPhieuTra),
    -- rang buoc phu de ChiTietPhieuTraHang tham chieu (maPhieuTra, maHD)
    CONSTRAINT UQ_PhieuTraHang_maPhieuTra_maHD UNIQUE (maPhieuTra, maHD),
    CONSTRAINT FK_PhieuTraHang_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_PhieuTraHang_NguoiLap FOREIGN KEY (nguoiLap) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_PhieuTraHang_NguoiDuyet FOREIGN KEY (nguoiDuyet) REFERENCES NhanVien(maNV),
    CONSTRAINT CK_PhieuTraHang_maPhieuTra CHECK (maPhieuTra LIKE 'PT[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT CK_PhieuTraHang_ngayLap CHECK (ngayLap <= DATEADD(MINUTE, 1, SYSDATETIME())),   -- du 1 phut cho lech gio / lam tron giay
    CONSTRAINT CK_PhieuTraHang_trangThai CHECK (trangThai IN (N'Chờ xác nhận', N'Đã duyệt', N'Đã từ chối', N'Đã hoàn tất')),
    CONSTRAINT CK_PhieuTraHang_tien CHECK (
        tongTienDuKien >= 0
        AND (soTienDuocDuyet IS NULL OR soTienDuocDuyet >= 0)
        AND (soTienThucTra IS NULL OR soTienThucTra >= 0)
    ),
    CONSTRAINT CK_PhieuTraHang_duyetKhongVuot CHECK (soTienDuocDuyet IS NULL OR soTienDuocDuyet <= tongTienDuKien),
    CONSTRAINT CK_PhieuTraHang_tuChoi CHECK (
        trangThai <> N'Đã từ chối'
        OR (lyDoTuChoi IS NOT NULL AND LEN(LTRIM(RTRIM(lyDoTuChoi))) > 0)
    ),
    CONSTRAINT CK_PhieuTraHang_daXuLy CHECK (
        (trangThai NOT IN (N'Đã duyệt', N'Đã hoàn tất') OR (soTienDuocDuyet IS NOT NULL AND nguoiDuyet IS NOT NULL))
        AND (trangThai <> N'Đã từ chối' OR nguoiDuyet IS NOT NULL)
    ),
    CONSTRAINT CK_PhieuTraHang_hoanTat CHECK (
        (trangThai = N'Đã hoàn tất' AND thoiDiemHoanTat IS NOT NULL AND soTienThucTra IS NOT NULL)
        OR (trangThai <> N'Đã hoàn tất' AND thoiDiemHoanTat IS NULL AND soTienThucTra IS NULL)
    ),
    -- duyet it hon du kien thi bat buoc ghi ly do giam hoan
    CONSTRAINT CK_PhieuTraHang_giamHoan CHECK (
        soTienDuocDuyet IS NULL
        OR soTienDuocDuyet >= tongTienDuKien
        OR (lyDoGiamHoan IS NOT NULL AND LEN(LTRIM(RTRIM(lyDoGiamHoan))) > 0)
    )
);
GO

/* ---------- 15. ChiTietPhieuTraHang ----------
   Khoa chinh gom ca tinhTrang: cung mot san pham co the co dong "Nguyen ven" va dong "Loi".
   Co them cot maHD de khoa ngoai ghep (maHD, maSP) bat buoc san pham tra PHAI thuoc hoa don goc. */
CREATE TABLE ChiTietPhieuTraHang (
    maPhieuTra  VARCHAR(15)    NOT NULL,
    maHD        VARCHAR(15)    NOT NULL,
    maSP        VARCHAR(15)    NOT NULL,
    tinhTrang   NVARCHAR(20)   NOT NULL,
    soLuongTra  INT            NOT NULL,
    lyDoTra     NVARCHAR(255)  NOT NULL,
    donGia      DECIMAL(12,2)  NOT NULL,   -- don gia thuc tra tren hoa don goc
    CONSTRAINT PK_ChiTietPhieuTraHang PRIMARY KEY (maPhieuTra, maSP, tinhTrang),
    CONSTRAINT FK_CTPTH_PhieuTraHang FOREIGN KEY (maPhieuTra, maHD) REFERENCES PhieuTraHang(maPhieuTra, maHD),
    CONSTRAINT FK_CTPTH_ChiTietHoaDon_SanPham FOREIGN KEY (maHD, maSP) REFERENCES ChiTietHoaDon_SanPham(maHD, maSP),
    CONSTRAINT CK_CTPTH_tinhTrang CHECK (tinhTrang IN (N'Nguyên vẹn', N'Lỗi')),
    CONSTRAINT CK_CTPTH_soLuongTra CHECK (soLuongTra > 0),
    CONSTRAINT CK_CTPTH_lyDoTra CHECK (LEN(LTRIM(RTRIM(lyDoTra))) > 0),
    CONSTRAINT CK_CTPTH_donGia CHECK (donGia > 0)
);
GO

/* ======================================================================
   PHAN 3: CHI MUC (SQL Server khong tu tao chi muc cho khoa ngoai)
   ====================================================================== */
CREATE INDEX IX_SanPham_maLoaiSP          ON SanPham(maLoaiSP);
CREATE INDEX IX_ThuCung_maKH              ON ThuCung(maKH);
CREATE INDEX IX_LichHen_maNV_ngayGioHen   ON LichHen(maNV, ngayGioHen);              -- kiem tra trung gio
CREATE INDEX IX_LichHen_maKH              ON LichHen(maKH);
CREATE INDEX IX_LichHen_thoiDiemHoanThanh ON LichHen(thoiDiemHoanThanh) WHERE thoiDiemHoanThanh IS NOT NULL;  -- thong ke luot dich vu
CREATE INDEX IX_ChiTietLichHen_maDV       ON ChiTietLichHen(maDV);
CREATE INDEX IX_HoaDon_ngayLap            ON HoaDon(ngayLap) INCLUDE (tongTien);     -- thong ke doanh thu, tra cuu
CREATE INDEX IX_HoaDon_maKH               ON HoaDon(maKH);
CREATE INDEX IX_HoaDon_maNV               ON HoaDon(maNV);
CREATE INDEX IX_CTHDSP_maSP               ON ChiTietHoaDon_SanPham(maSP);            -- thong ke so luong ban
CREATE INDEX IX_PhieuTraHang_maHD         ON PhieuTraHang(maHD);
CREATE INDEX IX_PhieuTraHang_hoanTat      ON PhieuTraHang(trangThai, thoiDiemHoanTat);
CREATE INDEX IX_CTPTH_maHD_maSP           ON ChiTietPhieuTraHang(maHD, maSP);
GO

/* ======================================================================
   KIEM TRA NHANH: liet ke cac bang vua tao
   ====================================================================== */
SELECT name AS TenBang FROM sys.tables ORDER BY name;
GO
