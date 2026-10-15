package app;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import view.HeaderPanel;
import view.SidebarPanel;

public class NavigationController {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private SidebarPanel sidebarPanel;
    private HeaderPanel headerPanel;
    private Map<String, String> screenTitles;

    public NavigationController(CardLayout cardLayout, JPanel contentPanel, SidebarPanel sidebarPanel, HeaderPanel headerPanel) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.sidebarPanel = sidebarPanel;
        this.headerPanel = headerPanel;

        screenTitles = new HashMap<>();
        screenTitles.put("DASHBOARD", "Dashboard");
        screenTitles.put("POS", "Bán hàng");
        screenTitles.put("PRODUCTS", "Quản lý sản phẩm");
        screenTitles.put("SERVICES", "Quản lý dịch vụ");
        screenTitles.put("IMPORT", "Nhập hàng");
        screenTitles.put("CUSTOMERS", "Quản lý khách hàng");
        screenTitles.put("PETS", "Quản lý thú cưng");
        screenTitles.put("APPOINTMENTS", "Quản lý lịch hẹn chăm sóc");
        screenTitles.put("HISTORY", "Lịch sử chăm sóc");
        screenTitles.put("INVOICES", "Hoá đơn");
        screenTitles.put("EMPLOYEES", "Quản lý nhân viên");
        screenTitles.put("ACCOUNTS", "Quản lý tài khoản");
        screenTitles.put("STATISTICS", "Thống kê & báo cáo");
    }

    public void switchScreen(String screenCode) {
        cardLayout.show(contentPanel, screenCode);
        sidebarPanel.setActiveMenu(screenCode);
        if (screenTitles.containsKey(screenCode)) {
            headerPanel.setBreadcrumb(screenTitles.get(screenCode));
        }
    }
}