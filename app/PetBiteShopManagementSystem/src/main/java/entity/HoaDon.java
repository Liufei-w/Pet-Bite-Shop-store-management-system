package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayLap;
	private String trangThai;
	private String phuongThucThanhToan;
	private BigDecimal tienKhachDua;
	private BigDecimal tienThua;
	private BigDecimal tongTien;
	public HoaDon(String maHoaDon, LocalDateTime ngayLap, String trangThai, String phuongThucThanhToan,
			BigDecimal tienKhachDua, BigDecimal tienThua, BigDecimal tongTien) {
		super();
		setMaHoaDon(maHoaDon);
		setNgayLap(ngayLap);
		setTrangThai(trangThai);
		setPhuongThucThanhToan(phuongThucThanhToan);
		setTienKhachDua(tienKhachDua);
		setTienThua(tienThua);
		setTongTien(tongTien);
	}
	public HoaDon() {
		super();
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public LocalDateTime getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDateTime ngayLap) {
		this.ngayLap = ngayLap;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}
	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}
	public BigDecimal getTienKhachDua() {
		return tienKhachDua;
	}
	public void setTienKhachDua(BigDecimal tienKhachDua) {
		this.tienKhachDua = tienKhachDua;
	}
	public BigDecimal getTienThua() {
		return tienThua;
	}
	public void setTienThua(BigDecimal tienThua) {
		this.tienThua = tienThua;
	}
	public BigDecimal getTongTien() {
		return tongTien;
	}
	public void setTongTien( BigDecimal tongTien) {
		this.tongTien = tongTien;
	}
	
	
}
