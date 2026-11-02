package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Giao diện màn hình / hộp thoại "Cập nhật nhân viên" cho Pet Bite Shop.
 * Chuẩn thiết kế UI theo Figma.
 */
public class EmployeeUpdatePanel extends JPanel {

    // Màu sắc theo chuẩn giao diện mẫu
    private final Color mauNenChinh        = Color.WHITE;
    private final Color mauCamChinh        = new Color(245, 158, 35);   // #F59E23 (Nút Lưu cập nhật)
    private final Color mauCamHover        = new Color(230, 142, 25);
    private final Color mauChuTieuDe       = new Color(30, 41, 59);     // #1E293B
    private final Color mauChuPhu          = new Color(138, 153, 168);  // #8A99A8
    private final Color mauNhanCot         = new Color(28, 43, 62);     // #1C2B3E
    private final Color mauVienInput       = new Color(229, 235, 242);  // #E5EBF2 viền xám xanh nhạt
    private final Color mauNenInput        = new Color(248, 250, 252);  // #F8FAFC
    private final Color mauNenDisabled     = new Color(241, 245, 249);  // Nền xám mờ cho ô không cho sửa
    private final Color mauChuDisabled     = new Color(148, 163, 184);  // Chữ xám mờ
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
    private JComboBox<String> cbChucVu;
    private JComboBox<String> cbCaLamViec;
    private JTextField txtDiaChi;

    // Các nút thao tác
    private JButton btnHuy;
    private JButton btnLuuCapNhat;

    // Dữ liệu nhân viên đang chỉnh sửa
    private EmployeeManagementPanel.NhanVienItem nhanVienHienTai;

    public interface OnUpdateListener {
        void onUpdateSuccess(EmployeeManagementPanel.NhanVienItem nv, String diaChi);
    }
    private OnUpdateListener onUpdateListener;
    private Runnable onCancelListener;

    public EmployeeUpdatePanel(EmployeeManagementPanel.NhanVienItem nv, OnUpdateListener onUpdateListener, Runnable onCancelListener) {
        this.nhanVienHienTai = nv;
        this.onUpdateListener = onUpdateListener;
        this.onCancelListener = onCancelListener;
        khoiTaoGiaoDien();
        napDuLieu(nv);
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout());
        setBackground(mauNenChinh);
        setBorder(new EmptyBorder(26, 32, 24, 32));

