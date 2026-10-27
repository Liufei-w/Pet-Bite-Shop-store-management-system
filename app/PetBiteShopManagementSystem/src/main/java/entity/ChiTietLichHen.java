package entity;

public class ChiTietLichHen {
	private String ghiChuRieng;
	private int thoiGianDuKien;
	public ChiTietLichHen(String ghiChuRieng, int thoiGianDuKien) {
		super();
		this.ghiChuRieng = ghiChuRieng;
		this.thoiGianDuKien = thoiGianDuKien;
	}
	public ChiTietLichHen() {
		super();
	}
	public String getGhiChuRieng() {
		return ghiChuRieng;
	}
	public void setGhiChuRieng(String ghiChuRieng) {
		this.ghiChuRieng = ghiChuRieng;
	}
	public int getThoiGianDuKien() {
		return thoiGianDuKien;
	}
	public void setThoiGianDuKien(int thoiGianDuKien) {
		this.thoiGianDuKien = thoiGianDuKien;
	}
	
	

}
