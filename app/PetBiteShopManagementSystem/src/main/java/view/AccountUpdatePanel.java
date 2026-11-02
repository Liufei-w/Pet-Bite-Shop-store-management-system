package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Giao diện màn hình / hộp thoại "Cập nhật tài khoản" cho Pet Bite Shop.
 * Chuẩn thiết kế UI theo Figma kèm icon con mắt ẩn/hiện mật khẩu.
 */
public class AccountUpdatePanel extends JPanel {

    // Màu sắc theo chuẩn giao diện mẫu
    private final Color mauNenChinh        = Color.WHITE;
    private final Color mauCamChinh        = new Color(245, 158, 35);   // #F59E23 (Nút Lưu cập nhật)
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
    private JTextField txtMaTK;
    private JComboBox<String> cbNhanVien;
    private JTextField txtTenDangNhap;
    private JComboBox<String> cbTrangThai;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;

    // Các nút thao tác
    private JButton btnHuy;
    private JButton btnLuuCapNhat;

    // Dữ liệu tài khoản đang sửa
    private EmployeeManagementPanel.TaiKhoanItem taiKhoanHienTai;

    public interface OnUpdateListener {
        void onUpdateSuccess(EmployeeManagementPanel.TaiKhoanItem tk);
    }
    private OnUpdateListener onUpdateListener;
    private Runnable onCancelListener;

    public AccountUpdatePanel(EmployeeManagementPanel.TaiKhoanItem tk, OnUpdateListener onUpdateListener, Runnable onCancelListener) {
        this.taiKhoanHienTai = tk;
        this.onUpdateListener = onUpdateListener;
        this.onCancelListener = onCancelListener;
        khoiTaoGiaoDien();
        napDuLieu(tk);
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout());
        setBackground(mauNenChinh);
        setBorder(new EmptyBorder(26, 32, 24, 32));

