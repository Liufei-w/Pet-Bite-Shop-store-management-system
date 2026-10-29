package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JComboBox<String> cbStatus;
    private JComboBox<String> cbSort;
    private JButton btnAdd;

    public CustomerManagementPanel() {
        // Thiết lập layout và màu nền 
        setBackground(AppTheme.MAIN_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 1. HEADER TOP
        JPanel topInfo = new JPanel(new GridLayout(2, 1, 0, 4));
        topInfo.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Quản lý khách hàng");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        JLabel lblDesc = new JLabel("Tra cứu, thêm, cập nhật và quản lý trạng thái khách hàng");
        lblDesc.setFont(AppTheme.FONT_PLAIN);
        lblDesc.setForeground(AppTheme.TEXT_SECONDARY);
        
        topInfo.add(lblTitle);
        topInfo.add(lblDesc);

        // 2. TOOLBAR (Tìm kiếm, Lọc, Nút Thêm)
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel searchFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchFilter.setOpaque(false);
        
        txtSearch = new JTextField("   🔍 Tìm theo mã khách hàng, họ tên hoặc số điện thoại", 28);
        txtSearch.setPreferredSize(new Dimension(320, 38));
        txtSearch.setFont(AppTheme.FONT_PLAIN);
        
        cbStatus = new JComboBox<>(new String[]{"Trạng thái", "Hoạt động", "Ngừng"});
        cbStatus.setPreferredSize(new Dimension(130, 38));
        cbStatus.setFont(AppTheme.FONT_PLAIN);
        
        cbSort = new JComboBox<>(new String[]{"Sắp xếp: Mới nhất", "Cũ nhất"});
        cbSort.setPreferredSize(new Dimension(150, 38));
        cbSort.setFont(AppTheme.FONT_PLAIN);

        searchFilter.add(txtSearch);
        searchFilter.add(cbStatus);
        searchFilter.add(cbSort);

        btnAdd = new JButton("+ Thêm khách hàng");
        btnAdd.setBackground(AppTheme.PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(AppTheme.FONT_BOLD);
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new Dimension(160, 38));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        toolbar.add(searchFilter, BorderLayout.WEST);
        toolbar.add(btnAdd, BorderLayout.EAST);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.setOpaque(false);
        northWrapper.add(topInfo, BorderLayout.NORTH);
        northWrapper.add(toolbar, BorderLayout.CENTER);
        add(northWrapper, BorderLayout.NORTH);

        // 3. TABLE DATA 
        String[] columns = {"Mã KH", "Họ và tên", "Giới tính", "Ngày sinh", "SĐT", "Email", "Địa chỉ", "Trạng thái", "Thao tác"};
        Object[][] data = {
            {"KH241018001", "Nguyễn Minh Anh", "Nữ", "12/05/2002", "0903123456", "minhanh@gmail.com", "12 Nguyễn Trãi, Q.5", "Hoạt động", "Sửa"},
            {"KH241018002", "Trần Quốc Bảo", "Nam", "21/09/1999", "0987654321", "quocbao@gmail.com", "45 Lê Lợi, Q.1", "Hoạt động", "Sửa"},
            {"KH241018003", "Lê Mỹ Linh", "Nữ", "08/12/2001", "0378123456", "mylinh@gmail.com", "18 Trường Chinh, Q.TB", "Ngừng", "Sửa"},
            {"KH241018004", "Phạm Gia Hưng", "Nam", "03/03/1998", "0856789123", "giahung@gmail.com", "220 Điện Biên Phủ, Q.3", "Hoạt động", "Sửa"},
            {"KH241018005", "Hoàng Thu Hà", "Nữ", "15/07/2000", "0791234567", "thuha@gmail.com", "85 Phan Xích Long, PN", "Hoạt động", "Sửa"}
        };

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép thao tác qua nút bấm, không cho sửa trực tiếp trên bảng
                return false; 
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(45);
        table.setShowVerticalLines(false);
        table.setGridColor(AppTheme.BORDER);
        table.getTableHeader().setFont(AppTheme.FONT_BOLD);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFont(AppTheme.FONT_PLAIN);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER), "Danh sách khách hàng", 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, AppTheme.FONT_BOLD, AppTheme.TEXT_PRIMARY
        ));
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // 4. XỬ LÝ SỰ KIỆN (EVENTS) CƠ BẢN 
        initEventHandlers();
    }

    private void initEventHandlers() {
        // Sự kiện nút Thêm khách hàng
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(CustomerManagementPanel.this, 
                    "Mở form thêm khách hàng mới (Sẽ tích hợp ở giai đoạn tiếp theo).", 
                    "Thêm khách hàng", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Sự kiện click vào bảng (để xử lý nút Sửa hoặc chọn dòng)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                // Nếu click vào cột "Thao tác" (cột thứ 8)
                if (row >= 0 && col == 8) {
                    String maKH = (String) model.getValueAt(row, 0);
                    String tenKH = (String) model.getValueAt(row, 1);
                    JOptionPane.showMessageDialog(CustomerManagementPanel.this, 
                        "Mở form cập nhật thông tin cho khách hàng: " + tenKH + " (" + maKH + ")", 
                        "Cập nhật khách hàng", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}