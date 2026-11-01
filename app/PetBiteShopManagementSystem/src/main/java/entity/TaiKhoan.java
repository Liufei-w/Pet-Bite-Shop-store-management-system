package entity;

import java.util.Objects;

public class TaiKhoan {
	private String tenDangNhap;
	private String matKhau;
	private boolean trangThai;
	private String maTaiKhoan;
	public TaiKhoan(String tenDangNhap, String matKhau, boolean trangThai, String maTaiKhoan) {
		super();
		setMaTaiKhoan(maTaiKhoan);
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
		setTrangThai(trangThai);
	}
	public TaiKhoan(String maTaiKhoan) {
		super();
		this.maTaiKhoan = maTaiKhoan;
	}
	public String getTenDangNhap() {
		return tenDangNhap;
	}
	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	public String getMaTaiKhoan() {
		return maTaiKhoan;
	}
	public void setMaTaiKhoan(String maTaiKhoan) {
		this.maTaiKhoan = maTaiKhoan;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maTaiKhoan, tenDangNhap);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(maTaiKhoan, other.maTaiKhoan) && Objects.equals(tenDangNhap, other.tenDangNhap);
	}
	@Override
	public String toString() {
		return "TaiKhoan [tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau + ", trangThai=" + trangThai
				+ ", maTaiKhoan=" + maTaiKhoan + "]";
	}
	
	

}
