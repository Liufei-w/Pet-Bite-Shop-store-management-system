package entity;

import java.time.LocalDateTime;

public class LichHen {
	private String maLichHen;
	private LocalDateTime ngayGioHen;
	private String trangThai;
	public LichHen(String maLichHen, LocalDateTime ngayGioHen, String trangThai) {
		super();
		this.maLichHen = maLichHen;
		this.ngayGioHen = ngayGioHen;
		this.trangThai = trangThai;
	}
	public LichHen() {
		super();
	}
	public String getMaLichHen() {
		return maLichHen;
	}
	public void setMaLichHen(String maLichHen) {
		this.maLichHen = maLichHen;
	}
	public LocalDateTime getNgayGioHen() {
		return ngayGioHen;
	}
	public void setNgayGioHen(LocalDateTime ngayGioHen) {
		this.ngayGioHen = ngayGioHen;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	

}
