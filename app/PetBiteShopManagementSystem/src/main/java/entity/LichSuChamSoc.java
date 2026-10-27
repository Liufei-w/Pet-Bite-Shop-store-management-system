package entity;

public class LichSuChamSoc {
	private String maLichSuChamSoc;
	private double canNang;
	private String ghiChu;
	public LichSuChamSoc(String maLichSuChamSoc, double canNang, String ghiChu) {
		super();
		this.maLichSuChamSoc = maLichSuChamSoc;
		this.canNang = canNang;
		this.ghiChu = ghiChu;
	}
	public LichSuChamSoc() {
		super();
	}
	public String getMaLichSuChamSoc() {
		return maLichSuChamSoc;
	}
	public void setMaLichSuChamSoc(String maLichSuChamSoc) {
		this.maLichSuChamSoc = maLichSuChamSoc;
	}
	public double getCanNang() {
		return canNang;
	}
	public void setCanNang(double canNang) {
		this.canNang = canNang;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	

}
