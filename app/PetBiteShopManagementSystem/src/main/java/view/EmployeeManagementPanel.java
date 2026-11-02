package view;

import app.UserSession;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Màn hình Quản lý nhân viên và Quản lý tài khoản nhân viên.
 * 
 * Các yêu cầu đã hoàn thiện:
 * 1. Tự động nhận diện vai trò từ UserSession khi đăng nhập (không cần menu demo).
 * 2. Chức năng NHẬP để tìm kiếm (real-time): Tìm theo Mã NV, Tên NV hoặc Số điện thoại.
 * 3. Bảng quản lý tài khoản nhân viên:
 *    - Không tự hiển thị mặc định (mặc định bị ẩn).
 *    - Chỉ khi Chủ cửa hàng bấm thao tác "Chi tiết" ở 1 nhân viên thì mới hiển thị,
 *      và chỉ hiển thị duy nhất thông tin tài khoản của nhân viên đó.
 *    - TUYỆT ĐỐI KHÔNG hiển thị cho nhân viên thường xem (luôn bị ẩn đối với nhân viên).
 * 4. Nhân viên chỉ xem được thông tin của bản thân (mật khẩu nhân viên khác bị che, thông tin cá nhân bị che).
 * 5. Nhân viên không có cột "Thao tác" và không có nút "+ Thêm nhân viên".
 */
public class EmployeeManagementPanel extends JPanel {

    // ===== Model Nhân Viên =====
    public static class NhanVienItem {
        public String maNV;
        public String hoTen;
        public String gioiTinh;
        public String ngaySinh;
        public String soDienThoai;
        public String email;
        public String chucVu;
        public String caLamViec;
        public String trangThai; // "Hoạt động" hoặc "Bị khóa"

        public NhanVienItem(String maNV, String hoTen, String gioiTinh, String ngaySinh,
                            String soDienThoai, String email, String chucVu, String caLamViec, String trangThai) {
            this.maNV = maNV;
            this.hoTen = hoTen;
            this.gioiTinh = gioiTinh;
            this.ngaySinh = ngaySinh;
            this.soDienThoai = soDienThoai;
            this.email = email;
            this.chucVu = chucVu;
            this.caLamViec = caLamViec;
            this.trangThai = trangThai;
        }
    }

    // ===== Model Tài Khoản =====
    public static class TaiKhoanItem {
        public String maNV;
        public String hoTen;
        public String tenDangNhap;
        public String matKhau;
        public String trangThai; // "Hoạt động" hoặc "Bị khóa"

        public TaiKhoanItem(String maNV, String hoTen, String tenDangNhap, String matKhau, String trangThai) {
            this.maNV = maNV;
            this.hoTen = hoTen;
            this.tenDangNhap = tenDangNhap;
            this.matKhau = matKhau;
            this.trangThai = trangThai;
        }
    }

    // Bảng màu chuẩn giao diện Pet Bite Shop
    private final Color mauNenChinh        = new Color(244, 246, 249);
    private final Color mauCamChinh        = new Color(244, 163, 44);
    private final Color mauChuTieuDe       = new Color(28, 56, 76);
    private final Color mauChuPhu          = new Color(127, 144, 158);
    private final Color mauVien            = new Color(227, 232, 238);
    private final Color mauNenHeaderBang   = new Color(247, 250, 253);
    private final Color mauXanhText        = new Color(46, 175, 100);
    private final Color mauXanhBg          = new Color(234, 247, 238);
    private final Color mauDoText          = new Color(229, 107, 111);
    private final Color mauDoBg            = new Color(253, 235, 236);
    private final Color mauNutSuaBg        = new Color(255, 244, 223);
    private final Color mauNutSuaText      = new Color(167, 100, 0);

    // Font chữ
    private final Font fontTieuDe          = new Font("Segoe UI", Font.BOLD, 18);
    private final Font fontPhu             = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font fontDam             = new Font("Segoe UI", Font.BOLD, 13);
    private final Font fontBadge           = new Font("Segoe UI", Font.BOLD, 12);

    // Danh sách dữ liệu trong bộ nhớ
    private final List<NhanVienItem> danhSachNV = new ArrayList<>();
    private final List<TaiKhoanItem> danhSachTK = new ArrayList<>();

    // Các thành phần giao diện
    private JTextField txtTimKiem;
    private JComboBox<String> cbChucVu;
    private JComboBox<String> cbTrangThai;
    private JButton btnThemNhanVien;

    // Bảng Nhân viên
    private JTable tableNhanVien;
    private DefaultTableModel modelNhanVien;
    private JScrollPane scrollNhanVien;

    // Bảng Tài khoản nhân viên (Mặc định ẩn, chỉ Chủ cửa hàng bấm "Chi tiết" mới hiện)
    private JPanel panelTaiKhoanWrapper;
    private JLabel lblTenNhanVienDangXemTK;
    private JTable tableTaiKhoan;
    private DefaultTableModel modelTaiKhoan;
    private JScrollPane scrollTaiKhoan;

    // Thống kê số lượng
    private JLabel lblThongKeNV;

    // Trạng thái quyền lấy từ phiên đăng nhập
    private boolean isChuCuaHang = false;
    private String currentMaNV = "";

