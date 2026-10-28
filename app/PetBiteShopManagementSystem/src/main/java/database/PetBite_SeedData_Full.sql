/* ======================================================================
   PET BITE - DU LIEU MAU (SEED) CHO TOAN BO CHUC NANG
   ----------------------------------------------------------------------
   - XOA SACH du lieu cu trong 15 bang roi nap lai => chay lai duoc nhieu lan.
   - Gom 2 phan:
       + PHAN CO DINH : lich su tu 11/2025 den 21/10/2026 (ma ket thuc 001, 002...)
       + PHAN DONG    : lich hen / hoa don / phieu tra tinh THEO GIO MAY LUC CHAY
                        (hom nay, hom qua, tuan toi...) de test lich hen sap toi, han tra
                        hang 168 gio, ban hang... (ma ket thuc 9xx)
   - Mat khau TAT CA tai khoan: PetBite@123 (luu dang SHA-256 hex 64 ky tu).
     Neu nhom dung thuat toan bam khac, cap nhat cot matKhau bang cau UPDATE o cuoi file.

   BANG "CHUC NANG -> DU LIEU DE TEST"
   ----------------------------------------------------------------------
   Dang nhap, Doi mat khau
       Tai khoan: chu (Chu cua hang), thungan1, thungan2, chamsoc1, chamsoc2.
       thungan3 dang BI KHOA (phai bi tu choi dang nhap).
   Quan ly nhan vien / tai khoan
       8 nhan vien. NV Ho Thanh Phong (Cham soc) va Ngo Bao Chau (Thu ngan) CHUA co tai khoan
       -> dung de test "Them tai khoan". Dang Quoc Bao da ngung lam viec.
   Quan ly loai san pham / san pham
       6 loai (1 loai ngung), 20 san pham. Ton thap: Pate Pedigree (3), Nha cay cho meo (4),
       Hat Me-O (8). Het hang: Banh thuong cho cho (0). Ngung kinh doanh: Hat cho meo 1kg.
   Quan ly dich vu
       8 dich vu (1 dich vu tam ngung: Kham suc khoe tong quat).
   Quan ly khach hang / thu cung
       16 khach (2 ngung quan ly). Trung ten de test tim kiem: Nguyen Thi Lan / Nguyen Van Lan.
       Tran Thi Hoa CHUA co thu cung. Le Huu Phuoc co 3 thu cung. 20 thu cung (1 ngung: Dau).
   Lich hen
       Co du trang thai: Da dat (7 lich TUONG LAI, dong), Dang thuc hien, Hoan thanh, Da huy.
       Test TRUNG GIO: Pham Thu Ha (NV4) co lich NGAY MAI 09:00-10:05 (tong 65 phut);
       dat them lich cua Pham Thu Ha luc 09:30 ngay mai phai bi bao trung.
       Vo Minh Tuan (NV5) co lich ngay mai 09:30-10:30.
   Lich su cham soc
       Muc (Poodle) va Lu (Corgi) moi con co 4 lan cham soc, can nang tang dan.
   Ban hang / Thanh toan (POS)
       Dich vu da hoan thanh nhung CHUA thanh toan: Le Huu Phuoc (2 lich -> 3 dong, test gop
       nhieu lich hen vao 1 hoa don), Pham Minh Anh, Ngo Duc Anh, Pham Quoc Viet.
       San pham het hang / ngung kinh doanh de test chan khong cho ban.
   Tra cuu hoa don
       43 hoa don co dinh + 14 hoa don dong; co hoa don chi san pham, chi dich vu, hon hop.
   Tra hang va hoan tien
       Hoa don CON HAN tra (dong): HD hom nay -> 6 ngay truoc; hoa don CON KHOANG 4 GIO;
       hoa don HET HAN (> 168 gio): hoa don dong so 10..14 va cac hoa don co dinh cu.
       Luu y: vai hoa don co dinh cuoi thang 10/2026 (18-21/10) CON HAN neu chay truoc 28/10/2026.
       Hoa don da tra mot phan: SP Pate Pedigree (mua 4, da tra 2 -> con duoc tra 2).
       Phieu: 2 Cho xac nhan, 2 Da duyet (cho hoan tien), 1 Da tu choi, 4 Da hoan tat.
   Thong ke
       Doanh thu theo ngay/thang/nam, so luong ban, luot dich vu: xem KET QUA MONG DOI o cuoi file.
   ====================================================================== */

USE PetBite;
GO
SET NOCOUNT ON;
SET XACT_ABORT ON;
GO

BEGIN TRANSACTION;

/* ---------- PHAN 0: XOA DU LIEU CU (thu tu nguoc) ---------- */
DELETE FROM ChiTietPhieuTraHang;
DELETE FROM PhieuTraHang;
DELETE FROM ChiTietHoaDon_DichVu;
DELETE FROM ChiTietHoaDon_SanPham;
DELETE FROM HoaDon;
DELETE FROM LichSuChamSoc;
DELETE FROM ChiTietLichHen;
DELETE FROM LichHen;
DELETE FROM ThuCung;
DELETE FROM SanPham;
DELETE FROM TaiKhoan;
DELETE FROM NhanVien;
DELETE FROM DichVu;
DELETE FROM KhachHang;
DELETE FROM LoaiSanPham;

/* ---------- PHAN 1: DANH MUC ---------- */

-- Loai cuoi cung (Hang ngung nhap) o trang thai ngung
INSERT INTO LoaiSanPham (maLoaiSP, tenLoaiSP, moTa, trangThai) VALUES
    (N'LSP251001001', N'Thức ăn', N'Thức ăn hạt, pate, bánh thưởng', 1),
    (N'LSP251001002', N'Đồ chơi', N'Đồ chơi cho chó mèo', 1),
    (N'LSP251001003', N'Phụ kiện', N'Vòng cổ, dây dắt, quần áo, bát ăn', 1),
    (N'LSP251001004', N'Vệ sinh và chăm sóc', N'Sữa tắm, cát vệ sinh, dụng cụ chải lông', 1),
    (N'LSP251001005', N'Chuồng và nhà ở', N'Chuồng, nhà cây, giường nằm', 1),
    (N'LSP251001006', N'Hàng ngừng nhập', N'Nhóm hàng không còn kinh doanh', 0);

-- KH11 (Ly Van Son) va KH16 (Vu Thuy Duong) ngung quan ly; KH8 khong co gioi tinh; KH6 khong co dia chi
INSERT INTO KhachHang (maKH, hoTen, soDienThoai, gioiTinh, diaChi, trangThai) VALUES
    (N'KH251001001', N'Nguyễn Thị Lan', N'0382345601', N'Nữ', N'12 Lê Lợi, Quận 1, TP.HCM', 1),
    (N'KH251001002', N'Trần Minh Khoa', N'0903456702', N'Nam', N'45 Nguyễn Trãi, Quận 5, TP.HCM', 1),
    (N'KH251001003', N'Lê Thu Trang', N'0914567803', N'Nữ', N'78 Cách Mạng Tháng 8, Quận 3, TP.HCM', 1),
    (N'KH251001004', N'Phạm Quốc Việt', N'0975678904', N'Nam', N'9 Phan Xích Long, Phú Nhuận, TP.HCM', 1),
    (N'KH251001005', N'Võ Ngọc Hân', N'0586789005', N'Nữ', N'210 Võ Văn Tần, Quận 3, TP.HCM', 1),
    (N'KH251001006', N'Đặng Gia Bảo', N'0707890106', N'Nam', NULL, 1),
    (N'KH251001007', N'Bùi Thanh Tùng', N'0788901207', N'Nam', N'33 Điện Biên Phủ, Bình Thạnh, TP.HCM', 1),
    (N'KH251001008', N'Hoàng Mỹ Linh', N'0369012308', NULL, N'5 Hoàng Văn Thụ, Tân Bình, TP.HCM', 1),
    (N'KH251001009', N'Ngô Đức Anh', N'0850123409', N'Nam', N'88 Lý Thường Kiệt, Quận 10, TP.HCM', 1),
    (N'KH251001010', N'Đỗ Khánh Vy', N'0931234510', N'Nữ', N'17 Nguyễn Văn Linh, Quận 7, TP.HCM', 1),
    (N'KH251001011', N'Lý Văn Sơn', N'0392345611', N'Nam', N'21 Lạc Long Quân, Quận 11, TP.HCM', 0),
    (N'KH251001012', N'Nguyễn Văn Lan', N'0912000012', N'Nam', N'64 Nguyễn Đình Chiểu, Quận 3, TP.HCM', 1),
    (N'KH251001013', N'Trần Thị Hoa', N'0943000013', N'Nữ', N'102 Bà Hom, Quận 6, TP.HCM', 1),
    (N'KH251001014', N'Phạm Minh Anh', N'0364000014', N'Khác', N'7 Trần Quang Khải, Quận 1, TP.HCM', 1),
    (N'KH251001015', N'Lê Hữu Phước', N'0845000015', N'Nam', N'150 Nguyễn Thị Thập, Quận 7, TP.HCM', 1),
    (N'KH251001016', N'Vũ Thùy Dương', N'0776000016', N'Nữ', N'36 Phạm Văn Đồng, Thủ Đức, TP.HCM', 0);

