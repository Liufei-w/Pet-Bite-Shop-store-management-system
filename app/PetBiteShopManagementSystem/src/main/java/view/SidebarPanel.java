package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import app.NavigationController;

public class SidebarPanel extends JPanel {
    private static final String LOGO_PATH = "/Logo.png";

    // Kích thước
    private static final int SIDEBAR_WIDTH = 260;
    private static final int ITEM_WIDTH    = 236;
    private static final int ITEM_HEIGHT   = 38;
    private static final int FOOTER_ITEM_H = 34;

    // Màu & font cho sidebar
    private static final Color MENU_TEXT  = new Color(0xDC, 0xE6, 0xF0); // chữ menu thường
    private static final Color MENU_TITLE = new Color(0x8F, 0xA9, 0xC4); // "MENU CHÍNH"
    private static final Color DOT_COLOR  = new Color(0xA9, 0xBC, 0xCF); // chấm tròn chưa active
    private static final Font MENU_FONT      = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font MENU_FONT_BOLD = new Font("SansSerif", Font.BOLD, 14);

    private Map<String, JPanel> menuItems = new LinkedHashMap<>();
    private Map<String, JLabel> dotLabels = new LinkedHashMap<>();
    private Map<String, JLabel> textLabels = new LinkedHashMap<>();
    private Map<String, JPanel> subPanels = new LinkedHashMap<>();
    private NavigationController navController;
    private String currentActiveCode = "DASHBOARD";