        // 1. PHẦN ĐẦU (Header): Tiêu đề và mô tả
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Cập nhật tài khoản");
        lblTitle.setFont(fontTieuDe);
        lblTitle.setForeground(mauChuTieuDe);
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(4));

        JLabel lblDesc = new JLabel("Chỉnh sửa thông tin tài khoản và lưu cập nhật.");
        lblDesc.setFont(fontMoTa);
        lblDesc.setForeground(mauChuPhu);
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        // 2. PHẦN THÂN (Form Body): 3 hàng x 2 cột + Banner Lưu ý
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(20, 0, 16, 0));

        // Hàng 1: Mã tài khoản & Nhân viên (2 cột)
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setOpaque(false);

        txtMaTK = taoTextField("", false);
        txtMaTK.setForeground(mauChuTieuDe);

        cbNhanVien = taoComboBox(new String[]{""});
        cbNhanVien.setEnabled(false); // Cố định theo tài khoản của nhân viên tương ứng

        row1.add(taoFormItem("Mã tài khoản", txtMaTK));
        row1.add(taoFormItem("Nhân viên", cbNhanVien));
        formContainer.add(row1);
        formContainer.add(Box.createVerticalStrut(16));

        // Hàng 2: Tên đăng nhập & Trạng thái (2 cột)
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setOpaque(false);

        txtTenDangNhap = taoTextField("", false);
        txtTenDangNhap.setForeground(mauChuTieuDe);

        cbTrangThai = taoComboBox(new String[]{"Hoạt động", "Bị khóa"});

        row2.add(taoFormItem("Tên đăng nhập", txtTenDangNhap));
        row2.add(taoFormItem("Trạng thái", cbTrangThai));
        formContainer.add(row2);
        formContainer.add(Box.createVerticalStrut(16));

        // Hàng 3: Mật khẩu & Xác nhận mật khẩu (2 cột, có con mắt ẩn/hiện)
        JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 0));
        row3.setOpaque(false);

        txtMatKhau = taoPasswordFieldWithPlaceholder("Nhập mật khẩu");
        JPanel pMatKhauWithEye = taoPasswordFieldWithToggleEye(txtMatKhau);

        txtXacNhanMatKhau = taoPasswordFieldWithPlaceholder("Nhập lại mật khẩu");
        JPanel pXacNhanMatKhauWithEye = taoPasswordFieldWithToggleEye(txtXacNhanMatKhau);

        row3.add(taoFormItem("Mật khẩu", pMatKhauWithEye));
        row3.add(taoFormItem("Xác nhận mật khẩu", pXacNhanMatKhauWithEye));
        formContainer.add(row3);
        formContainer.add(Box.createVerticalStrut(20));

        // Banner Ghi chú / Cảnh báo hợp lệ
        JPanel pLuuY = new JPanel();
        pLuuY.setLayout(new BoxLayout(pLuuY, BoxLayout.Y_AXIS));
        pLuuY.setOpaque(false);
        pLuuY.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel pLuuYWrapper = new JPanel(new BorderLayout()) {
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
        pLuuYWrapper.setOpaque(false);

        JLabel lblLuuY1 = new JLabel("• Mật khẩu tối thiểu 8 ký tự, có chữ hoa, chữ thường, chữ số và ký tự đặc biệt, không chứa khoảng trắng.");
        lblLuuY1.setFont(fontGhiChu);
        lblLuuY1.setForeground(mauChuLuuY);

        JLabel lblLuuY2 = new JLabel("• Nhân viên chỉ được tạo tài khoản khi đang làm việc và chưa có tài khoản khác.");
        lblLuuY2.setFont(fontGhiChu);
        lblLuuY2.setForeground(mauChuLuuY);

        pLuuY.add(lblLuuY1);
        pLuuY.add(Box.createVerticalStrut(6));
        pLuuY.add(lblLuuY2);
        pLuuYWrapper.add(pLuuY, BorderLayout.CENTER);

        formContainer.add(pLuuYWrapper);

        add(formContainer, BorderLayout.CENTER);

        // 3. PHẦN ĐÁY (Footer): Nút Hủy và Nút Lưu cập nhật bên phải
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(14, 0, 4, 0));

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

        btnLuuCapNhat = new JButton("Lưu cập nhật") {
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
        btnLuuCapNhat.setPreferredSize(new Dimension(150, 44));
        btnLuuCapNhat.setFont(fontNut);
        btnLuuCapNhat.setForeground(Color.WHITE);
        btnLuuCapNhat.setFocusPainted(false);
        btnLuuCapNhat.setBorderPainted(false);
        btnLuuCapNhat.setContentAreaFilled(false);
        btnLuuCapNhat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuuCapNhat.addActionListener(e -> xuLyLuu());

        footerPanel.add(btnHuy);
        footerPanel.add(btnLuuCapNhat);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void napDuLieu(EmployeeManagementPanel.TaiKhoanItem tk) {
        if (tk == null) return;
        // Sinh mã tài khoản dạng TK + mã nhân viên nếu chưa có
        String maTK = tk.maNV != null && tk.maNV.startsWith("NV") ? "TK" + tk.maNV.substring(2) : "TK" + tk.maNV;
        txtMaTK.setText(maTK);

        String nvInfo = tk.maNV + " - " + tk.hoTen;
        cbNhanVien.setModel(new DefaultComboBoxModel<>(new String[]{nvInfo}));
        cbNhanVien.setSelectedItem(nvInfo);

        txtTenDangNhap.setText(tk.tenDangNhap);
        cbTrangThai.setSelectedItem(tk.trangThai != null ? tk.trangThai : "Hoạt động");

        String passRaw = tk.matKhau != null ? tk.matKhau : "";
        if (passRaw.equalsIgnoreCase("f8d8a1f8392201070c68af1314ab8c76a3d450545a7e553dacf6d8a06f01b8cc")) {
            passRaw = "PetBite@123";
        }
        txtMatKhau.setText(passRaw);
        txtXacNhanMatKhau.setText(passRaw);
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
        cb.setForeground(mauChuTieuDe);
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
                lbl.setForeground(mauChuTieuDe);
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

        return cb;
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
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword()).trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (matKhau.length() < 8) {
            JOptionPane.showMessageDialog(this, "Mật khẩu tối thiểu phải từ 8 ký tự trở lên!", "Quy định mật khẩu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không trùng khớp!", "Lỗi xác nhận mật khẩu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        taiKhoanHienTai.matKhau = matKhau;
        taiKhoanHienTai.trangThai = trangThai;

        if (onUpdateListener != null) {
            onUpdateListener.onUpdateSuccess(taiKhoanHienTai);
        }

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            ((JDialog) window).dispose();
        }
    }
}