        // 1. PHẦN ĐẦU (Header): Tiêu đề và mô tả
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Cập nhật nhân viên");
        lblTitle.setFont(fontTieuDe);
        lblTitle.setForeground(mauChuTieuDe);
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(4));

        JLabel lblDesc = new JLabel("Chỉnh sửa thông tin nhân viên và lưu cập nhật.");
        lblDesc.setFont(fontMoTa);
        lblDesc.setForeground(mauChuPhu);
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        // 2. PHẦN THÂN (Form Body):
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(18, 0, 16, 0));

        // Hàng 1: Mã nhân viên & Họ và tên (2 cột tỉ lệ 1:1)
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setOpaque(false);

        txtMaNV = taoTextFieldDisabled(""); // Làm mờ, không cho sửa
        txtHoTen = taoTextFieldWithPlaceholder("Nhập họ và tên");

        row1.add(taoFormItem("Mã nhân viên", txtMaNV));
        row1.add(taoFormItem("Họ và tên", txtHoTen));
        formContainer.add(row1);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 2: Giới tính, Ngày sinh, Số điện thoại (3 cột)
        JPanel row2 = new JPanel(new GridLayout(1, 3, 20, 0));
        row2.setOpaque(false);

        cbGioiTinh = taoComboBox(new String[]{"Nam", "Nữ"});
        txtNgaySinh = taoTextFieldWithPlaceholder("dd/mm/yyyy");
        txtSoDienThoai = taoTextFieldWithPlaceholder("Nhập số điện thoại");

        row2.add(taoFormItem("Giới tính", cbGioiTinh));
        row2.add(taoFormItem("Ngày sinh", txtNgaySinh));
        row2.add(taoFormItem("Số điện thoại", txtSoDienThoai));
        formContainer.add(row2);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 3: Email & Chức vụ (2 cột)
        JPanel row3 = new JPanel(new GridLayout(1, 2, 20, 0));
        row3.setOpaque(false);

        txtEmail = taoTextFieldWithPlaceholder("Nhập địa chỉ email");
        cbChucVu = taoComboBox(new String[]{"Chăm sóc", "Thu ngân"});

        row3.add(taoFormItem("Email", txtEmail));
        row3.add(taoFormItem("Chức vụ", cbChucVu));
        formContainer.add(row3);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 4: Ca làm việc (cột 1 chiếm 1/2 hàng như hình mẫu)
        JPanel row4 = new JPanel(new GridLayout(1, 2, 20, 0));
        row4.setOpaque(false);

        cbCaLamViec = taoComboBox(new String[]{"Ca sáng", "Ca tối", "Ca hành chính"});

        row4.add(taoFormItem("Ca làm việc", cbCaLamViec));
        row4.add(Box.createGlue());
        formContainer.add(row4);
        formContainer.add(Box.createVerticalStrut(14));

        // Hàng 5: Địa chỉ (Full width)
        JPanel row5 = new JPanel(new BorderLayout());
        row5.setOpaque(false);
        txtDiaChi = taoTextFieldWithPlaceholder("Nhập địa chỉ nhân viên");
        txtDiaChi.setPreferredSize(new Dimension(txtDiaChi.getPreferredSize().width, 68));
        row5.add(taoFormItem("Địa chỉ", txtDiaChi), BorderLayout.CENTER);
        formContainer.add(row5);
        formContainer.add(Box.createVerticalStrut(14));

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

        // 3. PHẦN ĐÁY (Footer): Nút Hủy và Nút Lưu cập nhật bên phải
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

    private void napDuLieu(EmployeeManagementPanel.NhanVienItem nv) {
        if (nv == null) return;
        txtMaNV.setText(nv.maNV);
        txtHoTen.setText(nv.hoTen);
        cbGioiTinh.setSelectedItem(nv.gioiTinh);
        txtNgaySinh.setText(nv.ngaySinh);
        txtSoDienThoai.setText(nv.soDienThoai);
        txtEmail.setText(nv.email);
        cbChucVu.setSelectedItem(nv.chucVu);
        cbCaLamViec.setSelectedItem(nv.caLamViec);
        txtDiaChi.setText("12 Nguyễn Trãi, Quận 5, TP.HCM");
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

    /**
     * Tạo ô text làm mờ và không cho sửa cho Mã nhân viên
     */
    private JTextField taoTextFieldDisabled(String text) {
        JTextField tf = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenDisabled);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(mauVienInput);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setEditable(false);
        tf.setFocusable(false);
        tf.setFont(fontInput);
        tf.setForeground(mauChuDisabled); // Màu chữ xám mờ
        tf.setPreferredSize(new Dimension(150, 42));
        tf.setBorder(new EmptyBorder(0, 14, 0, 14));
        tf.setCursor(Cursor.getDefaultCursor());
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
        String hoTen = txtHoTen.getText().trim();
        String gioiTinh = (String) cbGioiTinh.getSelectedItem();
        String ngaySinh = txtNgaySinh.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        String chucVu = (String) cbChucVu.getSelectedItem();
        String caLamViec = (String) cbCaLamViec.getSelectedItem();
        String diaChi = txtDiaChi.getText().trim();

        if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || ngaySinh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ họ tên, ngày sinh, số điện thoại và email!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate dob = parseNgaySinh(ngaySinh);
        if (dob == null) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ (dd/mm/yyyy)!", "Lỗi ngày sinh", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long tuoi = ChronoUnit.YEARS.between(dob, LocalDate.now());
        if (tuoi < 18) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải từ đủ 18 tuổi trở lên (Hiện tại: " + tuoi + " tuổi)!", "Quy định tuổi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!sdt.matches("^0[0-9]{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải đúng định dạng số Việt Nam 10 chữ số (bắt đầu bằng 0)!", "Lỗi số điện thoại", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nhanVienHienTai.hoTen = hoTen;
        nhanVienHienTai.gioiTinh = gioiTinh;
        nhanVienHienTai.ngaySinh = ngaySinh;
        nhanVienHienTai.soDienThoai = sdt;
        nhanVienHienTai.email = email;
        nhanVienHienTai.chucVu = chucVu;
        nhanVienHienTai.caLamViec = caLamViec;

        if (onUpdateListener != null) {
            onUpdateListener.onUpdateSuccess(nhanVienHienTai, diaChi);
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
}

