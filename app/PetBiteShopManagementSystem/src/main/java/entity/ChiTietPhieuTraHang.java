package entity;

import java.math.BigDecimal;

public class ChiTietPhieuTraHang {
	private int soLuongTra;
	private String tinhTrang;
	private String lyDoTra;
	private BigDecimal donGia;
	private int soThuTu;
	public ChiTietPhieuTraHang(int soLuongTra, String tinhTrang, String lyDoTra, BigDecimal donGia, int soThuTu) {
		super();
		setSoLuongTra(soLuongTra);
		setTinhTrang(tinhTrang);
		setLyDoTra(lyDoTra);
		setDonGia(donGia);
		setSoThuTu(soThuTu);
		
	}
	public ChiTietPhieuTraHang() {
		super();
	}
	public int getSoLuongTra() {
		return soLuongTra;
	}
	public void setSoLuongTra(int soLuongTra) {
		this.soLuongTra = soLuongTra;
	}
	public String getTinhTrang() {
		return tinhTrang;
	}
	public void setTinhTrang(String tinhTrang) {
		this.tinhTrang = tinhTrang;
	}
	public String getLyDoTra() {
		return lyDoTra;
	}
	public void setLyDoTra(String lyDoTra) {
		this.lyDoTra = lyDoTra;
	}
	public BigDecimal getDonGia() {
		return donGia;
	}
	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}
	public int getSoThuTu() {
		return soThuTu;
	}
	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}
	
	public BigDecimal getTinhThanhTien() {
		return donGia.multiply(BigDecimal.valueOf(soLuongTra));
	}
	
	
	

}