    public EmployeeManagementPanel() {
        // 1. Nạp dữ liệu mẫu ban đầu
        khoiTaoDuLieu();

        // 2. Xác định vai trò từ UserSession
        capNhatQuyenTuSession();

        // 3. Xây dựng giao diện
        khoiTaoGiaoDien();

        // 4. Nạp dữ liệu lên bảng nhân viên
        napDuLieuBangNhanVien();
    }

    /**
     * Khởi tạo dữ liệu mẫu khớp với hình ảnh và kịch bản hệ thống.
     */
    private void khoiTaoDuLieu() {
        danhSachNV.clear();
        danhSachTK.clear();

        // Dữ liệu 4 nhân viên như trong ảnh mẫu
        danhSachNV.add(new NhanVienItem("NV241018001", "Trần Minh Phúc", "Nam", "10/05/1999", "0903456789", "tranphuc@gmail.com", "Chăm sóc", "Ca sáng", "Hoạt động"));
        danhSachNV.add(new NhanVienItem("NV241018002", "Lê Thu Trang", "Nữ", "12/09/2000", "0903987654", "thutrang@gmail.com", "Chăm sóc", "Ca tối", "Hoạt động"));
        danhSachNV.add(new NhanVienItem("NV241018003", "Nguyễn Mai Hân", "Nữ", "07/01/1998", "0851234567", "maihan@gmail.com", "Thu ngân", "Ca sáng", "Hoạt động"));
        danhSachNV.add(new NhanVienItem("NV241018004", "Phạm Hữu Long", "Nam", "21/03/1997", "0791234567", "huulong@gmail.com", "Thu ngân", "Ca tối", "Bị khóa"));

        // Dữ liệu tài khoản tương ứng
        danhSachTK.add(new TaiKhoanItem("NV241018001", "Trần Minh Phúc", "tranphuc", "tranphuc@123", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV241018002", "Lê Thu Trang", "thutrang", "thutrang@123", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV241018003", "Nguyễn Mai Hân", "maihan", "maihan@123", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV241018004", "Phạm Hữu Long", "huulong", "huulong@123", "Bị khóa"));

        // Hỗ trợ thêm các tài khoản fallback đăng nhập (chu, thungan1, thungan2, chamsoc1, chamsoc2)
        danhSachNV.add(new NhanVienItem("NV251001002", "Trần Thị Mai", "Nữ", "21/08/1998", "0901000002", "thungan1@gmail.com", "Thu ngân", "Ca sáng", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV251001002", "Trần Thị Mai", "thungan1", "PetBite@123", "Hoạt động"));

        danhSachNV.add(new NhanVienItem("NV251001003", "Lê Hoàng Nam", "Nam", "14/02/1999", "0901000003", "thungan2@gmail.com", "Thu ngân", "Ca tối", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV251001003", "Lê Hoàng Nam", "thungan2", "PetBite@123", "Hoạt động"));

        danhSachNV.add(new NhanVienItem("NV251001004", "Phạm Thu Hà", "Nữ", "30/11/1997", "0901000004", "chamsoc1@gmail.com", "Chăm sóc", "Ca sáng", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV251001004", "Phạm Thu Hà", "chamsoc1", "PetBite@123", "Hoạt động"));

        danhSachNV.add(new NhanVienItem("NV251001005", "Võ Minh Tuấn", "Nam", "05/06/1996", "0901000005", "chamsoc2@gmail.com", "Chăm sóc", "Ca tối", "Hoạt động"));
        danhSachTK.add(new TaiKhoanItem("NV251001005", "Võ Minh Tuấn", "chamsoc2", "PetBite@123", "Hoạt động"));
    }

    /**
     * Tự động lấy quyền từ UserSession mà không cần menu demo.
     */
    private void capNhatQuyenTuSession() {
        UserSession session = UserSession.getInstance();
        if (session != null && session.isLoggedIn()) {
            String vaiTro = session.getVaiTro();
            isChuCuaHang = "Chủ cửa hàng".equalsIgnoreCase(vaiTro);

            if (session.getNhanVien() != null && session.getNhanVien().getMaNV() != null) {
                currentMaNV = session.getNhanVien().getMaNV();
            } else if (session.getTaiKhoan() != null) {
                String uName = session.getTaiKhoan().getTenDangNhap();
                for (TaiKhoanItem tk : danhSachTK) {
                    if (tk.tenDangNhap.equalsIgnoreCase(uName)) {
                        currentMaNV = tk.maNV;
                        break;
                    }
                }
            }
        } else {
            // Mặc định nếu chạy trực tiếp không qua login: đặt quyền Chủ cửa hàng để kiểm tra
            isChuCuaHang = true;
            currentMaNV = "NV241018001";
        }
    }

    /**
     * Khởi tạo bố cục giao diện.
     */
    private void khoiTaoGiaoDien() {
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(16, 20, 20, 20));

        // Container chính thẻ trắng
        JPanel theTrangChinh = new JPanel(new BorderLayout());
        theTrangChinh.setBackground(Color.WHITE);
        theTrangChinh.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mauVien, 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));

        // ===== 1. KHỐI TRÊN: TIÊU ĐỀ & BỘ LỌC TÌM KIẾM =====
        JPanel panelTren = new JPanel();
        panelTren.setLayout(new BoxLayout(panelTren, BoxLayout.Y_AXIS));
        panelTren.setOpaque(false);

        // Tiêu đề
        JPanel panelTieuDe = new JPanel(new BorderLayout());
        panelTieuDe.setOpaque(false);

        JPanel panelTextTieuDe = new JPanel(new GridLayout(2, 1, 0, 3));
        panelTextTieuDe.setOpaque(false);
        JLabel lblTitle = new JLabel("Danh sách nhân viên");
        lblTitle.setFont(fontTieuDe);
        lblTitle.setForeground(mauChuTieuDe);

        JLabel lblSub = new JLabel("Bao gồm thu ngân và nhân viên chăm sóc. Chủ cửa hàng là actor riêng, không quản lý như nhân viên thường.");
        lblSub.setFont(fontPhu);
        lblSub.setForeground(mauChuPhu);

        panelTextTieuDe.add(lblTitle);
        panelTextTieuDe.add(lblSub);
        panelTieuDe.add(panelTextTieuDe, BorderLayout.WEST);

        panelTren.add(panelTieuDe);
        panelTren.add(Box.createVerticalStrut(14));

        // Thanh công cụ: Ô nhập tìm kiếm, Lọc chức vụ, Lọc trạng thái, Nút Thêm NV
        JPanel panelToolbar = new JPanel(new BorderLayout());
        panelToolbar.setOpaque(false);

        JPanel panelBoLocTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBoLocTrai.setOpaque(false);

        // Ô NHẬP TÌM KIẾM (Có placeholder vẽ chìm, không bị kẹt chuỗi)
        txtTimKiem = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(156, 163, 175));
                    g2.setFont(fontPhu);
                    Insets insets = getInsets();
                    g2.drawString("🔍 Tìm theo mã, tên hoặc SĐT", insets.left + 5, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        txtTimKiem.setPreferredSize(new Dimension(280, 38));
        txtTimKiem.setFont(fontPhu);
        txtTimKiem.setForeground(mauChuTieuDe);
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(mauVien, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        // LẮNG NGHE SỰ KIỆN GÕ PHÍM ĐỂ TÌM KIẾM NGAY LẬP TỨC (REAL-TIME SEARCH)
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                napDuLieuBangNhanVien();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                napDuLieuBangNhanVien();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                napDuLieuBangNhanVien();
            }
        });

        // Dropdown Chức vụ
        cbChucVu = new JComboBox<>(new String[]{"Chức vụ: Tất cả", "Chăm sóc", "Thu ngân"});
        cbChucVu.setPreferredSize(new Dimension(140, 38));
        cbChucVu.setFont(fontPhu);
        cbChucVu.setBackground(new Color(248, 251, 253));
        cbChucVu.setFocusable(false);
        cbChucVu.addActionListener(e -> napDuLieuBangNhanVien());

        // Dropdown Trạng thái
        cbTrangThai = new JComboBox<>(new String[]{"Trạng thái: Tất cả", "Hoạt động", "Bị khóa"});
        cbTrangThai.setPreferredSize(new Dimension(140, 38));
        cbTrangThai.setFont(fontPhu);
        cbTrangThai.setBackground(new Color(248, 251, 253));
        cbTrangThai.setFocusable(false);
        cbTrangThai.addActionListener(e -> napDuLieuBangNhanVien());

        panelBoLocTrai.add(txtTimKiem);
        panelBoLocTrai.add(cbChucVu);
        panelBoLocTrai.add(cbTrangThai);

        // Nút Thêm nhân viên (CHỈ HIỂN THỊ KHI LÀ CHỦ CỬA HÀNG)
        btnThemNhanVien = new JButton("+ Thêm nhân viên");
        btnThemNhanVien.setPreferredSize(new Dimension(160, 38));
        btnThemNhanVien.setFont(fontDam);
        btnThemNhanVien.setBackground(mauCamChinh);
        btnThemNhanVien.setForeground(Color.WHITE);
        btnThemNhanVien.setFocusPainted(false);
        btnThemNhanVien.setBorderPainted(false);
        btnThemNhanVien.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemNhanVien.setVisible(isChuCuaHang);
        btnThemNhanVien.addActionListener(e -> moHopThoaiThemNhanVien());

        panelToolbar.add(panelBoLocTrai, BorderLayout.WEST);
        panelToolbar.add(btnThemNhanVien, BorderLayout.EAST);

        panelTren.add(panelToolbar);
        panelTren.add(Box.createVerticalStrut(12));

        theTrangChinh.add(panelTren, BorderLayout.NORTH);

        // ===== 2. KHỐI GIỮA: BẢNG NHÂN VIÊN & BẢNG TÀI KHOẢN =====
        JPanel panelChuaHaiBang = new JPanel();
        panelChuaHaiBang.setLayout(new BoxLayout(panelChuaHaiBang, BoxLayout.Y_AXIS));
        panelChuaHaiBang.setOpaque(false);

        // --- BẢNG 1: DANH SÁCH NHÂN VIÊN ---
        modelNhanVien = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableNhanVien = new JTable(modelNhanVien);
        cauHinhTable(tableNhanVien);

        // BẮT SỰ KIỆN CLICK VÀO CỘT THAO TÁC CỦA CHỦ CỬA HÀNG
        tableNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isChuCuaHang) return; // Nhân viên không có cột thao tác

                int row = tableNhanVien.rowAtPoint(e.getPoint());
                int col = tableNhanVien.columnAtPoint(e.getPoint());
                if (row >= 0 && col == tableNhanVien.getColumnCount() - 1) { // Cột Thao tác
                    String maNV = (String) tableNhanVien.getValueAt(row, 0);
                    NhanVienItem nv = timNhanVien(maNV);
                    if (nv != null) {
                        Rectangle cellRect = tableNhanVien.getCellRect(row, col, false);
                        int clickX = e.getX() - cellRect.x;

                        // Click vào "Chi tiết" (nửa trái của ô)
                        if (clickX < cellRect.width / 2) {
                            hienThiBangTaiKhoanMotNhanVien(nv);
                        } else {
                            // Click vào "Sửa" (nửa phải của ô)
                            moHopThoaiSuaNhanVien(nv);
                        }
                    }
                }
            }
        });

