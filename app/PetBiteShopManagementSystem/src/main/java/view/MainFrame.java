package view;

import java.awt.*;
import javax.swing.*;
import app.NavigationController;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private SidebarPanel sidebarPanel;
    private HeaderPanel headerPanel;
    private NavigationController navController;

    public MainFrame() {
        setTitle("PET BITE SHOP - Quản lý cửa hàng thú cưng");
        setSize(1250, 790);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        sidebarPanel = new SidebarPanel();
        headerPanel = new HeaderPanel();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.MAIN_BG);
        
        
        // Đăng ký các màn hình vào CardLayout
        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new CustomerManagementPanel(), "CUSTOMERS");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ THÚ CƯNG"), "PETS");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ LOẠI SẢN PHẨM"), "PRODUCT_TYPES");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ SẢN PHẨM"), "PRODUCTS");
        contentPanel.add(new ServiceManagementPanel(), "SERVICES");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ LỊCH HẸN"), "APPOINTMENTS");
        contentPanel.add(new PlaceholderPanel("LỊCH SỬ CHĂM SÓC"), "HISTORY");
        contentPanel.add(new PlaceholderPanel("BÁN HÀNG & THANH TOÁN"), "POS");
        contentPanel.add(new PlaceholderPanel("TRA CỨU HÓA ĐƠN"), "INVOICES");
        contentPanel.add(new PlaceholderPanel("PHIẾU TRẢ HÀNG & HOÀN TIỀN"), "RETURNS");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ NHÂN VIÊN"), "EMPLOYEES");
        contentPanel.add(new PlaceholderPanel("QUẢN LÝ TÀI KHOẢN"), "ACCOUNTS");
        contentPanel.add(new PlaceholderPanel("THỐNG KÊ DOANH THU"), "STAT_REVENUE");
        contentPanel.add(new PlaceholderPanel("THỐNG KÊ SỐ LƯỢNG SẢN PHẨM"), "STAT_PRODUCTS");
        contentPanel.add(new PlaceholderPanel("THỐNG KÊ LƯỢT DỊCH VỤ"), "STAT_SERVICES");
        contentPanel.add(new PlaceholderPanel("BÁO CÁO TỒN KHO"), "STAT_INVENTORY");

        // Khởi tạo NavigationController
        navController = new NavigationController(cardLayout, contentPanel, sidebarPanel, headerPanel);
        sidebarPanel.setNavigationController(navController);

        // Bố trí cục bộ MainFrame
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(AppTheme.MAIN_BG);
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        add(sidebarPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Mặc định hiển thị Quản lý dịch vụ giống hình tham chiếu
        navController.switchScreen("SERVICES");
    }
}