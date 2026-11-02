package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ThongKe_DAO;
import entity.SanPham;

public class ThongKePanel extends JPanel implements ActionListener {

    private String currentScreenCode;
    private ThongKe_DAO thongKeDAO = new ThongKe_DAO();

    // Components lọc dữ liệu & Panel chứa thanh lọc
    private JPanel panelFilter;
    private JComboBox<String> cboKieuThoiGian;
    private JTextField txtTuNgay, txtDenNgay;
    private JButton btnThongKe, btnXuatExcel;

    // Thẻ Card tổng quan
    private JLabel lblValCard1, lblTitleCard1;
    private JLabel lblValCard2, lblTitleCard2;
    private JLabel lblValCard3, lblTitleCard3;
    private JLabel lblValCard4, lblTitleCard4;

    // Bảng chi tiết
    private JTable tblChiTiet;
    private DefaultTableModel modelTable;

    public ThongKePanel(String screenCode) {
        this.currentScreenCode = screenCode;
        setLayout(new BorderLayout(0, 16));
        setBackground(AppTheme.MAIN_BG);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        panelFilter = createFilterPanel();
        add(panelFilter, BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
        
        setupScreenMode(screenCode);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        cboKieuThoiGian = new JComboBox<>(new String[]{"Hôm nay", "Theo Tháng", "Theo Năm", "Tùy chọn khoảng"});
        cboKieuThoiGian.setFont(AppTheme.FONT_PLAIN);
        
        // Bắt sự kiện thay đổi lựa chọn thời gian trên ComboBox để tự động cập nhật mốc ngày
        cboKieuThoiGian.addActionListener(e -> updateDateRangeBySelection());

        LocalDate now = LocalDate.now();
        txtTuNgay = new JTextField(now.withDayOfMonth(1).toString(), 9);
        txtDenNgay = new JTextField(now.toString(), 9);

        btnThongKe = createButton("Xem thống kê", AppTheme.SIDEBAR_BG, Color.WHITE);
        btnXuatExcel = createButton("Xuất Excel", new Color(40, 167, 69), Color.WHITE);

        btnThongKe.addActionListener(this);
        btnXuatExcel.addActionListener(this);

        panel.add(createLabel("Thời gian:"));
        panel.add(cboKieuThoiGian);
        panel.add(createLabel("Từ ngày:"));
        panel.add(txtTuNgay);
        panel.add(createLabel("Đến ngày:"));
        panel.add(txtDenNgay);
        panel.add(btnThongKe);
        panel.add(btnXuatExcel);

        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setOpaque(false);

        // Cards Tổng Quan
        JPanel panelCards = new JPanel(new GridLayout(1, 4, 16, 0));
        panelCards.setOpaque(false);
        panelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        lblTitleCard1 = createCardTitle("TỔNG");
        lblValCard1 = createCardValue("0", AppTheme.SIDEBAR_BG);
        panelCards.add(createCardPanel(lblTitleCard1, lblValCard1, AppTheme.SIDEBAR_BG));

        lblTitleCard2 = createCardTitle("THÔNG SỐ 2");
        lblValCard2 = createCardValue("0", new Color(23, 162, 184));
        panelCards.add(createCardPanel(lblTitleCard2, lblValCard2, new Color(23, 162, 184)));

        lblTitleCard3 = createCardTitle("THÔNG SỐ 3");
        lblValCard3 = createCardValue("0", new Color(255, 165, 0));
        panelCards.add(createCardPanel(lblTitleCard3, lblValCard3, new Color(255, 165, 0)));

        lblTitleCard4 = createCardTitle("THÔNG SỐ 4");
        lblValCard4 = createCardValue("0", new Color(220, 53, 69));
        panelCards.add(createCardPanel(lblTitleCard4, lblValCard4, new Color(220, 53, 69)));

        panelMain.add(panelCards);
        panelMain.add(Box.createRigidArea(new Dimension(0, 16)));

        // Bảng Chi Tiết bọc trong BorderLayout
        JPanel panelTableContainer = new JPanel(new BorderLayout());
        panelTableContainer.setOpaque(false);

        modelTable = new DefaultTableModel();
        tblChiTiet = new JTable(modelTable);
        tblChiTiet.setFont(AppTheme.FONT_PLAIN);
        tblChiTiet.setRowHeight(32);
        tblChiTiet.getTableHeader().setFont(AppTheme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1));
        panelTableContainer.add(scrollPane, BorderLayout.CENTER);

        panelMain.add(panelTableContainer);

        return panelMain;
    }

