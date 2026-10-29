package view;

import app.UserSession;
import dao.TaiKhoan_DAO;
import dao.TaiKhoan_DAO.LoginResult;
import entity.TaiKhoan;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Màn hình Đăng nhập (LoginFrame) - Pet Bite Shop
 * Thiết kế chuẩn theo giao diện tham chiếu:
 * - Bên trái: Panel thông điệp thương hiệu, logo, tranh minh họa thú cưng và danh sách tính năng
 * - Bên phải: Khung đăng nhập hiện đại với đầy đủ tính năng kiểm tra tài khoản, hiện/ẩn mật khẩu, quên mật khẩu
 */
public class LoginFrame extends JFrame {

    // ===== Bảng màu chuẩn thiết kế =====
    private static final Color TEAL_DARK         = new Color(0x0C, 0x3A, 0x4A); // #0C3A4A
    private static final Color TEAL_GRADIENT_BOT = new Color(0x07, 0x2A, 0x36); // #072A36
    private static final Color TEAL_ACCENT       = new Color(0x1B, 0x6E, 0x7C); // #1B6E7C
    private static final Color TEAL_LIGHT_TEXT   = new Color(0x94, 0xB6, 0xC4); // #94B6C4
    private static final Color ORANGE_PRIMARY    = new Color(0xF5, 0x9E, 0x0B); // #F59E0B / #FFA03A
    private static final Color ORANGE_HOVER      = new Color(0xE8, 0x8E, 0x00); // #E88E00
    private static final Color CARD_BEIGE_BG     = new Color(0xF6, 0xEF, 0xE5); // #F6EFE5
    private static final Color RIGHT_BG          = new Color(0xF8, 0xFA, 0xFC); // #F8FAFC
    private static final Color TEXT_DARK         = new Color(0x11, 0x18, 0x27); // #111827
    private static final Color TEXT_MUTED        = new Color(0x64, 0x74, 0x8B); // #64748B
    private static final Color BORDER_GRAY       = new Color(0xE2, 0xE8, 0xF0); // #E2E8F0
    private static final Color BORDER_FOCUSED    = new Color(0x1B, 0x6E, 0x7C); // #1B6E7C

    // DAO xác thực
    private final TaiKhoan_DAO taiKhoanDAO;

    // Các thành phần tương tác
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JButton btnLogin;
    private JLabel lblErrorMsg;
    private JPanel usernameContainer;
    private JPanel passwordContainer;
    private boolean isUsernameFocused = false;
    private boolean isPasswordFocused = false;

