package entity;

/**
 * Loại sản phẩm. trangThai là trạng thái nghiệp vụ, không phải nhãn trên giao diện.
 * true: đang hoạt động, false: ngừng hoạt động.
 */
public class LoaiSanPham {

    private String maLoaiSanPham;
    private String tenLoai;
    private String moTa;
    private boolean trangThai;

    public LoaiSanPham() {
        this.trangThai = true;
    }

    public LoaiSanPham(String maLoaiSanPham, String tenLoai, String moTa, boolean trangThai) {
        setMaLoaiSanPham(maLoaiSanPham);
        setTenLoai(tenLoai);
        setMoTa(moTa);
        setTrangThai(trangThai);
    }

    public String getMaLoaiSanPham() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSanPham(String maLoaiSanPham) {
        if (maLoaiSanPham == null || !maLoaiSanPham.matches("LSP\\d{9}")) {
            throw new IllegalArgumentException("Mã loại sản phẩm phải có dạng LSPYYMMDDXXX (LSP + 9 chữ số).");
        }
        this.maLoaiSanPham = maLoaiSanPham;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        if (tenLoai == null || tenLoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại sản phẩm không được để trống.");
        }
        this.tenLoai = tenLoai.trim();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty()) {
            this.moTa = null;
            return;
        }
        String noiDung = moTa.trim();
        if (noiDung.length() > 255) {
            throw new IllegalArgumentException("Mô tả không được dài quá 255 ký tự.");
        }
        this.moTa = noiDung;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "LoaiSanPham [maLoaiSanPham=" + maLoaiSanPham + ", tenLoai=" + tenLoai
                + ", moTa=" + moTa + ", trangThai=" + (trangThai ? "Đang hoạt động" : "Ngừng hoạt động") + "]";
    }
}
