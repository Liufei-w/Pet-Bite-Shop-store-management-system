package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CustomerManagementPanel extends JPanel {
    private JTable bangKhachHang;
    private DefaultTableModel moHinhBang;
    private JTextField oNhapTimKiem;
    private JButton nutTimKiem;
    private JComboBox<String> hopChonTrangThai;
    private JButton nutThemKhachHang;

    // Danh sách dữ liệu khách hàng toàn cục để lưu trữ dữ liệu thêm mới không bị mất khi lọc/tìm kiếm
    private Object[][] danhSachKhachHangGoc = {
        {"KH261006001", "Nguyễn Minh Anh", "Nữ", "0901234567", "12 Nguyễn Văn Bảo, Gò Vấp", "Đang hoạt động", "ThaoTac"},
        {"KH261006002", "Trần Quốc Huy", "Nam", "0912345678", "25 Quang Trung, Gò Vấp", "Đang hoạt động", "ThaoTac"},
        {"KH261006003", "Lê Thảo Vy", "Nữ", "0987654321", "88 Phan Huy Ích, Gò Vấp", "Đang hoạt động", "ThaoTac"},
        {"KH261006004", "Phạm Gia Bảo", "Nam", "0933222111", "41 Nguyễn Oanh, Gò Vấp", "Ngừng hoạt động", "ThaoTac"},
        {"KH261006005", "Vũ Ngọc Linh", "Nữ", "0966888999", "17 Trường Chinh, Tân Bình", "Đang hoạt động", "ThaoTac"},
        {"KH261006006", "Nguyễn Hà My", "Nữ", "0977555444", "55 Lê Văn Thọ, Gò Vấp", "Đang hoạt động", "ThaoTac"}
    };

    // Màu sắc chuẩn 
    private final Color mauNenChinh         = new Color(0xF4, 0xF6, 0xFA);
    private final Color mauTheTrang         = Color.WHITE;
    private final Color mauChuTieuDe        = new Color(0x1C, 0x38, 0x4C);
    private final Color mauChuPhu           = new Color(0x7F, 0x90, 0x9E);
    private final Color mauVienNhe          = new Color(0xE3, 0xE8, 0xEE);
    private final Color mauNenOData         = new Color(0xF8, 0xFB, 0xFD);
    private final Color mauHongNutChinh     = new Color(0xD9, 0x82, 0x8B); 
    private final Color mauNenTieuDeCot     = new Color(0xF7, 0xFA, 0xFD);
    private final Color mauTieuDeCot        = new Color(0x4C, 0x64, 0x74);
    private final Color mauDuongKeBang      = new Color(0xEE, 0xF2, 0xF6);
    private final Color mauXanhTrangThai    = new Color(0x2E, 0xAF, 0x64);
    private final Color mauNenXanhTrangThai = new Color(0xEA, 0xF7, 0xEE);
    private final Color mauDoTrangThai      = new Color(0xE5, 0x6B, 0x6F);
    private final Color mauNenDoTrangThai   = new Color(0xFD, 0xEB, 0xEC);
    private final Color mauNutSua           = new Color(0xA7, 0x64, 0x00);
    private final Color mauNenNutSua        = new Color(0xFF, 0xF4, 0xF0);
    private final Color mauNutThuCung       = new Color(0x3B, 0x7A, 0x9E);
    private final Color mauNenNutThuCung    = new Color(0xF0, 0xF6, 0xFA);

    public CustomerManagementPanel() {
        khoiTaoGiaoDien();
        khoiTaoSuKien();
    }

    private void khoiTaoGiaoDien() {
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 24, 24));

        JPanel theChuaNoiDung = new JPanel(new BorderLayout());
        theChuaNoiDung.setBackground(mauTheTrang);
        theChuaNoiDung.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // ===== 1. HEADER TOP (Tiêu đề & Mô tả) =====
        JPanel khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        JLabel nhanTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        nhanTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nhanTieuDe.setForeground(mauChuTieuDe);

        JLabel nhanMoTa = new JLabel("Theo dõi thông tin khách hàng, trạng thái hoạt động và thú cưng liên quan");
        nhanMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nhanMoTa.setForeground(mauChuPhu);

        khungTieuDeKhoi.add(nhanTieuDe);
        khungTieuDeKhoi.add(nhanMoTa);

        // ===== 2. TOOLBAR (Tìm kiếm, Trạng thái, Nút Thêm) =====
        JPanel khungThanhCongCu = new JPanel(new BorderLayout());
        khungThanhCongCu.setOpaque(false);
        khungThanhCongCu.setBorder(new EmptyBorder(16, 0, 16, 0));

        JPanel khungBoLocBenTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        khungBoLocBenTrai.setOpaque(false);

        oNhapTimKiem = new JTextField(22);
        oNhapTimKiem.setPreferredSize(new Dimension(280, 38));
        oNhapTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        oNhapTimKiem.setForeground(mauChuTieuDe);
        oNhapTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        nutTimKiem = new JButton("🔍 Tìm kiếm");
        nutTimKiem.setPreferredSize(new Dimension(110, 38));
        nutTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutTimKiem.setForeground(Color.WHITE);
        nutTimKiem.setBackground(mauHongNutChinh);
        nutTimKiem.setFocusPainted(false);
        nutTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        hopChonTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang hoạt động", "Ngừng hoạt động"});
        hopChonTrangThai.setPreferredSize(new Dimension(150, 38));
        hopChonTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hopChonTrangThai.setBackground(mauNenOData);

        khungBoLocBenTrai.add(oNhapTimKiem);
        khungBoLocBenTrai.add(nutTimKiem);
        khungBoLocBenTrai.add(hopChonTrangThai);

        nutThemKhachHang = new JButton("+ Thêm khách hàng");
        nutThemKhachHang.setPreferredSize(new Dimension(160, 38));
        nutThemKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nutThemKhachHang.setForeground(Color.WHITE);
        nutThemKhachHang.setBackground(mauHongNutChinh);
        nutThemKhachHang.setFocusPainted(false);
        nutThemKhachHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutThemKhachHang, BorderLayout.EAST);

        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS));
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);

        theChuaNoiDung.add(khungPhiaTren, BorderLayout.NORTH);

        // ===== 3. TABLE DATA =====
        String[] tieuDeCot = {"Mã KH", "Họ tên", "Giới tính", "Số điện thoại", "Địa chỉ", "Trạng thái", "Thao tác"};

        moHinhBang = new DefaultTableModel(danhSachKhachHangGoc, tieuDeCot) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bangKhachHang = new JTable(moHinhBang);
        bangKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bangKhachHang.setRowHeight(52);
        bangKhachHang.setShowGrid(true);
        bangKhachHang.setGridColor(mauDuongKeBang);
        bangKhachHang.setSelectionBackground(new Color(0xF2, 0xF7, 0xFC));
        bangKhachHang.setSelectionForeground(mauChuTieuDe);
        bangKhachHang.setFillsViewportHeight(true);

        JTableHeader dauBang = bangKhachHang.getTableHeader();
        dauBang.setPreferredSize(new Dimension(0, 46));
        dauBang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dauBang.setBackground(mauNenTieuDeCot);
        dauBang.setForeground(mauTieuDeCot);
        dauBang.setReorderingAllowed(false);

        thietLapLaiCotBang();

        JScrollPane thanhCuon = new JScrollPane(bangKhachHang);
        thanhCuon.getViewport().setBackground(mauTheTrang);
        thanhCuon.setBorder(BorderFactory.createLineBorder(mauVienNhe, 1));

        theChuaNoiDung.add(thanhCuon, BorderLayout.CENTER);
        add(theChuaNoiDung, BorderLayout.CENTER);
    }

    private void khoiTaoSuKien() {
        // Sự kiện Tìm kiếm & Lọc dữ liệu thời gian thực
        ActionListener hanhDongTimKiem = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tuKhoa = oNhapTimKiem.getText().trim().toLowerCase();
                String trangThaiChon = (String) hopChonTrangThai.getSelectedItem();

                DefaultTableModel modelMoi = new DefaultTableModel(new String[]{"Mã KH", "Họ tên", "Giới tính", "Số điện thoại", "Địa chỉ", "Trạng thái", "Thao tác"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) { return false; }
                };

                for (Object[] hang : danhSachKhachHangGoc) {
                    String maKH = hang[0].toString().toLowerCase();
                    String tenKH = hang[1].toString().toLowerCase();
                    String sdt = hang[3].toString().toLowerCase();
                    String trangThaiHang = hang[5].toString();

                    boolean khopTuKhoa = tuKhoa.isEmpty() || maKH.contains(tuKhoa) || tenKH.contains(tuKhoa) || sdt.contains(tuKhoa);
                    boolean khopTrangThai = trangThaiChon.equals("Tất cả trạng thái") || trangThaiHang.equalsIgnoreCase(trangThaiChon);

                    if (khopTuKhoa && khopTrangThai) {
                        modelMoi.addRow(hang);
                    }
                }

                // Sửa lỗi columnIdentifiers bằng cách truyền Vector tên cột tường minh
                String[] tieuDeCot = {"Mã KH", "Họ tên", "Giới tính", "Số điện thoại", "Địa chỉ", "Trạng thái", "Thao tác"};
                moHinhBang.setDataVector(modelMoi.getDataVector(), new Vector<>(java.util.Arrays.asList(tieuDeCot)));
                thietLapLaiCotBang();
            }
        };

        nutTimKiem.addActionListener(hanhDongTimKiem);
        oNhapTimKiem.addActionListener(hanhDongTimKiem);
        hopChonTrangThai.addActionListener(hanhDongTimKiem);

        // Sự kiện mở form Thêm khách hàng mới
        nutThemKhachHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hienThiDialogThemKhachHang();
            }
        });

        // Sự kiện click chọn dòng hoặc nút thao tác trên bảng
        bangKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int hangChon = bangKhachHang.getSelectedRow();
                int cotChon = bangKhachHang.getSelectedColumn();
                if (hangChon >= 0) {
                    String maKH = bangKhachHang.getValueAt(hangChon, 0).toString();
                    String tenKH = bangKhachHang.getValueAt(hangChon, 1).toString();
                    
                    if (cotChon == 6) {
                        JOptionPane.showMessageDialog(CustomerManagementPanel.this, 
                            "Thực hiện sửa thông tin cho khách hàng: " + tenKH + " (" + maKH + ")", 
                            "Thao tác", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    private void thietLapLaiCotBang() {
        if (bangKhachHang.getColumnCount() > 0) {
            DefaultTableCellRenderer canLeTrai = new DefaultTableCellRenderer();
            canLeTrai.setBorder(new EmptyBorder(0, 12, 0, 12));
            
            DefaultTableCellRenderer canLeGiua = new DefaultTableCellRenderer();
            canLeGiua.setHorizontalAlignment(SwingConstants.CENTER);

            bangKhachHang.getColumnModel().getColumn(0).setPreferredWidth(120);
            bangKhachHang.getColumnModel().getColumn(0).setCellRenderer(canLeTrai);
            bangKhachHang.getColumnModel().getColumn(1).setPreferredWidth(160);
            bangKhachHang.getColumnModel().getColumn(1).setCellRenderer(canLeTrai);
            bangKhachHang.getColumnModel().getColumn(2).setPreferredWidth(80);
            bangKhachHang.getColumnModel().getColumn(2).setCellRenderer(canLeGiua);
            bangKhachHang.getColumnModel().getColumn(3).setPreferredWidth(120);
            bangKhachHang.getColumnModel().getColumn(3).setCellRenderer(canLeGiua);
            bangKhachHang.getColumnModel().getColumn(4).setPreferredWidth(220);
            bangKhachHang.getColumnModel().getColumn(4).setCellRenderer(canLeTrai);
            bangKhachHang.getColumnModel().getColumn(5).setPreferredWidth(150);
            bangKhachHang.getColumnModel().getColumn(5).setCellRenderer(new BoKetXuatTrangThai());
            bangKhachHang.getColumnModel().getColumn(6).setPreferredWidth(160);
            bangKhachHang.getColumnModel().getColumn(6).setCellRenderer(new BoKetXuatThaoTac());
        }
    }

    private void hienThiDialogThemKhachHang() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khách hàng mới", true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 15));
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(mauTheTrang);

        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSDT = new JTextField();
        JTextField txtDiaChi = new JTextField();

        panelForm.add(new JLabel("Họ tên khách hàng:"));
        panelForm.add(txtHoTen);
        panelForm.add(new JLabel("Giới tính:"));
        panelForm.add(cbGioiTinh);
        panelForm.add(new JLabel("Số điện thoại:"));
        panelForm.add(txtSDT);
        panelForm.add(new JLabel("Địa chỉ:"));
        panelForm.add(txtDiaChi);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelNut.setBackground(mauNenChinh);
        JButton btnLuu = new JButton("Lưu thông tin");
        JButton btnHuy = new JButton("Hủy");

        btnLuu.setBackground(mauHongNutChinh);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            if(txtHoTen.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ họ tên và số điện thoại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Tạo mã KH tự động tăng dựa trên số lượng khách hàng gốc
            String maMoi = String.format("KH261006%03d", danhSachKhachHangGoc.length + 1);
            Object[] khachHangMoi = new Object[]{
                maMoi,
                txtHoTen.getText().trim(),
                cbGioiTinh.getSelectedItem().toString(),
                txtSDT.getText().trim(),
                txtDiaChi.getText().trim(),
                "Đang hoạt động",
                "ThaoTac"
            };

            // Lưu vào mảng dữ liệu gốc để không bị mất khi tìm kiếm hay lọc
            Object[][] mangMoi = new Object[danhSachKhachHangGoc.length + 1][];
            System.arraycopy(danhSachKhachHangGoc, 0, mangMoi, 0, danhSachKhachHangGoc.length);
            mangMoi[danhSachKhachHangGoc.length] = khachHangMoi;
            danhSachKhachHangGoc = mangMoi;

            // Thêm trực tiếp lên bảng giao diện
            moHinhBang.addRow(khachHangMoi);

            JOptionPane.showMessageDialog(dialog, "Thêm khách hàng mới thành công!");
            dialog.dispose();
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        panelNut.add(btnLuu);
        panelNut.add(btnHuy);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelNut, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private class BoKetXuatTrangThai extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String giaTri = value != null ? value.toString() : "";
            boolean laHoatDong = giaTri.equalsIgnoreCase("Đang hoạt động");

            JPanel khungBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungBadge.setOpaque(false);

            JLabel nhanBadge = new JLabel(giaTri, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(laHoatDong ? mauNenXanhTrangThai : mauNenDoTrangThai);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            nhanBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanBadge.setForeground(laHoatDong ? mauXanhTrangThai : mauDoTrangThai);
            nhanBadge.setPreferredSize(new Dimension(laHoatDong ? 120 : 130, 28));

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }

    private class BoKetXuatThaoTac extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel khungThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
            khungThaoTac.setOpaque(false);

            JLabel nhanNutSua = new JLabel("✏️ Sửa", SwingConstants.CENTER);
            nhanNutSua.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanNutSua.setForeground(mauNutSua);
            nhanNutSua.setOpaque(true);
            nhanNutSua.setBackground(mauNenNutSua);
            nhanNutSua.setPreferredSize(new Dimension(55, 28));

            JLabel nhanNutThuCung = new JLabel("🐾 Thú cưng", SwingConstants.CENTER);
            nhanNutThuCung.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nhanNutThuCung.setForeground(mauNutThuCung);
            nhanNutThuCung.setOpaque(true);
            nhanNutThuCung.setBackground(mauNenNutThuCung);
            nhanNutThuCung.setPreferredSize(new Dimension(80, 28));

            khungThaoTac.add(nhanNutSua);
            khungThaoTac.add(nhanNutThuCung);
            return khungThaoTac;
        }
    }
}