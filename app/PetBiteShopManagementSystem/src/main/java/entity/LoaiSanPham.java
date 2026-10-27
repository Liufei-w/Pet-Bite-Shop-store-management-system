package entity;

public class LoaiSanPham {
	private String maLoaiSanPham;
	private String tenLoai;
	private String moTa;
	private String trangThai;
	public LoaiSanPham(String maLoaiSanPham, String tenLoai, String moTa, String trangThai) {
		super();
		this.maLoaiSanPham = maLoaiSanPham;
		this.tenLoai = tenLoai;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}
	public LoaiSanPham() {
		super();
	}
	public String getMaLoaiSanPham() {
		return maLoaiSanPham;
	}
	public void setMaLoaiSanPham(String maLoaiSanPham) {
		this.maLoaiSanPham = maLoaiSanPham;
	}
	public String getTenLoai() {
		return tenLoai;
	}
	public void setTenLoai(String tenLoai) {
		this.tenLoai = tenLoai;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	
}
