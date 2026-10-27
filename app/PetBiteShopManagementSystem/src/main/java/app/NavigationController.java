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

        // BẮT BUỘC PHẢI KHỞI TẠO HASHMAP TRƯỚC KHI DÙNG .put()
        this.screenTitles = new HashMap<>();

        // Đăng ký tiêu đề cho từng màn hình
        screenTitles.put("DASHBOARD", "Trang chủ / Dashboard");
        screenTitles.put("CUSTOMERS", "Quản lý khách hàng");
        screenTitles.put("PETS", "Quản lý thú cưng");
        screenTitles.put("PRODUCT_TYPES", "Quản lý loại sản phẩm");
        screenTitles.put("PRODUCTS", "Quản lý sản phẩm");
        screenTitles.put("SERVICES", "Quản lý dịch vụ");
        screenTitles.put("APPOINTMENTS", "Quản lý lịch hẹn chăm sóc");
        screenTitles.put("HISTORY", "Tra cứu lịch sử chăm sóc");
        screenTitles.put("POS", "Bán hàng trực tiếp");
        screenTitles.put("INVOICES", "Tra cứu hóa đơn");
        screenTitles.put("RETURNS", "Quản lý phiếu trả hàng");
        screenTitles.put("EMPLOYEES", "Quản lý nhân viên");
        screenTitles.put("ACCOUNTS", "Quản lý tài khoản hệ thống");
        screenTitles.put("STAT_REVENUE", "Thống kê & báo cáo > Thống kê doanh thu");
        screenTitles.put("STAT_PRODUCTS", "Thống kê & báo cáo > Thống kê số lượng sản phẩm");
        screenTitles.put("STAT_SERVICES", "Thống kê & báo cáo > Thống kê lượt sử dụng dịch vụ");
        screenTitles.put("STAT_INVENTORY", "Thống kê & báo cáo > Báo cáo tồn kho");
    }

    public void switchScreen(String screenCode) {
        cardLayout.show(contentPanel, screenCode);
        sidebarPanel.setActiveMenu(screenCode);
        if (screenTitles.containsKey(screenCode)) {
            headerPanel.setBreadcrumb(screenTitles.get(screenCode));
        }
    }
}