    public SidebarPanel() {
        setBackground(AppTheme.SIDEBAR_BG);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setLayout(new BorderLayout());

        // ===== 1. Logo =====
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 100));

        GridBagConstraints lg = new GridBagConstraints();
        lg.anchor = GridBagConstraints.WEST;
        lg.weightx = 1;
        lg.insets = new Insets(0, 24, 0, 0);
        logoPanel.add(buildLogo(), lg);
        add(logoPanel, BorderLayout.NORTH);

        // ===== 2. Menu =====
        JPanel menuListPanel = new JPanel();
        menuListPanel.setOpaque(false);
        menuListPanel.setLayout(new BoxLayout(menuListPanel, BoxLayout.Y_AXIS));
        menuListPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        menuListPanel.add(createDividerLine());
        menuListPanel.add(Box.createVerticalStrut(14));

        JLabel lblMenuTitle = new JLabel("MENU CHÍNH");
        lblMenuTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblMenuTitle.setForeground(MENU_TITLE);
        lblMenuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(0, 4, 8, 0));
        menuListPanel.add(lblMenuTitle);

        addMenuItem(menuListPanel, "DASHBOARD", "Dashboard");
        addMenuItem(menuListPanel, "CUSTOMERS", "Khách hàng");
        addMenuItem(menuListPanel, "PETS", "Thú cưng");
        addMenuItem(menuListPanel, "PRODUCT_TYPES", "Loại sản phẩm");
        addMenuItem(menuListPanel, "PRODUCTS", "Sản phẩm");
        addMenuItem(menuListPanel, "SERVICES", "Dịch vụ");
        addMenuItem(menuListPanel, "APPOINTMENTS", "Lịch hẹn");
        addMenuItem(menuListPanel, "HISTORY", "Lịch sử chăm sóc");
        addMenuItem(menuListPanel, "POS", "Bán hàng");
        addMenuItem(menuListPanel, "INVOICES", "Hóa đơn");
        addMenuItem(menuListPanel, "RETURNS", "Phiếu trả hàng");
        addMenuItem(menuListPanel, "EMPLOYEES", "Nhân viên");
        addMenuItem(menuListPanel, "ACCOUNTS", "Tài khoản");

        List<String[]> statSubs = new ArrayList<>();
        statSubs.add(new String[]{"STAT_REVENUE", "Thống kê doanh thu"});
        statSubs.add(new String[]{"STAT_PRODUCTS", "Thống kê số lượng sản phẩm"});
        statSubs.add(new String[]{"STAT_SERVICES", "Thống kê lượt sử dụng dịch vụ"});
        statSubs.add(new String[]{"STAT_INVENTORY", "Báo cáo tồn kho"});
        addParentMenuItem(menuListPanel, "STAT_PARENT", "Thống kê & báo cáo", statSubs);

        JScrollPane scrollPane = new JScrollPane(menuListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ===== 3. Footer =====
        JPanel footerWrapper = new JPanel();
        footerWrapper.setOpaque(false);
        footerWrapper.setLayout(new BoxLayout(footerWrapper, BoxLayout.Y_AXIS));
        footerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 12, 30, 12));

        footerWrapper.add(createDividerLine());
        footerWrapper.add(Box.createVerticalStrut(12));

        JPanel profileItem = createFooterItem("⚙", "Cá nhân");
        JPanel logoutItem = createFooterItem("↪", "Đăng xuất");

        profileItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (navController != null) navController.switchScreen("PROFILE");
            }
        });
        logoutItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        footerWrapper.add(profileItem);
        footerWrapper.add(Box.createVerticalStrut(2));
        footerWrapper.add(logoutItem);
        add(footerWrapper, BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------- Logo
    private JComponent buildLogo() {
        try {
            URL imgUrl = getClass().getResource(LOGO_PATH);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                if (w > 0 && h > 0) {
                    double scale = Math.min(210.0 / w, 56.0 / h);
                    Image img = icon.getImage().getScaledInstance(
                            (int) (w * scale), (int) (h * scale), Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(img));
                }
            }
        } catch (Exception ignored) {
        }
        return new FallbackLogo();
    }

    /** Logo vẽ tay khi không có file Logo.png: vòng tròn + dấu chân + "PET BITE" / "SHOP". */
    private static class FallbackLogo extends JPanel {
        FallbackLogo() {
            setOpaque(false);
            setPreferredSize(new Dimension(210, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Vòng tròn
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(2, 6, 44, 44);

            // Dấu chân
            g2.fillOval(16, 29, 16, 13);
            g2.fillOval(13, 21, 6, 8);
            g2.fillOval(20, 16, 6, 9);
            g2.fillOval(27, 16, 6, 9);
            g2.fillOval(34, 21, 6, 8);

            // Chữ
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            g2.drawString("PET BITE", 58, 32);
            g2.setColor(AppTheme.PRIMARY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString("SHOP", 59, 46);
            g2.dispose();
        }
    }

    // ---------------------------------------------------------------- Divider
    private JComponent createDividerLine() {
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(AppTheme.BORDER_LINE);
                g.drawLine(11, 0, getWidth() - 11, 0);
            }
        };
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        line.setMaximumSize(new Dimension(ITEM_WIDTH, 1));
        line.setPreferredSize(new Dimension(ITEM_WIDTH, 1));
        return line;
    }

    // ---------------------------------------------------------------- Menu items
    private void addMenuItem(JPanel parent, String code, String text) {
        JPanel itemPanel = createMenuPanel(text, code, 0);
        menuItems.put(code, itemPanel);
        attachMenuEvents(itemPanel, code);
        parent.add(itemPanel);
        parent.add(Box.createVerticalStrut(3));
    }

    private void addParentMenuItem(JPanel parentPanel, String parentCode, String titleText, List<String[]> subItems) {
        JPanel parentItem = createMenuPanel(titleText, parentCode, 0);
        menuItems.put(parentCode, parentItem);

        JPanel subContainer = new JPanel();
        subContainer.setOpaque(false);
        subContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.Y_AXIS));
        subContainer.setVisible(false);
        subPanels.put(parentCode, subContainer);

        for (String[] item : subItems) {
            String code = item[0];
            JPanel subItem = createMenuPanel(item[1], code, 16);
            menuItems.put(code, subItem);
            attachMenuEvents(subItem, code);
            subContainer.add(subItem);
            subContainer.add(Box.createVerticalStrut(2));
        }

        parentItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                subContainer.setVisible(!subContainer.isVisible());
                parentPanel.revalidate();
                parentPanel.repaint();
            }
            public void mouseEntered(MouseEvent e) {
                if (!parentCode.equals(currentActiveCode)) {
                    parentItem.setBackground(AppTheme.HOVER_BG);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!parentCode.equals(currentActiveCode)) {
                    parentItem.setBackground(AppTheme.SIDEBAR_BG);
                }
            }
        });

        parentPanel.add(parentItem);
        parentPanel.add(subContainer);
        parentPanel.add(Box.createVerticalStrut(3));
    }

    private void attachMenuEvents(JPanel panel, String code) {
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (navController != null) navController.switchScreen(code);
            }
            public void mouseEntered(MouseEvent e) {
                if (!code.equals(currentActiveCode)) {
                    panel.setBackground(AppTheme.HOVER_BG);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!code.equals(currentActiveCode)) {
                    panel.setBackground(AppTheme.SIDEBAR_BG);
                }
            }
        });
    }

    /** Panel bo góc, dùng chung cho mục menu và footer (nền vẽ theo getBackground()). */
    private JPanel createRoundedPanel(int height) {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color bg = getBackground();
                if (bg.equals(AppTheme.ACTIVE_BG) || bg.equals(AppTheme.HOVER_BG)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.dispose();
                }
            }
        };
        panel.setOpaque(false);
        panel.setBackground(AppTheme.SIDEBAR_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(ITEM_WIDTH, height));
        panel.setPreferredSize(new Dimension(ITEM_WIDTH, height));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return panel;
    }

    // Một dòng menu: chấm tròn + chữ
    private JPanel createMenuPanel(String text, String code, int extraIndent) {
        JPanel panel = createRoundedPanel(ITEM_HEIGHT);

        JLabel lblDot = new JLabel("●", SwingConstants.CENTER);
        lblDot.setFont(new Font("SansSerif", Font.BOLD, 8));
        lblDot.setForeground(DOT_COLOR);
        lblDot.setPreferredSize(new Dimension(10, 16));
        dotLabels.put(code, lblDot);

        JLabel lblText = new JLabel(text);
        lblText.setFont(MENU_FONT);
        lblText.setForeground(MENU_TEXT);
        textLabels.put(code, lblText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 15 + extraIndent, 0, 0);
        panel.add(lblDot, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 28, 0, 8);
        panel.add(lblText, gbc);

        return panel;
    }

    // Mục footer: icon + chữ đậm trắng, không có chấm tròn
    private JPanel createFooterItem(String icon, String text) {
        JPanel panel = createRoundedPanel(FOOTER_ITEM_H);

        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Dialog", Font.BOLD, 15));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setPreferredSize(new Dimension(18, 18));

        JLabel lblText = new JLabel(text);
        lblText.setFont(MENU_FONT_BOLD);
        lblText.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 12, 0, 0);
        panel.add(lblIcon, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 6, 0, 8);
        panel.add(lblText, gbc);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(AppTheme.HOVER_BG);
                panel.repaint();
            }
            public void mouseExited(MouseEvent e) {
                panel.setBackground(AppTheme.SIDEBAR_BG);
                panel.repaint();
            }
        });
        return panel;
    }

    public void setNavigationController(NavigationController navController) {
        this.navController = navController;
    }

    // Active: nền #205677, chấm cam #FFB14B, chữ trắng đậm
    public void setActiveMenu(String screenCode) {
        this.currentActiveCode = screenCode;

        for (Map.Entry<String, JPanel> entry : menuItems.entrySet()) {
            String code = entry.getKey();
            JPanel panel = entry.getValue();
            JLabel lblDot = dotLabels.get(code);
            JLabel lblText = textLabels.get(code);

            if (code.equals(screenCode)) {
                panel.setBackground(AppTheme.ACTIVE_BG);
                if (lblDot != null) lblDot.setForeground(AppTheme.PRIMARY);
                if (lblText != null) {
                    lblText.setFont(MENU_FONT_BOLD);
                    lblText.setForeground(Color.WHITE);
                }
            } else {
                panel.setBackground(AppTheme.SIDEBAR_BG);
                if (lblDot != null) lblDot.setForeground(DOT_COLOR);
                if (lblText != null) {
                    lblText.setFont(MENU_FONT);
                    lblText.setForeground(MENU_TEXT);
                }
            }
            panel.repaint();
        }

        if (screenCode.startsWith("STAT_")) {
            JPanel sub = subPanels.get("STAT_PARENT");
            if (sub != null && !sub.isVisible()) {
                sub.setVisible(true);
                sub.getParent().revalidate();
            }
        }
    }
}