        scrollNhanVien = new JScrollPane(tableNhanVien);
        scrollNhanVien.getViewport().setBackground(Color.WHITE);
        scrollNhanVien.setBorder(new LineBorder(mauVien, 1));
        scrollNhanVien.setPreferredSize(new Dimension(800, 230));
        panelChuaHaiBang.add(scrollNhanVien);

        panelChuaHaiBang.add(Box.createVerticalStrut(14));

        // --- BẢNG 2: QUẢN LÝ TÀI KHOẢN NHÂN VIÊN ---
        panelTaiKhoanWrapper = new JPanel(new BorderLayout());
        panelTaiKhoanWrapper.setOpaque(false);
        panelTaiKhoanWrapper.setVisible(false); // MẶC ĐỊNH BỊ ẨN

        // Tiêu đề bảng tài khoản kèm mũi tên đỏ + nút đóng
        JPanel panelHeaderTK = new JPanel(new BorderLayout());
        panelHeaderTK.setOpaque(false);
        panelHeaderTK.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel panelTieuDeTK = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTieuDeTK.setOpaque(false);

        JLabel lblArrowNote = new JLabel("CHỈ CHỦ CỬA HÀNG MỚI THẤY PHẦN NÀY  ➔ ");
        lblArrowNote.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblArrowNote.setForeground(new Color(220, 38, 38));