-- Dich vu Kham suc khoe tong quat dang tam ngung cung cap
INSERT INTO DichVu (maDV, tenDV, giaDichVu, thoiGianThucHien, moTa, trangThai) VALUES
    (N'DV251001001', N'Tắm thú cưng', 150000, 45, N'Tắm, sấy khô, vệ sinh cơ bản', 1),
    (N'DV251001002', N'Cắt tỉa lông', 220000, 60, N'Cắt tỉa tạo kiểu theo giống', 1),
    (N'DV251001003', N'Cắt móng', 50000, 15, N'Cắt và dũa móng', 1),
    (N'DV251001004', N'Vệ sinh tai', 60000, 20, N'Làm sạch tai', 1),
    (N'DV251001005', N'Spa thư giãn', 350000, 90, N'Gói spa massage và dưỡng lông', 1),
    (N'DV251001006', N'Khám sức khỏe tổng quát', 180000, 30, N'Dịch vụ tạm ngừng cung cấp', 0),
    (N'DV251001007', N'Chải lông gỡ rối', 80000, 30, N'Chải và gỡ rối lông dài', 1),
    (N'DV251001008', N'Nhuộm lông nghệ thuật', 300000, 120, NULL, 1);

-- NV1 = Chu cua hang; NV2, NV3, NV8 = Thu ngan; NV4, NV5, NV7 = Cham soc; NV6 = Thu ngan da nghi viec
INSERT INTO NhanVien (maNV, hoTen, soDienThoai, gioiTinh, ngaySinh, diaChi, vaiTro, trangThai) VALUES
    (N'NV251001001', N'Nguyễn Văn Hùng', N'0901000001', N'Nam', N'1985-04-12', N'25 Trần Hưng Đạo, Quận 1, TP.HCM', N'Chủ cửa hàng', 1),
    (N'NV251001002', N'Trần Thị Mai', N'0901000002', N'Nữ', N'1998-08-21', N'14 Lê Văn Sỹ, Quận 3, TP.HCM', N'Thu ngân', 1),
    (N'NV251001003', N'Lê Hoàng Nam', N'0901000003', N'Nam', N'1999-02-14', N'60 Nguyễn Thị Minh Khai, Quận 1, TP.HCM', N'Thu ngân', 1),
    (N'NV251001004', N'Phạm Thu Hà', N'0901000004', N'Nữ', N'1997-11-30', N'8 Hai Bà Trưng, Quận 1, TP.HCM', N'Chăm sóc', 1),
    (N'NV251001005', N'Võ Minh Tuấn', N'0901000005', N'Nam', N'1996-06-05', N'102 Pasteur, Quận 3, TP.HCM', N'Chăm sóc', 1),
    (N'NV251001006', N'Đặng Quốc Bảo', N'0901000006', N'Nam', N'1995-01-18', N'3 Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM', N'Thu ngân', 0),
    (N'NV251001007', N'Hồ Thanh Phong', N'0901000007', N'Nam', N'2001-03-09', N'47 Võ Thị Sáu, Quận 3, TP.HCM', N'Chăm sóc', 1),
    (N'NV251001008', N'Ngô Bảo Châu', N'0901000008', N'Nữ', N'2002-12-25', N'19 Trương Định, Quận 3, TP.HCM', N'Thu ngân', 1);

-- Mat khau chung PetBite@123 (SHA-256). NV7 va NV8 chua co tai khoan; tai khoan cua NV6 dang bi khoa
INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, trangThai, maNV) VALUES
    (N'TK251001001', N'chu', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 1, N'NV251001001'),
    (N'TK251001002', N'thungan1', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 1, N'NV251001002'),
    (N'TK251001003', N'thungan2', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 1, N'NV251001003'),
    (N'TK251001004', N'chamsoc1', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 1, N'NV251001004'),
    (N'TK251001005', N'chamsoc2', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 1, N'NV251001005'),
    (N'TK251001006', N'thungan3', N'f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc', 0, N'NV251001006');

-- SP12 het hang (ton = 0); SP13 ngung kinh doanh; SP14, SP15, SP17, SP18 ton thap. Gia ban hien tai co the khac don gia tren hoa don cu
INSERT INTO SanPham (maSP, tenSP, donViTinh, giaBan, soLuongTon, trangThai, maLoaiSP) VALUES
    (N'SP251001001', N'Hạt Royal Canin cho chó 2kg', N'Túi', 385000, 40, 1, N'LSP251001001'),
    (N'SP251001002', N'Pate Whiskas cho mèo', N'Lon', 28000, 120, 1, N'LSP251001001'),
    (N'SP251001003', N'Xương gặm sạch răng', N'Gói', 45000, 80, 1, N'LSP251001001'),
    (N'SP251001004', N'Bóng cao su phát tiếng', N'Cái', 60000, 35, 1, N'LSP251001002'),
    (N'SP251001005', N'Cần câu mèo lông vũ', N'Cái', 55000, 25, 1, N'LSP251001002'),
    (N'SP251001006', N'Vòng cổ có chuông', N'Cái', 85000, 30, 1, N'LSP251001003'),
    (N'SP251001007', N'Dây dắt chó 1.5m', N'Cái', 120000, 18, 1, N'LSP251001003'),
    (N'SP251001008', N'Áo thú cưng size M', N'Cái', 150000, 12, 1, N'LSP251001003'),
    (N'SP251001009', N'Sữa tắm khử mùi 500ml', N'Chai', 140000, 22, 1, N'LSP251001004'),
    (N'SP251001010', N'Cát vệ sinh cho mèo 5L', N'Túi', 95000, 60, 1, N'LSP251001004'),
    (N'SP251001011', N'Lược chải lông', N'Cái', 70000, 15, 1, N'LSP251001004'),
    (N'SP251001012', N'Bánh thưởng cho chó', N'Gói', 38000, 0, 1, N'LSP251001001'),
    (N'SP251001013', N'Hạt cho mèo 1kg (ngừng kinh doanh)', N'Túi', 160000, 0, 0, N'LSP251001006'),
    (N'SP251001014', N'Hạt Me-O cho mèo 1.2kg', N'Túi', 98000, 8, 1, N'LSP251001001'),
    (N'SP251001015', N'Pate Pedigree cho chó', N'Gói', 24000, 3, 1, N'LSP251001001'),
    (N'SP251001016', N'Gặm xương da bò', N'Cái', 52000, 45, 1, N'LSP251001001'),
    (N'SP251001017', N'Chuồng nhựa size M', N'Cái', 450000, 6, 1, N'LSP251001005'),
    (N'SP251001018', N'Nhà cây cho mèo', N'Cái', 890000, 4, 1, N'LSP251001005'),
    (N'SP251001019', N'Bát ăn inox', N'Cái', 65000, 50, 1, N'LSP251001003'),
    (N'SP251001020', N'Thuốc xịt khử mùi', N'Chai', 78000, 28, 1, N'LSP251001004');

-- TC20 (Dau) da ngung quan ly; TC6, TC10, TC19 thieu ngay sinh / giong / gioi tinh; KH13 khong co thu cung
INSERT INTO ThuCung (maThuCung, tenThuCung, loai, giong, gioiTinh, ngaySinh, canNang, trangThai, maKH) VALUES
    (N'TC251001001', N'Mực', N'Chó', N'Poodle', N'Đực', N'2022-03-15', 5.2, 1, N'KH251001001'),
    (N'TC251001002', N'Mướp', N'Mèo', N'Anh lông ngắn', N'Cái', N'2023-01-20', 3.8, 1, N'KH251001001'),
    (N'TC251001003', N'Lu', N'Chó', N'Corgi', N'Đực', N'2021-07-10', 11.5, 1, N'KH251001002'),
    (N'TC251001004', N'Bơ', N'Chó', N'Golden Retriever', N'Cái', N'2020-11-05', 27.4, 1, N'KH251001003'),
    (N'TC251001005', N'Kitty', N'Mèo', N'Ba Tư', N'Cái', N'2023-06-18', 4.1, 1, N'KH251001004'),
    (N'TC251001006', N'Cốm', N'Chó', N'Phốc sóc', N'Đực', NULL, 2.9, 1, N'KH251001005'),
    (N'TC251001007', N'Misa', N'Mèo', N'Xiêm', N'Cái', N'2022-09-09', 3.3, 1, N'KH251001006'),
    (N'TC251001008', N'Tobi', N'Chó', N'Husky', N'Đực', N'2021-02-14', 21.0, 1, N'KH251001007'),
    (N'TC251001009', N'Nấm', N'Chó', N'Pug', N'Cái', N'2022-12-01', 8.6, 1, N'KH251001008'),
    (N'TC251001010', N'Bông', N'Thỏ', NULL, NULL, NULL, 1.8, 1, N'KH251001009'),
    (N'TC251001011', N'Sushi', N'Mèo', N'Munchkin', N'Đực', N'2024-02-02', 3.0, 1, N'KH251001010'),
    (N'TC251001012', N'Pi', N'Hamster', N'Syria', N'Cái', N'2025-05-10', 0.15, 1, N'KH251001002'),
    (N'TC251001013', N'Gấu', N'Chó', N'Alaska', N'Đực', N'2021-05-05', 33.0, 1, N'KH251001015'),
    (N'TC251001014', N'Tuyết', N'Mèo', N'Anh lông dài', N'Cái', N'2022-03-03', 4.4, 1, N'KH251001015'),
    (N'TC251001015', N'Cam', N'Mèo', N'Tam thể', N'Cái', N'2024-08-08', 2.9, 1, N'KH251001015'),
    (N'TC251001016', N'Lucky', N'Chó', N'Becgie', N'Đực', N'2020-01-12', 30.2, 1, N'KH251001012'),
    (N'TC251001017', N'Bin', N'Chó', N'Chihuahua', N'Đực', N'2023-09-09', 2.3, 1, N'KH251001014'),
    (N'TC251001018', N'Mochi', N'Mèo', N'Scottish', N'Cái', N'2024-12-01', 2.7, 1, N'KH251001014'),
    (N'TC251001019', N'Rùa', N'Rùa', NULL, NULL, NULL, 0.9, 1, N'KH251001016'),
    (N'TC251001020', N'Đậu', N'Mèo', N'Xiêm', N'Đực', N'2018-06-06', 4.0, 0, N'KH251001001');

