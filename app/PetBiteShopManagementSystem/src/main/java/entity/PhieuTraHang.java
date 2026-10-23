package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuTraHang {
	private String maPhieuTra;
	private LocalDate ngayLap;
	private String trangThai;
	private BigDecimal tongTienDuKien;
	private BigDecimal soTienDuocDuyet;
	private BigDecimal soTienThucTra;
	private String lyDoTuChoi;
	private LocalDateTime thoiDiemHoanTat;
	public PhieuTraHang(String maPhieuTra, LocalDate ngayLap, String trangThai, BigDecimal tongTienDuKien,
			BigDecimal soTienDuocDuyet, BigDecimal soTienThucTra, String lyDoTuChoi, LocalDateTime thoiDiemHoanTat) {
		super();
		setMaPhieuTra(maPhieuTra);
		setNgayLap(ngayLap);
		setTrangThai(trangThai);
		setTongTienDuKien(tongTienDuKien);
		setSoTienDuocDuyet(soTienDuocDuyet);
		setSoTienThucTra(soTienThucTra);
		setLyDoTuChoi(lyDoTuChoi);
		setThoiDiemHoanTat(thoiDiemHoanTat);
	}
	public PhieuTraHang(String maPhieuTra) {
		super();
		this.maPhieuTra = maPhieuTra;
	}
	public String getMaPhieuTra() {
		return maPhieuTra;
	}
	public void setMaPhieuTra(String maPhieuTra) {
		this.maPhieuTra = maPhieuTra;
	}
	public LocalDate getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public BigDecimal getTongTienDuKien() {
		return tongTienDuKien;
	}
	public void setTongTienDuKien(BigDecimal tongTienDuKien) {
		this.tongTienDuKien = tongTienDuKien;
	}
	public BigDecimal getSoTienDuocDuyet() {
		return soTienDuocDuyet;
	}
	public void setSoTienDuocDuyet(BigDecimal soTienDuocDuyet) {
		this.soTienDuocDuyet = soTienDuocDuyet;
	}
	public BigDecimal getSoTienThucTra() {
		return soTienThucTra;
	}
	public void setSoTienThucTra(BigDecimal soTienThucTra) {
		this.soTienThucTra = soTienThucTra;
	}
	public String getLyDoTuChoi() {
		return lyDoTuChoi;
	}
	public void setLyDoTuChoi(String lyDoTuChoi) {
		this.lyDoTuChoi = lyDoTuChoi;
	}
	public LocalDateTime getThoiDiemHoanTat() {
		return thoiDiemHoanTat;
	}
	public void setThoiDiemHoanTat(LocalDateTime thoiDiemHoanTat) {
		this.thoiDiemHoanTat = thoiDiemHoanTat;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maPhieuTra);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuTraHang other = (PhieuTraHang) obj;
		return Objects.equals(maPhieuTra, other.maPhieuTra);
	}
	@Override
	public String toString() {
		return "PhieuTraHang [maPhieuTra=" + maPhieuTra + ", ngayLap=" + ngayLap + ", trangThai=" + trangThai
				+ ", tongTienDuKien=" + tongTienDuKien + ", soTienDuocDuyet=" + soTienDuocDuyet + ", soTienThucTra="
				+ soTienThucTra + ", lyDoTuChoi=" + lyDoTuChoi + ", thoiDiemHoanTat=" + thoiDiemHoanTat + "]";
	}
	
	
}
