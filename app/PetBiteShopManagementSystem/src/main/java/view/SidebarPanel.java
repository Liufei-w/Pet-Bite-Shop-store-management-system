package view;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import app.NavigationController;

public class SidebarPanel extends JPanel {
    // Đường dẫn trỏ tới file logo.png trong thư mục src/main/resources/logo.png
    private static final String LOGO_PATH = "/logo.png"; 

    private Map<String, JButton> mainButtons = new LinkedHashMap<>();
    private Map<String, JButton> subButtons = new LinkedHashMap<>();
    private Map<String, JPanel> subPanels = new LinkedHashMap<>();
    private NavigationController navController;
    private String currentActiveCode = "SERVICES";

    public SidebarPanel() {
        setBackground(AppTheme.SIDEBAR_BG);
        setPreferredSize(new Dimension(260, 0));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER));

        // 1. TOP: KHU VỰC CHỈ HIỂN THỊ LOGO (Đã xóa bỏ text và icon mẫu)
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(260, 100));
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel lblLogoImage = new JLabel();
        boolean loadedImage = false;

        // Tải ảnh logo từ thư mục resources
        try {
            URL imgUrl = getClass().getResource(LOGO_PATH);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                // Cân chỉnh kích thước ảnh logo cho phù hợp với chiều rộng Sidebar (vd: rộng 160px, cao 70px)
                Image img = icon.getImage().getScaledInstance(160, 70, Image.SCALE_SMOOTH);
                lblLogoImage.setIcon(new ImageIcon(img));
                loadedImage = true;
            }
        } catch (Exception e) {
            loadedImage = false;
        }

        // Trường hợp không tìm thấy file, hiển thị thông báo nhắc nhở nhỏ để dễ kiểm tra
        if (!loadedImage) {
            lblLogoImage.setText("[Chưa tìm thấy logo.png]");
            lblLogoImage.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblLogoImage.setForeground(Color.RED);
        }

        logoPanel.add(lblLogoImage);
        add(logoPanel, BorderLayout.NORTH);

        // 2. CENTER: DANH SÁCH MENU (HỖ TRỢ SUBMENU SỔ XUỐNG)
        JPanel menuListPanel = new JPanel();
        menuListPanel.setOpaque(false);
        menuListPanel.setLayout(new BoxLayout(menuListPanel, BoxLayout.Y_AXIS));

        addSingleMenu(menuListPanel, "DASHBOARD", "🏠  Dashboard");
        addSingleMenu(menuListPanel, "POS", "🛒  Bán hàng");
        addSingleMenu(menuListPanel, "PRODUCTS", "📦  Quản lý sản phẩm");
        addSingleMenu(menuListPanel, "SERVICES", "✂️  Quản lý dịch vụ");

        // Menu Nhập hàng (Có menu con sổ xuống)
        List<String[]> importSubs = new ArrayList<>();
        importSubs.add(new String[]{"IMPORT_CREATE", "Tạo phiếu nhập"});
        importSubs.add(new String[]{"IMPORT_SEARCH", "Tìm kiếm phiếu nhập"});
        addParentMenu(menuListPanel, "IMPORT_PARENT", "📥  Nhập hàng", importSubs);

        addSingleMenu(menuListPanel, "CUSTOMERS", "👥  Quản lý khách hàng");
        addSingleMenu(menuListPanel, "PETS", "🐶  Quản lý thú cưng");
        addSingleMenu(menuListPanel, "APPOINTMENTS", "📅  Quản lý lịch hẹn chăm sóc");
        addSingleMenu(menuListPanel, "HISTORY", "🕒  Lịch sử chăm sóc");

        // Menu Hoá đơn (Có menu con sổ xuống)
        List<String[]> invoiceSubs = new ArrayList<>();
        invoiceSubs.add(new String[]{"INVOICE_SEARCH", "Tra cứu hoá đơn"});
        addParentMenu(menuListPanel, "INVOICE_PARENT", "📄  Hoá đơn", invoiceSubs);

        addSingleMenu(menuListPanel, "EMPLOYEES", "👤  Quản lý nhân viên");
        addSingleMenu(menuListPanel, "ACCOUNTS", "⚙️  Quản lý tài khoản");
        addSingleMenu(menuListPanel, "STATISTICS", "📊  Thống kê & báo cáo");

        JScrollPane scrollPane = new JScrollPane(menuListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addSingleMenu(JPanel parent, String code, String text) {
        JButton btn = createMenuButton(text, 20);
        mainButtons.put(code, btn);
        btn.addActionListener(e -> {
            if (navController != null) navController.switchScreen(code);
        });
        parent.add(btn);
        parent.add(Box.createVerticalStrut(2));
    }

    private void addParentMenu(JPanel parentPanel, String parentCode, String titleText, List<String[]> subItems) {
        JButton btnParent = createMenuButton(titleText + "   >", 20);
        mainButtons.put(parentCode, btnParent);

        JPanel subContainer = new JPanel();
        subContainer.setOpaque(false);
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.Y_AXIS));
        subContainer.setVisible(false); // Mặc định ẩn
        subPanels.put(parentCode, subContainer);

        for (String[] item : subItems) {
            String code = item[0];
            String text = item[1];
            JButton btnSub = createMenuButton(text, 35); // Thụt lề trái cho menu con
            subButtons.put(code, btnSub);

            btnSub.addActionListener(e -> {
                if (navController != null) navController.switchScreen(code);
            });
            subContainer.add(btnSub);
            subContainer.add(Box.createVerticalStrut(2));
        }

        btnParent.addActionListener(e -> {
            boolean isVisible = subContainer.isVisible();
            subContainer.setVisible(!isVisible);
            btnParent.setText(titleText + (isVisible ? "   >" : "   v"));
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        parentPanel.add(btnParent);
        parentPanel.add(subContainer);
        parentPanel.add(Box.createVerticalStrut(2));
    }

    private JButton createMenuButton(String text, int paddingLeft) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(AppTheme.FONT_PLAIN);
        btn.setForeground(AppTheme.TEXT_PRIMARY);
        btn.setBackground(AppTheme.SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(240, 38));
        btn.setPreferredSize(new Dimension(240, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, paddingLeft, 0, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(AppTheme.ACTIVE_BG)) {
                    btn.setBackground(new Color(250, 238, 235));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(AppTheme.ACTIVE_BG)) {
                    btn.setBackground(AppTheme.SIDEBAR_BG);
                }
            }
        });

        return btn;
    }

    public void setNavigationController(NavigationController navController) {
        this.navController = navController;
    }

    public void setActiveMenu(String screenCode) {
        this.currentActiveCode = screenCode;

        for (JButton b : mainButtons.values()) {
            b.setBackground(AppTheme.SIDEBAR_BG);
            b.setForeground(AppTheme.TEXT_PRIMARY);
            b.setFont(AppTheme.FONT_PLAIN);
        }
        for (JButton b : subButtons.values()) {
            b.setBackground(AppTheme.SIDEBAR_BG);
            b.setForeground(AppTheme.TEXT_PRIMARY);
            b.setFont(AppTheme.FONT_PLAIN);
        }

        if (mainButtons.containsKey(screenCode)) {
            JButton btn = mainButtons.get(screenCode);
            btn.setBackground(AppTheme.ACTIVE_BG);
            btn.setForeground(AppTheme.PRIMARY);
            btn.setFont(AppTheme.FONT_BOLD);
        } else if (subButtons.containsKey(screenCode)) {
            JButton btn = subButtons.get(screenCode);
            btn.setBackground(AppTheme.ACTIVE_BG);
            btn.setForeground(AppTheme.PRIMARY);
            btn.setFont(AppTheme.FONT_BOLD);

            if (screenCode.startsWith("IMPORT")) {
                JPanel sub = subPanels.get("IMPORT_PARENT");
                if (sub != null && !sub.isVisible()) {
                    sub.setVisible(true);
                    mainButtons.get("IMPORT_PARENT").setText("📥  Nhập hàng   v");
                }
            } else if (screenCode.startsWith("INVOICE")) {
                JPanel sub = subPanels.get("INVOICE_PARENT");
                if (sub != null && !sub.isVisible()) {
                    sub.setVisible(true);
                    mainButtons.get("INVOICE_PARENT").setText("📄  Hoá đơn   v");
                }
            }
        }
    }
}