/* ---------- PHAN 2: LICH HEN CO DINH ---------- */

-- 20 lich Hoan thanh + 1 lich Da huy. Hai lich HOAN THANH nhung CHUA THANH TOAN: LH261020001, LH261021001
INSERT INTO LichHen (maLichHen, ngayGioHen, trangThai, tongThoiGianDuKien, thoiDiemHoanThanh, maKH, maThuCung, maNV) VALUES
    (N'LH251108001', '2025-11-08T09:00:00', N'Hoàn thành', 60, '2025-11-08T10:05:00', N'KH251001001', N'TC251001001', N'NV251001004'),
    (N'LH251122001', '2025-11-22T14:00:00', N'Hoàn thành', 60, '2025-11-22T15:10:00', N'KH251001002', N'TC251001003', N'NV251001005'),
    (N'LH251215001', '2025-12-15T10:00:00', N'Hoàn thành', 65, '2025-12-15T11:15:00', N'KH251001003', N'TC251001004', N'NV251001004'),
    (N'LH260110001', '2026-01-10T09:30:00', N'Hoàn thành', 45, '2026-01-10T10:20:00', N'KH251001001', N'TC251001001', N'NV251001004'),
    (N'LH260214001', '2026-02-14T15:00:00', N'Hoàn thành', 90, '2026-02-14T16:40:00', N'KH251001004', N'TC251001005', N'NV251001005'),
    (N'LH260320001', '2026-03-20T08:30:00', N'Hoàn thành', 105, '2026-03-20T10:30:00', N'KH251001005', N'TC251001006', N'NV251001004'),
    (N'LH260418001', '2026-04-18T13:00:00', N'Hoàn thành', 15, '2026-04-18T13:25:00', N'KH251001002', N'TC251001003', N'NV251001005'),
    (N'LH260525001', '2026-05-25T09:00:00', N'Hoàn thành', 45, '2026-05-25T09:55:00', N'KH251001006', N'TC251001007', N'NV251001004'),
    (N'LH260630001', '2026-06-30T16:00:00', N'Hoàn thành', 105, '2026-06-30T17:50:00', N'KH251001003', N'TC251001004', N'NV251001005'),
    (N'LH260720001', '2026-07-20T10:00:00', N'Hoàn thành', 45, '2026-07-20T10:50:00', N'KH251001007', N'TC251001008', N'NV251001004'),
    (N'LH260825001', '2026-08-25T14:30:00', N'Hoàn thành', 65, '2026-08-25T15:45:00', N'KH251001001', N'TC251001002', N'NV251001005'),
    (N'LH260915001', '2026-09-15T09:00:00', N'Hoàn thành', 60, '2026-09-15T10:10:00', N'KH251001008', N'TC251001009', N'NV251001004'),
    (N'LH261008001', '2026-10-08T10:00:00', N'Hoàn thành', 45, '2026-10-08T10:55:00', N'KH251001002', N'TC251001003', N'NV251001004'),
    (N'LH261015001', '2026-10-15T15:00:00', N'Hoàn thành', 60, '2026-10-15T16:05:00', N'KH251001005', N'TC251001006', N'NV251001005'),
    (N'LH261020001', '2026-10-20T09:30:00', N'Hoàn thành', 60, '2026-10-20T10:40:00', N'KH251001009', N'TC251001010', N'NV251001004'),
    (N'LH261021001', '2026-10-21T15:00:00', N'Hoàn thành', 60, '2026-10-21T16:05:00', N'KH251001004', N'TC251001005', N'NV251001005'),
    (N'LH261019001', '2026-10-19T11:00:00', N'Đã hủy', 90, NULL, N'KH251001006', N'TC251001007', N'NV251001004'),
    (N'LH260405001', '2026-04-05T10:00:00', N'Hoàn thành', 75, '2026-04-05T11:30:00', N'KH251001001', N'TC251001001', N'NV251001004'),
    (N'LH260614001', '2026-06-14T15:00:00', N'Hoàn thành', 60, '2026-06-14T16:15:00', N'KH251001002', N'TC251001003', N'NV251001004'),
    (N'LH260802001', '2026-08-02T09:00:00', N'Hoàn thành', 60, '2026-08-02T10:10:00', N'KH251001001', N'TC251001001', N'NV251001005'),
    (N'LH260920001', '2026-09-20T11:00:00', N'Hoàn thành', 45, '2026-09-20T11:55:00', N'KH251001012', N'TC251001016', N'NV251001005');

INSERT INTO ChiTietLichHen (maLichHen, maDV, ghiChuRieng, thoiGianDuKien, trangThaiThanhToan) VALUES
    (N'LH251108001', N'DV251001001', NULL, 45, 1),
    (N'LH251108001', N'DV251001003', N'Móng hơi dài', 15, 1),
    (N'LH251122001', N'DV251001002', N'Tỉa gọn kiểu teddy', 60, 1),
    (N'LH251215001', N'DV251001001', NULL, 45, 1),
    (N'LH251215001', N'DV251001004', NULL, 20, 1),
    (N'LH260110001', N'DV251001001', NULL, 45, 1),
    (N'LH260214001', N'DV251001005', N'Dùng sữa tắm dịu nhẹ', 90, 1),
    (N'LH260320001', N'DV251001001', NULL, 45, 1),
    (N'LH260320001', N'DV251001002', NULL, 60, 1),
    (N'LH260418001', N'DV251001003', NULL, 15, 1),
    (N'LH260525001', N'DV251001001', NULL, 45, 1),
    (N'LH260630001', N'DV251001005', NULL, 90, 1),
    (N'LH260630001', N'DV251001003', NULL, 15, 1),
    (N'LH260720001', N'DV251001001', N'Lông dày, sấy kỹ', 45, 1),
    (N'LH260825001', N'DV251001001', NULL, 45, 1),
    (N'LH260825001', N'DV251001004', NULL, 20, 1),
    (N'LH260915001', N'DV251001002', NULL, 60, 1),
    (N'LH261008001', N'DV251001001', NULL, 45, 1),
    (N'LH261015001', N'DV251001001', NULL, 45, 1),
    (N'LH261015001', N'DV251001003', NULL, 15, 1),
    (N'LH261020001', N'DV251001002', N'Thỏ nhút nhát, thao tác nhẹ', 60, 0),
    (N'LH261021001', N'DV251001001', NULL, 45, 0),
    (N'LH261021001', N'DV251001003', NULL, 15, 0),
    (N'LH261019001', N'DV251001005', NULL, 90, 0),
    (N'LH260405001', N'DV251001001', NULL, 45, 1);
INSERT INTO ChiTietLichHen (maLichHen, maDV, ghiChuRieng, thoiGianDuKien, trangThaiThanhToan) VALUES
    (N'LH260405001', N'DV251001007', N'Lông rối sau mùa mưa', 30, 1),
    (N'LH260614001', N'DV251001002', NULL, 60, 1),
    (N'LH260802001', N'DV251001001', NULL, 45, 1),
    (N'LH260802001', N'DV251001003', NULL, 15, 1),
    (N'LH260920001', N'DV251001001', NULL, 45, 1);

-- Moi lich hen Hoan thanh co dung 1 lich su. Muc (TC1) va Lu (TC3) co 4 lan, can nang tang dan
INSERT INTO LichSuChamSoc (maLS, canNang, ghiChu, maLichHen) VALUES
    (N'LS251108001', 4.75, N'Sức khỏe tốt, lông hơi rối', N'LH251108001'),
    (N'LS251122001', 11.05, NULL, N'LH251122001'),
    (N'LS251215001', 27.25, NULL, N'LH251215001'),
    (N'LS260110001', 4.9, NULL, N'LH260110001'),
    (N'LS260214001', 3.95, N'Da nhạy cảm, nên dùng sữa tắm dịu nhẹ', N'LH260214001'),
    (N'LS260320001', 2.75, NULL, N'LH260320001'),
    (N'LS260418001', 11.2, NULL, N'LH260418001'),
    (N'LS260525001', 3.3, NULL, N'LH260525001'),
    (N'LS260630001', 27.4, NULL, N'LH260630001'),
    (N'LS260720001', 21.0, NULL, N'LH260720001'),
    (N'LS260825001', 3.8, NULL, N'LH260825001'),
    (N'LS260915001', 8.6, NULL, N'LH260915001'),
    (N'LS261008001', 11.5, NULL, N'LH261008001'),
    (N'LS261015001', 2.9, NULL, N'LH261015001'),
    (N'LS261020001', 1.8, N'Thỏ hay hoảng sợ, lần sau đặt lịch buổi sáng', N'LH261020001'),
    (N'LS261021001', 4.1, NULL, N'LH261021001'),
    (N'LS260405001', 5.05, NULL, N'LH260405001'),
    (N'LS260614001', 11.35, NULL, N'LH260614001'),
    (N'LS260802001', 5.2, NULL, N'LH260802001'),
    (N'LS260920001', 30.2, NULL, N'LH260920001');

/* ---------- PHAN 3: HOA DON CO DINH ---------- */

