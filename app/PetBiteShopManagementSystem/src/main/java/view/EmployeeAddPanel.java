package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Giao diện màn hình / hộp thoại "Thêm nhân viên" cho Pet Bite Shop.
 * Chuẩn thiết kế UI theo Figma (ảnh đính kèm).
 */
public class EmployeeAddPanel extends JPanel {

    // Màu sắc theo chuẩn giao diện mẫu
    private final Color mauNenChinh        = Color.WHITE;
    private final Color mauCamChinh        = new Color(245, 158, 35);   // #F59E23 (Nút Lưu cam)
    private final Color mauCamHover        = new Color(230, 142, 25);
    private final Color mauChuTieuDe       = new Color(30, 41, 59);     // #1E293B
    private final Color mauChuPhu          = new Color(138, 153, 168);  // #8A99A8
    private final Color mauNhanCot         = new Color(28, 43, 62);     // #1C2B3E
    private final Color mauVienInput       = new Color(229, 235, 242);  // #E5EBF2 viền xám xanh nhạt
    private final Color mauNenInput        = new Color(248, 250, 252);  // #F8FAFC
    private final Color mauPlaceholder     = new Color(170, 182, 194);  // #AAB6C2
    private final Color mauNenLuuY         = new Color(254, 248, 237);  // Nền banner ghi chú vàng kem
    private final Color mauChuLuuY         = new Color(160, 95, 20);    // Chữ ghi chú nâu cam
    private final Color mauNutHuyBg        = new Color(245, 248, 250);  // Nền nút Hủy
    private final Color mauNutHuyBorder    = new Color(227, 234, 241);  // Viền nút Hủy
    private final Color mauNutHuyText      = new Color(74, 90, 107);    // Màu chữ nút Hủy

    // Font chữ chuẩn
    private final Font fontTieuDe          = new Font("Segoe UI", Font.BOLD, 26);
    private final Font fontMoTa            = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font fontNhanField       = new Font("Segoe UI", Font.BOLD, 13);
    private final Font fontInput           = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font fontGhiChu          = new Font("Segoe UI", Font.BOLD, 12);
    private final Font fontNut             = new Font("Segoe UI", Font.BOLD, 14);

    // Các thành phần nhập liệu
    private JTextField txtMaNV;
    private JTextField txtHoTen;
    private JComboBox<String> cbGioiTinh;
    private JTextField txtNgaySinh;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JComboBox<String> cbCaLamViec;
    private JComboBox<String> cbChucVu;
    private JTextField txtDiaChi;
    private JTextField txtTenDangNhap;
    private JComboBox<String> cbTrangThai;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;

    // Các nút thao tác
    private JButton btnHuy;
    private JButton btnLuu;

    // Callback khi thêm thành công hoặc hủy
    public interface OnSaveListener {
        void onSaveSuccess(EmployeeManagementPanel.NhanVienItem nv, EmployeeManagementPanel.TaiKhoanItem tk, String diaChi);
    }
    private OnSaveListener onSaveListener;
    private Runnable onCancelListener;

    public EmployeeAddPanel() {
        this(null, null);
    }

    public EmployeeAddPanel(OnSaveListener onSaveListener, Runnable onCancelListener) {
        this.onSaveListener = onSaveListener;
        this.onCancelListener = onCancelListener;
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout());
        setBackground(mauNenChinh);
        setBorder(new EmptyBorder(26, 32, 24, 32));

