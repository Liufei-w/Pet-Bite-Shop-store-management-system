package entity;

import java.math.BigDecimal;

public class DichVu {
	private String maDichVu;
	private String tenDichVu;
	private BigDecimal giaDichVu;
	private int thoiGianThucHien;
	private String moTa;
	private String trangThai;
	public DichVu(String maDichVu, String tenDichVu, BigDecimal giaDichVu, int thoiGianThucHien, String moTa,
			String trangThai) {
		super();
		this.maDichVu = maDichVu;
		this.tenDichVu = tenDichVu;
		this.giaDichVu = giaDichVu;
		this.thoiGianThucHien = thoiGianThucHien;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}
	public DichVu() {
		super();
	}
	public String getMaDichVu() {
		return maDichVu;
	}
	public void setMaDichVu(String maDichVu) {
		this.maDichVu = maDichVu;
	}
	public String getTenDichVu() {
		return tenDichVu;
	}
	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}
	public BigDecimal getGiaDichVu() {
		return giaDichVu;
	}
	public void setGiaDichVu(BigDecimal giaDichVu) {
		this.giaDichVu = giaDichVu;
	}
	public int getThoiGianThucHien() {
		return thoiGianThucHien;
	}
	public void setThoiGianThucHien(int thoiGianThucHien) {
		this.thoiGianThucHien = thoiGianThucHien;
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
	@Override
	public String toString() {
		return "DichVu [maDichVu=" + maDichVu + ", tenDichVu=" + tenDichVu + ", giaDichVu=" + giaDichVu
				+ ", thoiGianThucHien=" + thoiGianThucHien + ", moTa=" + moTa + ", trangThai=" + trangThai + "]";
	}
	
	

}
