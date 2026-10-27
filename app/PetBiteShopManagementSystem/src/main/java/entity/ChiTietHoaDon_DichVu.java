package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon_DichVu {
	private BigDecimal donGia;

	public ChiTietHoaDon_DichVu(BigDecimal donGia) {
		super();
		this.donGia = donGia;
	}

	public ChiTietHoaDon_DichVu() {
		super();
	}

	public BigDecimal getThanhTien() {
		return donGia;
	}

	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}
	
	

}
