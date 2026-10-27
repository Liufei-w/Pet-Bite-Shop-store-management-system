package entity;

import java.math.BigDecimal;

public class SanPham {
	private String maSanPham;
	private String tenSanPham;
	private String donViTinh;
	private BigDecimal giaBan;
	private int soLuongTon;
	private String trangThai;
	public SanPham(String maSanPham, String tenSanPham, String donViTinh, BigDecimal giaBan, int soLuongTon,
			String trangThai) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.donViTinh = donViTinh;
		this.giaBan = giaBan;
		this.soLuongTon = soLuongTon;
		this.trangThai = trangThai;
	}
	public SanPham() {
		super();
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public String getDonViTinh() {
		return donViTinh;
	}
	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}
	public BigDecimal getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(BigDecimal giaBan) {
		this.giaBan = giaBan;
	}
	public int getSoLuongTon() {
		return soLuongTon;
	}
	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	
}
