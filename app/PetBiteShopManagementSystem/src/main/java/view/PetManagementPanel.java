package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PetManagementPanel extends JPanel {
    private JTable bangThuCung;
    private DefaultTableModel moHinhBang;
    private JTextField oNhapTimKiem;
    private JButton nutTimKiem;
    private JComboBox<String> hopChonTrangThai;
    private JButton nutThemThuCung;

    // Danh sách dữ liệu thú cưng gốc
    private Object[][] danhSachThuCungGoc = {
        {"TC261006001", "Milo", "12/03/2025", "4.2 kg", "Nguyễn Minh Anh", "0901234567", "Đang quản lý", "🗑️ Xóa"},
        {"TC261006002", "Luna", "04/08/2024", "3.5 kg", "Trần Quốc Huy", "0912345678", "Đang quản lý", "🗑️ Xóa"},
        {"TC261006003", "Bơ", "20/11/2023", "2.9 kg", "Lê Thảo Vy", "0987654321", "Đang quản lý", "🗑️ Xóa"},
        {"TC261006004", "Rocky", "18/02/2022", "5.0 kg", "Phạm Gia Bảo", "0933222111", "Ngừng quản lý", "🗑️ Xóa"},
        {"TC261006005", "Bella", "09/09/2024", "6.1 kg", "Vũ Ngọc Linh", "0966888999", "Đang quản lý", "🗑️ Xóa"},
        {"TC261006006", "Leo", "30/05/2025", "2.6 kg", "Nguyễn Hà My", "0977555444", "Đang quản lý", "🗑️ Xóa"}
    };

    // Màu sắc 
    private final Color mauNenChinh         = new Color(0xF4, 0xF6, 0xFA);
    private final Color mauTheTrang         = Color.WHITE;
    private final Color mauChuTieuDe        = new Color(0x1C, 0x38, 0x4C);
    private final Color mauChuPhu           = new Color(0x7F, 0x90, 0x9E);
    private final Color mauVienNhe          = new Color(0xE3, 0xE8, 0xEE);
    private final Color mauNenOData         = new Color(0xF8, 0xFB, 0xFD);
    private final Color mauHongNutChinh     = new Color(0xD9, 0x82, 0x8B); 
    private final Color mauTieuDeCot        = new Color(0x4C, 0x64, 0x74);
    private final Color mauNenTieuDeCot     = new Color(0xF7, 0xFA, 0xFD);
    private final Color mauDuongKeBang      = new Color(0xEE, 0xF2, 0xF6);
    private final Color mauXanhTrangThai    = new Color(0x2E, 0xAF, 0x64);
    private final Color mauNenXanhTrangThai = new Color(0xEA, 0xF7, 0xEE);
    private final Color mauDoTrangThai      = new Color(0xE5, 0x6B, 0x6F);
    private final Color mauNenDoTrangThai   = new Color(0xFD, 0xEB, 0xEC);

    // Font chữ
    private final Font phongTieuDeKhoi      = new Font("Segoe UI", Font.BOLD, 18);
    private final Font phongChuMoTa         = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongNutChucNang     = new Font("Segoe UI", Font.BOLD, 13);
    private final Font phongBangDuLieu      = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongTieuDeBang      = new Font("Segoe UI", Font.BOLD, 13);
    private final Font phongBadgeNho        = new Font("Segoe UI", Font.BOLD, 12);

    public PetManagementPanel() {
        khoiTaoGiaoDien();
        khoiTaoSuKien();
    }

    private void khoiTaoGiaoDien() {
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 24, 24));

        JPanel theChuaDanhSach = new JPanel(new BorderLayout());
        theChuaDanhSach.setBackground(mauTheTrang);
        theChuaDanhSach.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // 1. Khung tiêu đề khối & mô tả 
        JPanel khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        JLabel nhanTieuDeDanhSach = new JLabel("QUẢN LÝ THÚ CƯNG");
        nhanTieuDeDanhSach.setFont(phongTieuDeKhoi);
        nhanTieuDeDanhSach.setForeground(mauChuTieuDe);

        JLabel nhanMoTaDanhSach = new JLabel("Theo dõi hồ sơ thú cưng, chủ sở hữu, tình trạng và thông tin chăm sóc cơ bản");
        nhanMoTaDanhSach.setFont(phongChuMoTa);
        nhanMoTaDanhSach.setForeground(mauChuPhu);

        khungTieuDeKhoi.add(nhanTieuDeDanhSach);
        khungTieuDeKhoi.add(nhanMoTaDanhSach);

        //  2. Thanh công cụ (Tìm kiếm, Nút Tìm, Trạng thái, Nút Thêm) 
        JPanel khungThanhCongCu = new JPanel(new BorderLayout());
        khungThanhCongCu.setOpaque(false);
        khungThanhCongCu.setBorder(new EmptyBorder(16, 0, 16, 0));

        JPanel khungBoLocBenTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        khungBoLocBenTrai.setOpaque(false);

        oNhapTimKiem = new JTextField(20);
        oNhapTimKiem.setPreferredSize(new Dimension(240, 38));
        oNhapTimKiem.setFont(phongChuMoTa);
        oNhapTimKiem.setForeground(mauChuTieuDe);
        oNhapTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        nutTimKiem = new JButton("🔍 Tìm kiếm");
        nutTimKiem.setPreferredSize(new Dimension(110, 38));
        nutTimKiem.setFont(phongNutChucNang);
        nutTimKiem.setForeground(Color.WHITE);
        nutTimKiem.setBackground(mauHongNutChinh);
        nutTimKiem.setFocusPainted(false);
        nutTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String[] danhSachTrangThai = {"Tất cả trạng thái", "Đang quản lý", "Ngừng quản lý"};
        hopChonTrangThai = new JComboBox<>(danhSachTrangThai);
        hopChonTrangThai.setPreferredSize(new Dimension(160, 38));
        hopChonTrangThai.setFont(phongChuMoTa);
        hopChonTrangThai.setBackground(mauNenOData);

        khungBoLocBenTrai.add(oNhapTimKiem);
        khungBoLocBenTrai.add(nutTimKiem);
        khungBoLocBenTrai.add(hopChonTrangThai);

        nutThemThuCung = new JButton("+ Thêm thú cưng");
        nutThemThuCung.setPreferredSize(new Dimension(150, 38));
        nutThemThuCung.setFont(phongNutChucNang);
        nutThemThuCung.setForeground(Color.WHITE);
        nutThemThuCung.setBackground(mauHongNutChinh);
        nutThemThuCung.setFocusPainted(false);
        nutThemThuCung.setCursor(new Cursor(Cursor.HAND_CURSOR));

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutThemThuCung, BorderLayout.EAST);

        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS));
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);
        
        theChuaDanhSach.add(khungPhiaTren, BorderLayout.NORTH);

        //3. Bảng dữ liệu 
        String[] tieuDeCacCot = {"Mã thú cưng", "Tên thú cưng", "Ngày sinh", "Cân nặng", "Chủ sở hữu", "Số điện thoại", "Trạng thái", "Thao tác"};

        moHinhBang = new DefaultTableModel(danhSachThuCungGoc, tieuDeCacCot) {
            @Override
            public boolean isCellEditable(int hang, int cot) { return false; }
        };

        bangThuCung = new JTable(moHinhBang);
        bangThuCung.setFont(phongBangDuLieu);
        bangThuCung.setRowHeight(52);
        bangThuCung.setShowGrid(true);
        bangThuCung.setGridColor(mauDuongKeBang);
        bangThuCung.setSelectionBackground(new Color(0xF2, 0xF7, 0xFC));
        bangThuCung.setSelectionForeground(mauChuTieuDe);
        bangThuCung.setFillsViewportHeight(true);

        JTableHeader dauBang = bangThuCung.getTableHeader();
        dauBang.setPreferredSize(new Dimension(0, 46));
        dauBang.setFont(phongTieuDeBang);
        dauBang.setBackground(mauNenTieuDeCot);
        dauBang.setForeground(mauTieuDeCot);
        dauBang.setReorderingAllowed(false);

        thietLapLaiCotBang();

        JScrollPane thanhCuonBang = new JScrollPane(bangThuCung);
        thanhCuonBang.getViewport().setBackground(mauTheTrang);
        thanhCuonBang.setBorder(BorderFactory.createLineBorder(mauVienNhe, 1));
        
        theChuaDanhSach.add(thanhCuonBang, BorderLayout.CENTER);
        add(theChuaDanhSach, BorderLayout.CENTER);
    }

    private void khoiTaoSuKien() {
        ActionListener hanhDongLoc = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tuKhoa = oNhapTimKiem.getText().trim().toLowerCase();
                String trangThaiChon = (String) hopChonTrangThai.getSelectedItem();

                DefaultTableModel modelMoi = new DefaultTableModel(new String[]{"Mã thú cưng", "Tên thú cưng", "Ngày sinh", "Cân nặng", "Chủ sở hữu", "Số điện thoại", "Trạng thái", "Thao tác"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) { return false; }
                };

                for (Object[] hang : danhSachThuCungGoc) {
                    String maTC = hang[0].toString().toLowerCase();
                    String tenTC = hang[1].toString().toLowerCase();
                    String tenChu = hang[4].toString().toLowerCase();
                    String trangThaiHang = hang[6].toString();

                    boolean khopTuKhoa = tuKhoa.isEmpty() || maTC.contains(tuKhoa) || tenTC.contains(tuKhoa) || tenChu.contains(tuKhoa);
                    boolean khopTrangThai = trangThaiChon.equals("Tất cả trạng thái") || trangThaiHang.equalsIgnoreCase(trangThaiChon);

                    if (khopTuKhoa && khopTrangThai) {
                        modelMoi.addRow(hang);
                    }
                }

                String[] tieuDeCacCot = {"Mã thú cưng", "Tên thú cưng", "Ngày sinh", "Cân nặng", "Chủ sở hữu", "Số điện thoại", "Trạng thái", "Thao tác"};
                moHinhBang.setDataVector(modelMoi.getDataVector(), new Vector<>(java.util.Arrays.asList(tieuDeCacCot)));
                thietLapLaiCotBang();
            }
        };

        nutTimKiem.addActionListener(hanhDongLoc);
        oNhapTimKiem.addActionListener(hanhDongLoc);
        hopChonTrangThai.addActionListener(hanhDongLoc);

        nutThemThuCung.addActionListener(e -> hienThiDialogThemThuCung());

        // Bắt sự kiện click vào cột Thao tác (Cột thứ 8 - index 7): Bấm vào là hiển thị hộp thoại XÓA ngay lập tức
        bangThuCung.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int hangChon = bangThuCung.getSelectedRow();
                int cotChon = bangThuCung.getSelectedColumn();
                if (hangChon >= 0 && cotChon == 7) {
                    String maTC = bangThuCung.getValueAt(hangChon, 0).toString();
                    String tenTC = bangThuCung.getValueAt(hangChon, 1).toString();
                    
                    int xacNhan = JOptionPane.showConfirmDialog(PetManagementPanel.this, 
                        "Bạn có chắc muốn xóa thú cưng: " + tenTC + " (" + maTC + ") không?", 
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (xacNhan == JOptionPane.YES_OPTION) {
                        xoaThuCungKhoiDanhSachGoc(maTC);
                        hanhDongLoc.actionPerformed(null);
                        JOptionPane.showMessageDialog(PetManagementPanel.this, "Đã xóa thú cưng thành công!");
                    }
                }
            }
        });
    }

    private void xoaThuCungKhoiDanhSachGoc(String maTC) {
        int indexXoa = -1;
        for (int i = 0; i < danhSachThuCungGoc.length; i++) {
            if (danhSachThuCungGoc[i][0].toString().equals(maTC)) {
                indexXoa = i;
                break;
            }
        }
        if (indexXoa >= 0) {
            Object[][] mangMoi = new Object[danhSachThuCungGoc.length - 1][];
            System.arraycopy(danhSachThuCungGoc, 0, mangMoi, 0, indexXoa);
            System.arraycopy(danhSachThuCungGoc, indexXoa + 1, mangMoi, indexXoa, danhSachThuCungGoc.length - indexXoa - 1);
            danhSachThuCungGoc = mangMoi;
        }
    }

    private void thietLapLaiCotBang() {
        if (bangThuCung.getColumnCount() > 0) {
            DefaultTableCellRenderer canLeTrai = new DefaultTableCellRenderer();
            canLeTrai.setBorder(new EmptyBorder(0, 12, 0, 12));
            
            DefaultTableCellRenderer canLeGiua = new DefaultTableCellRenderer();
            canLeGiua.setHorizontalAlignment(SwingConstants.CENTER);

            bangThuCung.getColumnModel().getColumn(0).setPreferredWidth(120);
            bangThuCung.getColumnModel().getColumn(0).setCellRenderer(canLeTrai);
            bangThuCung.getColumnModel().getColumn(1).setPreferredWidth(130);
            bangThuCung.getColumnModel().getColumn(1).setCellRenderer(canLeTrai);
            bangThuCung.getColumnModel().getColumn(2).setPreferredWidth(110);
            bangThuCung.getColumnModel().getColumn(2).setCellRenderer(canLeGiua);
            bangThuCung.getColumnModel().getColumn(3).setPreferredWidth(90);
            bangThuCung.getColumnModel().getColumn(3).setCellRenderer(canLeGiua);
            bangThuCung.getColumnModel().getColumn(4).setPreferredWidth(170);
            bangThuCung.getColumnModel().getColumn(4).setCellRenderer(canLeTrai);
            bangThuCung.getColumnModel().getColumn(5).setPreferredWidth(120);
            bangThuCung.getColumnModel().getColumn(5).setCellRenderer(canLeGiua);
            bangThuCung.getColumnModel().getColumn(6).setPreferredWidth(140);
            bangThuCung.getColumnModel().getColumn(6).setCellRenderer(new BoKetXuatTrangThai());
            
            // Cột Thao tác chỉ chứa nút Xóa
            bangThuCung.getColumnModel().getColumn(7).setPreferredWidth(110);
            bangThuCung.getColumnModel().getColumn(7).setCellRenderer(canLeGiua);
        }
    }

    private void hienThiDialogThemThuCung() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm thú cưng mới", true);
        dialog.setSize(460, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 15));
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(mauTheTrang);

        JTextField txtTenTC = new JTextField();
        
        JSpinner spinnerNgaySinh = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerNgaySinh, "dd/MM/yyyy");
        spinnerNgaySinh.setEditor(dateEditor);

        JTextField txtCanNang = new JTextField();
        JTextField txtChuSoHuu = new JTextField();
        JTextField txtSDT = new JTextField();

        panelForm.add(new JLabel("Tên thú cưng:"));
        panelForm.add(txtTenTC);
        panelForm.add(new JLabel("Ngày sinh:"));
        panelForm.add(spinnerNgaySinh);
        panelForm.add(new JLabel("Cân nặng:"));
        panelForm.add(txtCanNang);
        panelForm.add(new JLabel("Chủ sở hữu:"));
        panelForm.add(txtChuSoHuu);
        panelForm.add(new JLabel("Số điện thoại:"));
        panelForm.add(txtSDT);

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
            if(txtTenTC.getText().trim().isEmpty() || txtChuSoHuu.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên thú cưng và tên chủ sở hữu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date ngayChon = (Date) spinnerNgaySinh.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngaySinhStr = sdf.format(ngayChon);

            String maMoi = String.format("TC261006%03d", danhSachThuCungGoc.length + 1);
            Object[] thuCungMoi = new Object[]{
                maMoi,
                txtTenTC.getText().trim(),
                ngaySinhStr,
                txtCanNang.getText().trim() + (txtCanNang.getText().trim().endsWith("kg") ? "" : " kg"),
                txtChuSoHuu.getText().trim(),
                txtSDT.getText().trim(),
                "Đang quản lý",
                "🗑️ Xóa"
            };

            Object[][] mangMoi = new Object[danhSachThuCungGoc.length + 1][];
            System.arraycopy(danhSachThuCungGoc, 0, mangMoi, 0, danhSachThuCungGoc.length);
            mangMoi[danhSachThuCungGoc.length] = thuCungMoi;
            danhSachThuCungGoc = mangMoi;

            moHinhBang.addRow(thuCungMoi);
            JOptionPane.showMessageDialog(dialog, "Thêm thú cưng mới thành công!");
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
            boolean laDangQuanLy = giaTri.equalsIgnoreCase("Đang quản lý");

            JPanel khungBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungBadge.setOpaque(false);

            JLabel nhanBadge = new JLabel(giaTri, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(laDangQuanLy ? mauNenXanhTrangThai : mauNenDoTrangThai);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            nhanBadge.setFont(phongBadgeNho);
            nhanBadge.setForeground(laDangQuanLy ? mauXanhTrangThai : mauDoTrangThai);
            nhanBadge.setPreferredSize(new Dimension(laDangQuanLy ? 115 : 125, 28));

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }
}