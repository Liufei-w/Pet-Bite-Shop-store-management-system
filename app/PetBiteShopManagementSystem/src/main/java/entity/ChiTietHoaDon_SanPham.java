package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon_SanPham {
	private int soLuong;
	private BigDecimal donGia;
	public ChiTietHoaDon_SanPham(int soLuong, BigDecimal donGia) {
		super();
		this.soLuong = soLuong;
		this.donGia = donGia;
	}
	public ChiTietHoaDon_SanPham() {
		super();
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public BigDecimal getDonGia() {
		return donGia;
	}
	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}
	public BigDecimal thanhTien() {
		return donGia.multiply(BigDecimal.valueOf(soLuong));
	}

}