-- 43 hoa don: 26 tien mat, 17 chuyen khoan; co hoa don chi san pham, chi dich vu, va hon hop
INSERT INTO HoaDon (maHD, ngayLap, trangThai, phuongThucTT, tienKhachDua, tongTien, tienThua, maKH, maNV) VALUES
    (N'HD251103001', '2025-11-03T10:20:00', N'Đã thanh toán', N'Tiền mặt', 500000, 488000, 12000, N'KH251001001', N'NV251001006'),
    (N'HD251108001', '2025-11-08T10:30:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 325000, NULL, N'KH251001001', N'NV251001002'),
    (N'HD251115001', '2025-11-15T16:45:00', N'Đã thanh toán', N'Tiền mặt', 250000, 205000, 45000, N'KH251001005', N'NV251001006'),
    (N'HD251122001', '2025-11-22T15:20:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 200000, NULL, N'KH251001002', N'NV251001002'),
    (N'HD251205001', '2025-12-05T09:40:00', N'Đã thanh toán', N'Tiền mặt', 400000, 358000, 42000, N'KH251001003', N'NV251001003'),
    (N'HD251215001', '2025-12-15T11:30:00', N'Đã thanh toán', N'Tiền mặt', 300000, 290000, 10000, N'KH251001003', N'NV251001002'),
    (N'HD251224001', '2025-12-24T18:10:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 420000, NULL, N'KH251001006', N'NV251001003'),
    (N'HD251231001', '2025-12-31T17:25:00', N'Đã thanh toán', N'Tiền mặt', 800000, 770000, 30000, N'KH251001004', N'NV251001002'),
    (N'HD260110001', '2026-01-10T10:30:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 150000, NULL, N'KH251001001', N'NV251001002'),
    (N'HD260118001', '2026-01-18T14:15:00', N'Đã thanh toán', N'Tiền mặt', 200000, 170000, 30000, N'KH251001007', N'NV251001003'),
    (N'HD260214001', '2026-02-14T17:00:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 420000, NULL, N'KH251001004', N'NV251001002'),
    (N'HD260226001', '2026-02-26T09:50:00', N'Đã thanh toán', N'Tiền mặt', 300000, 280000, 20000, N'KH251001008', N'NV251001003'),
    (N'HD260320001', '2026-03-20T10:45:00', N'Đã thanh toán', N'Tiền mặt', 550000, 510000, 40000, N'KH251001005', N'NV251001003'),
    (N'HD260330001', '2026-03-30T15:05:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 670000, NULL, N'KH251001002', N'NV251001002'),
    (N'HD260405001', '2026-04-05T11:45:00', N'Đã thanh toán', N'Tiền mặt', 300000, 300000, 0, N'KH251001001', N'NV251001002'),
    (N'HD260418001', '2026-04-18T13:40:00', N'Đã thanh toán', N'Tiền mặt', 50000, 50000, 0, N'KH251001002', N'NV251001002'),
    (N'HD260427001', '2026-04-27T11:10:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 404000, NULL, N'KH251001009', N'NV251001003'),
    (N'HD260509001', '2026-05-09T16:20:00', N'Đã thanh toán', N'Tiền mặt', 300000, 270000, 30000, N'KH251001010', N'NV251001002'),
    (N'HD260525001', '2026-05-25T10:10:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 290000, NULL, N'KH251001006', N'NV251001003'),
    (N'HD260612001', '2026-06-12T12:00:00', N'Đã thanh toán', N'Tiền mặt', 1200000, 1155000, 45000, N'KH251001003', N'NV251001002'),
    (N'HD260614001', '2026-06-14T16:30:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 220000, NULL, N'KH251001002', N'NV251001003'),
    (N'HD260630001', '2026-06-30T18:05:00', N'Đã thanh toán', N'Tiền mặt', 400000, 400000, 0, N'KH251001003', N'NV251001003'),
    (N'HD260704001', '2026-07-04T09:35:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 359000, NULL, N'KH251001001', N'NV251001002'),
    (N'HD260720001', '2026-07-20T11:05:00', N'Đã thanh toán', N'Tiền mặt', 250000, 245000, 5000, N'KH251001007', N'NV251001003'),
    (N'HD260802001', '2026-08-02T10:30:00', N'Đã thanh toán', N'Tiền mặt', 300000, 295000, 5000, N'KH251001001', N'NV251001002');
INSERT INTO HoaDon (maHD, ngayLap, trangThai, phuongThucTT, tienKhachDua, tongTien, tienThua, maKH, maNV) VALUES
    (N'HD260808001', '2026-08-08T14:50:00', N'Đã thanh toán', N'Tiền mặt', 250000, 210000, 40000, N'KH251001005', N'NV251001002'),
    (N'HD260815001', '2026-08-15T11:00:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 580000, NULL, N'KH251001015', N'NV251001002'),
    (N'HD260825001', '2026-08-25T16:00:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 490000, NULL, N'KH251001001', N'NV251001003'),
    (N'HD260905001', '2026-09-05T10:25:00', N'Đã thanh toán', N'Tiền mặt', 900000, 860000, 40000, N'KH251001008', N'NV251001002'),
    (N'HD260910001', '2026-09-10T15:30:00', N'Đã thanh toán', N'Tiền mặt', 400000, 394000, 6000, N'KH251001014', N'NV251001003'),
    (N'HD260915001', '2026-09-15T10:30:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 220000, NULL, N'KH251001008', N'NV251001003'),
    (N'HD260920001', '2026-09-20T12:00:00', N'Đã thanh toán', N'Tiền mặt', 150000, 150000, 0, N'KH251001012', N'NV251001003'),
    (N'HD260929001', '2026-09-29T19:00:00', N'Đã thanh toán', N'Tiền mặt', 400000, 390000, 10000, N'KH251001004', N'NV251001002'),
    (N'HD261002001', '2026-10-02T09:10:00', N'Đã thanh toán', N'Tiền mặt', 350000, 330000, 20000, N'KH251001002', N'NV251001002'),
    (N'HD261002002', '2026-10-02T15:40:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 115000, NULL, N'KH251001010', N'NV251001003'),
    (N'HD261008001', '2026-10-08T11:15:00', N'Đã thanh toán', N'Tiền mặt', 300000, 290000, 10000, N'KH251001002', N'NV251001002'),
    (N'HD261010001', '2026-10-10T10:15:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 994000, NULL, N'KH251001015', N'NV251001002'),
    (N'HD261012001', '2026-10-12T17:30:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 565000, NULL, N'KH251001007', N'NV251001003'),
    (N'HD261014001', '2026-10-14T13:40:00', N'Đã thanh toán', N'Tiền mặt', 250000, 228000, 22000, N'KH251001012', N'NV251001003'),
    (N'HD261015001', '2026-10-15T16:20:00', N'Đã thanh toán', N'Tiền mặt', 200000, 200000, 0, N'KH251001005', N'NV251001002'),
    (N'HD261018001', '2026-10-18T19:00:00', N'Đã thanh toán', N'Tiền mặt', 350000, 320000, 30000, N'KH251001006', N'NV251001003'),
    (N'HD261020001', '2026-10-20T14:20:00', N'Đã thanh toán', N'Tiền mặt', 250000, 244000, 6000, N'KH251001001', N'NV251001003'),
    (N'HD261021001', '2026-10-21T10:00:00', N'Đã thanh toán', N'Chuyển khoản', NULL, 260000, NULL, N'KH251001003', N'NV251001002');

-- thanhTien la cot tinh toan nen KHONG co trong danh sach cot

INSERT INTO ChiTietHoaDon_SanPham (maHD, maSP, soLuong, donGia) VALUES
    (N'HD251103001', N'SP251001001', 1, 380000),
    (N'HD251103001', N'SP251001002', 4, 27000),
    (N'HD251108001', N'SP251001009', 1, 135000),
    (N'HD251115001', N'SP251001004', 2, 60000),
    (N'HD251115001', N'SP251001006', 1, 85000),
    (N'HD251205001', N'SP251001010', 2, 95000),
    (N'HD251205001', N'SP251001002', 6, 28000),
    (N'HD251215001', N'SP251001003', 2, 45000),
    (N'HD251224001', N'SP251001008', 2, 150000),
    (N'HD251224001', N'SP251001007', 1, 120000),
    (N'HD251231001', N'SP251001001', 2, 385000),
    (N'HD260118001', N'SP251001005', 2, 55000),
    (N'HD260118001', N'SP251001004', 1, 60000),
    (N'HD260214001', N'SP251001011', 1, 70000),
    (N'HD260226001', N'SP251001002', 10, 28000),
    (N'HD260320001', N'SP251001009', 1, 140000),
    (N'HD260330001', N'SP251001001', 1, 385000),
    (N'HD260330001', N'SP251001010', 3, 95000),
    (N'HD260405001', N'SP251001011', 1, 70000),
    (N'HD260427001', N'SP251001007', 1, 120000),
    (N'HD260427001', N'SP251001006', 2, 85000),
    (N'HD260427001', N'SP251001012', 3, 38000),
    (N'HD260509001', N'SP251001008', 1, 150000),
    (N'HD260509001', N'SP251001004', 2, 60000),
    (N'HD260525001', N'SP251001009', 1, 140000);
INSERT INTO ChiTietHoaDon_SanPham (maHD, maSP, soLuong, donGia) VALUES
    (N'HD260612001', N'SP251001001', 3, 385000),
    (N'HD260704001', N'SP251001002', 8, 28000),
    (N'HD260704001', N'SP251001003', 3, 45000),
    (N'HD260720001', N'SP251001010', 1, 95000),
    (N'HD260802001', N'SP251001010', 1, 95000),
    (N'HD260808001', N'SP251001005', 1, 55000),
    (N'HD260808001', N'SP251001006', 1, 85000),
    (N'HD260808001', N'SP251001011', 1, 70000),
    (N'HD260815001', N'SP251001017', 1, 450000),
    (N'HD260815001', N'SP251001019', 2, 65000),
    (N'HD260825001', N'SP251001009', 2, 140000),
    (N'HD260905001', N'SP251001001', 2, 385000),
    (N'HD260905001', N'SP251001003', 2, 45000),
    (N'HD260910001', N'SP251001014', 2, 98000),
    (N'HD260910001', N'SP251001015', 5, 24000),
    (N'HD260910001', N'SP251001020', 1, 78000),
    (N'HD260929001', N'SP251001007', 2, 120000),
    (N'HD260929001', N'SP251001008', 1, 150000),
    (N'HD261002001', N'SP251001002', 5, 28000),
    (N'HD261002001', N'SP251001010', 2, 95000),
    (N'HD261002002', N'SP251001004', 1, 60000),
    (N'HD261002002', N'SP251001005', 1, 55000),
    (N'HD261008001', N'SP251001009', 1, 140000),
    (N'HD261010001', N'SP251001018', 1, 890000),
    (N'HD261010001', N'SP251001016', 2, 52000);
INSERT INTO ChiTietHoaDon_SanPham (maHD, maSP, soLuong, donGia) VALUES
    (N'HD261012001', N'SP251001001', 1, 385000),
    (N'HD261012001', N'SP251001003', 4, 45000),
    (N'HD261014001', N'SP251001014', 1, 98000),
    (N'HD261014001', N'SP251001016', 1, 52000),
    (N'HD261014001', N'SP251001020', 1, 78000),
    (N'HD261018001', N'SP251001006', 2, 85000),
    (N'HD261018001', N'SP251001008', 1, 150000),
    (N'HD261020001', N'SP251001002', 6, 28000),
    (N'HD261020001', N'SP251001012', 2, 38000),
    (N'HD261021001', N'SP251001010', 2, 95000),
    (N'HD261021001', N'SP251001011', 1, 70000);

INSERT INTO ChiTietHoaDon_DichVu (maHD, maLichHen, maDV, donGia) VALUES
    (N'HD251108001', N'LH251108001', N'DV251001001', 140000),
    (N'HD251108001', N'LH251108001', N'DV251001003', 50000),
    (N'HD251122001', N'LH251122001', N'DV251001002', 200000),
    (N'HD251215001', N'LH251215001', N'DV251001001', 140000),
    (N'HD251215001', N'LH251215001', N'DV251001004', 60000),
    (N'HD260110001', N'LH260110001', N'DV251001001', 150000),
    (N'HD260214001', N'LH260214001', N'DV251001005', 350000),
    (N'HD260320001', N'LH260320001', N'DV251001001', 150000),
    (N'HD260320001', N'LH260320001', N'DV251001002', 220000),
    (N'HD260405001', N'LH260405001', N'DV251001001', 150000),
    (N'HD260405001', N'LH260405001', N'DV251001007', 80000),
    (N'HD260418001', N'LH260418001', N'DV251001003', 50000),
    (N'HD260525001', N'LH260525001', N'DV251001001', 150000),
    (N'HD260614001', N'LH260614001', N'DV251001002', 220000),
    (N'HD260630001', N'LH260630001', N'DV251001005', 350000),
    (N'HD260630001', N'LH260630001', N'DV251001003', 50000),
    (N'HD260720001', N'LH260720001', N'DV251001001', 150000),
    (N'HD260802001', N'LH260802001', N'DV251001001', 150000),
    (N'HD260802001', N'LH260802001', N'DV251001003', 50000),
    (N'HD260825001', N'LH260825001', N'DV251001001', 150000),
    (N'HD260825001', N'LH260825001', N'DV251001004', 60000),
    (N'HD260915001', N'LH260915001', N'DV251001002', 220000),
    (N'HD260920001', N'LH260920001', N'DV251001001', 150000),
    (N'HD261008001', N'LH261008001', N'DV251001001', 150000),
    (N'HD261015001', N'LH261015001', N'DV251001001', 150000);
INSERT INTO ChiTietHoaDon_DichVu (maHD, maLichHen, maDV, donGia) VALUES
    (N'HD261015001', N'LH261015001', N'DV251001003', 50000);

/* ---------- PHAN 4: PHIEU TRA HANG CO DINH ---------- */

-- 3 phieu Da hoan tat (1 phieu hoan LECH THANG, 1 phieu duyet it hon du kien), 1 Cho xac nhan, 1 Da duyet, 1 Da tu choi
INSERT INTO PhieuTraHang (maPhieuTra, ngayLap, trangThai, tongTienDuKien, soTienDuocDuyet, soTienThucTra, lyDoTuChoi, thoiDiemHoanTat, lyDoGiamHoan, maHD, nguoiLap, nguoiDuyet) VALUES
    (N'PT260401001', '2026-04-01T10:00:00', N'Đã hoàn tất', 95000, 95000, 95000, NULL, '2026-04-02T09:30:00', NULL, N'HD260330001', N'NV251001002', N'NV251001001'),
    (N'PT261002001', '2026-10-02T11:00:00', N'Đã hoàn tất', 150000, 150000, 150000, NULL, '2026-10-03T14:00:00', NULL, N'HD260929001', N'NV251001002', N'NV251001001'),
    (N'PT261014001', '2026-10-14T09:00:00', N'Đã hoàn tất', 90000, 80000, 80000, NULL, '2026-10-14T15:30:00', N'Bao bì đã mở, trừ 10.000đ trên mỗi gói', N'HD261012001', N'NV251001003', N'NV251001001'),
    (N'PT261021001', '2026-10-21T10:00:00', N'Chờ xác nhận', 85000, NULL, NULL, NULL, NULL, NULL, N'HD261018001', N'NV251001003', NULL),
    (N'PT261021002', '2026-10-21T16:00:00', N'Đã duyệt', 190000, 190000, NULL, NULL, NULL, NULL, N'HD261021001', N'NV251001002', N'NV251001001'),
    (N'PT261021003', '2026-10-21T09:00:00', N'Đã từ chối', 84000, NULL, NULL, N'Bao bì đã mở, không đủ điều kiện hoàn trả', NULL, NULL, N'HD261020001', N'NV251001003', N'NV251001001');

-- Phieu Da duyet cuoi cung co 2 dong cung san pham: 1 Nguyen ven + 1 Loi
INSERT INTO ChiTietPhieuTraHang (maPhieuTra, maHD, maSP, tinhTrang, soLuongTra, lyDoTra, donGia) VALUES
    (N'PT260401001', N'HD260330001', N'SP251001010', N'Lỗi', 1, N'Túi bị rách, cát vón cục', 95000),
    (N'PT261002001', N'HD260929001', N'SP251001008', N'Nguyên vẹn', 1, N'Khách đổi ý, không dùng', 150000),
    (N'PT261014001', N'HD261012001', N'SP251001003', N'Lỗi', 2, N'Gói bị ẩm, mốc', 45000),
    (N'PT261021001', N'HD261018001', N'SP251001006', N'Lỗi', 1, N'Chuông bị hỏng', 85000),
    (N'PT261021002', N'HD261021001', N'SP251001010', N'Nguyên vẹn', 1, N'Mua nhầm loại cát', 95000),
    (N'PT261021002', N'HD261021001', N'SP251001010', N'Lỗi', 1, N'Túi bị rách', 95000),
    (N'PT261021003', N'HD261020001', N'SP251001002', N'Nguyên vẹn', 3, N'Khách không cần nữa', 28000);

/* ======================================================================
   PHAN 5: DU LIEU DONG - tinh theo gio may luc chay (ma ket thuc 9xx)
   ====================================================================== */
DECLARE @bayGio DATETIME2(0) = SYSDATETIME();

DECLARE @LH_a VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, -210, @bayGio), 'yyMMdd') + '901';
DECLARE @LH_b VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 870, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '902';
DECLARE @LH_c VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 540, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '903';
DECLARE @LH_u1 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, -75, @bayGio), 'yyMMdd') + '904';
DECLARE @LH_u2 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 900, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '905';
DECLARE @LH_u3 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '906';
DECLARE @LH_n VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, -20, @bayGio), 'yyMMdd') + '907';
DECLARE @LH_x VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 780, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '908';
DECLARE @LH_f1 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 540, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '909';
DECLARE @LH_f2 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 570, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '910';
DECLARE @LH_f3 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 840, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '911';
DECLARE @LH_f4 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 600, CAST(DATEADD(DAY, 2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '912';
DECLARE @LH_f5 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 900, CAST(DATEADD(DAY, 3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '913';
DECLARE @LH_f6 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 540, CAST(DATEADD(DAY, 5, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '914';
DECLARE @LH_f7 VARCHAR(15) = 'LH' + FORMAT(DATEADD(MINUTE, 660, CAST(DATEADD(DAY, 7, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '915';
DECLARE @HD_1 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, -90, @bayGio), 'yyMMdd') + '901';
DECLARE @HD_2 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, -40, @bayGio), 'yyMMdd') + '902';
DECLARE @HD_3 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 680, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '903';
DECLARE @HD_4 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 965, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '904';
DECLARE @HD_5 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 640, CAST(DATEADD(DAY, -3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '905';
DECLARE @HD_6 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 1035, CAST(DATEADD(DAY, -4, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '906';
DECLARE @HD_7 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 750, CAST(DATEADD(DAY, -5, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '907';
DECLARE @HD_8 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 1125, CAST(DATEADD(DAY, -6, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '908';
DECLARE @HD_9 VARCHAR(15) = 'HD' + FORMAT(DATEADD(HOUR, -164, @bayGio), 'yyMMdd') + '909';
DECLARE @HD_10 VARCHAR(15) = 'HD' + FORMAT(DATEADD(HOUR, -200, @bayGio), 'yyMMdd') + '910';
DECLARE @HD_11 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 610, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '911';
DECLARE @HD_12 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 920, CAST(DATEADD(DAY, -10, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '912';
DECLARE @HD_13 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 590, CAST(DATEADD(DAY, -12, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '913';
DECLARE @HD_14 VARCHAR(15) = 'HD' + FORMAT(DATEADD(MINUTE, 865, CAST(DATEADD(DAY, -13, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '914';
DECLARE @LS_a VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 60, DATEADD(MINUTE, -210, @bayGio)), 'yyMMdd') + '901';
DECLARE @LS_b VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 80, DATEADD(MINUTE, 870, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), 'yyMMdd') + '902';
DECLARE @LS_c VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 55, DATEADD(MINUTE, 540, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), 'yyMMdd') + '903';
DECLARE @LS_u1 VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 55, DATEADD(MINUTE, -75, @bayGio)), 'yyMMdd') + '904';
DECLARE @LS_u2 VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 110, DATEADD(MINUTE, 900, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), 'yyMMdd') + '905';
DECLARE @LS_u3 VARCHAR(15) = 'LS' + FORMAT(DATEADD(MINUTE, 50, DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), 'yyMMdd') + '906';
DECLARE @PT_1 VARCHAR(15) = 'PT' + FORMAT(DATEADD(MINUTE, 840, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '901';
DECLARE @PT_2 VARCHAR(15) = 'PT' + FORMAT(DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -4, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '902';
DECLARE @PT_3 VARCHAR(15) = 'PT' + FORMAT(DATEADD(MINUTE, 570, CAST(DATEADD(DAY, -3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), 'yyMMdd') + '903';

-- Lich hen dong: a = hom nay, Hoan thanh, DA thanh toan (hoa don 2); b = 2 ngay truoc, Hoan thanh, da thanh toan (hoa don 4); c = 9 ngay truoc, Hoan thanh, da thanh toan (hoa don 11); u1 = hom nay, Hoan thanh 20 phut truoc, CHUA thanh toan; u2 = hom qua, Hoan thanh, CHUA thanh toan (cung khach voi u3); u3 = hom qua, Hoan thanh, CHUA thanh toan (cung khach voi u2); n = hom nay, Dang thuc hien; x = hom qua, Da huy; f1 = NGAY MAI 09:00 NV4 (65 phut) - moc test trung gio; f2 = ngay mai 09:30 NV5 (60 phut); f3 = ngay mai 14:00 NV4 Spa; f4 = +2 ngay; f5 = +3 ngay; f6 = +5 ngay, dich vu 120 phut; f7 = +7 ngay
INSERT INTO LichHen (maLichHen, ngayGioHen, trangThai, tongThoiGianDuKien, thoiDiemHoanThanh, maKH, maThuCung, maNV) VALUES
    (@LH_a, DATEADD(MINUTE, -210, @bayGio), N'Hoàn thành', 45, DATEADD(MINUTE, 60, DATEADD(MINUTE, -210, @bayGio)), N'KH251001010', N'TC251001011', N'NV251001005'),
    (@LH_b, DATEADD(MINUTE, 870, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Hoàn thành', 75, DATEADD(MINUTE, 80, DATEADD(MINUTE, 870, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), N'KH251001012', N'TC251001016', N'NV251001005'),
    (@LH_c, DATEADD(MINUTE, 540, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Hoàn thành', 45, DATEADD(MINUTE, 55, DATEADD(MINUTE, 540, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), N'KH251001007', N'TC251001008', N'NV251001004'),
    (@LH_u1, DATEADD(MINUTE, -75, @bayGio), N'Hoàn thành', 15, DATEADD(MINUTE, 55, DATEADD(MINUTE, -75, @bayGio)), N'KH251001014', N'TC251001017', N'NV251001004'),
    (@LH_u2, DATEADD(MINUTE, 900, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Hoàn thành', 105, DATEADD(MINUTE, 110, DATEADD(MINUTE, 900, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), N'KH251001015', N'TC251001013', N'NV251001005'),
    (@LH_u3, DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Hoàn thành', 45, DATEADD(MINUTE, 50, DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0)))), N'KH251001015', N'TC251001014', N'NV251001004'),
    (@LH_n, DATEADD(MINUTE, -20, @bayGio), N'Đang thực hiện', 15, NULL, N'KH251001009', N'TC251001010', N'NV251001004'),
    (@LH_x, DATEADD(MINUTE, 780, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã hủy', 90, NULL, N'KH251001006', N'TC251001007', N'NV251001004'),
    (@LH_f1, DATEADD(MINUTE, 540, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 65, NULL, N'KH251001007', N'TC251001008', N'NV251001004'),
    (@LH_f2, DATEADD(MINUTE, 570, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 60, NULL, N'KH251001004', N'TC251001005', N'NV251001005'),
    (@LH_f3, DATEADD(MINUTE, 840, CAST(DATEADD(DAY, 1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 90, NULL, N'KH251001002', N'TC251001003', N'NV251001004'),
    (@LH_f4, DATEADD(MINUTE, 600, CAST(DATEADD(DAY, 2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 45, NULL, N'KH251001005', N'TC251001006', N'NV251001005'),
    (@LH_f5, DATEADD(MINUTE, 900, CAST(DATEADD(DAY, 3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 45, NULL, N'KH251001003', N'TC251001004', N'NV251001004'),
    (@LH_f6, DATEADD(MINUTE, 540, CAST(DATEADD(DAY, 5, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 120, NULL, N'KH251001014', N'TC251001018', N'NV251001005'),
    (@LH_f7, DATEADD(MINUTE, 660, CAST(DATEADD(DAY, 7, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã đặt', 45, NULL, N'KH251001012', N'TC251001016', N'NV251001005');

INSERT INTO ChiTietLichHen (maLichHen, maDV, ghiChuRieng, thoiGianDuKien, trangThaiThanhToan) VALUES
    (@LH_a, N'DV251001001', NULL, 45, 1),
    (@LH_b, N'DV251001001', NULL, 45, 1),
    (@LH_b, N'DV251001007', NULL, 30, 1),
    (@LH_c, N'DV251001001', NULL, 45, 1),
    (@LH_u1, N'DV251001003', N'Chó nhỏ, giữ chắc tay', 15, 0),
    (@LH_u2, N'DV251001001', NULL, 45, 0),
    (@LH_u2, N'DV251001002', NULL, 60, 0),
    (@LH_u3, N'DV251001001', NULL, 45, 0),
    (@LH_n, N'DV251001003', NULL, 15, 0),
    (@LH_x, N'DV251001005', NULL, 90, 0),
    (@LH_f1, N'DV251001001', NULL, 45, 0),
    (@LH_f1, N'DV251001004', NULL, 20, 0),
    (@LH_f2, N'DV251001002', NULL, 60, 0),
    (@LH_f3, N'DV251001005', N'Khách yêu cầu dùng tinh dầu', 90, 0),
    (@LH_f4, N'DV251001001', NULL, 45, 0),
    (@LH_f5, N'DV251001003', NULL, 15, 0),
    (@LH_f5, N'DV251001007', NULL, 30, 0),
    (@LH_f6, N'DV251001008', NULL, 120, 0),
    (@LH_f7, N'DV251001001', NULL, 45, 0);

INSERT INTO LichSuChamSoc (maLS, canNang, ghiChu, maLichHen) VALUES
    (@LS_a, 3.1, N'Mèo khỏe, hơi sợ nước', @LH_a),
    (@LS_b, 30.1, NULL, @LH_b),
    (@LS_c, 20.9, N'Lông dày, cần sấy lâu hơn', @LH_c),
    (@LS_u1, 2.3, N'Chó nhỏ, hơi run khi cắt móng', @LH_u1),
    (@LS_u2, 32.9, NULL, @LH_u2),
    (@LS_u3, 4.4, N'Lông dài, dễ rối', @LH_u3);

-- Hoa dong: 1 = hom nay, co the tra; 2 = hom nay, co dich vu; 3 = hom qua, chi san pham; 4 = 2 ngay truoc, dich vu + san pham; 5 = 3 ngay truoc, co phieu CHO XAC NHAN; 6 = 4 ngay truoc, co phieu DA DUYET (cho hoan tien); 7 = 5 ngay truoc, da tra mot phan (Pate Pedigree: mua 4, tra 2); 8 = 6 ngay truoc, con it ngay de tra; 9 = cach day 164 gio -> CON KHOANG 4 GIO de tra; 10 = cach day 200 gio -> HET HAN tra hang; 11 = 9 ngay truoc (het han); 12 = 10 ngay truoc (het han); 13 = 12 ngay truoc (het han); 14 = 13 ngay truoc, hoa don lon (het han)
INSERT INTO HoaDon (maHD, ngayLap, trangThai, phuongThucTT, tienKhachDua, tongTien, tienThua, maKH, maNV) VALUES
    (@HD_1, DATEADD(MINUTE, -90, @bayGio), N'Đã thanh toán', N'Tiền mặt', 200000, 185000, 15000, N'KH251001009', N'NV251001002'),
    (@HD_2, DATEADD(MINUTE, -40, @bayGio), N'Đã thanh toán', N'Chuyển khoản', NULL, 210000, NULL, N'KH251001010', N'NV251001003'),
    (@HD_3, DATEADD(MINUTE, 680, CAST(DATEADD(DAY, -1, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 300000, 261000, 39000, N'KH251001015', N'NV251001002'),
    (@HD_4, DATEADD(MINUTE, 965, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Chuyển khoản', NULL, 308000, NULL, N'KH251001012', N'NV251001003'),
    (@HD_5, DATEADD(MINUTE, 640, CAST(DATEADD(DAY, -3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 300000, 252000, 48000, N'KH251001014', N'NV251001002'),
    (@HD_6, DATEADD(MINUTE, 1035, CAST(DATEADD(DAY, -4, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Chuyển khoản', NULL, 275000, NULL, N'KH251001005', N'NV251001003'),
    (@HD_7, DATEADD(MINUTE, 750, CAST(DATEADD(DAY, -5, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 500000, 481000, 19000, N'KH251001003', N'NV251001002'),
    (@HD_8, DATEADD(MINUTE, 1125, CAST(DATEADD(DAY, -6, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 300000, 270000, 30000, N'KH251001002', N'NV251001003'),
    (@HD_9, DATEADD(HOUR, -164, @bayGio), N'Đã thanh toán', N'Chuyển khoản', NULL, 190000, NULL, N'KH251001008', N'NV251001002'),
    (@HD_10, DATEADD(HOUR, -200, @bayGio), N'Đã thanh toán', N'Tiền mặt', 200000, 159000, 41000, N'KH251001006', N'NV251001003'),
    (@HD_11, DATEADD(MINUTE, 610, CAST(DATEADD(DAY, -9, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Chuyển khoản', NULL, 150000, NULL, N'KH251001007', N'NV251001002'),
    (@HD_12, DATEADD(MINUTE, 920, CAST(DATEADD(DAY, -10, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 350000, 308000, 42000, N'KH251001001', N'NV251001003'),
    (@HD_13, DATEADD(MINUTE, 590, CAST(DATEADD(DAY, -12, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Tiền mặt', 150000, 135000, 15000, N'KH251001004', N'NV251001002'),
    (@HD_14, DATEADD(MINUTE, 865, CAST(DATEADD(DAY, -13, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã thanh toán', N'Chuyển khoản', NULL, 890000, NULL, N'KH251001010', N'NV251001003');

INSERT INTO ChiTietHoaDon_SanPham (maHD, maSP, soLuong, donGia) VALUES
    (@HD_1, N'SP251001003', 2, 45000),
    (@HD_1, N'SP251001010', 1, 95000),
    (@HD_2, N'SP251001004', 1, 60000),
    (@HD_3, N'SP251001014', 2, 98000),
    (@HD_3, N'SP251001019', 1, 65000),
    (@HD_4, N'SP251001020', 1, 78000),
    (@HD_5, N'SP251001002', 4, 28000),
    (@HD_5, N'SP251001009', 1, 140000),
    (@HD_6, N'SP251001006', 1, 85000),
    (@HD_6, N'SP251001007', 1, 120000),
    (@HD_6, N'SP251001011', 1, 70000),
    (@HD_7, N'SP251001001', 1, 385000),
    (@HD_7, N'SP251001015', 4, 24000),
    (@HD_8, N'SP251001008', 1, 150000),
    (@HD_8, N'SP251001004', 2, 60000),
    (@HD_9, N'SP251001010', 2, 95000),
    (@HD_10, N'SP251001005', 1, 55000),
    (@HD_10, N'SP251001016', 2, 52000),
    (@HD_12, N'SP251001009', 1, 140000),
    (@HD_12, N'SP251001002', 6, 28000),
    (@HD_13, N'SP251001003', 3, 45000),
    (@HD_14, N'SP251001018', 1, 890000);

INSERT INTO ChiTietHoaDon_DichVu (maHD, maLichHen, maDV, donGia) VALUES
    (@HD_2, @LH_a, N'DV251001001', 150000),
    (@HD_4, @LH_b, N'DV251001001', 150000),
    (@HD_4, @LH_b, N'DV251001007', 80000),
    (@HD_11, @LH_c, N'DV251001001', 150000);

-- Phieu dong: 1 Cho xac nhan (hoa don 5), 1 Da hoan tat (hoa don 7), 1 Da duyet cho hoan tien (hoa don 6)
INSERT INTO PhieuTraHang (maPhieuTra, ngayLap, trangThai, tongTienDuKien, soTienDuocDuyet, soTienThucTra, lyDoTuChoi, thoiDiemHoanTat, lyDoGiamHoan, maHD, nguoiLap, nguoiDuyet) VALUES
    (@PT_1, DATEADD(MINUTE, 840, CAST(DATEADD(DAY, -2, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Chờ xác nhận', 28000, NULL, NULL, NULL, NULL, NULL, @HD_5, N'NV251001003', NULL),
    (@PT_2, DATEADD(MINUTE, 600, CAST(DATEADD(DAY, -4, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã hoàn tất', 48000, 48000, 48000, NULL, DATEADD(MINUTE, 930, CAST(DATEADD(DAY, -4, CAST(@bayGio AS DATE)) AS DATETIME2(0))), NULL, @HD_7, N'NV251001002', N'NV251001001'),
    (@PT_3, DATEADD(MINUTE, 570, CAST(DATEADD(DAY, -3, CAST(@bayGio AS DATE)) AS DATETIME2(0))), N'Đã duyệt', 85000, 85000, NULL, NULL, NULL, NULL, @HD_6, N'NV251001003', N'NV251001001');

INSERT INTO ChiTietPhieuTraHang (maPhieuTra, maHD, maSP, tinhTrang, soLuongTra, lyDoTra, donGia) VALUES
    (@PT_1, @HD_5, N'SP251001002', N'Lỗi', 1, N'Lon bị móp, rò rỉ', 28000),
    (@PT_2, @HD_7, N'SP251001015', N'Nguyên vẹn', 2, N'Khách mua dư', 24000),
    (@PT_3, @HD_6, N'SP251001006', N'Nguyên vẹn', 1, N'Không vừa cổ thú cưng', 85000);

COMMIT TRANSACTION;
GO

/* ======================================================================
   PHAN 6: TRUY VAN KIEM TRA + KET QUA MONG DOI
   Cac truy van Q0-Q5 chi tinh PHAN CO DINH (ma ket thuc < 900), nen ket qua khong doi theo ngay chay.
   ====================================================================== */

-- Q0. Tong quan phan co dinh. Mong doi: soHoaDon = 43, soPhieuTra = 6, soLichHen = 21
SELECT (SELECT COUNT(*) FROM HoaDon WHERE RIGHT(maHD, 3) < '900') AS soHoaDon,
       (SELECT COUNT(*) FROM PhieuTraHang WHERE RIGHT(maPhieuTra, 3) < '900') AS soPhieuTra,
       (SELECT COUNT(*) FROM LichHen WHERE RIGHT(maLichHen, 3) < '900') AS soLichHen;
GO

/* Q1. DOANH THU THEO THANG = ban san pham - tien hoan (tinh vao THANG HOAN TAT phieu) + dich vu
       thang | banSP | dichVu | tienHoan | doanhThu
       2025-11 | 828.000 | 390.000 | 0 | 1.218.000
       2025-12 | 1.638.000 | 200.000 | 0 | 1.838.000
       2026-01 | 170.000 | 150.000 | 0 | 320.000
       2026-02 | 350.000 | 350.000 | 0 | 700.000
       2026-03 | 810.000 | 370.000 | 0 | 1.180.000
       2026-04 | 474.000 | 280.000 | 95.000 | 659.000
       2026-05 | 410.000 | 150.000 | 0 | 560.000
       2026-06 | 1.155.000 | 620.000 | 0 | 1.775.000
       2026-07 | 454.000 | 150.000 | 0 | 604.000
       2026-08 | 1.165.000 | 410.000 | 0 | 1.575.000
       2026-09 | 1.644.000 | 370.000 | 0 | 2.014.000
       2026-10 | 3.196.000 | 350.000 | 230.000 | 3.316.000
       Luu y: hoa don 29/09 co phieu tra hoan tat ngay 03/10 hoan 150.000 -> tien hoan tinh vao THANG 10. */
SELECT thang, SUM(tienSP) AS banSP, SUM(tienDV) AS dichVu, SUM(tienHoan) AS tienHoan,
       SUM(tienSP) - SUM(tienHoan) + SUM(tienDV) AS doanhThu
FROM (
    SELECT FORMAT(h.ngayLap, 'yyyy-MM') AS thang, SUM(c.thanhTien) AS tienSP, 0 AS tienDV, 0 AS tienHoan
    FROM HoaDon h JOIN ChiTietHoaDon_SanPham c ON c.maHD = h.maHD
    WHERE RIGHT(h.maHD, 3) < '900'
    GROUP BY FORMAT(h.ngayLap, 'yyyy-MM')
    UNION ALL
    SELECT FORMAT(h.ngayLap, 'yyyy-MM'), 0, SUM(d.thanhTien), 0
    FROM HoaDon h JOIN ChiTietHoaDon_DichVu d ON d.maHD = h.maHD
    WHERE RIGHT(h.maHD, 3) < '900'
    GROUP BY FORMAT(h.ngayLap, 'yyyy-MM')
    UNION ALL
    SELECT FORMAT(p.thoiDiemHoanTat, 'yyyy-MM'), 0, 0, SUM(p.soTienThucTra)
    FROM PhieuTraHang p
    WHERE p.trangThai = N'Đã hoàn tất' AND RIGHT(p.maPhieuTra, 3) < '900'
    GROUP BY FORMAT(p.thoiDiemHoanTat, 'yyyy-MM')
) x
GROUP BY thang
ORDER BY thang;
GO

/* Q2. DOANH THU THEO NAM (cong cac thang o Q1)
       nam | banSP | dichVu | tienHoan | doanhThu
       2025 | 2.466.000 | 590.000 | 0 | 3.056.000
       2026 | 9.828.000 | 3.200.000 | 325.000 | 12.703.000
       Truy van duoi day tra ve tong ban hang chua tru hoan: 2025 = 3.056.000; 2026 = 13.028.000 */
SELECT YEAR(h.ngayLap) AS nam, SUM(h.tongTien) AS tongBanHang_chuaTruHoan
FROM HoaDon h
WHERE RIGHT(h.maHD, 3) < '900'
GROUP BY YEAR(h.ngayLap)
ORDER BY nam;
GO

/* Q3. DOANH THU THEO NGAY TRONG THANG 10/2026 (ngay | banSP | dichVu | tienHoan)
       10/10 | 994.000 | 0 | 0
       20/10 | 244.000 | 0 | 0
       21/10 | 260.000 | 0 | 0
       02/10 | 445.000 | 0 | 0
       12/10 | 565.000 | 0 | 0
       03/10 | 0 | 0 | 150.000
       14/10 | 228.000 | 0 | 80.000
       15/10 | 0 | 200.000 | 0
       08/10 | 140.000 | 150.000 | 0
       18/10 | 320.000 | 0 | 0
       Truy van chi liet ke ngay co hoa don. Tien hoan nam o phieu tra hang (03/10 hoan 150.000, 14/10 hoan 80.000). */
SELECT CAST(h.ngayLap AS DATE) AS ngay, SUM(h.tongTien) AS tongBan
FROM HoaDon h
WHERE RIGHT(h.maHD, 3) < '900' AND h.ngayLap >= '20261001' AND h.ngayLap < '20261101'
GROUP BY CAST(h.ngayLap AS DATE)
ORDER BY ngay;
GO

/* Q4. SO LUONG SAN PHAM BAN RA (tong, chua tru hang tra). Mong doi:
       Pate Whiskas cho mèo | 39
       Cát vệ sinh cho mèo 5L | 11
       Xương gặm sạch răng | 11
       Hạt Royal Canin cho chó 2kg | 10
       Sữa tắm khử mùi 500ml | 6
       Bóng cao su phát tiếng | 6
       Vòng cổ có chuông | 6
       Áo thú cưng size M | 5
       Bánh thưởng cho chó | 5
       Pate Pedigree cho chó | 5
       Dây dắt chó 1.5m | 4
       Cần câu mèo lông vũ | 4
       Lược chải lông | 4
       Hạt Me-O cho mèo 1.2kg | 3
       Gặm xương da bò | 3
       Bát ăn inox | 2
       Thuốc xịt khử mùi | 2
       Chuồng nhựa size M | 1
       Nhà cây cho mèo | 1 */
SELECT s.maSP, s.tenSP, SUM(c.soLuong) AS soLuongBan
FROM ChiTietHoaDon_SanPham c
JOIN HoaDon h ON h.maHD = c.maHD
JOIN SanPham s ON s.maSP = c.maSP
WHERE RIGHT(h.maHD, 3) < '900'
GROUP BY s.maSP, s.tenSP
ORDER BY soLuongBan DESC, s.tenSP;
GO

/* Q5. LUOT SU DUNG DICH VU (dem tu lich hen HOAN THANH theo thoiDiemHoanThanh, KHONG tinh tu hoa don). Mong doi:
       Tắm thú cưng | 13
       Cắt móng | 6
       Cắt tỉa lông | 5
       Vệ sinh tai | 2
       Spa thư giãn | 2
       Chải lông gỡ rối | 1
       Lich "Da huy" khong duoc tinh; dich vu da hoan thanh nhung chua thanh toan VAN duoc tinh. */
SELECT d.maDV, d.tenDV, COUNT(*) AS luotSuDung
FROM ChiTietLichHen c
JOIN LichHen l ON l.maLichHen = c.maLichHen AND l.trangThai = N'Hoàn thành'
JOIN DichVu d ON d.maDV = c.maDV
WHERE RIGHT(l.maLichHen, 3) < '900'
GROUP BY d.maDV, d.tenDV
ORDER BY luotSuDung DESC, d.tenDV;
GO

/* ======================================================================
   Q6 - Q14: KIEM TRA CAC CHUC NANG KHAC (tinh ca du lieu dong)
   ====================================================================== */
-- Q6. Tra cuu hoa don: danh sach kem khach hang, thu ngan, so dong, so phieu tra
SELECT h.maHD, h.ngayLap, kh.hoTen AS khachHang, nv.hoTen AS thuNgan, h.phuongThucTT, h.tongTien,
       (SELECT COUNT(*) FROM ChiTietHoaDon_SanPham c WHERE c.maHD = h.maHD) AS dongSanPham,
       (SELECT COUNT(*) FROM ChiTietHoaDon_DichVu d WHERE d.maHD = h.maHD) AS dongDichVu,
       (SELECT COUNT(*) FROM PhieuTraHang p WHERE p.maHD = h.maHD) AS soPhieuTra
FROM HoaDon h
JOIN KhachHang kh ON kh.maKH = h.maKH
JOIN NhanVien nv ON nv.maNV = h.maNV
ORDER BY h.ngayLap DESC;
GO

/* Q7. DICH VU DA HOAN THANH NHUNG CHUA THANH TOAN (nguon cho man hinh Ban hang).
       Mong doi: 7 dong (phan co dinh: Ngô Đức Anh, Phạm Quốc Việt) + 4 dong dong (hom nay va hom qua; Le Huu Phuoc co 2 lich, 3 dong) */
SELECT l.maLichHen, kh.hoTen, d.tenDV, c.trangThaiThanhToan
FROM ChiTietLichHen c
JOIN LichHen l ON l.maLichHen = c.maLichHen
JOIN KhachHang kh ON kh.maKH = l.maKH
JOIN DichVu d ON d.maDV = c.maDV
WHERE l.trangThai = N'Hoàn thành' AND c.trangThaiThanhToan = 0
ORDER BY kh.hoTen, l.thoiDiemHoanThanh;
GO

/* Q8. PHIEU TRA HANG THEO TRANG THAI. Mong doi: Chờ xác nhận 2, Đã duyệt 2, Đã hoàn tất 4, Đã từ chối 1 */
SELECT trangThai, COUNT(*) AS soPhieu FROM PhieuTraHang GROUP BY trangThai;
GO

/* Q9. HOA DON CON HAN TRA HANG (168 gio ke tu luc thanh toan).
       Mong doi (chay ngay luc nap du lieu): hoa don dong 1..8 con han, hoa don dong 9 con khoang 4 gio;
       hoa don dong 10..14 da het han. Neu chay truoc 28/10/2026 se thay them vai hoa don co dinh 18-21/10. */
SELECT h.maHD, h.ngayLap, kh.hoTen, h.tongTien,
       168 - DATEDIFF(HOUR, h.ngayLap, SYSDATETIME()) AS soGioConLai
FROM HoaDon h JOIN KhachHang kh ON kh.maKH = h.maKH
WHERE DATEDIFF(SECOND, h.ngayLap, SYSDATETIME()) <= 168 * 3600
ORDER BY h.ngayLap DESC;
GO

/* Q10. SAN PHAM SAP HET HANG (ton <= 5, dang kinh doanh). Mong doi: Bánh thưởng cho chó (0); Pate Pedigree cho chó (3); Nhà cây cho mèo (4) */
SELECT maSP, tenSP, soLuongTon FROM SanPham WHERE soLuongTon <= 5 AND trangThai = 1 ORDER BY soLuongTon;
GO

/* Q11. LICH HEN SAP TOI (Da dat, tu bay gio tro di). Mong doi: 7 lich (ngay mai: 3 lich, +2, +3, +5, +7 ngay) */
SELECT l.maLichHen, l.ngayGioHen, l.tongThoiGianDuKien AS phut, nv.hoTen AS nhanVien, kh.hoTen AS khachHang
FROM LichHen l JOIN NhanVien nv ON nv.maNV = l.maNV JOIN KhachHang kh ON kh.maKH = l.maKH
WHERE l.trangThai = N'Đã đặt' AND l.ngayGioHen > SYSDATETIME()
ORDER BY l.ngayGioHen;
GO

/* Q12. TEST TRUNG GIO: cac lich NGAY MAI theo nhan vien. Mong doi 3 dong:
       Pham Thu Ha 09:00-10:05, Pham Thu Ha 14:00-15:30, Vo Minh Tuan 09:30-10:30.
       Dat them lich cho Pham Thu Ha luc 09:30 ngay mai phai bi bao TRUNG (dang ban den 10:05). */
SELECT l.maLichHen, l.ngayGioHen, DATEADD(MINUTE, l.tongThoiGianDuKien, l.ngayGioHen) AS gioKetThuc, nv.hoTen
FROM LichHen l JOIN NhanVien nv ON nv.maNV = l.maNV
WHERE l.trangThai IN (N'Đã đặt', N'Đang thực hiện')
  AND CAST(l.ngayGioHen AS DATE) = DATEADD(DAY, 1, CAST(SYSDATETIME() AS DATE))
ORDER BY nv.hoTen, l.ngayGioHen;
GO

/* Q13. LICH SU CAN NANG CUA MUC (Poodle). Mong doi: 4.75, 4.90, 5.05, 5.20 (4 dong theo thu tu thoi gian) */
SELECT l.thoiDiemHoanThanh, s.canNang, s.ghiChu
FROM LichSuChamSoc s JOIN LichHen l ON l.maLichHen = s.maLichHen
JOIN ThuCung t ON t.maThuCung = l.maThuCung
WHERE t.tenThuCung = N'Mực' AND t.loai = N'Chó'
ORDER BY l.thoiDiemHoanThanh;
GO

-- Q14. Tai khoan dang nhap (mat khau chung PetBite@123). Mong doi: 6 tai khoan, thungan3 co trangThai = 0
SELECT t.tenDangNhap, n.hoTen, n.vaiTro, t.trangThai FROM TaiKhoan t JOIN NhanVien n ON n.maNV = t.maNV ORDER BY n.vaiTro, t.tenDangNhap;
GO

/* Neu nhom dung thuat toan bam KHAC SHA-256, doi mat khau mau bang cau nay (thay <chuoi bam cua PetBite@123>):
   UPDATE TaiKhoan SET matKhau = '<chuoi bam cua PetBite@123>'; */