    public LoginFrame() {
        this.taiKhoanDAO = new TaiKhoan_DAO();

        setTitle("PET BITE SHOP - Đăng nhập hệ thống");
        setSize(1100, 720);
        setMinimumSize(new Dimension(980, 640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Icon ứng dụng
        try {
            URL iconUrl = getClass().getResource("/logo_cua_hang.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {
        }

        // Bố cục chính chia làm 2 phần (Trái: 440px cố định, Phải: co giãn)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(createLeftPanel(), BorderLayout.WEST);
        mainContainer.add(createRightPanel(), BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    // =========================================================================
    // 1. PANEL BÊN TRÁI: THƯƠNG HIỆU & MINH HỌA
    // =========================================================================
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, TEAL_DARK, 0, getHeight(), TEAL_GRADIENT_BOT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        leftPanel.setPreferredSize(new Dimension(440, 0));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(36, 44, 36, 36));

        // 1. Logo
        JComponent logoComp = buildBrandLogo();
        logoComp.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(logoComp);
        leftPanel.add(Box.createVerticalStrut(28));

        // 2. Thanh gạch ngang cam ngắn
        JPanel orangeBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ORANGE_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        orangeBar.setOpaque(false);
        orangeBar.setMaximumSize(new Dimension(36, 4));
        orangeBar.setPreferredSize(new Dimension(36, 4));
        orangeBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(orangeBar);
        leftPanel.add(Box.createVerticalStrut(20));

        // 3. Tiêu đề lớn: "Yêu thương mỗi ngày, chăm sóc trọn vẹn."
        JLabel lblHeading1 = new JLabel("Yêu thương mỗi ngày,");
        lblHeading1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeading1.setForeground(Color.WHITE);
        lblHeading1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHeading2 = new JLabel("chăm sóc trọn vẹn.");
        lblHeading2.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeading2.setForeground(Color.WHITE);
        lblHeading2.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(lblHeading1);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(lblHeading2);
        leftPanel.add(Box.createVerticalStrut(12));

        // 4. Mô tả phụ
        JLabel lblDesc = new JLabel("Giải pháp quản lý dành riêng cho cửa hàng thú cưng.");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(TEAL_LIGHT_TEXT);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(lblDesc);
        leftPanel.add(Box.createVerticalStrut(26));

        // 5. Thẻ minh họa thú cưng đáng yêu
        PetIllustrationCard illustrationCard = new PetIllustrationCard();
        illustrationCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(illustrationCard);
        leftPanel.add(Box.createVerticalStrut(28));

        // 6. Danh sách tính năng chính (chấm tròn cam)
        leftPanel.add(createFeatureItem("Quản lý khách hàng & thú cưng"));
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(createFeatureItem("Lịch hẹn & dịch vụ chăm sóc"));
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(createFeatureItem("Bán hàng & báo cáo"));

        leftPanel.add(Box.createVerticalGlue());
        return leftPanel;
    }

    private JComponent buildBrandLogo() {
        try {
            URL imgUrl = getClass().getResource("/Logo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                if (w > 0 && h > 0) {
                    double scale = Math.min(190.0 / w, 50.0 / h);
                    Image img = icon.getImage().getScaledInstance((int) (w * scale), (int) (h * scale), Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(img));
                }
            }
        } catch (Exception ignored) {
        }

        // Vẽ logo dự phòng đẹp mắt nếu file ảnh không tải được
        JPanel logoFallback = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Vòng tròn trắng
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawOval(2, 4, 38, 38);
                // Dấu chân thú cưng
                g2.fillOval(14, 24, 14, 11);
                g2.fillOval(11, 17, 5, 7);
                g2.fillOval(17, 13, 5, 8);
                g2.fillOval(23, 13, 5, 8);
                g2.fillOval(29, 17, 5, 7);
                g2.dispose();
            }
        };
        logoFallback.setOpaque(false);
        logoFallback.setPreferredSize(new Dimension(200, 48));

        JLabel lblText = new JLabel("<html><span style='color:white;font-weight:bold;font-size:16px;'>PET BITE</span><br><span style='color:#F59E0B;font-weight:bold;font-size:9px;'>SHOP</span></html>");
        lblText.setBorder(new EmptyBorder(0, 44, 0, 0));
        logoFallback.add(lblText);
        return logoFallback;
    }