        lblTenNhanVienDangXemTK = new JLabel("Thông tin tài khoản");
        lblTenNhanVienDangXemTK.setFont(fontDam);
        lblTenNhanVienDangXemTK.setForeground(mauChuTieuDe);

        panelTieuDeTK.add(lblArrowNote);
        panelTieuDeTK.add(lblTenNhanVienDangXemTK);
        panelHeaderTK.add(panelTieuDeTK, BorderLayout.WEST);

        // Nút thu gọn / đóng bảng tài khoản
        JButton btnDongTK = new JButton("✕ Đóng");
        btnDongTK.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDongTK.setForeground(mauChuPhu);
        btnDongTK.setBackground(Color.WHITE);
        btnDongTK.setBorder(new LineBorder(mauVien, 1, true));
        btnDongTK.setFocusPainted(false);
        btnDongTK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDongTK.addActionListener(e -> {
            panelTaiKhoanWrapper.setVisible(false);
            revalidate();
            repaint();
        });
        panelHeaderTK.add(btnDongTK, BorderLayout.EAST);

        panelTaiKhoanWrapper.add(panelHeaderTK, BorderLayout.NORTH);

        modelTaiKhoan = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableTaiKhoan = new JTable(modelTaiKhoan);
        cauHinhTable(tableTaiKhoan);

