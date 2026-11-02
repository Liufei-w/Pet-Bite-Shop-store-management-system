package view;

import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CareHistoryManagementPanel extends JPanel {

    private JTable bangLichSu;
    private DefaultTableModel moHinhBang;
    private JTextField oNhapTimKiem;
    private JButton nutTimKiem;
    private JButton nutLamMoi;
    private JComboBox<String> hopChonDichVu;

    private final Color mauNenChinh = AppTheme.MAIN_BG;
    private final Color mauTheTrang = AppTheme.CARD_BG;
    private final Color mauChuTieuDe = new Color(0x12, 0x39, 0x58);
    private final Color mauChuPhu = AppTheme.TEXT_SECONDARY;
    private final Color mauVienNhe = AppTheme.BORDER;
    private final Color mauNenOData = new Color(248, 251, 253);
    private final Color mauHongNutChinh = new Color(217, 130, 139);
    private final Color mauNenTieuDeCot = new Color(247, 250, 253);
    private final Color mauTieuDeCot = new Color(76, 100, 116);
    private final Color mauDuongKeBang = new Color(238, 242, 246);
    private final Color mauXanhTrangThai = new Color(46, 175, 100);
    private final Color mauNenXanhTrangThai = new Color(234, 247, 238);
    private final Color mauNutXoa = new Color(190, 65, 65);
    private final Color mauNenNutXoa = new Color(255, 239, 239);

    // Lưu dữ liệu ban đầu để làm mới
    private Object[][] duLieuBanDau = {
        {"LS261008001", "06/10/2026 09:30", "Milo", "Nguyễn Minh Anh",
            "Tắm & sấy", "NV Chăm sóc 01", "Hoàn thành", "Xóa"},
        {"LS261008002", "06/10/2026 11:00", "Bông", "Trần Quốc Huy",
            "Cắt tỉa lông", "NV Chăm sóc 02", "Hoàn thành", "Xóa"},
        {"LS261008003", "07/10/2026 14:15", "Lu", "Lê Thảo Vy",
            "Khám sức khỏe", "NV Chăm sóc 01", "Đang theo dõi", "Xóa"}
    };

    public CareHistoryManagementPanel() {
        khoiTaoGiaoDien();
        khoiTaoSuKien();
    }

    private void khoiTaoGiaoDien() {
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel theChuaNoiDung = new JPanel(new BorderLayout());
        theChuaNoiDung.setBackground(mauTheTrang);
        theChuaNoiDung.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // ===== TIÊU ĐỀ =====
        JPanel khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("LỊCH SỬ CHĂM SÓC");
        nhanTieuDe.setFont(AppTheme.FONT_TITLE);
        nhanTieuDe.setForeground(mauChuTieuDe);

        JLabel nhanMoTa = new JLabel(
            "Tra cứu danh sách và theo dõi chi tiết lịch sử dịch vụ, tình trạng thú cưng sau khi chăm sóc"
        );
        nhanMoTa.setFont(AppTheme.FONT_PLAIN);
        nhanMoTa.setForeground(mauChuPhu);

        khungTieuDeKhoi.add(nhanTieuDe);
        khungTieuDeKhoi.add(nhanMoTa);

        // ===== THANH CÔNG CỤ =====
        JPanel khungThanhCongCu = new JPanel(new BorderLayout());
        khungThanhCongCu.setOpaque(false);
        khungThanhCongCu.setBorder(new EmptyBorder(16, 0, 16, 0));

        JPanel khungBoLocBenTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        khungBoLocBenTrai.setOpaque(false);

        // Ô tìm kiếm
        oNhapTimKiem = new JTextField();
        oNhapTimKiem.setPreferredSize(new Dimension(300, 38));
        oNhapTimKiem.setFont(AppTheme.FONT_PLAIN);
        oNhapTimKiem.setForeground(new Color(45, 62, 80));
        oNhapTimKiem.setCaretColor(new Color(45, 62, 80));
        oNhapTimKiem.setSelectedTextColor(Color.WHITE);
        oNhapTimKiem.setSelectionColor(new Color(80, 140, 180));
        oNhapTimKiem.setBackground(mauNenOData);
        oNhapTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));
        oNhapTimKiem.putClientProperty("JTextField.placeholderText",
            "Nhập mã LS, tên thú cưng hoặc khách hàng");

        // Nút tìm kiếm
        nutTimKiem = new JButton("Tìm kiếm");
        nutTimKiem.setPreferredSize(new Dimension(100, 38));
        nutTimKiem.setFont(AppTheme.FONT_BOLD);
        nutTimKiem.setForeground(Color.WHITE);
        nutTimKiem.setBackground(mauHongNutChinh);
        nutTimKiem.setFocusPainted(false);
        nutTimKiem.setBorderPainted(false);
        nutTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ComboBox dịch vụ
        String[] danhSachDichVu = {
            "Tất cả dịch vụ",
            "Tắm & sấy",
            "Cắt tỉa lông",
            "Khám sức khỏe",
            "Tiêm phòng"
        };

        hopChonDichVu = new JComboBox<>(danhSachDichVu);
        hopChonDichVu.setPreferredSize(new Dimension(140, 38));
        hopChonDichVu.setFont(AppTheme.FONT_PLAIN);
        hopChonDichVu.setForeground(new Color(45, 62, 80));
        hopChonDichVu.setBackground(mauNenOData);
        hopChonDichVu.setFocusable(false);

        khungBoLocBenTrai.add(oNhapTimKiem);
        khungBoLocBenTrai.add(nutTimKiem);
        khungBoLocBenTrai.add(hopChonDichVu);

        // Nút làm mới
        nutLamMoi = new JButton("Làm mới");
        nutLamMoi.setPreferredSize(new Dimension(100, 38));
        nutLamMoi.setFont(AppTheme.FONT_BOLD);
        nutLamMoi.setForeground(new Color(45, 62, 80));
        nutLamMoi.setBackground(Color.WHITE);
        nutLamMoi.setFocusPainted(false);
        nutLamMoi.setBorder(BorderFactory.createLineBorder(mauVienNhe));
        nutLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutLamMoi, BorderLayout.EAST);

        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS));
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);

        theChuaNoiDung.add(khungPhiaTren, BorderLayout.NORTH);

        // ===== BẢNG LỊCH SỬ =====
        String[] tieuDeCot = {
            "Mã LS", "Ngày giờ", "Thú cưng", "Khách hàng",
            "Dịch vụ", "NV chăm sóc", "Trạng thái", "Thao tác"
        };

        moHinhBang = new DefaultTableModel(duLieuBanDau, tieuDeCot) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bangLichSu = new JTable(moHinhBang);
        bangLichSu.setFont(AppTheme.FONT_PLAIN);
        bangLichSu.setForeground(new Color(45, 62, 80));
        bangLichSu.setBackground(Color.WHITE);
        bangLichSu.setRowHeight(52);
        bangLichSu.setShowGrid(true);
        bangLichSu.setGridColor(mauDuongKeBang);
        bangLichSu.setSelectionBackground(new Color(242, 247, 252));
        bangLichSu.setSelectionForeground(new Color(45, 62, 80));
        bangLichSu.setFillsViewportHeight(true);
        bangLichSu.setRowSelectionAllowed(true);
        bangLichSu.setColumnSelectionAllowed(false);

        // Header bảng
        JTableHeader dauBang = bangLichSu.getTableHeader();
        dauBang.setPreferredSize(new Dimension(0, 46));
        dauBang.setFont(AppTheme.FONT_BOLD);
        dauBang.setBackground(mauNenTieuDeCot);
        dauBang.setForeground(mauTieuDeCot);
        dauBang.setReorderingAllowed(false);
        dauBang.setBorder(BorderFactory.createMatteBorder(
            0, 0, 1, 0, mauDuongKeBang
        ));

        // Renderer chữ thông thường
        DefaultTableCellRenderer canLeTrai = new DefaultTableCellRenderer();
        canLeTrai.setBorder(new EmptyBorder(0, 12, 0, 12));
        canLeTrai.setForeground(new Color(45, 62, 80));

        DefaultTableCellRenderer canLeGiua = new DefaultTableCellRenderer();
        canLeGiua.setHorizontalAlignment(SwingConstants.CENTER);
        canLeGiua.setForeground(new Color(45, 62, 80));

        bangLichSu.getColumnModel().getColumn(0).setPreferredWidth(110);
        bangLichSu.getColumnModel().getColumn(0).setCellRenderer(canLeTrai);

        bangLichSu.getColumnModel().getColumn(1).setPreferredWidth(140);
        bangLichSu.getColumnModel().getColumn(1).setCellRenderer(canLeGiua);

        bangLichSu.getColumnModel().getColumn(2).setPreferredWidth(100);
        bangLichSu.getColumnModel().getColumn(2).setCellRenderer(canLeTrai);

        bangLichSu.getColumnModel().getColumn(3).setPreferredWidth(150);
        bangLichSu.getColumnModel().getColumn(3).setCellRenderer(canLeTrai);

        bangLichSu.getColumnModel().getColumn(4).setPreferredWidth(140);
        bangLichSu.getColumnModel().getColumn(4).setCellRenderer(canLeTrai);

        bangLichSu.getColumnModel().getColumn(5).setPreferredWidth(130);
        bangLichSu.getColumnModel().getColumn(5).setCellRenderer(canLeTrai);

        bangLichSu.getColumnModel().getColumn(6).setPreferredWidth(130);
        bangLichSu.getColumnModel().getColumn(6)
            .setCellRenderer(new BoKetXuatTrangThaiLichSu());

        bangLichSu.getColumnModel().getColumn(7).setPreferredWidth(100);
        bangLichSu.getColumnModel().getColumn(7)
            .setCellRenderer(new BoKetXuatThaoTacXoa());

        JScrollPane thanhCuon = new JScrollPane(bangLichSu);
        thanhCuon.getViewport().setBackground(mauTheTrang);
        thanhCuon.setBorder(BorderFactory.createLineBorder(mauVienNhe, 1));

        theChuaNoiDung.add(thanhCuon, BorderLayout.CENTER);
        add(theChuaNoiDung, BorderLayout.CENTER);
    }

    // ===== CÁC SỰ KIỆN =====
    private void khoiTaoSuKien() {
        nutTimKiem.addActionListener(e -> thucHienTimKiem());
        oNhapTimKiem.addActionListener(e -> thucHienTimKiem());
        hopChonDichVu.addActionListener(e -> thucHienTimKiem());

        nutLamMoi.addActionListener(e -> {
            oNhapTimKiem.setText("");
            hopChonDichVu.setSelectedIndex(0);
            khoiPhucDanhSach();
        });

        // Bấm vào cột Thao tác để xóa
        bangLichSu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int dong = bangLichSu.rowAtPoint(e.getPoint());
                int cot = bangLichSu.columnAtPoint(e.getPoint());

                if (dong >= 0 && cot == 7) {
                    dong = bangLichSu.convertRowIndexToModel(dong);
                    xacNhanXoa(dong);
                }
            }
        });
    }

    // ===== TÌM KIẾM VÀ LỌC =====
    private void thucHienTimKiem() {
        String tuKhoa = oNhapTimKiem.getText().trim().toLowerCase();
        String dichVu = hopChonDichVu.getSelectedItem().toString();

        TableRowSorter<DefaultTableModel> boLoc = new TableRowSorter<>(moHinhBang);
        bangLichSu.setRowSorter(boLoc);

        RowFilter<DefaultTableModel, Integer> dieuKien = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String maLS = entry.getStringValue(0).toLowerCase();
                String thuCung = entry.getStringValue(2).toLowerCase();
                String khachHang = entry.getStringValue(3).toLowerCase();
                String tenDichVu = entry.getStringValue(4);

                boolean khopTuKhoa = tuKhoa.isEmpty()
                    || maLS.contains(tuKhoa)
                    || thuCung.contains(tuKhoa)
                    || khachHang.contains(tuKhoa);

                boolean khopDichVu = dichVu.equals("Tất cả dịch vụ")
                    || tenDichVu.equalsIgnoreCase(dichVu);

                return khopTuKhoa && khopDichVu;
            }
        };

        boLoc.setRowFilter(dieuKien);
    }

    // ===== KHÔI PHỤC DANH SÁCH =====
    private void khoiPhucDanhSach() {
        moHinhBang.setRowCount(0);
        for (Object[] dong : duLieuBanDau) {
            moHinhBang.addRow(dong.clone());
        }
        bangLichSu.setRowSorter(null);
    }

    // ===== XÓA LỊCH SỬ =====
    private void xacNhanXoa(int dong) {
        String maLS = moHinhBang.getValueAt(dong, 0).toString();

        int luaChon = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa lịch sử chăm sóc " + maLS + " không?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (luaChon == JOptionPane.YES_OPTION) {
            moHinhBang.removeRow(dong);
            JOptionPane.showMessageDialog(
                this,
                "Đã xóa lịch sử chăm sóc " + maLS + ".",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ===== RENDER TRẠNG THÁI =====
    private class BoKetXuatTrangThaiLichSu extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column
        ) {
            String giaTri = value != null ? value.toString() : "";
            boolean laHoanThanh = giaTri.equalsIgnoreCase("Hoàn thành");

            JPanel khungBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungBadge.setOpaque(true);
            khungBadge.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

            JLabel nhanBadge = new JLabel(giaTri, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(laHoanThanh ? mauNenXanhTrangThai : new Color(255, 244, 223));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            nhanBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanBadge.setForeground(laHoanThanh ? mauXanhTrangThai : new Color(167, 100, 0));
            nhanBadge.setPreferredSize(new Dimension(115, 28));
            nhanBadge.setOpaque(false);

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }

    // ===== RENDER NÚT XÓA =====
    private class BoKetXuatThaoTacXoa extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column
        ) {
            JPanel khungThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungThaoTac.setOpaque(true);
            khungThaoTac.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

            JLabel nhanNutXoa = new JLabel("Xóa", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(mauNenNutXoa);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            nhanNutXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanNutXoa.setForeground(mauNutXoa);
            nhanNutXoa.setPreferredSize(new Dimension(65, 28));
            nhanNutXoa.setOpaque(false);
            nhanNutXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

            khungThaoTac.add(nhanNutXoa);
            return khungThaoTac;
        }
    }
}