    /** Tự động điền ngày tháng tương ứng khi người dùng chọn ComboBox */
    private void updateDateRangeBySelection() {
        String selected = (String) cboKieuThoiGian.getSelectedItem();
        LocalDate now = LocalDate.now();

        if (selected == null) return;

        switch (selected) {
            case "Hôm nay":
                txtTuNgay.setText(now.toString());
                txtDenNgay.setText(now.toString());
                txtTuNgay.setEnabled(false);
                txtDenNgay.setEnabled(false);
                break;
            case "Theo Tháng":
                txtTuNgay.setText(now.withDayOfMonth(1).toString());
                txtDenNgay.setText(now.toString());
                txtTuNgay.setEnabled(false);
                txtDenNgay.setEnabled(false);
                break;
            case "Theo Năm":
                txtTuNgay.setText(now.withDayOfYear(1).toString());
                txtDenNgay.setText(now.toString());
                txtTuNgay.setEnabled(false);
                txtDenNgay.setEnabled(false);
                break;
            case "Tùy chọn khoảng":
                txtTuNgay.setEnabled(true);
                txtDenNgay.setEnabled(true);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnThongKe)) {
            loadDataToTable();
        } else if (o.equals(btnXuatExcel)) {
            JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang được phát triển!");
        }
    }

    public void loadDataToTable() {
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();
        modelTable.setRowCount(0);

        DecimalFormat dfMoney = new DecimalFormat("#,##0 VNĐ");

        if (currentScreenCode.equals("STAT_REVENUE")) {
            ArrayList<Object[]> list = thongKeDAO.getThongKeDoanhThu(tuNgay, denNgay);
            double tongTienChuaVAT = 0;
            double tongThanhTien = 0;
            double tongHoanTra = 0;

            for (Object[] row : list) {
                modelTable.addRow(row);
                String loai = (String) row[3];
                double thanhTien = (double) row[6];
                double chuaVAT = (double) row[5];

                if (loai.equals("Bán hàng")) {
                    tongTienChuaVAT += chuaVAT;
                    tongThanhTien += thanhTien;
                } else if (loai.equals("Trả hàng")) {
                    tongHoanTra += Math.abs(thanhTien);
                }
            }

            lblTitleCard1.setText("TỔNG DOANH THU (CHƯA VAT)");
            lblValCard1.setText(dfMoney.format(tongTienChuaVAT));

            lblTitleCard2.setText("TỔNG THANH TOÁN (GỒM VAT)");
            lblValCard2.setText(dfMoney.format(tongThanhTien));

            lblTitleCard3.setText("TỔNG TIỀN HOÀN TRẢ");
            lblValCard3.setText(dfMoney.format(tongHoanTra));

            lblTitleCard4.setText("DOANH THU THỰC TẾ");
            lblValCard4.setText(dfMoney.format(tongTienChuaVAT - tongHoanTra));

        } else if (currentScreenCode.equals("STAT_PRODUCTS")) {
            ArrayList<Object[]> list = thongKeDAO.getThongKeSanPham(tuNgay, denNgay);
            int tongSlBan = 0;
            int tongSlTra = 0;
            int tongSlThucTe = 0;

            for (Object[] row : list) {
                modelTable.addRow(row);
                tongSlBan += (int) row[4];
                tongSlTra += (int) row[5];
                tongSlThucTe += (int) row[6];
            }

            lblTitleCard1.setText("TỔNG SL BÁN THỰC TẾ");
            lblValCard1.setText(tongSlThucTe + " SP");

            lblTitleCard2.setText("TỔNG SL ĐÃ BÁN");
            lblValCard2.setText(tongSlBan + " SP");

            lblTitleCard3.setText("TỔNG SL KHÁCH TRẢ");
            lblValCard3.setText(tongSlTra + " SP");

            lblTitleCard4.setText("SẢN PHẨM GHI NHẬN");
            lblValCard4.setText(list.size() + " Mặt hàng");

        } else if (currentScreenCode.equals("STAT_SERVICES")) {
            ArrayList<Object[]> list = thongKeDAO.getThongKeDichVu(tuNgay, denNgay);
            int tongLuot = 0;
            double tongDoanhThuDV = 0;

            for (Object[] row : list) {
                modelTable.addRow(row);
                tongLuot += (int) row[4];
                tongDoanhThuDV += (double) row[5];
            }

            lblTitleCard1.setText("TỔNG LƯỢT DỊCH VỤ");
            lblValCard1.setText(tongLuot + " Lượt");

            lblTitleCard2.setText("TỔNG DOANH THU DỊCH VỤ");
            lblValCard2.setText(dfMoney.format(tongDoanhThuDV));

            lblTitleCard3.setText("DỊCH VỤ CUNG CẤP");
            lblValCard3.setText(list.size() + " Dịch vụ");

            lblTitleCard4.setText("TRẠNG THÁI");
            lblValCard4.setText("Hoàn thành");

        } else if (currentScreenCode.equals("STAT_INVENTORY")) {
            ArrayList<SanPham> list = thongKeDAO.getBaoCaoTonKho();
            int stt = 1;
            int totalTon = 0;

            for (SanPham sp : list) {
                Object[] row = new Object[] {
                    stt++,
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDonViTinh(),
                    sp.getGiaBan() != null ? dfMoney.format(sp.getGiaBan()) : "0 VNĐ",
                    sp.getSoLuongTon()
                };
                modelTable.addRow(row);
                totalTon += sp.getSoLuongTon();
            }

            lblTitleCard1.setText("TỔNG LOẠI SẢN PHẨM");
            lblValCard1.setText(list.size() + " Loại");

            lblTitleCard2.setText("TỔNG SỐ LƯỢNG TỒN");
            lblValCard2.setText(totalTon + " Sản phẩm");

            lblTitleCard3.setText("TRẠNG THÁI KHO");
            lblValCard3.setText("Ổn định");

            lblTitleCard4.setText("CẬP NHẬT LÚC");
            lblValCard4.setText("Hôm nay");
        }
    }

    public void setupScreenMode(String screenCode) {
        this.currentScreenCode = screenCode;
        modelTable.setRowCount(0);
        
        // Ẩn thanh lọc thời gian nếu là màn hình Báo cáo tồn kho
        boolean isInventory = screenCode.equals("STAT_INVENTORY");
        if (panelFilter != null) {
            panelFilter.setVisible(!isInventory);
        }

        switch (screenCode) {
            case "STAT_REVENUE":
                modelTable.setColumnIdentifiers(new String[]{"STT", "Mã HĐ / Phiếu", "Ngày tạo", "Loại", "Khách hàng", "Tiền chưa VAT", "Thành tiền"});
                loadDataToTable();
                break;
            case "STAT_PRODUCTS":
                modelTable.setColumnIdentifiers(new String[]{"STT", "Mã SP", "Tên sản phẩm", "ĐVT", "SL Bán", "SL Trả", "SL Thực tế"});
                loadDataToTable();
                break;
            case "STAT_SERVICES":
                modelTable.setColumnIdentifiers(new String[]{"STT", "Mã DV", "Tên dịch vụ", "Đơn giá", "Số lượt", "Doanh thu DV"});
                loadDataToTable();
                break;
            case "STAT_INVENTORY":
                modelTable.setColumnIdentifiers(new String[]{"STT", "Mã SP", "Tên sản phẩm", "ĐVT", "Đơn giá bán", "Số lượng tồn"});
                loadDataToTable();
                break;
        }
    }

    // --- Helper UI Methods ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.FONT_BOLD);
        label.setForeground(AppTheme.SIDEBAR_BG);
        return label;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(AppTheme.FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createCardTitle(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(120, 130, 140));
        lbl.setBorder(new EmptyBorder(8, 12, 0, 0));
        return lbl;
    }

    private JLabel createCardValue(String value, Color color) {
        JLabel lbl = new JLabel(value, SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(color);
        lbl.setBorder(new EmptyBorder(0, 12, 8, 0));
        return lbl;
    }

    private JPanel createCardPanel(JLabel lblTitle, JLabel lblValue, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createLineBorder(AppTheme.BORDER, 1)
        ));
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}