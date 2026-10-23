package entity;

import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
	
	private String maNhanVien;
	private String ho;
	private String ten;
	private String gioiTinh;
	private LocalDate ngaySinh;
	private String soDienThoai;
	private String diaChi;
	private String chucVu;
	private String trangThai;
	
	public NhanVien(String maNhanVien, String ho, String ten, String gioiTinh, LocalDate ngaySinh, String soDienThoai,
			String diaChi, String chucVu, String trangThai) {
		setMaNhanVien(maNhanVien);
		setHo(ho);
		setTen(ten);
		setGioiTinh(gioiTinh);
		setNgaySinh(ngaySinh);
		setSoDienThoai(soDienThoai);
		setDiaChi(diaChi);
		setChucVu(chucVu);
		setTrangThai(trangThai);
	}

	public NhanVien(String maNhanVien) {
		setMaNhanVien(maNhanVien);
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		this.ho = ho;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		if(du18Tuoi(ngaySinh)) {
			this.ngaySinh = ngaySinh;
		}else {
			throw new IllegalArgumentException("Nhân viên phải đủ 18 tuổi");
		}
		
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	
	public boolean du18Tuoi(LocalDate ngaySinh) {
		int tuoi = LocalDate.now().getYear() - ngaySinh.getYear();
		if(tuoi<18)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", ho=" + ho + ", ten=" + ten + ", gioiTinh=" + gioiTinh
				+ ", ngaySinh=" + ngaySinh + ", soDienThoai=" + soDienThoai + ", diaChi=" + diaChi + ", chucVu="
				+ chucVu + ", trangThai=" + trangThai + "]";
	}
	
	
	
	

}
