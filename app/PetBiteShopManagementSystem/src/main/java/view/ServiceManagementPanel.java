package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ServiceManagementPanel extends JPanel {
    public ServiceManagementPanel() {
        setBackground(AppTheme.MAIN_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Header Info
        JPanel topInfo = new JPanel(new GridLayout(2, 1, 0, 4));
        topInfo.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.PRIMARY);
        
        JLabel lblDesc = new JLabel("Quản lý thông tin dịch vụ, thời gian thực hiện và trạng thái cung cấp");
        lblDesc.setFont(AppTheme.FONT_PLAIN);
        lblDesc.setForeground(AppTheme.TEXT_SECONDARY);
        
        topInfo.add(lblTitle);
        topInfo.add(lblDesc);

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel searchFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchFilter.setOpaque(false);
        
        JTextField txtSearch = new JTextField("  🔍 Mã dịch vụ hoặc tên dịch vụ", 22);
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setForeground(AppTheme.TEXT_SECONDARY);
        
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang cung cấp", "Ngừng cung cấp"});
        cbStatus.setPreferredSize(new Dimension(160, 35));
        
        searchFilter.add(txtSearch);
        searchFilter.add(cbStatus);

        JButton btnAdd = new JButton("+ Thêm dịch vụ");
        btnAdd.setBackground(AppTheme.PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(AppTheme.FONT_BOLD);
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new Dimension(130, 35));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        toolbar.add(searchFilter, BorderLayout.WEST);
        toolbar.add(btnAdd, BorderLayout.EAST);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.setOpaque(false);
        northWrapper.add(topInfo, BorderLayout.NORTH);
        northWrapper.add(toolbar, BorderLayout.CENTER);
        add(northWrapper, BorderLayout.NORTH);

        // Table Data
        String[] columns = {"Mã DV", "Tên dịch vụ", "Giá dịch vụ", "Thời gian", "Mô tả ngắn", "Trạng thái", "Thao tác"};
        Object[][] data = {
            {"DV261005001", "Tắm & sấy thú cưng", "150.000 đ", "60 phút", "Chăm sóc vệ sinh cơ bản", "Đang cung cấp", "✏️ Sửa"},
            {"DV261005002", "Cắt tỉa lông", "180.000 đ", "75 phút", "Làm đẹp và tạo kiểu lông", "Đang cung cấp", "✏️ Sửa"},
            {"DV261005003", "Vệ sinh tai", "70.000 đ", "30 phút", "Làm sạch tai cho thú cưng", "Đang cung cấp", "✏️ Sửa"},
            {"DV261005004", "Cắt móng", "50.000 đ", "20 phút", "Cắt móng và kiểm tra nhẹ", "Đang cung cấp", "✏️ Sửa"},
            {"DV261005005", "Combo chăm sóc cơ bản", "280.000 đ", "120 phút", "Tắm, sấy, cắt móng, vệ sinh tai", "Ngừng cung cấp", "✏️ Sửa"},
            {"DV261005006", "Xịt dưỡng lông", "40.000 đ", "15 phút", "Bổ sung dưỡng chất cho lông", "Đang cung cấp", "✏️ Sửa"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(AppTheme.BORDER);
        table.getTableHeader().setFont(AppTheme.FONT_BOLD);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER), "Danh sách dịch vụ", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, AppTheme.FONT_BOLD, AppTheme.TEXT_PRIMARY
        ));
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }
}