        // Click sửa tài khoản (Chỉ chủ cửa hàng)
        tableTaiKhoan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isChuCuaHang) return;
                int row = tableTaiKhoan.rowAtPoint(e.getPoint());
                int col = tableTaiKhoan.columnAtPoint(e.getPoint());
                if (row >= 0 && col == tableTaiKhoan.getColumnCount() - 1) {
                    String tenDangNhap = (String) tableTaiKhoan.getValueAt(row, 1);
                    TaiKhoanItem tk = timTaiKhoan(tenDangNhap);
                    if (tk != null) {
                        moHopThoaiSuaTaiKhoan(tk);
                    }
                }
            }
        });

        scrollTaiKhoan = new JScrollPane(tableTaiKhoan);
        scrollTaiKhoan.getViewport().setBackground(Color.WHITE);
        scrollTaiKhoan.setBorder(new LineBorder(mauVien, 1));
        scrollTaiKhoan.setPreferredSize(new Dimension(800, 85));
        panelTaiKhoanWrapper.add(scrollTaiKhoan, BorderLayout.CENTER);

        panelChuaHaiBang.add(panelTaiKhoanWrapper);

        theTrangChinh.add(panelChuaHaiBang, BorderLayout.CENTER);

        // ===== 3. KHỐI ĐÁY: THỐNG KÊ DÒNG =====
        JPanel panelDay = new JPanel(new BorderLayout());
        panelDay.setOpaque(false);
        panelDay.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblThongKeNV = new JLabel("Hiển thị 1–4");
        lblThongKeNV.setFont(fontPhu);
        lblThongKeNV.setForeground(mauChuPhu);

        panelDay.add(lblThongKeNV, BorderLayout.WEST);
        theTrangChinh.add(panelDay, BorderLayout.SOUTH);

        add(theTrangChinh, BorderLayout.CENTER);
    }

    private void cauHinhTable(JTable table) {
        table.setRowHeight(46);
        table.setFont(fontPhu);
        table.setShowGrid(true);
        table.setGridColor(new Color(238, 242, 246));
        table.setSelectionBackground(new Color(242, 247, 252));
        table.setSelectionForeground(mauChuTieuDe);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(fontDam);
        header.setBackground(mauNenHeaderBang);
        header.setForeground(new Color(76, 100, 116));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, mauVien));
    }

    // =========================================================================
    // XỬ LÝ LỌC & NẠP DỮ LIỆU BẢNG NHÂN VIÊN
    // =========================================================================
    private void napDuLieuBangNhanVien() {
        String rawKey = txtTimKiem != null ? txtTimKiem.getText().trim() : "";
        String keyword = boDauTiengViet(rawKey);

        String locChucVu = cbChucVu != null ? (String) cbChucVu.getSelectedItem() : "Chức vụ: Tất cả";
        String locTrangThai = cbTrangThai != null ? (String) cbTrangThai.getSelectedItem() : "Trạng thái: Tất cả";

        String[] cotNV;
        if (isChuCuaHang) {
            cotNV = new String[]{"Mã NV", "Họ và tên", "Giới tính", "Ngày sinh", "SĐT", "Email", "Chức vụ", "Ca làm việc", "Trạng thái", "Thao tác"};
        } else {
            cotNV = new String[]{"Mã NV", "Họ và tên", "Giới tính", "Ngày sinh", "SĐT", "Email", "Chức vụ", "Ca làm việc", "Trạng thái"};
        }
        modelNhanVien.setDataVector(null, cotNV);

        int count = 0;
        for (NhanVienItem nv : danhSachNV) {
            // TÌM THEO: MÃ NHÂN VIÊN, HỌ TÊN HOẶC SỐ ĐIỆN THOẠI (hỗ trợ cả có dấu và không dấu)
            if (!keyword.isEmpty()) {
                String maNVNoSign = boDauTiengViet(nv.maNV);
                String hoTenNoSign = boDauTiengViet(nv.hoTen);
                String sdt = nv.soDienThoai != null ? nv.soDienThoai : "";

                boolean match = maNVNoSign.contains(keyword) || hoTenNoSign.contains(keyword) || sdt.contains(keyword);
                if (!match) continue;
            }

            // Lọc chức vụ
            if (locChucVu != null && !locChucVu.equals("Chức vụ: Tất cả") && !nv.chucVu.equalsIgnoreCase(locChucVu)) {
                continue;
            }

            // Lọc trạng thái
            if (locTrangThai != null && !locTrangThai.equals("Trạng thái: Tất cả") && !nv.trangThai.equalsIgnoreCase(locTrangThai)) {
                continue;
            }

            // Nhân viên chỉ xem đầy đủ thông tin của bản thân; nhân viên khác bị che ngày sinh, SĐT, email
            boolean laChinhMinh = nv.maNV.equalsIgnoreCase(currentMaNV);

            String ngaySinhHienThi;
            String sdtHienThi;
            String emailHienThi;

            if (isChuCuaHang || laChinhMinh) {
                ngaySinhHienThi = nv.ngaySinh;
                sdtHienThi = nv.soDienThoai;
                emailHienThi = nv.email;
            } else {
                ngaySinhHienThi = cheNgaySinh(nv.ngaySinh);
                sdtHienThi = cheSoDienThoai(nv.soDienThoai);
                emailHienThi = cheEmail(nv.email);
            }

            if (isChuCuaHang) {
                modelNhanVien.addRow(new Object[]{
                    nv.maNV, nv.hoTen, nv.gioiTinh, ngaySinhHienThi, sdtHienThi, emailHienThi,
                    nv.chucVu, nv.caLamViec, nv.trangThai, "Chi tiết   Sửa"
                });
            } else {
                modelNhanVien.addRow(new Object[]{
                    nv.maNV, nv.hoTen, nv.gioiTinh, ngaySinhHienThi, sdtHienThi, emailHienThi,
                    nv.chucVu, nv.caLamViec, nv.trangThai
                });
            }
            count++;
        }

        if (lblThongKeNV != null) {
            lblThongKeNV.setText("Hiển thị 1–" + count);
        }

        datRendererBangNhanVien();
    }

    /**
     * HIỂN THỊ THÔNG TIN TÀI KHOẢN CỦA 1 NHÂN VIÊN KHI CHỦ CỬA HÀNG BẤM "CHI TIẾT"
     */
    private void hienThiBangTaiKhoanMotNhanVien(NhanVienItem nv) {
        if (!isChuCuaHang) {
            panelTaiKhoanWrapper.setVisible(false);
            return;
        }

        TaiKhoanItem tk = timTaiKhoanTheoMaNV(nv.maNV);
        if (tk == null) {
            JOptionPane.showMessageDialog(this, "Nhân viên này hiện chưa có tài khoản hệ thống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        lblTenNhanVienDangXemTK.setText("Tài khoản của nhân viên: " + nv.hoTen + " (" + nv.maNV + ")");

        String[] cotTK = new String[]{"Nhân viên", "Tên đăng nhập", "Mật khẩu", "Trạng thái", "Thao tác"};
        modelTaiKhoan.setDataVector(null, cotTK);

        modelTaiKhoan.addRow(new Object[]{
            tk.hoTen, tk.tenDangNhap, tk.matKhau, tk.trangThai, "Sửa"
        });

        datRendererBangTaiKhoan();

        panelTaiKhoanWrapper.setVisible(true);
        panelTaiKhoanWrapper.revalidate();
        panelTaiKhoanWrapper.repaint();
        revalidate();
        repaint();
    }

    private void datRendererBangNhanVien() {
        DefaultTableCellRenderer canTrai = new DefaultTableCellRenderer();
        canTrai.setBorder(new EmptyBorder(0, 10, 0, 10));

        DefaultTableCellRenderer canGiua = new DefaultTableCellRenderer();
        canGiua.setHorizontalAlignment(SwingConstants.CENTER);

        int colCount = tableNhanVien.getColumnCount();
        for (int i = 0; i < colCount; i++) {
            if (i == 2 || i == 3 || i == 6 || i == 7) {
                tableNhanVien.getColumnModel().getColumn(i).setCellRenderer(canGiua);
            } else {
                tableNhanVien.getColumnModel().getColumn(i).setCellRenderer(canTrai);
            }
        }

        tableNhanVien.getColumnModel().getColumn(8).setCellRenderer(new BadgeTrangThaiRenderer());

        if (isChuCuaHang && colCount > 9) {
            tableNhanVien.getColumnModel().getColumn(9).setCellRenderer(new ThaoTacNhanVienRenderer());
            tableNhanVien.getColumnModel().getColumn(9).setPreferredWidth(125);
        }
    }

    private void datRendererBangTaiKhoan() {
        DefaultTableCellRenderer canTrai = new DefaultTableCellRenderer();
        canTrai.setBorder(new EmptyBorder(0, 10, 0, 10));

        for (int i = 0; i < tableTaiKhoan.getColumnCount(); i++) {
            tableTaiKhoan.getColumnModel().getColumn(i).setCellRenderer(canTrai);
        }

        tableTaiKhoan.getColumnModel().getColumn(3).setCellRenderer(new BadgeTrangThaiRenderer());

        if (tableTaiKhoan.getColumnCount() > 4) {
            tableTaiKhoan.getColumnModel().getColumn(4).setCellRenderer(new ThaoTacTaiKhoanRenderer());
            tableTaiKhoan.getColumnModel().getColumn(4).setPreferredWidth(80);
        }
    }

    // =========================================================================
    // CÁC HÀM CHE DỮ LIỆU
    // =========================================================================
    private String cheSoDienThoai(String sdt) {
        if (sdt == null || sdt.length() < 7) return sdt;
        return sdt.substring(0, 4) + "***" + sdt.substring(sdt.length() - 3);
    }

    private String cheEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        int atIndex = email.indexOf("@");
        String name = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (name.length() <= 2) {
            return name.charAt(0) + "***" + domain;
        }
        return name.substring(0, 3) + "***" + domain;
    }

    private String cheNgaySinh(String dob) {
        if (dob == null || dob.length() < 6) return dob;
        if (dob.contains("/")) {
            String[] p = dob.split("/");
            if (p.length == 3) {
                return p[0] + "/" + p[1] + "/****";
            }
        }
        return dob.substring(0, 5) + "/****";
    }

    public static String boDauTiengViet(String str) {
        if (str == null) return "";
        String nfd = Normalizer.normalize(str, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(nfd).replaceAll("").replace('đ', 'd').replace('Đ', 'D').toLowerCase();
    }

    // =========================================================================
    // CÁC HỘP THOẠI (DIALOG)
    // =========================================================================
    private void moHopThoaiThemNhanVien() {
        if (!isChuCuaHang) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm nhân viên mới", true);
        dialog.setSize(480, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pContent = new JPanel(new GridLayout(9, 2, 10, 10));
        pContent.setBorder(new EmptyBorder(20, 24, 20, 24));

        String maTuDong = "NV241018" + String.format("%03d", danhSachNV.size() + 1);

        JTextField txtMa = new JTextField(maTuDong);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtNgaySinh = new JTextField("10/05/2000");
        JTextField txtSdt = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> cbChucVuInput = new JComboBox<>(new String[]{"Chăm sóc", "Thu ngân"});
        JComboBox<String> cbCaInput = new JComboBox<>(new String[]{"Ca sáng", "Ca tối"});
        JTextField txtMatKhauBanDau = new JTextField("PetBite@123");

        pContent.add(new JLabel("Mã nhân viên:"));
        pContent.add(txtMa);
        pContent.add(new JLabel("Họ và tên:"));
        pContent.add(txtTen);
        pContent.add(new JLabel("Giới tính:"));
        pContent.add(cbGioiTinh);
        pContent.add(new JLabel("Ngày sinh (dd/mm/yyyy):"));
        pContent.add(txtNgaySinh);
        pContent.add(new JLabel("Số điện thoại:"));
        pContent.add(txtSdt);
        pContent.add(new JLabel("Email:"));
        pContent.add(txtEmail);
        pContent.add(new JLabel("Chức vụ:"));
        pContent.add(cbChucVuInput);
        pContent.add(new JLabel("Ca làm việc:"));
        pContent.add(cbCaInput);
        pContent.add(new JLabel("Mật khẩu ban đầu:"));
        pContent.add(txtMatKhauBanDau);

        dialog.add(pContent, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        JButton btnLuu = new JButton("Lưu nhân viên");
        btnLuu.setBackground(mauCamChinh);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(fontDam);

        JButton btnHuy = new JButton("Hủy bỏ");

        btnLuu.addActionListener(e -> {
            String hoTen = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String email = txtEmail.getText().trim();

            if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ họ tên, SĐT và email!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            NhanVienItem nvMoi = new NhanVienItem(
                txtMa.getText(),
                hoTen,
                (String) cbGioiTinh.getSelectedItem(),
                txtNgaySinh.getText().trim(),
                sdt,
                email,
                (String) cbChucVuInput.getSelectedItem(),
                (String) cbCaInput.getSelectedItem(),
                "Hoạt động"
            );
            danhSachNV.add(nvMoi);

            String usernameMoi = taoUsername(hoTen);
            String passMoi = txtMatKhauBanDau.getText().trim();
            danhSachTK.add(new TaiKhoanItem(nvMoi.maNV, hoTen, usernameMoi, passMoi, "Hoạt động"));

            JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!\nTài khoản: " + usernameMoi + " | Mật khẩu: " + passMoi);
            dialog.dispose();
            napDuLieuBangNhanVien();
        });

        btnHuy.addActionListener(e -> dialog.dispose());
        pBtn.add(btnHuy);
        pBtn.add(btnLuu);
        dialog.add(pBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void moHopThoaiSuaNhanVien(NhanVienItem nv) {
        if (!isChuCuaHang) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật nhân viên: " + nv.maNV, true);
        dialog.setSize(460, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pContent = new JPanel(new GridLayout(8, 2, 10, 10));
        pContent.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTextField txtTen = new JTextField(nv.hoTen);
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setSelectedItem(nv.gioiTinh);
        JTextField txtNgaySinh = new JTextField(nv.ngaySinh);
        JTextField txtSdt = new JTextField(nv.soDienThoai);
        JTextField txtEmail = new JTextField(nv.email);
        JComboBox<String> cbChucVuInput = new JComboBox<>(new String[]{"Chăm sóc", "Thu ngân"});
        cbChucVuInput.setSelectedItem(nv.chucVu);
        JComboBox<String> cbCaInput = new JComboBox<>(new String[]{"Ca sáng", "Ca tối"});
        cbCaInput.setSelectedItem(nv.caLamViec);
        JComboBox<String> cbTrangThaiInput = new JComboBox<>(new String[]{"Hoạt động", "Bị khóa"});
        cbTrangThaiInput.setSelectedItem(nv.trangThai);

        pContent.add(new JLabel("Họ và tên:"));
        pContent.add(txtTen);
        pContent.add(new JLabel("Giới tính:"));
        pContent.add(cbGioiTinh);
        pContent.add(new JLabel("Ngày sinh:"));
        pContent.add(txtNgaySinh);
        pContent.add(new JLabel("Số điện thoại:"));
        pContent.add(txtSdt);
        pContent.add(new JLabel("Email:"));
        pContent.add(txtEmail);
        pContent.add(new JLabel("Chức vụ:"));
        pContent.add(cbChucVuInput);
        pContent.add(new JLabel("Ca làm việc:"));
        pContent.add(cbCaInput);
        pContent.add(new JLabel("Trạng thái:"));
        pContent.add(cbTrangThaiInput);

        dialog.add(pContent, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.setBackground(mauCamChinh);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(fontDam);

        JButton btnHuy = new JButton("Hủy bỏ");

        btnLuu.addActionListener(e -> {
            nv.hoTen = txtTen.getText().trim();
            nv.gioiTinh = (String) cbGioiTinh.getSelectedItem();
            nv.ngaySinh = txtNgaySinh.getText().trim();
            nv.soDienThoai = txtSdt.getText().trim();
            nv.email = txtEmail.getText().trim();
            nv.chucVu = (String) cbChucVuInput.getSelectedItem();
            nv.caLamViec = (String) cbCaInput.getSelectedItem();
            nv.trangThai = (String) cbTrangThaiInput.getSelectedItem();

            for (TaiKhoanItem tk : danhSachTK) {
                if (tk.maNV.equalsIgnoreCase(nv.maNV)) {
                    tk.hoTen = nv.hoTen;
                    tk.trangThai = nv.trangThai;
                    break;
                }
            }

            JOptionPane.showMessageDialog(dialog, "Cập nhật nhân viên thành công!");
            dialog.dispose();
            napDuLieuBangNhanVien();

            if (panelTaiKhoanWrapper.isVisible() && lblTenNhanVienDangXemTK.getText().contains(nv.maNV)) {
                hienThiBangTaiKhoanMotNhanVien(nv);
            }
        });

        btnHuy.addActionListener(e -> dialog.dispose());
        pBtn.add(btnHuy);
        pBtn.add(btnLuu);
        dialog.add(pBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void moHopThoaiSuaTaiKhoan(TaiKhoanItem tk) {
        if (!isChuCuaHang) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa tài khoản: " + tk.tenDangNhap, true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pContent = new JPanel(new GridLayout(4, 2, 10, 12));
        pContent.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTextField txtUser = new JTextField(tk.tenDangNhap);
        txtUser.setEditable(false);
        JTextField txtPass = new JTextField(tk.matKhau);
        JComboBox<String> cbTrangThaiTK = new JComboBox<>(new String[]{"Hoạt động", "Bị khóa"});
        cbTrangThaiTK.setSelectedItem(tk.trangThai);

        pContent.add(new JLabel("Nhân viên:"));
        pContent.add(new JLabel(tk.hoTen));
        pContent.add(new JLabel("Tên đăng nhập:"));
        pContent.add(txtUser);
        pContent.add(new JLabel("Mật khẩu mới:"));
        pContent.add(txtPass);
        pContent.add(new JLabel("Trạng thái tài khoản:"));
        pContent.add(cbTrangThaiTK);

        dialog.add(pContent, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        JButton btnLuu = new JButton("Lưu thay đổi");
        btnLuu.setBackground(mauCamChinh);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(fontDam);

        JButton btnHuy = new JButton("Hủy bỏ");

        btnLuu.addActionListener(e -> {
            String newPass = txtPass.getText().trim();
            if (newPass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            tk.matKhau = newPass;
            tk.trangThai = (String) cbTrangThaiTK.getSelectedItem();

            for (NhanVienItem nv : danhSachNV) {
                if (nv.maNV.equalsIgnoreCase(tk.maNV)) {
                    nv.trangThai = tk.trangThai;
                    break;
                }
            }

            JOptionPane.showMessageDialog(dialog, "Cập nhật tài khoản thành công!");
            dialog.dispose();

            NhanVienItem nv = timNhanVien(tk.maNV);
            if (nv != null) {
                hienThiBangTaiKhoanMotNhanVien(nv);
            }
            napDuLieuBangNhanVien();
        });

        btnHuy.addActionListener(e -> dialog.dispose());
        pBtn.add(btnHuy);
        pBtn.add(btnLuu);
        dialog.add(pBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private NhanVienItem timNhanVien(String maNV) {
        for (NhanVienItem nv : danhSachNV) {
            if (nv.maNV.equalsIgnoreCase(maNV)) return nv;
        }
        return null;
    }

    private TaiKhoanItem timTaiKhoan(String tenDangNhap) {
        for (TaiKhoanItem tk : danhSachTK) {
            if (tk.tenDangNhap.equalsIgnoreCase(tenDangNhap)) return tk;
        }
        return null;
    }

    private TaiKhoanItem timTaiKhoanTheoMaNV(String maNV) {
        for (TaiKhoanItem tk : danhSachTK) {
            if (tk.maNV.equalsIgnoreCase(maNV)) return tk;
        }
        return null;
    }

    private String taoUsername(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) return "user";
        String[] parts = hoTen.trim().toLowerCase().split("\\s+");
        String ten = parts[parts.length - 1];
        String hoDem = "";
        for (int i = 0; i < parts.length - 1; i++) {
            hoDem += parts[i].charAt(0);
        }
        return boDauTiengViet(ten + hoDem);
    }

    // =========================================================================
    // CELL RENDERERS
    // =========================================================================
    private class BadgeTrangThaiRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            String text = value != null ? value.toString() : "";
            JPanel p = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    boolean isHoatDong = "Hoạt động".equalsIgnoreCase(text);
                    Color bg = isHoatDong ? mauXanhBg : mauDoBg;
                    g2.setColor(bg);
                    g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 12, 12);
                    g2.dispose();
                }
            };
            p.setOpaque(false);

            JLabel lbl = new JLabel(text);
            lbl.setFont(fontBadge);
            lbl.setForeground("Hoạt động".equalsIgnoreCase(text) ? mauXanhText : mauDoText);
            p.add(lbl);
            return p;
        }
    }

    private class ThaoTacNhanVienRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
            p.setOpaque(false);

            JLabel btnChiTiet = taoBadgeNut("Chi tiết", new Color(240, 243, 246), mauChuTieuDe);
            JLabel btnSua = taoBadgeNut("Sửa", mauNutSuaBg, mauNutSuaText);

            p.add(btnChiTiet);
            p.add(btnSua);
            return p;
        }
    }

    private class ThaoTacTaiKhoanRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
            p.setOpaque(false);

            JLabel btnSua = taoBadgeNut("Sửa", mauNutSuaBg, mauNutSuaText);
            p.add(btnSua);
            return p;
        }
    }

    private JLabel taoBadgeNut(String text, Color bg, Color fg) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(fontBadge);
        lbl.setForeground(fg);
        lbl.setPreferredSize(new Dimension(54, 26));
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return lbl;
    }
}

