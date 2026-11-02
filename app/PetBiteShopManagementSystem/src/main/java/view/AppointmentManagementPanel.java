
package view;

import entity.LichHen;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AppointmentManagementPanel extends JPanel {

    private static class LichHenExt extends LichHen {
        private String khachHang;
        private String thuCung;
        private String nhanVien;
        private String dichVu;

        public LichHenExt(String maLichHen, LocalDateTime ngayGioHen,
                String trangThai, String khachHang, String thuCung,
                String nhanVien, String dichVu) {
            super(maLichHen, ngayGioHen, trangThai);
            this.khachHang = khachHang;
            this.thuCung = thuCung;
            this.nhanVien = nhanVien;
            this.dichVu = dichVu;
        }

        public String getKhachHang() { return khachHang; }
        public String getThuCung() { return thuCung; }
        public String getNhanVien() { return nhanVien; }
        public String getDichVu() { return dichVu; }
    }

    private JTable bangLichHen;
    private DefaultTableModel moHinhBang;
    private JTextField oNhapMaLichHen;
    private JTextField oNhapKhachThuCung;
    private JTextField oNhapNgayHen;
    private JComboBox<String> hopChonNhanVien;
    private JButton nutTaoLichHen;
    private JButton nutTimKiem;

    private ArrayList<LichHenExt> danhSachLichHenGoc;
    private ArrayList<LichHenExt> danhSachHienThi;

    private final Color mauNenChinh = AppTheme.MAIN_BG;
    private final Color mauTheTrang = Color.WHITE;
    private final Color mauChuTieuDe = AppTheme.TEXT_PRIMARY;
    private final Color mauChuPhu = AppTheme.TEXT_SECONDARY;
    private final Color mauVienNhe = AppTheme.BORDER;
    private final Color mauNenOData = new Color(0xF8, 0xFB, 0xFD);
    private final Color mauChuBoLoc = new Color(0x60, 0x74, 0x83);
    private final Color mauHongNutChinh = new Color(0xD9, 0x82, 0x8B);
    private final Color mauNenTieuDeCot = new Color(0xF7, 0xFA, 0xFD);
    private final Color mauTieuDeCot = new Color(0x4C, 0x64, 0x74);
    private final Color mauDuongKeBang = new Color(0xEE, 0xF2, 0xF6);
    private final Color mauXanhTrangThai = new Color(0x2E, 0xAF, 0x64);
    private final Color mauNenXanhTrangThai = new Color(0xEA, 0xF7, 0xEE);
    private final Color mauCamTrangThai = new Color(0xA7, 0x64, 0x00);
    private final Color mauNenCamTrangThai = new Color(0xFF, 0xF4, 0xF0);

    public AppointmentManagementPanel() {
        khoiTaoDuLieuMau();
        khoiTaoGiaoDien();
        khoiTaoSuKien();
    }

    private void khoiTaoDuLieuMau() {
        danhSachLichHenGoc = new ArrayList<>();

        danhSachLichHenGoc.add(new LichHenExt(
            "LH261005001",
            LocalDateTime.of(2026, 10, 6, 9, 0),
            "Đã đặt", "Nguyễn Minh Anh", "Milo",
            "NV Chăm sóc 01", "Tắm & sấy"
        ));

        danhSachLichHenGoc.add(new LichHenExt(
            "LH261005002",
            LocalDateTime.of(2026, 10, 6, 10, 30),
            "Đang thực hiện", "Trần Quốc Huy", "Bông",
            "NV Chăm sóc 02", "Cắt tỉa + Cắt móng"
        ));

        danhSachHienThi = new ArrayList<>(danhSachLichHenGoc);
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

        JPanel khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("QUẢN LÝ LỊCH HẸN");
        nhanTieuDe.setFont(AppTheme.FONT_TITLE);
        nhanTieuDe.setForeground(mauChuTieuDe);

        JLabel nhanMoTa = new JLabel(
            "Quản lý lịch hẹn chăm sóc, theo dõi nhân viên phụ trách và trạng thái thực hiện"
        );
        nhanMoTa.setFont(AppTheme.FONT_PLAIN);
        nhanMoTa.setForeground(mauChuPhu);

        khungTieuDeKhoi.add(nhanTieuDe);
        khungTieuDeKhoi.add(nhanMoTa);

        JPanel khungThanhCongCu = new JPanel(new BorderLayout());
        khungThanhCongCu.setOpaque(false);
        khungThanhCongCu.setBorder(new EmptyBorder(16, 0, 16, 0));

        JPanel khungBoLocBenTrai = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 12, 0)
        );
        khungBoLocBenTrai.setOpaque(false);

        oNhapMaLichHen = taoOnhapPlaceholder("Nhập mã lịch hẹn", 140);
        oNhapKhachThuCung = taoOnhapPlaceholder("Tìm khách hoặc thú cưng", 180);
        oNhapNgayHen = taoOnhapPlaceholder("Chọn ngày", 130);

        String[] danhSachNhanVien = {
            "Chọn nhân viên", "NV Chăm sóc 01",
            "NV Chăm sóc 02", "NV Chăm sóc 03"
        };

        hopChonNhanVien = new JComboBox<>(danhSachNhanVien);
        hopChonNhanVien.setPreferredSize(new Dimension(150, 38));
        hopChonNhanVien.setFont(AppTheme.FONT_PLAIN);
        hopChonNhanVien.setForeground(new Color(45, 62, 80));
        hopChonNhanVien.setBackground(mauNenOData);

        khungBoLocBenTrai.add(oNhapMaLichHen);
        khungBoLocBenTrai.add(oNhapKhachThuCung);
        khungBoLocBenTrai.add(oNhapNgayHen);
        khungBoLocBenTrai.add(hopChonNhanVien);

        nutTimKiem = new JButton("Tìm");
        nutTimKiem.setPreferredSize(new Dimension(80, 38));
        nutTimKiem.setFont(AppTheme.FONT_BOLD);
        nutTimKiem.setForeground(Color.WHITE);
        nutTimKiem.setBackground(new Color(65, 130, 170));
        nutTimKiem.setFocusPainted(false);
        nutTimKiem.setBorderPainted(false);
        nutTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        khungBoLocBenTrai.add(nutTimKiem);

        nutTaoLichHen = new JButton("+ Tạo lịch hẹn") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(mauHongNutChinh);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        nutTaoLichHen.setPreferredSize(new Dimension(135, 38));
        nutTaoLichHen.setFont(AppTheme.FONT_BOLD);
        nutTaoLichHen.setForeground(Color.WHITE);
        nutTaoLichHen.setFocusPainted(false);
        nutTaoLichHen.setContentAreaFilled(false);
        nutTaoLichHen.setBorderPainted(false);
        nutTaoLichHen.setCursor(new Cursor(Cursor.HAND_CURSOR));

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutTaoLichHen, BorderLayout.EAST);

        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(
            new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS)
        );
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);

        theChuaNoiDung.add(khungPhiaTren, BorderLayout.NORTH);

        capNhatBangDuLieu();

        JScrollPane thanhCuon = new JScrollPane(bangLichHen);
        thanhCuon.getViewport().setBackground(mauTheTrang);
        thanhCuon.setBorder(BorderFactory.createLineBorder(mauVienNhe, 1));

        theChuaNoiDung.add(thanhCuon, BorderLayout.CENTER);
        add(theChuaNoiDung, BorderLayout.CENTER);
    }

    private void capNhatBangDuLieu() {
        String[] tieuDeCot = {
            "Mã LH", "Khách hàng", "Thú cưng", "Ngày giờ",
            "NV chăm sóc", "Dịch vụ", "Trạng thái", "Thao tác"
        };

        Object[][] duLieu = new Object[danhSachHienThi.size()][8];

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (int i = 0; i < danhSachHienThi.size(); i++) {
            LichHenExt lh = danhSachHienThi.get(i);

            duLieu[i][0] = lh.getMaLichHen();
            duLieu[i][1] = lh.getKhachHang();
            duLieu[i][2] = lh.getThuCung();
            duLieu[i][3] = lh.getNgayGioHen() != null
                ? lh.getNgayGioHen().format(formatter) : "";
            duLieu[i][4] = lh.getNhanVien();
            duLieu[i][5] = lh.getDichVu();
            duLieu[i][6] = lh.getTrangThai();
            duLieu[i][7] = "ThaoTac";
        }

        if (moHinhBang == null) {
            moHinhBang = new DefaultTableModel(duLieu, tieuDeCot) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            bangLichHen = new JTable(moHinhBang);
            bangLichHen.setFont(AppTheme.FONT_PLAIN);
            bangLichHen.setRowHeight(52);
            bangLichHen.setShowGrid(true);
            bangLichHen.setGridColor(mauDuongKeBang);
            bangLichHen.setSelectionBackground(new Color(0xF2, 0xF7, 0xFC));
            bangLichHen.setSelectionForeground(new Color(32, 58, 80));
            bangLichHen.setFillsViewportHeight(true);

            JTableHeader dauBang = bangLichHen.getTableHeader();
            dauBang.setPreferredSize(new Dimension(0, 46));
            dauBang.setFont(AppTheme.FONT_BOLD);
            dauBang.setBackground(mauNenTieuDeCot);
            dauBang.setForeground(mauTieuDeCot);
            dauBang.setReorderingAllowed(false);
            dauBang.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, mauDuongKeBang
            ));

            thietLapRendererCot();
        } else {
            moHinhBang.setDataVector(duLieu, tieuDeCot);
            thietLapRendererCot();
        }
    }

    private void thietLapRendererCot() {
        DefaultTableCellRenderer canLeTrai = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                c.setForeground(new Color(32, 58, 80));
                c.setBackground(
                    isSelected ? table.getSelectionBackground() : Color.WHITE
                );
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(new EmptyBorder(0, 12, 0, 12));

                return c;
            }
        };

        DefaultTableCellRenderer canLeGiua = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );

                c.setForeground(new Color(32, 58, 80));
                c.setBackground(
                    isSelected ? table.getSelectionBackground() : Color.WHITE
                );
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.CENTER);

                return c;
            }
        };

        if (bangLichHen.getColumnCount() >= 8) {
            bangLichHen.getColumnModel().getColumn(0).setPreferredWidth(100);
            bangLichHen.getColumnModel().getColumn(0).setCellRenderer(canLeTrai);

            bangLichHen.getColumnModel().getColumn(1).setPreferredWidth(140);
            bangLichHen.getColumnModel().getColumn(1).setCellRenderer(canLeTrai);

            bangLichHen.getColumnModel().getColumn(2).setPreferredWidth(90);
            bangLichHen.getColumnModel().getColumn(2).setCellRenderer(canLeTrai);

            bangLichHen.getColumnModel().getColumn(3).setPreferredWidth(120);
            bangLichHen.getColumnModel().getColumn(3).setCellRenderer(canLeGiua);

            bangLichHen.getColumnModel().getColumn(4).setPreferredWidth(130);
            bangLichHen.getColumnModel().getColumn(4).setCellRenderer(canLeTrai);

            bangLichHen.getColumnModel().getColumn(5).setPreferredWidth(150);
            bangLichHen.getColumnModel().getColumn(5).setCellRenderer(canLeTrai);

            bangLichHen.getColumnModel().getColumn(6).setPreferredWidth(120);
            bangLichHen.getColumnModel().getColumn(6)
                .setCellRenderer(new BoKetXuatTrangThaiLich());

            bangLichHen.getColumnModel().getColumn(7).setPreferredWidth(160);
            bangLichHen.getColumnModel().getColumn(7)
                .setCellRenderer(new BoKetXuatThaoTac());
        }
    }

    private JTextField taoOnhapPlaceholder(
            String placeholderText, int chieuRong) {

        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                g2.setColor(mauNenOData);
                g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1, 8, 8
                );
                g2.dispose();

                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D gPlaceholder = (Graphics2D) g.create();

                    gPlaceholder.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                    );
                    gPlaceholder.setColor(new Color(110, 130, 150));
                    gPlaceholder.setFont(AppTheme.FONT_PLAIN);

                    Insets insets = getInsets();
                    FontMetrics fm = gPlaceholder.getFontMetrics();

                    int y = (getHeight() - fm.getHeight()) / 2
                        + fm.getAscent();

                    gPlaceholder.drawString(placeholderText, insets.left, y);
                    gPlaceholder.dispose();
                }
            }
        };

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.repaint();
            }
        });

        textField.setOpaque(false);
        textField.setPreferredSize(new Dimension(chieuRong, 38));
        textField.setFont(AppTheme.FONT_PLAIN);
        textField.setForeground(new Color(45, 62, 80));
        textField.setCaretColor(new Color(45, 62, 80));
        textField.setSelectedTextColor(Color.WHITE);
        textField.setSelectionColor(new Color(80, 140, 180));

        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        return textField;
    }

    private void khoiTaoSuKien() {
        DocumentListener boLangNgheTimKiem = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                thucHienLocDuLieu();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                thucHienLocDuLieu();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                thucHienLocDuLieu();
            }
        };

        oNhapMaLichHen.getDocument().addDocumentListener(boLangNgheTimKiem);
        oNhapKhachThuCung.getDocument().addDocumentListener(boLangNgheTimKiem);
        oNhapNgayHen.getDocument().addDocumentListener(boLangNgheTimKiem);

        hopChonNhanVien.addActionListener(e -> thucHienLocDuLieu());

        // Nhấn nút Tìm
        nutTimKiem.addActionListener(e -> thucHienLocDuLieu());

        // Nhấn Enter trong ô nhập cũng tìm kiếm
        ActionListener timKiemKhiNhanEnter = e -> thucHienLocDuLieu();

        oNhapMaLichHen.addActionListener(timKiemKhiNhanEnter);
        oNhapKhachThuCung.addActionListener(timKiemKhiNhanEnter);
        oNhapNgayHen.addActionListener(timKiemKhiNhanEnter);

        nutTaoLichHen.addActionListener(e -> hienThiDialogTaoLichHen());

        bangLichHen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bangLichHen.getSelectedRow();
                int col = bangLichHen.getSelectedColumn();

                if (row >= 0 && col == 7) {
                    String maLH = bangLichHen.getValueAt(row, 0).toString();

                    LichHenExt target = null;

                    for (LichHenExt lh : danhSachLichHenGoc) {
                        if (lh.getMaLichHen().equals(maLH)) {
                            target = lh;
                            break;
                        }
                    }

                    if (target != null) {
                        String[] options = {
                            "Chuyển sang Đang thực hiện",
                            "Đánh dấu Hoàn thành",
                            "Xóa lịch hẹn",
                            "Hủy"
                        };

                        int choice = JOptionPane.showOptionDialog(
                            AppointmentManagementPanel.this,
                            "Chọn thao tác cho lịch hẹn: " + maLH
                                + " (" + target.getKhachHang() + ")",
                            "Quản lý thao tác lịch hẹn",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );

                        if (choice == 0) {
                            target.setTrangThai("Đang thực hiện");
                            thucHienLocDuLieu();

                        } else if (choice == 1) {
                            target.setTrangThai("Đã hoàn thành");
                            thucHienLocDuLieu();

                        } else if (choice == 2) {
                            int confirm = JOptionPane.showConfirmDialog(
                                AppointmentManagementPanel.this,
                                "Bạn có chắc chắn muốn xóa lịch hẹn "
                                    + maLH + " không?",
                                "Xác nhận xóa",
                                JOptionPane.YES_NO_OPTION
                            );

                            if (confirm == JOptionPane.YES_OPTION) {
                                danhSachLichHenGoc.remove(target);
                                thucHienLocDuLieu();
                            }
                        }
                    }
                }
            }
        });
    }

    private void thucHienLocDuLieu() {
        String tuKhoaMa = oNhapMaLichHen.getText()
            .trim().toLowerCase();

        String tuKhoaKhachThuCung = oNhapKhachThuCung.getText()
            .trim().toLowerCase();

        String tuKhoaNgay = oNhapNgayHen.getText()
            .trim().toLowerCase();

        String nvChon = (String) hopChonNhanVien.getSelectedItem();

        danhSachHienThi.clear();

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (LichHenExt lh : danhSachLichHenGoc) {
            String ngayGioStr = lh.getNgayGioHen() != null
                ? lh.getNgayGioHen().format(formatter).toLowerCase()
                : "";

            boolean khopMa = tuKhoaMa.isEmpty()
                || lh.getMaLichHen().toLowerCase().contains(tuKhoaMa);

            boolean khopKhachThuCung = tuKhoaKhachThuCung.isEmpty()
                || lh.getKhachHang().toLowerCase().contains(tuKhoaKhachThuCung)
                || lh.getThuCung().toLowerCase().contains(tuKhoaKhachThuCung);

            boolean khopNgay = tuKhoaNgay.isEmpty()
                || ngayGioStr.contains(tuKhoaNgay);

            boolean khopNV = nvChon == null
                || nvChon.equals("Chọn nhân viên")
                || lh.getNhanVien().equalsIgnoreCase(nvChon);

            if (khopMa && khopKhachThuCung && khopNgay && khopNV) {
                danhSachHienThi.add(lh);
            }
        }

        capNhatBangDuLieu();
    }

    private void hienThiDialogTaoLichHen() {
        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Tạo lịch hẹn chăm sóc mới",
            true
        );

        dialog.setSize(500, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 12));
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(mauTheTrang);

        JTextField txtKhachHang = new JTextField();
        JTextField txtThuCung = new JTextField();

        JPanel panelNgayGio = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 4, 0)
        );
        panelNgayGio.setOpaque(false);

        JComboBox<String> cbNgay = new JComboBox<>(
            new String[]{"06", "07", "08", "09", "10",
                         "11", "12", "13", "14", "15"}
        );

        JComboBox<String> cbThang = new JComboBox<>(
            new String[]{"10", "11", "12"}
        );

        JComboBox<String> cbGio = new JComboBox<>(
            new String[]{"08", "09", "10", "11", "13",
                         "14", "15", "16", "17"}
        );

        JComboBox<String> cbPhut = new JComboBox<>(
            new String[]{"00", "15", "30", "45"}
        );

        panelNgayGio.add(cbNgay);
        panelNgayGio.add(new JLabel("/"));
        panelNgayGio.add(cbThang);
        panelNgayGio.add(cbGio);
        panelNgayGio.add(new JLabel(":"));
        panelNgayGio.add(cbPhut);

        JComboBox<String> cbNhanVien = new JComboBox<>(
            new String[]{
                "NV Chăm sóc 01",
                "NV Chăm sóc 02",
                "NV Chăm sóc 03"
            }
        );

        JComboBox<String> cbDichVu = new JComboBox<>(
            new String[]{
                "Tắm & sấy",
                "Cắt tỉa + Cắt móng",
                "Khám sức khỏe",
                "Vệ sinh tai & răng miệng"
            }
        );

        JComboBox<String> cbTrangThai = new JComboBox<>(
            new String[]{"Đã đặt", "Đang thực hiện"}
        );

        panelForm.add(new JLabel("Tên khách hàng:"));
        panelForm.add(txtKhachHang);
        panelForm.add(new JLabel("Tên thú cưng:"));
        panelForm.add(txtThuCung);
        panelForm.add(new JLabel("Chọn Ngày / Giờ:"));
        panelForm.add(panelNgayGio);
        panelForm.add(new JLabel("Nhân viên phụ trách:"));
        panelForm.add(cbNhanVien);
        panelForm.add(new JLabel("Chọn dịch vụ:"));
        panelForm.add(cbDichVu);
        panelForm.add(new JLabel("Trạng thái:"));
        panelForm.add(cbTrangThai);

        JPanel panelNut = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );
        panelNut.setBackground(mauNenChinh);

        JButton btnLuu = new JButton("Lưu lịch hẹn");
        JButton btnHuy = new JButton("Hủy");

        btnLuu.setBackground(mauHongNutChinh);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            if (txtKhachHang.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Vui lòng nhập tên khách hàng!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int day = Integer.parseInt(cbNgay.getSelectedItem().toString());
            int month = Integer.parseInt(cbThang.getSelectedItem().toString());
            int hour = Integer.parseInt(cbGio.getSelectedItem().toString());
            int minute = Integer.parseInt(cbPhut.getSelectedItem().toString());

            LocalDateTime parsedDateTime =
                LocalDateTime.of(2026, month, day, hour, minute);

            String maMoi = String.format(
                "LH261005%03d",
                danhSachLichHenGoc.size() + 1
            );

            LichHenExt lichHenMoi = new LichHenExt(
                maMoi,
                parsedDateTime,
                cbTrangThai.getSelectedItem().toString(),
                txtKhachHang.getText().trim(),
                txtThuCung.getText().trim().isEmpty()
                    ? "Thú cưng"
                    : txtThuCung.getText().trim(),
                cbNhanVien.getSelectedItem().toString(),
                cbDichVu.getSelectedItem().toString()
            );

            danhSachLichHenGoc.add(lichHenMoi);
            thucHienLocDuLieu();

            JOptionPane.showMessageDialog(
                dialog,
                "Tạo lịch hẹn thành công!"
            );

            dialog.dispose();
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        panelNut.add(btnLuu);
        panelNut.add(btnHuy);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelNut, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private class BoKetXuatTrangThaiLich extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            String giaTri = value != null ? value.toString() : "";
            boolean laDaDat = giaTri.equalsIgnoreCase("Đã đặt");

            JPanel khungBadge = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 10)
            );
            khungBadge.setOpaque(false);

            JLabel nhanBadge = new JLabel(
                giaTri, SwingConstants.CENTER
            ) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    g2.setColor(
                        laDaDat ? mauNenXanhTrangThai : mauNenCamTrangThai
                    );

                    g2.fillRoundRect(
                        0, 0, getWidth(), getHeight(), 16, 16
                    );

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            nhanBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanBadge.setForeground(
                laDaDat ? mauXanhTrangThai : Color.WHITE
            );
            nhanBadge.setPreferredSize(new Dimension(115, 28));

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }

    private class BoKetXuatThaoTac extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            JPanel khungThaoTac = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 6, 10)
            );
            khungThaoTac.setOpaque(false);

            JLabel nhanNutXuLy = new JLabel(
                "⚙️ Xử lý", SwingConstants.CENTER
            );
            nhanNutXuLy.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanNutXuLy.setForeground(new Color(0x3B, 0x7A, 0x9E));
            nhanNutXuLy.setOpaque(true);
            nhanNutXuLy.setBackground(new Color(0xF0, 0xF6, 0xFA));
            nhanNutXuLy.setPreferredSize(new Dimension(75, 28));

            JLabel nhanNutXoa = new JLabel(
                "🗑️ Xóa", SwingConstants.CENTER
            );
            nhanNutXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanNutXoa.setForeground(new Color(0xE5, 0x6B, 0x6F));
            nhanNutXoa.setOpaque(true);
            nhanNutXoa.setBackground(new Color(0xFD, 0xEB, 0xEC));
            nhanNutXoa.setPreferredSize(new Dimension(65, 28));

            khungThaoTac.add(nhanNutXuLy);
            khungThaoTac.add(nhanNutXoa);

            return khungThaoTac;
        }
    }
}