        // 1. PHẦN ĐẦU (Header): Tiêu đề và mô tả
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Thêm nhân viên");
        lblTitle.setFont(fontTieuDe);
        lblTitle.setForeground(mauChuTieuDe);
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(4));

        JLabel lblDesc = new JLabel("Nhập đầy đủ thông tin nhân viên trước khi lưu.");
        lblDesc.setFont(fontMoTa);
        lblDesc.setForeground(mauChuPhu);
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        // 2. PHẦN THÂN (Form Body):
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(18, 0, 16, 0));

        // Hàng 1: Mã nhân viên, Họ và tên, Giới tính, Ngày sinh (4 cột)
        JPanel row1 = new JPanel(new GridLayout(1, 4, 16, 0));
        row1.setOpaque(false);

        txtMaNV = taoTextField("NV241018005 (tự động)", false);
        txtMaNV.setForeground(mauPlaceholder);

        txtHoTen = taoTextFieldWithPlaceholder("Nhập họ và tên nhân viên");
        cbGioiTinh = taoComboBox(new String[]{"Chọn giới tính", "Nam", "Nữ"});
        txtNgaySinh = taoTextFieldWithPlaceholder("dd/mm/yyyy");

        row1.add(taoFormItem("Mã nhân viên", txtMaNV));
        row1.add(taoFormItem("Họ và tên", txtHoTen));
        row1.add(taoFormItem("Giới tính", cbGioiTinh));
        row1.add(taoFormItem("Ngày sinh", txtNgaySinh));
        formContainer.add(row1);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 2: Số điện thoại, Email, Ca làm việc, Chức vụ (4 cột)
        JPanel row2 = new JPanel(new GridLayout(1, 4, 16, 0));
        row2.setOpaque(false);

        txtSoDienThoai = taoTextFieldWithPlaceholder("Nhập số điện thoại");
        txtEmail = taoTextFieldWithPlaceholder("Nhập địa chỉ email");
        cbCaLamViec = taoComboBox(new String[]{"Ca sáng", "Ca tối", "Ca hành chính"});
        cbChucVu = taoComboBox(new String[]{"Chọn chức vụ", "Thu ngân", "Chăm sóc"});

        row2.add(taoFormItem("Số điện thoại", txtSoDienThoai));
        row2.add(taoFormItem("Email", txtEmail));
        row2.add(taoFormItem("Ca làm việc", cbCaLamViec));
        row2.add(taoFormItem("Chức vụ", cbChucVu));
        formContainer.add(row2);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 3: Địa chỉ (chiều cao 76px bo góc mềm mại như hình mẫu)
        JPanel row3 = new JPanel(new BorderLayout());
        row3.setOpaque(false);
        txtDiaChi = taoTextFieldWithPlaceholder("Nhập địa chỉ nhân viên");
        txtDiaChi.setPreferredSize(new Dimension(txtDiaChi.getPreferredSize().width, 76));
        row3.add(taoFormItem("Địa chỉ", txtDiaChi), BorderLayout.CENTER);
        formContainer.add(row3);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 4: Tên đăng nhập & Trạng thái (2 cột, mỗi cột rộng 1/4 hàng ngang giống 2 ô đầu của hàng 1 & 2)
        JPanel row4 = new JPanel(new GridLayout(1, 4, 16, 0));
        row4.setOpaque(false);

        txtTenDangNhap = taoTextFieldWithPlaceholder("Nhập tên đăng nhập");
        cbTrangThai = taoComboBox(new String[]{"Hoạt động", "Bị khóa"});

        row4.add(taoFormItem("Tên đăng nhập", txtTenDangNhap));
        row4.add(taoFormItem("Trạng thái", cbTrangThai));
        row4.add(Box.createGlue());
        row4.add(Box.createGlue());
        formContainer.add(row4);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 5: Mật khẩu & Xác nhận mật khẩu (2 cột, rộng tương tự 2 ô đầu)
        JPanel row5 = new JPanel(new GridLayout(1, 4, 16, 0));
        row5.setOpaque(false);

        txtMatKhau = taoPasswordFieldWithPlaceholder("Nhập mật khẩu");
        JPanel pMatKhauWithEye = taoPasswordFieldWithToggleEye(txtMatKhau);

        txtXacNhanMatKhau = taoPasswordFieldWithPlaceholder("Nhập lại mật khẩu");
        JPanel pXacNhanMatKhauWithEye = taoPasswordFieldWithToggleEye(txtXacNhanMatKhau);

        row5.add(taoFormItem("Mật khẩu", pMatKhauWithEye));
        row5.add(taoFormItem("Xác nhận mật khẩu", pXacNhanMatKhauWithEye));
        row5.add(Box.createGlue());
        row5.add(Box.createGlue());
        formContainer.add(row5);
        formContainer.add(Box.createVerticalStrut(18));

        // Tự động gợi ý username khi nhập Họ và tên nếu username đang rỗng
        txtHoTen.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtTenDangNhap.getText().trim().isEmpty() && !txtHoTen.getText().trim().isEmpty()) {
                    txtTenDangNhap.setText(EmployeeManagementPanel.boDauTiengViet(txtHoTen.getText().trim()).replaceAll("\\s+", ""));
                }
            }
        });

        // Banner Ghi chú / Cảnh báo hợp lệ
        JPanel pLuuY = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenLuuY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pLuuY.setOpaque(false);

        JLabel lblLuuY = new JLabel("• Nhân viên phải từ đủ 18 tuổi. Số điện thoại phải đúng định dạng Việt Nam 10 số và duy nhất.");
        lblLuuY.setFont(fontGhiChu);
        lblLuuY.setForeground(mauChuLuuY);
        pLuuY.add(lblLuuY);
        formContainer.add(pLuuY);

        add(formContainer, BorderLayout.CENTER);

        // 3. PHẦN ĐÁY (Footer): Nút Hủy và Nút Lưu bên phải
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(12, 0, 4, 0));

        btnHuy = new JButton("Hủy") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNutHuyBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(mauNutHuyBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnHuy.setPreferredSize(new Dimension(116, 44));
        btnHuy.setFont(fontNut);
        btnHuy.setForeground(mauNutHuyText);
        btnHuy.setFocusPainted(false);
        btnHuy.setBorderPainted(false);
        btnHuy.setContentAreaFilled(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.addActionListener(e -> xuLyHuy());

        btnLuu = new JButton("Lưu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? mauCamHover : mauCamChinh);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLuu.setPreferredSize(new Dimension(116, 44));
        btnLuu.setFont(fontNut);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setBorderPainted(false);
        btnLuu.setContentAreaFilled(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> xuLyLuu());

        footerPanel.add(btnHuy);
        footerPanel.add(btnLuu);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel taoFormItem(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(fontNhanField);
        lbl.setForeground(mauNhanCot);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);
        return panel;
    }

    private JTextField taoTextField(String text, boolean editable) {
        JTextField tf = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenInput);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(mauVienInput);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setEditable(editable);
        tf.setFont(fontInput);
        tf.setPreferredSize(new Dimension(150, 42));
        tf.setBorder(new EmptyBorder(0, 14, 0, 14));
        return tf;
    }

    private JTextField taoTextFieldWithPlaceholder(String placeholder) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenInput);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(mauVienInput);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                super.paintComponent(g);

                if (getText().isEmpty() && !hasFocus()) {
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(mauPlaceholder);
                    g2.setFont(fontInput);
                    Insets insets = getInsets();
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, insets.left, y);
                }
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        tf.setFont(fontInput);
        tf.setForeground(mauChuTieuDe);
        tf.setPreferredSize(new Dimension(150, 42));
        tf.setBorder(new EmptyBorder(0, 14, 0, 14));
        return tf;
    }

    private JPasswordField taoPasswordFieldWithPlaceholder(String placeholder) {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (getPassword().length == 0 && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(mauPlaceholder);
                    g2.setFont(fontInput);
                    Insets insets = getInsets();
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, insets.left, y);
                    g2.dispose();
                }
            }
        };
        pf.setOpaque(false);
        pf.setFont(fontInput);
        pf.setForeground(mauChuTieuDe);
        pf.setBorder(null);
        return pf;
    }

    /**
     * Bọc ô JPasswordField kèm nút icon con mắt 👁 để ẩn/hiện mật khẩu
     */
    private JPanel taoPasswordFieldWithToggleEye(JPasswordField passwordField) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenInput);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(mauVienInput);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(150, 42));
        wrapper.setBorder(new EmptyBorder(0, 14, 0, 8));

        wrapper.add(passwordField, BorderLayout.CENTER);

        char defaultEchoChar = passwordField.getEchoChar();
        boolean[] isVisible = new boolean[]{false};

        JButton btnToggleEye = new JButton("👁") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(230, 235, 240));
                    g2.fillRoundRect(0, 3, getWidth(), getHeight() - 6, 6, 6);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnToggleEye.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btnToggleEye.setForeground(mauChuPhu);
        btnToggleEye.setPreferredSize(new Dimension(34, 34));
        btnToggleEye.setFocusPainted(false);
        btnToggleEye.setBorderPainted(false);
        btnToggleEye.setContentAreaFilled(false);
        btnToggleEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleEye.setToolTipText("Hiển thị / Ẩn mật khẩu");

        btnToggleEye.addActionListener(e -> {
            isVisible[0] = !isVisible[0];
            if (isVisible[0]) {
                passwordField.setEchoChar((char) 0);
                btnToggleEye.setForeground(mauCamChinh);
                btnToggleEye.setText("🔒");
                btnToggleEye.setToolTipText("Ẩn mật khẩu");
            } else {
                passwordField.setEchoChar(defaultEchoChar);
                btnToggleEye.setForeground(mauChuPhu);
                btnToggleEye.setText("👁");
                btnToggleEye.setToolTipText("Hiển thị mật khẩu");
            }
            passwordField.requestFocusInWindow();
        });

        wrapper.add(btnToggleEye, BorderLayout.EAST);
        return wrapper;
    }

    private JComboBox<String> taoComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(fontInput);
        cb.setForeground(mauPlaceholder);
        cb.setBackground(mauNenInput);
        cb.setPreferredSize(new Dimension(150, 42));
        cb.setFocusable(false);

        cb.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(mauPlaceholder);
                        int midX = getWidth() / 2 - 2;
                        int midY = getHeight() / 2 - 1;
                        int[] xPoints = {midX - 4, midX + 4, midX};
                        int[] yPoints = {midY - 2, midY - 2, midY + 3};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(mauNenInput);
                g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g2.dispose();
            }
        });

        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(8, 12, 8, 12));
                lbl.setFont(fontInput);
                if (index < 0) {
                    lbl.setForeground(mauPlaceholder);
                } else {
                    lbl.setForeground(mauChuTieuDe);
                }
                return lbl;
            }
        });

        cb.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.AbstractBorder() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(mauVienInput);
                    g2.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
                    g2.dispose();
                }
            },
            new EmptyBorder(0, 4, 0, 4)
        ));

        cb.addActionListener(e -> {
            int idx = cb.getSelectedIndex();
            if (idx == 0 && (cb.getItemAt(0).startsWith("Chọn"))) {
                cb.setForeground(mauPlaceholder);
            } else {
                cb.setForeground(mauChuTieuDe);
            }
        });

        return cb;
    }

    public void setMaNhanVienTuDong(String maNV) {
        if (txtMaNV != null) {
            txtMaNV.setText(maNV + " (tự động)");
        }
    }

    private void xuLyHuy() {
        if (onCancelListener != null) {
            onCancelListener.run();
        } else {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        }
    }

    private void xuLyLuu() {
        String hoTen = txtHoTen.getText().trim();
        String gioiTinh = (String) cbGioiTinh.getSelectedItem();
        String ngaySinh = txtNgaySinh.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String caLamViec = (String) cbCaLamViec.getSelectedItem();
        String chucVu = (String) cbChucVu.getSelectedItem();
        String diaChi = txtDiaChi.getText().trim();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword()).trim();

        // 1. Kiểm tra trống các trường cơ bản
        if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || ngaySinh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin: Họ tên, Ngày sinh, Số điện thoại và Email!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (gioiTinh == null || gioiTinh.equals("Chọn giới tính")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (chucVu == null || chucVu.equals("Chọn chức vụ")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ cho nhân viên!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Kiểm tra định dạng ngày sinh & tuổi >= 18
        LocalDate dob = parseNgaySinh(ngaySinh);
        if (dob == null) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ! Vui lòng nhập theo định dạng dd/mm/yyyy (ví dụ: 15/05/2000).", "Lỗi ngày sinh", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long tuoi = ChronoUnit.YEARS.between(dob, LocalDate.now());
        if (tuoi < 18) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải từ đủ 18 tuổi trở lên (Hiện tại: " + tuoi + " tuổi)!", "Quy định tuổi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Kiểm tra số điện thoại Việt Nam (10 số, bắt đầu bằng 0)
        if (!sdt.matches("^0[0-9]{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải đúng định dạng số Việt Nam gồm 10 chữ số (bắt đầu bằng số 0)!", "Lỗi số điện thoại", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Kiểm tra tài khoản & mật khẩu
        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Lỗi tài khoản", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Lỗi tài khoản", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không trùng khớp!", "Lỗi mật khẩu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo item nhân viên và tài khoản
        String rawMaNV = txtMaNV.getText().trim();
        String maNV = rawMaNV.contains(" ") ? rawMaNV.substring(0, rawMaNV.indexOf(" ")) : rawMaNV;
        EmployeeManagementPanel.NhanVienItem nvMoi = new EmployeeManagementPanel.NhanVienItem(
            maNV, hoTen, gioiTinh, ngaySinh, sdt, email, chucVu, caLamViec, trangThai
        );

        EmployeeManagementPanel.TaiKhoanItem tkMoi = new EmployeeManagementPanel.TaiKhoanItem(
            maNV, hoTen, tenDangNhap, matKhau, trangThai
        );

        if (onSaveListener != null) {
            onSaveListener.onSaveSuccess(nvMoi, tkMoi, diaChi);
        }

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            ((JDialog) window).dispose();
        }
    }

    private LocalDate parseNgaySinh(String dobStr) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
        };
        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(dobStr, fmt);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.onSaveListener = listener;
    }

    public void setOnCancelListener(Runnable listener) {
        this.onCancelListener = listener;
    }
}
