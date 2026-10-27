package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * - Hóa đơn chỉ được lưu khi đã thanh toán thành công nên trạng thái phải luôn là "Đã thanh toán" 
 *  và ngayLap là thời điểm thanh toán.
 * - Một hóa đơn có thể có nhiều dòng sản phẩm và nhiều dòng dịch vụ (hoặc chỉ một trong hai loại).
 */
public class HoaDon {
    public static final String DA_THANH_TOAN = "Đã thanh toán";
    public static final String TIEN_MAT = "Tiền mặt";
    public static final String CHUYEN_KHOAN = "Chuyển khoản";

    /** Hạn trả hàng: 168 giờ kể từ thời điểm thanh toán. */
    public static final int SO_GIO_HAN_TRA = 168;

    private String maHD;
    private LocalDateTime ngayLap;
    private String trangThai;
    private String phuongThucTT;
    private BigDecimal tienKhachDua;   // chỉ có khi trả tiền mặt, chuyển khoản để null
    private BigDecimal tienThua;       // tiền mặt: tienKhachDua - tongTien
    private BigDecimal tongTien;

    private KhachHang khachHang;
    private NhanVien nhanVien;         // thu ngân lập hóa đơn
    private ArrayList<ChiTietHoaDon_SanPham> dsSanPham;
    private ArrayList<ChiTietHoaDon_DichVu> dsDichVu;

    public HoaDon() {
        this.trangThai = DA_THANH_TOAN;
        this.dsSanPham = new ArrayList<>();
        this.dsDichVu = new ArrayList<>();
    }

    public HoaDon(String maHD, LocalDateTime ngayLap, String phuongThucTT, BigDecimal tienKhachDua,
                  BigDecimal tienThua, BigDecimal tongTien, KhachHang khachHang, NhanVien nhanVien) {
        this();
        setMaHD(maHD);
        setNgayLap(ngayLap);
        setPhuongThucTT(phuongThucTT);
        setTienKhachDua(tienKhachDua);
        setTienThua(tienThua);
        setTongTien(tongTien);
        setKhachHang(khachHang);
        setNhanVien(nhanVien);
    }

