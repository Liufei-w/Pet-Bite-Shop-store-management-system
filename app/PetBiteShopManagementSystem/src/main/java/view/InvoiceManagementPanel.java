package view;

import dao.HoaDon_DAO;
import connectDB.ConnectDB;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InvoiceManagementPanel extends JPanel implements ActionListener {
    private JTable tableHDBody;
    private DefaultTableModel modelHD;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnRefresh;
    private HoaDon_DAO hd_dao;

    public InvoiceManagementPanel() {
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        hd_dao = new HoaDon_DAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(244, 246, 249));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //TIÊU ĐỀ
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh sách hóa đơn");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(18, 57, 88));

        JLabel lblSub = new JLabel("Tra cứu danh sách và xem chi tiết hóa đơn từ cơ sở dữ liệu.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(122, 140, 160));

        panelTop.add(lblTitle);
        panelTop.add(Box.createVerticalStrut(5));
        panelTop.add(lblSub);
        add(panelTop, BorderLayout.NORTH);

        //KHU VỰC GIỮA (BỘ LỌC + BẢNG)
        JPanel panelCenter = new JPanel(new BorderLayout(0, 10));
        panelCenter.setOpaque(false);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFilter.setBackground(Color.WHITE);
        panelFilter.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(250, 30));
        txtSearch.setToolTipText("Nhập mã hóa đơn hoặc tên khách hàng...");

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(255, 177, 75));
        btnSearch.setFocusPainted(false);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBackground(new Color(220, 225, 230));
        btnRefresh.setFocusPainted(false);
        
        btnSearch.addActionListener(this);
        btnRefresh.addActionListener(this);


        panelFilter.add(new JLabel("Tìm kiếm: "));
        panelFilter.add(txtSearch);
        panelFilter.add(btnSearch);
        panelFilter.add(btnRefresh);

        panelCenter.add(panelFilter, BorderLayout.NORTH);

        //JTABLE
        String[] colHeader = {"Mã HD", "Ngày lập", "Khách hàng", "NV lập", "PT thanh toán", "Tổng tiền", "Trạng thái", "Thao tác"};
        modelHD = new DefaultTableModel(colHeader, 0);
        tableHDBody = new JTable(modelHD);
        tableHDBody.setRowHeight(35);
        tableHDBody.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tableHDBody.getTableHeader().setBackground(new Color(230, 235, 245));

        //click chuột vào bảng để xem chi tiết
        tableHDBody.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableHDBody.rowAtPoint(evt.getPoint());
                int col = tableHDBody.columnAtPoint(evt.getPoint());
                if (col == 7 && row >= 0) {
                    String maHD = tableHDBody.getValueAt(row, 0).toString();
                    String khachHang = tableHDBody.getValueAt(row, 2).toString();
                    JOptionPane.showMessageDialog(null, 
                        "Xem chi tiết hóa đơn:\n- Mã HD: " + maHD + "\n- Khách hàng: " + khachHang,
                        "Chi tiết hóa đơn", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panelCenter.add(new JScrollPane(tableHDBody), BorderLayout.CENTER);
        add(panelCenter, BorderLayout.CENTER);

        // ĐỌC DỮ LIỆU TỪ DATABASE VÀO JTABLE
        DocDuLieuDatabaseVaoTable();
    }

    public void DocDuLieuDatabaseVaoTable() {
        modelHD.setRowCount(0);
        ArrayList<Object[]> list = hd_dao.getAllHDBody();
        for (Object[] row : list) {
            modelHD.addRow(row);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnSearch)) {
            String keyword = txtSearch.getText().trim().toLowerCase();
            modelHD.setRowCount(0);
            ArrayList<Object[]> list = hd_dao.getAllHDBody();
            for (Object[] row : list) {
                String maHD = row[0].toString().toLowerCase();
                String tenKH = row[2].toString().toLowerCase();
                if (keyword.isEmpty() || maHD.contains(keyword) || tenKH.contains(keyword)) {
                    modelHD.addRow(row);
                }
            }
        } else if (o.equals(btnRefresh)) {
            txtSearch.setText("");
            DocDuLieuDatabaseVaoTable();
        }
    }
}