    private JPanel createFeatureItem(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Chấm tròn cam
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ORANGE_PRIMARY);
                g2.fillOval(0, 3, 7, 7);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(8, 14));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(0xD6, 0xE4, 0xEC));

        row.add(dot);
        row.add(lbl);
        return row;
    }

    // =========================================================================
    // 2. PANEL BÊN PHẢI: FORM ĐĂNG NHẬP
    // =========================================================================
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Nền sáng tổng thể
                g2.setColor(RIGHT_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Quầng sáng trang trí màu kem/đào nhẹ góc trên bên phải
                int auraSize = Math.max(340, getWidth() / 2);
                RadialGradientPaint rgp = new RadialGradientPaint(
                    getWidth() - 30, 10, auraSize,
                    new float[]{0.0f, 0.45f, 0.85f, 1.0f},
                    new Color[]{
                        new Color(0xFD, 0xEE, 0xDC, 220),
                        new Color(0xFD, 0xF3, 0xE7, 130),
                        new Color(0xFE, 0xFA, 0xF4, 50),
                        new Color(0xF8, 0xFA, 0xFC, 0)
                    }
                );
                g2.setPaint(rgp);
                g2.fillOval(getWidth() - auraSize, -auraSize / 3, auraSize * 2, auraSize * 2);
                g2.dispose();
            }
        };

        // Khung nội dung trung tâm (rộng ~470px để các dòng chữ hiển thị trọn vẹn)
        JPanel centerContent = new JPanel();
        centerContent.setOpaque(false);
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setPreferredSize(new Dimension(470, 600));

        // 1. Nhãn phân mục
        JLabel lblKicker = new JLabel("PET BITE SHOP • QUẢN LÝ CỬA HÀNG");
        lblKicker.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblKicker.setForeground(TEAL_ACCENT);
        lblKicker.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. Tiêu đề chào mừng
        JLabel lblTitle = new JLabel("Chào mừng trở lại!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 29));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3. Phụ đề
        JLabel lblSubtitle = new JLabel("Đăng nhập để tiếp tục quản lý hoạt động cửa hàng.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(TEXT_MUTED);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerContent.add(lblKicker);
        centerContent.add(Box.createVerticalStrut(12));
        centerContent.add(lblTitle);
        centerContent.add(Box.createVerticalStrut(6));
        centerContent.add(lblSubtitle);
        centerContent.add(Box.createVerticalStrut(24));

        // 4. Thẻ Form Đăng Nhập màu trắng bo góc
        JPanel formCard = createFormCard();
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerContent.add(formCard);
        centerContent.add(Box.createVerticalStrut(22));

        // 5. Ghi chú phân quyền dưới card
        JLabel lblNote = new JLabel("Chỉ dành cho nhân viên và chủ cửa hàng được cấp quyền.", SwingConstants.CENTER);
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNote.setForeground(new Color(0x94, 0xA3, 0xB8));
        lblNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(lblNote);
        centerContent.add(Box.createVerticalStrut(18));

        // 6. Bản quyền chân trang
        JLabel lblCopyright = new JLabel("© 2026 PET BITE SHOP", SwingConstants.CENTER);
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(0x94, 0xA3, 0xB8));
        lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(lblCopyright);

        rightPanel.add(centerContent);
        return rightPanel;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth() - 1;
                int h = getHeight() - 1;
                // Đổ bóng mềm
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fillRoundRect(2, 3, w - 2, h - 2, 20, 20);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(1, 2, w - 1, h - 1, 20, 20);
                // Nền trắng thẻ
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 20, 20);
                // Viền thẻ
                g2.setColor(BORDER_GRAY);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, w, h, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(460, 360));
        card.setMaximumSize(new Dimension(460, 370));

        // Tiêu đề form
        JLabel lblFormTitle = new JLabel("Thông tin đăng nhập");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(0x1E, 0x29, 0x3B));
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblFormTitle);
        card.add(Box.createVerticalStrut(18));

        // Nhãn "Tên đăng nhập"
        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(new Color(0x37, 0x41, 0x51));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblUsername);
        card.add(Box.createVerticalStrut(8));

        // Ô nhập "Tên đăng nhập"
        usernameContainer = createInputContainer(true);
        usernameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(usernameContainer);
        card.add(Box.createVerticalStrut(16));

        // Nhãn "Mật khẩu"
        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(0x37, 0x41, 0x51));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblPassword);
        card.add(Box.createVerticalStrut(8));

        // Ô nhập "Mật khẩu"
        passwordContainer = createInputContainer(false);
        passwordContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passwordContainer);
        card.add(Box.createVerticalStrut(14));

        // Dòng tùy chọn: "Hiện mật khẩu" & "Quên mật khẩu?"
        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkShowPassword = new JCheckBox("Hiện mật khẩu");
        chkShowPassword.setOpaque(false);
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPassword.setForeground(new Color(0x47, 0x55, 0x69));
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('\u2022');
            }
        });
        optionsRow.add(chkShowPassword, BorderLayout.WEST);

        JLabel lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblForgotPassword.setForeground(TEAL_ACCENT);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiHuongDanQuenMatKhau();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblForgotPassword.setText("<html><u>Quên mật khẩu?</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblForgotPassword.setText("Quên mật khẩu?");
            }
        });
        optionsRow.add(lblForgotPassword, BorderLayout.EAST);
        card.add(optionsRow);
        card.add(Box.createVerticalStrut(12));

        // Thông báo trạng thái / lỗi
        lblErrorMsg = new JLabel("");
        lblErrorMsg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblErrorMsg.setForeground(new Color(0xDC, 0x26, 0x26));
        lblErrorMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblErrorMsg.setVisible(false);
        card.add(lblErrorMsg);
        card.add(Box.createVerticalStrut(6));

        // Nút "Đăng nhập  ➔" với mũi tên vẽ vector đảm bảo không bao giờ lỗi font
        btnLogin = new JButton("Đăng nhập") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Màu nền nút
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0xD9, 0x77, 0x06));
                } else if (getModel().isRollover()) {
                    g2.setColor(ORANGE_HOVER);
                } else {
                    g2.setColor(ORANGE_PRIMARY);
                }
                g2.fillRoundRect(0, 0, w, h, 12, 12);

                // Chữ và mũi tên
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int textW = fm.stringWidth(text);
                int totalW = textW + 18; // text + khoảng cách + mũi tên
                int startX = (w - totalW) / 2;
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

                g2.drawString(text, startX, textY);

                // Vẽ mũi tên ➔
                int arrowStartX = startX + textW + 8;
                int arrowMidY = h / 2;
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(arrowStartX, arrowMidY, arrowStartX + 10, arrowMidY);
                g2.drawLine(arrowStartX + 6, arrowMidY - 4, arrowStartX + 10, arrowMidY);
                g2.drawLine(arrowStartX + 6, arrowMidY + 4, arrowStartX + 10, arrowMidY);

                g2.dispose();
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setPreferredSize(new Dimension(390, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> xuLyDangNhap());

        // Nhấn Enter để đăng nhập
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDangNhap();
            }
        };
        txtUsername.addActionListener(enterAction);
        txtPassword.addActionListener(enterAction);

        card.add(btnLogin);
        return card;
    }

    /**
     * Tạo khung nhập liệu bo góc với icon ở bên trái
     */
    private JPanel createInputContainer(boolean isUsername) {
        JPanel container = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth() - 1;
                int h = getHeight() - 1;

                // Nền ô nhập
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 10, 10);

                // Đường viền thay đổi khi focus
                boolean focused = isUsername ? isUsernameFocused : isPasswordFocused;
                if (focused) {
                    g2.setColor(BORDER_FOCUSED);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER_GRAY);
                    g2.setStroke(new BasicStroke(1.0f));
                }
                g2.drawRoundRect(0, 0, w, h, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        container.setPreferredSize(new Dimension(390, 44));
        container.setBorder(new EmptyBorder(0, 14, 0, 14));

        // Icon bên trái vẽ vector sắc nét
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color iconColor = (isUsername ? isUsernameFocused : isPasswordFocused) ? TEAL_ACCENT : new Color(0x94, 0xA3, 0xB8);
                g2.setColor(iconColor);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (isUsername) {
                    // Biểu tượng người (User icon)
                    g2.drawOval(4, 2, 8, 8);
                    g2.drawArc(1, 11, 14, 10, 0, 180);
                } else {
                    // Biểu tượng ổ khóa thanh mảnh (Lock icon)
                    g2.drawRoundRect(4, 1, 8, 8, 4, 4); // móc khóa
                    g2.drawRoundRect(2, 7, 12, 10, 3, 3); // thân khóa
                    g2.fillOval(7, 10, 2, 2);
                    g2.drawLine(8, 12, 8, 14);
                }
                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(18, 20));
        container.add(iconPanel, BorderLayout.WEST);

        if (isUsername) {
            txtUsername = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().isEmpty() && !isFocusOwner()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setColor(new Color(0x94, 0xA3, 0xB8));
                        g2.setFont(getFont());
                        FontMetrics fm = g2.getFontMetrics();
                        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                        g2.drawString("Nhập tên đăng nhập", 0, y);
                        g2.dispose();
                    }
                }
            };
            txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtUsername.setForeground(TEXT_DARK);
            txtUsername.setBorder(null);
            txtUsername.setOpaque(false);
            txtUsername.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isUsernameFocused = true;
                    container.repaint();
                    txtUsername.repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    isUsernameFocused = false;
                    container.repaint();
                    txtUsername.repaint();
                }
            });
            container.add(txtUsername, BorderLayout.CENTER);
        } else {
            txtPassword = new JPasswordField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getPassword().length == 0 && !isFocusOwner()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setColor(new Color(0x94, 0xA3, 0xB8));
                        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                        FontMetrics fm = g2.getFontMetrics();
                        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                        g2.drawString("••••••••", 0, y);
                        g2.dispose();
                    }
                }
            };
            txtPassword.setEchoChar('\u2022');
            txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtPassword.setForeground(TEXT_DARK);
            txtPassword.setBorder(null);
            txtPassword.setOpaque(false);
            txtPassword.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isPasswordFocused = true;
                    container.repaint();
                    txtPassword.repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    isPasswordFocused = false;
                    container.repaint();
                    txtPassword.repaint();
                }
            });
            container.add(txtPassword, BorderLayout.CENTER);
        }

        return container;
    }

    // =========================================================================
    // 3. XỬ LÝ NGHIỆP VỤ ĐĂNG NHẬP
    // =========================================================================
    private void xuLyDangNhap() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        lblErrorMsg.setVisible(false);

        if (username.isEmpty()) {
            hienLoi("Vui lòng nhập tên đăng nhập!");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            hienLoi("Vui lòng nhập mật khẩu!");
            txtPassword.requestFocus();
            return;
        }

        // Trạng thái đang xử lý
        btnLogin.setEnabled(false);

        SwingUtilities.invokeLater(() -> {
            try {
                LoginResult result = taiKhoanDAO.dangNhap(username, password);

                if (result.isSuccess()) {
                    TaiKhoan taiKhoan = result.getTaiKhoan();
                    // Lưu phiên người dùng toàn cục
                    UserSession.getInstance().setTaiKhoan(taiKhoan);

                    lblErrorMsg.setForeground(new Color(0x16, 0xA3, 0x4A)); // Xanh lá thành công
                    lblErrorMsg.setText("✓ " + result.getMessage());
                    lblErrorMsg.setVisible(true);

                    // Chuyển sang màn hình chính MainFrame
                    Timer timer = new Timer(350, evt -> {
                        dispose(); // Đóng LoginFrame
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    hienLoi(result.getMessage());
                    btnLogin.setEnabled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                hienLoi("Lỗi hệ thống: " + ex.getMessage());
                btnLogin.setEnabled(true);
            }
        });
    }

    private void hienLoi(String msg) {
        lblErrorMsg.setForeground(new Color(0xDC, 0x26, 0x26));
        lblErrorMsg.setText("⚠ " + msg);
        lblErrorMsg.setVisible(true);
    }

    private void hienThiHuongDanQuenMatKhau() {
        String thongBao = "<html><body style='width: 320px; font-family: Segoe UI, sans-serif;'>"
                + "<h3 style='color: #0C3A4A; margin-bottom: 8px;'>Khôi phục mật khẩu tài khoản</h3>"
                + "<p style='color: #475569; font-size: 11px;'>"
                + "Vì lý do bảo mật hệ thống nội bộ Pet Bite Shop, vui lòng liên hệ trực tiếp:"
                + "</p>"
                + "<ul style='color: #1E293B; font-size: 11px;'>"
                + "<li><b>Quản trị viên hệ thống (Admin)</b></li>"
                + "<li><b>Chủ cửa hàng (Quản lý)</b></li>"
                + "</ul>"
                + "<p style='color: #64748B; font-size: 10px; margin-top: 10px;'>"
                + "<i>Gợi ý mật khẩu mặc định dữ liệu mẫu: <b>PetBite@123</b></i>"
                + "</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(this, thongBao, "Hỗ trợ tài khoản Pet Bite Shop", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    // 4. THẺ MINH HỌA CHÓ & MÈO ĐÁNG YÊU (PET ILLUSTRATION CARD)
    // =========================================================================
    private static class PetIllustrationCard extends JPanel {
        PetIllustrationCard() {
            setOpaque(false);
            setMaximumSize(new Dimension(350, 186));
            setPreferredSize(new Dimension(350, 186));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 1. Thẻ màu kem bo góc tròn mềm mại
            g2.setColor(CARD_BEIGE_BG);
            g2.fillRoundRect(0, 0, w, h, 24, 24);

            // Tọa độ trung tâm 2 chú thú cưng
            int dogCenterX = w / 2 - 62;
            int dogCenterY = 70;
            int catCenterX = w / 2 + 62;
            int catCenterY = 70;

            // ----- VẼ CHÚ CHÓ NÂU VÀNG (BÊN TRÁI) -----
            // Tai trái & tai phải rủ xuống màu nâu sẫm
            g2.setColor(new Color(0x8D, 0x48, 0x24));
            g2.fillRoundRect(dogCenterX - 46, dogCenterY - 24, 26, 52, 20, 20);
            g2.fillRoundRect(dogCenterX + 20, dogCenterY - 24, 26, 52, 20, 20);

            // Đầu chú chó màu nâu vàng
            g2.setColor(new Color(0xE7, 0xA8, 0x67));
            g2.fillOval(dogCenterX - 40, dogCenterY - 36, 80, 80);

            // Mắt chú chó
            g2.setColor(new Color(0x3B, 0x22, 0x14));
            g2.fillOval(dogCenterX - 20, dogCenterY - 2, 7, 7);
            g2.fillOval(dogCenterX + 13, dogCenterY - 2, 7, 7);

            // Vùng mõm sáng màu kem
            g2.setColor(new Color(0xF9, 0xE8, 0xD4));
            g2.fillOval(dogCenterX - 22, dogCenterY + 4, 44, 34);

            // Mũi chú chó hình oval màu nâu đen
            g2.setColor(new Color(0x3B, 0x22, 0x14));
            g2.fillOval(dogCenterX - 8, dogCenterY + 10, 16, 11);

            // Nụ cười chú chó
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(dogCenterX - 9, dogCenterY + 16, 9, 8, 190, 160);
            g2.drawArc(dogCenterX, dogCenterY + 16, 9, 8, 190, 160);

            // ----- VẼ CHÚ MÈO XANH NGỌC XÁM (BÊN PHẢI) -----
            // Tai mèo hình tam giác
            int[] leftEarX = {catCenterX - 36, catCenterX - 18, catCenterX - 34};
            int[] leftEarY = {catCenterY - 14, catCenterY - 42, catCenterY - 46};
            int[] rightEarX = {catCenterX + 18, catCenterX + 36, catCenterX + 34};
            int[] rightEarY = {catCenterY - 42, catCenterY - 14, catCenterY - 46};

            g2.setColor(new Color(0x7C, 0x9F, 0x9F));
            g2.fillPolygon(leftEarX, leftEarY, 3);
            g2.fillPolygon(rightEarX, rightEarY, 3);

            // Đầu chú mèo
            g2.setColor(new Color(0x7C, 0x9F, 0x9F));
            g2.fillOval(catCenterX - 39, dogCenterY - 34, 78, 78);

            // Mắt chú mèo
            g2.setColor(new Color(0x24, 0x47, 0x48));
            g2.fillOval(catCenterX - 18, catCenterY - 1, 7, 7);
            g2.fillOval(catCenterX + 11, catCenterY - 1, 7, 7);

            // Mũi chú mèo
            g2.setColor(new Color(0x24, 0x47, 0x48));
            int[] catNoseX = {catCenterX - 5, catCenterX + 5, catCenterX};
            int[] catNoseY = {catCenterY + 11, catCenterY + 11, catCenterY + 16};
            g2.fillPolygon(catNoseX, catNoseY, 3);

            // Miệng chú mèo
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(catCenterX - 7, catCenterY + 15, 7, 6, 200, 150);
            g2.drawArc(catCenterX, catCenterY + 15, 7, 6, 190, 150);

            // Râu mèo 2 bên má
            g2.drawLine(catCenterX - 22, catCenterY + 8, catCenterX - 35, catCenterY + 5);
            g2.drawLine(catCenterX - 22, catCenterY + 15, catCenterX - 35, catCenterY + 17);
            g2.drawLine(catCenterX + 22, catCenterY + 8, catCenterX + 35, catCenterY + 5);
            g2.drawLine(catCenterX + 22, catCenterY + 15, catCenterX + 35, catCenterY + 17);

            // Vòng cung trang trí giữa 2 bé
            g2.setColor(new Color(0xD8, 0xBC, 0x9E));
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(w / 2 - 16, dogCenterY + 44, 32, 14, 0, 180);

            // Dòng chữ thương hiệu: "CHĂM SÓC • KẾT NỐI • TIN CẬY"
            g2.setColor(new Color(0x1E, 0x4D, 0x5C));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String slogan = "CHĂM SÓC • KẾT NỐI • TIN CẬY";
            FontMetrics fm = g2.getFontMetrics();
            int sloganX = (w - fm.stringWidth(slogan)) / 2;
            g2.drawString(slogan, sloganX, h - 22);

            g2.dispose();
        }
    }
}