    public HoaDon(HoaDon hd) {
        this.maHD = hd.maHD;
        this.ngayLap = hd.ngayLap;
        this.trangThai = hd.trangThai;
        this.phuongThucTT = hd.phuongThucTT;
        this.tienKhachDua = hd.tienKhachDua;
        this.tienThua = hd.tienThua;
        this.tongTien = hd.tongTien;
        this.khachHang = hd.khachHang;
        this.nhanVien = hd.nhanVien;
        this.dsSanPham = new ArrayList<>(hd.dsSanPham);
        this.dsDichVu = new ArrayList<>(hd.dsDichVu);
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        if (maHD == null || !maHD.matches("HD\\d{9}")) {
            throw new IllegalArgumentException("Mã hóa đơn phải có dạng HDYYMMDDXXX (HD + 9 chữ số)");
        }
        this.maHD = maHD;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        if (ngayLap == null || ngayLap.isAfter(LocalDateTime.now().plusMinutes(1))) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được để trống hoặc lớn hơn thời điểm hiện tại.");
        }
        this.ngayLap = ngayLap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        if (!DA_THANH_TOAN.equals(trangThai)) {
            throw new IllegalArgumentException("Hóa đơn chỉ được lưu với trạng thái \"Đã thanh toán\".");
        }
        this.trangThai = trangThai;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        if (!TIEN_MAT.equals(phuongThucTT) && !CHUYEN_KHOAN.equals(phuongThucTT)) {
            throw new IllegalArgumentException("Phương thức thanh toán chỉ nhận: Tiền mặt hoặc Chuyển khoản.");
        }
        this.phuongThucTT = phuongThucTT;
    }

    public BigDecimal getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(BigDecimal tienKhachDua) {
        // chuyển khoản thì để null
        if (tienKhachDua != null && tienKhachDua.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tiền khách đưa không được âm.");
        }
        this.tienKhachDua = tienKhachDua;
    }

    public BigDecimal getTienThua() {
        return tienThua;
    }

    public void setTienThua(BigDecimal tienThua) {
        if (tienThua != null && tienThua.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tiền thừa không được âm.");
        }
        this.tienThua = tienThua;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền hóa đơn phải lớn hơn 0.");
        }
        this.tongTien = tongTien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        if (khachHang == null) {
            throw new IllegalArgumentException("Hóa đơn phải có khách hàng.");
        }
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Hóa đơn phải có nhân viên lập.");
        }
        this.nhanVien = nhanVien;
    }

    public ArrayList<ChiTietHoaDon_SanPham> getDsSanPham() {
        return dsSanPham;
    }

    public ArrayList<ChiTietHoaDon_DichVu> getDsDichVu() {
        return dsDichVu;
    }

    /** Thêm một dòng sản phẩm. Mỗi sản phẩm chỉ xuất hiện một dòng trong hóa đơn. */
    public void themChiTietSanPham(ChiTietHoaDon_SanPham ct) {
        if (ct == null) {
            throw new IllegalArgumentException("Dòng sản phẩm không được để trống.");
        }
        for (ChiTietHoaDon_SanPham x : dsSanPham) {
            if (x.getSanPham().getMaSP().equals(ct.getSanPham().getMaSP())) {
                throw new IllegalArgumentException("Sản phẩm này đã có trong hóa đơn, hãy sửa số lượng.");
            }
        }
        dsSanPham.add(ct);
    }

    /** Thêm một dòng dịch vụ. Mỗi dịch vụ của một lịch hẹn chỉ được thanh toán một lần. */
    public void themChiTietDichVu(ChiTietHoaDon_DichVu ct) {
        if (ct == null) {
            throw new IllegalArgumentException("Dòng dịch vụ không được để trống.");
        }
        for (ChiTietHoaDon_DichVu x : dsDichVu) {
            if (x.getMaLichHen().equals(ct.getMaLichHen())
                    && x.getDichVu().getMaDV().equals(ct.getDichVu().getMaDV())) {
                throw new IllegalArgumentException("Dịch vụ này của lịch hẹn đã có trong hóa đơn.");
            }
        }
        dsDichVu.add(ct);
    }

    // Nghiệp vụ
    /** Tổng tiền = tổng thành tiền sản phẩm + tổng thành tiền dịch vụ. */
    public BigDecimal tinhTongThanhTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon_SanPham ct : dsSanPham) {
            tong = tong.add(ct.tinhThanhTien());
        }
        for (ChiTietHoaDon_DichVu ct : dsDichVu) {
            tong = tong.add(ct.tinhThanhTien());
        }
        return tong;
    }

    /** Tính lại tổng tiền từ các dòng chi tiết và gán vào thuộc tính tongTien. */
    public void capNhatTongTien() {
        setTongTien(tinhTongThanhTien());
    }

    /** Tiền thừa = tiền khách đưa - tổng tiền (tiền mặt). Chuyển khoản trả về null. */
    public BigDecimal tinhTienThua() {
        if (!TIEN_MAT.equals(phuongThucTT) || tienKhachDua == null || tongTien == null) {
            return null;
        }
        return tienKhachDua.subtract(tongTien);
    }

    /**
     * Kiểm tra hóa đơn đủ điều kiện để lưu (gọi trước khi lưu vào CSDL).
     */
    public void kiemTraThanhToan() {
        if (dsSanPham.isEmpty() && dsDichVu.isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn phải có ít nhất một sản phẩm hoặc một dịch vụ.");
        }
        if (tongTien == null || tongTien.compareTo(tinhTongThanhTien()) != 0) {
            throw new IllegalArgumentException("Tổng tiền không khớp với các dòng chi tiết.");
        }
        if (phuongThucTT == null) {
            throw new IllegalArgumentException("Chưa chọn phương thức thanh toán.");
        }
        if (TIEN_MAT.equals(phuongThucTT)) {
            if (tienKhachDua == null || tienKhachDua.compareTo(tongTien) < 0) {
                throw new IllegalArgumentException("Tiền khách đưa chưa đủ để thanh toán.");
            }
        } else if (tienKhachDua != null) {
            throw new IllegalArgumentException("Thanh toán chuyển khoản không nhập tiền khách đưa.");
        }
    }

    /** Còn hạn trả hàng không: chưa quá 168 giờ kể từ thời điểm thanh toán (ngayLap). */
    public boolean kiemTraConHanTra() {
        if (ngayLap == null) {
            return false;
        }
        return !LocalDateTime.now().isAfter(ngayLap.plusHours(SO_GIO_HAN_TRA));
    }

    @Override
    public String toString() {
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String ngay = (ngayLap == null) ? "?" : ngayLap.format(dinhDang);
        String kh = (khachHang == null) ? "?" : khachHang.getHoTen();
        String nv = (nhanVien == null) ? "?" : nhanVien.getHoTen();
        return "HoaDon [maHD=" + maHD + ", ngayLap=" + ngay + ", trangThai=" + trangThai
                + ", phuongThucTT=" + phuongThucTT + ", tienKhachDua=" + tienKhachDua
                + ", tienThua=" + tienThua + ", tongTien=" + tongTien
                + ", khachHang=" + kh + ", nhanVien=" + nv
                + ", soDongSanPham=" + dsSanPham.size() + ", soDongDichVu=" + dsDichVu.size() + "]";
    }
}