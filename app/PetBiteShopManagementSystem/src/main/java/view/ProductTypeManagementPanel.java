package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Giao diện màn hình Quản lý loại sản phẩm - Thiết kế theo bản vẽ Figma (node 130:1329)
 * Toàn bộ các biến được đặt tên theo tiếng Việt chuẩn cú pháp camelCase.
 */
public class ProductTypeManagementPanel extends JPanel {

    // Bảng màu chuẩn trích xuất từ thiết kế Figma
    private final Color mauNenChinh        = new Color(0xF4, 0xF6, 0xFA);
    private final Color mauTheTrang        = Color.WHITE;
    private final Color mauChuTieuDe       = new Color(0x1C, 0x38, 0x4C);
    private final Color mauChuPhu          = new Color(0x7F, 0x90, 0x9E);
    private final Color mauVienNhe         = new Color(0xE3, 0xE8, 0xEE);
    private final Color mauNenOData        = new Color(0xF8, 0xFB, 0xFD);
    private final Color mauChuBoLoc        = new Color(0x60, 0x74, 0x83);
    private final Color mauCamChinh        = new Color(0xF4, 0xA3, 0x2C);
    private final Color mauTieuDeCot       = new Color(0x4C, 0x64, 0x74);
    private final Color mauNenTieuDeCot    = new Color(0xF7, 0xFA, 0xFD);
    private final Color mauDuongKeBang     = new Color(0xEE, 0xF2, 0xF6);
    private final Color mauXanhTrangThai   = new Color(0x2E, 0xAF, 0x64);
    private final Color mauNenXanhTrangThai= new Color(0xEA, 0xF7, 0xEE);
    private final Color mauDoTrangThai     = new Color(0xE5, 0x6B, 0x6F);
    private final Color mauNenDoTrangThai  = new Color(0xFD, 0xEB, 0xEC);
    private final Color mauNutSua          = new Color(0xA7, 0x64, 0x00);
    private final Color mauNenNutSua       = new Color(0xFF, 0xF4, 0xDF);
    private final Color mauNutTrangActive  = new Color(0x12, 0x39, 0x58);

    // Font chữ giao diện
    private final Font phongTieuDeKhoi     = new Font("Segoe UI", Font.BOLD, 18);
    private final Font phongChuMoTa        = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongNutChucNang    = new Font("Segoe UI", Font.BOLD, 14);
    private final Font phongBangDuLieu     = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongTieuDeBang     = new Font("Segoe UI", Font.BOLD, 13);
    private final Font phongBadgeNho       = new Font("Segoe UI", Font.BOLD, 12);

    // Khai báo các thành phần giao diện
    private JPanel khungChinh;
    private JPanel theChuaDanhSach;
    private JPanel khungTieuDeKhoi;
    private JLabel nhanTieuDeDanhSach;
    private JLabel nhanMoTaDanhSach;
    private JPanel khungThanhCongCu;
    private JPanel khungBoLocBenTrai;
    private JTextField oNhapTimKiem;
    private JComboBox<String> hopChonTrangThai;
    private JComboBox<String> hopChonSapXep;
    private JButton nutThemLoaiSanPham;
    private JTable bangLoaiSanPham;
    private DefaultTableModel moHinhBang;
    private JScrollPane thanhCuonBang;
    private JPanel khungPhanTrang;
    private JLabel nhanThongKePhanTrang;
    private JPanel khungCacNutChuyenTrang;
    private JButton nutTrangTruoc;
    private JButton nutTrangSoMot;
    private JButton nutTrangSoHai;
    private JButton nutTrangKeTiep;

    public ProductTypeManagementPanel() {
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        // Cấu hình panel chính
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 24, 24));

        // Khung thẻ màu trắng chứa toàn bộ nội dung quản lý
        theChuaDanhSach = new JPanel(new BorderLayout());
        theChuaDanhSach.setBackground(mauTheTrang);
        theChuaDanhSach.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // ===== 1. Khung tiêu đề khối & mô tả =====
        khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        nhanTieuDeDanhSach = new JLabel("Danh sách loại sản phẩm");
        nhanTieuDeDanhSach.setFont(phongTieuDeKhoi);
        nhanTieuDeDanhSach.setForeground(mauChuTieuDe);

        nhanMoTaDanhSach = new JLabel("Bảng hiển thị đầy đủ các thuộc tính chính của đối tượng đang quản lý.");
        nhanMoTaDanhSach.setFont(phongChuMoTa);
        nhanMoTaDanhSach.setForeground(mauChuPhu);

        khungTieuDeKhoi.add(nhanTieuDeDanhSach);
        khungTieuDeKhoi.add(nhanMoTaDanhSach);

        // ===== 2. Thanh công cụ (Tìm kiếm, Lọc, Thêm mới) =====
        khungThanhCongCu = new JPanel(new BorderLayout());
        khungThanhCongCu.setOpaque(false);
        khungThanhCongCu.setBorder(new EmptyBorder(16, 0, 16, 0));

        // Nhóm bộ lọc bên trái
        khungBoLocBenTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        khungBoLocBenTrai.setOpaque(false);

        // Ô nhập tìm kiếm theo mã hoặc tên với Placeholder thông minh
        oNhapTimKiem = new JTextField(24) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(mauNenOData);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D gPlaceholder = (Graphics2D) g.create();
                    gPlaceholder.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    gPlaceholder.setColor(mauChuBoLoc);
                    gPlaceholder.setFont(phongChuMoTa);
                    Insets insets = getInsets();
                    java.awt.FontMetrics fm = gPlaceholder.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    gPlaceholder.drawString("  🔎 Tìm kiếm theo mã hoặc tên", insets.left, y);
                    gPlaceholder.dispose();
                }
            }
        };
        oNhapTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent suKien) {
                oNhapTimKiem.repaint();
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent suKien) {
                oNhapTimKiem.repaint();
            }
        });
        oNhapTimKiem.setOpaque(false);
        oNhapTimKiem.setPreferredSize(new Dimension(300, 38));
        oNhapTimKiem.setFont(phongChuMoTa);
        oNhapTimKiem.setForeground(mauChuTieuDe);
        oNhapTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        // Hộp chọn trạng thái
        String[] danhSachLuaChonTrangThai = {"Trạng thái", "Đang sử dụng", "Ngừng sử dụng"};
        hopChonTrangThai = new JComboBox<>(danhSachLuaChonTrangThai);
        hopChonTrangThai.setPreferredSize(new Dimension(140, 38));
        hopChonTrangThai.setFont(phongChuMoTa);
        hopChonTrangThai.setForeground(mauChuBoLoc);
        hopChonTrangThai.setBackground(mauNenOData);
        hopChonTrangThai.setFocusable(false);

        // Hộp chọn thứ tự sắp xếp
        String[] danhSachLuaChonSapXep = {"Sắp xếp: Mới nhất", "Sắp xếp: Cũ nhất", "Tên A-Z", "Tên Z-A"};
        hopChonSapXep = new JComboBox<>(danhSachLuaChonSapXep);
        hopChonSapXep.setPreferredSize(new Dimension(160, 38));
        hopChonSapXep.setFont(phongChuMoTa);
        hopChonSapXep.setForeground(mauChuBoLoc);
        hopChonSapXep.setBackground(mauNenOData);
        hopChonSapXep.setFocusable(false);

        khungBoLocBenTrai.add(oNhapTimKiem);
        khungBoLocBenTrai.add(hopChonTrangThai);
        khungBoLocBenTrai.add(hopChonSapXep);

        // Nút thêm mới loại sản phẩm bên phải
        nutThemLoaiSanPham = new JButton("+ Thêm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauCamChinh);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nutThemLoaiSanPham.setPreferredSize(new Dimension(120, 38));
        nutThemLoaiSanPham.setFont(phongNutChucNang);
        nutThemLoaiSanPham.setForeground(mauChuTieuDe);
        nutThemLoaiSanPham.setFocusPainted(false);
        nutThemLoaiSanPham.setContentAreaFilled(false);
        nutThemLoaiSanPham.setBorderPainted(false);
        nutThemLoaiSanPham.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Mở popup Thêm loại sản phẩm
        nutThemLoaiSanPham.addActionListener(suKien -> {
            Frame cuaSoCha = (Frame) SwingUtilities.getWindowAncestor(this);
            AddProductTypeDialog hopThoaiThem = new AddProductTypeDialog(cuaSoCha);
            hopThoaiThem.setVisible(true);
        });

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutThemLoaiSanPham, BorderLayout.EAST);

        // Ghép phần tiêu đề và thanh công cụ lên phía trên
        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS));
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);
        theChuaDanhSach.add(khungPhiaTren, BorderLayout.NORTH);

        // ===== 3. Bảng dữ liệu loại sản phẩm =====
        String[] tieuDeCacCot = {"Mã loại", "Tên loại sản phẩm", "Mô tả", "Trạng thái", "Thao tác"};
        Object[][] duLieuMauTuFigma = {
            {"LSP241018001", "Thức ăn", "Thức ăn khô, pate và snack…", "Đang sử dụng", "Sửa"},
            {"LSP241018002", "Phụ kiện", "Vòng cổ, dây dắt, chuồng…", "Đang sử dụng", "Sửa"},
            {"LSP241018003", "Vệ sinh", "Cát vệ sinh, sữa tắm, lược…", "Đang sử dụng", "Sửa"},
            {"LSP241018004", "Thuốc & bổ sung", "Vitamin, men tiêu hóa, thuốc…", "Ngừng sử dụng", "Sửa"},
            {"LSP241018005", "Đồ chơi", "Bóng, gặm nhấm, cần câu…", "Đang sử dụng", "Sửa"}
        };

        moHinhBang = new DefaultTableModel(duLieuMauTuFigma, tieuDeCacCot) {
            @Override
            public boolean isCellEditable(int hang, int cot) {
                return false; // Giao diện tĩnh, không chỉnh sửa trực tiếp trên ô
            }
        };

        bangLoaiSanPham = new JTable(moHinhBang);
        bangLoaiSanPham.setFont(phongBangDuLieu);
        bangLoaiSanPham.setRowHeight(52);
        bangLoaiSanPham.setShowGrid(true);
        bangLoaiSanPham.setGridColor(mauDuongKeBang);
        bangLoaiSanPham.setSelectionBackground(new Color(0xF2, 0xF7, 0xFC));
        bangLoaiSanPham.setSelectionForeground(mauChuTieuDe);
        bangLoaiSanPham.setFillsViewportHeight(true);

        // Tùy chỉnh thanh tiêu đề của bảng (JTableHeader)
        JTableHeader dauBang = bangLoaiSanPham.getTableHeader();
        dauBang.setPreferredSize(new Dimension(0, 46));
        dauBang.setFont(phongTieuDeBang);
        dauBang.setBackground(mauNenTieuDeCot);
        dauBang.setForeground(mauTieuDeCot);
        dauBang.setReorderingAllowed(false);
        dauBang.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, mauDuongKeBang));

        // Căn lề và kích thước các cột
        DefaultTableCellRenderer boCanLeTrai = new DefaultTableCellRenderer();
        boCanLeTrai.setBorder(new EmptyBorder(0, 14, 0, 14));
        boCanLeTrai.setForeground(mauChuTieuDe);

        bangLoaiSanPham.getColumnModel().getColumn(0).setPreferredWidth(140);
        bangLoaiSanPham.getColumnModel().getColumn(0).setCellRenderer(boCanLeTrai);

        bangLoaiSanPham.getColumnModel().getColumn(1).setPreferredWidth(180);
        bangLoaiSanPham.getColumnModel().getColumn(1).setCellRenderer(boCanLeTrai);

        bangLoaiSanPham.getColumnModel().getColumn(2).setPreferredWidth(340);
        bangLoaiSanPham.getColumnModel().getColumn(2).setCellRenderer(boCanLeTrai);

        // Cột Trạng thái: hiển thị badge bo góc màu xanh/đỏ
        bangLoaiSanPham.getColumnModel().getColumn(3).setPreferredWidth(160);
        bangLoaiSanPham.getColumnModel().getColumn(3).setCellRenderer(new BoKetXuatTrangThai());

        // Cột Thao tác: hiển thị nút "Sửa" màu cam nhạt
        bangLoaiSanPham.getColumnModel().getColumn(4).setPreferredWidth(110);
        bangLoaiSanPham.getColumnModel().getColumn(4).setCellRenderer(new BoKetXuatThaoTac());

        // Bắt sự kiện click vào nút "Sửa" ở cột thứ 4
        bangLoaiSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                int cotDuocChon = bangLoaiSanPham.columnAtPoint(suKien.getPoint());
                if (cotDuocChon == 4) {
                    Frame cuaSoCha = (Frame) SwingUtilities.getWindowAncestor(ProductTypeManagementPanel.this);
                    UpdateProductTypeDialog hopThoaiCapNhat = new UpdateProductTypeDialog(cuaSoCha);
                    hopThoaiCapNhat.setVisible(true);
                }
            }
        });

        thanhCuonBang = new JScrollPane(bangLoaiSanPham);
        thanhCuonBang.getViewport().setBackground(mauTheTrang);
        thanhCuonBang.setBorder(BorderFactory.createLineBorder(mauVienNhe, 1));
        theChuaDanhSach.add(thanhCuonBang, BorderLayout.CENTER);

        // ===== 4. Khung phân trang ở đáy bảng =====
        khungPhanTrang = new JPanel(new BorderLayout());
        khungPhanTrang.setOpaque(false);
        khungPhanTrang.setBorder(new EmptyBorder(16, 0, 0, 0));

        nhanThongKePhanTrang = new JLabel("Hiển thị 1–5 / 25");
        nhanThongKePhanTrang.setFont(phongChuMoTa);
        nhanThongKePhanTrang.setForeground(mauChuPhu);

        khungCacNutChuyenTrang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        khungCacNutChuyenTrang.setOpaque(false);

        nutTrangTruoc = taoNutChuyenTrang("<", false);
        nutTrangSoMot = taoNutChuyenTrang("1", true);   // Trang đang active
        nutTrangSoHai = taoNutChuyenTrang("2", false);
        nutTrangKeTiep = taoNutChuyenTrang(">", false);

        khungCacNutChuyenTrang.add(nutTrangTruoc);
        khungCacNutChuyenTrang.add(nutTrangSoMot);
        khungCacNutChuyenTrang.add(nutTrangSoHai);
        khungCacNutChuyenTrang.add(nutTrangKeTiep);

        khungPhanTrang.add(nhanThongKePhanTrang, BorderLayout.WEST);
        khungPhanTrang.add(khungCacNutChuyenTrang, BorderLayout.EAST);

        theChuaDanhSach.add(khungPhanTrang, BorderLayout.SOUTH);

        // Gắn thẻ nội dung vào giao diện tổng
        add(theChuaDanhSach, BorderLayout.CENTER);
    }

    /**
     * Hàm hỗ trợ tạo các nút số trang phân trang chuẩn kích thước 34x34 bo góc
     */
    private JButton taoNutChuyenTrang(String kyTu, boolean dangKichHoat) {
        JButton nut = new JButton(kyTu) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (dangKichHoat) {
                    g2.setColor(mauNutTrangActive);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                } else {
                    g2.setColor(mauTheTrang);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                    g2.setColor(mauVienNhe);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nut.setPreferredSize(new Dimension(34, 34));
        nut.setFont(new Font("Segoe UI", dangKichHoat ? Font.BOLD : Font.PLAIN, 13));
        nut.setForeground(dangKichHoat ? Color.WHITE : mauChuBoLoc);
        nut.setFocusPainted(false);
        nut.setContentAreaFilled(false);
        nut.setBorderPainted(false);
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nut.setMargin(new Insets(0, 0, 0, 0));
        return nut;
    }

    /**
     * Renderer hiển thị trạng thái dạng nhãn con nhộng (Pill Badge) bo tròn
     */
    private class BoKetXuatTrangThai extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String giaTri = value != null ? value.toString() : "";
            boolean laDangSuDung = giaTri.equalsIgnoreCase("Đang sử dụng");

            JPanel khungBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungBadge.setOpaque(false);

            JLabel nhanBadge = new JLabel(giaTri, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(laDangSuDung ? mauNenXanhTrangThai : mauNenDoTrangThai);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            nhanBadge.setFont(phongBadgeNho);
            nhanBadge.setForeground(laDangSuDung ? mauXanhTrangThai : mauDoTrangThai);
            nhanBadge.setPreferredSize(new Dimension(laDangSuDung ? 110 : 120, 28));

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }

    /**
     * Renderer hiển thị nút "Sửa" bo góc màu cam nhạt
     */
    private class BoKetXuatThaoTac extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel khungThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungThaoTac.setOpaque(false);

            JLabel nhanNutSua = new JLabel("Sửa", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(mauNenNutSua);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            nhanNutSua.setFont(phongBadgeNho);
            nhanNutSua.setForeground(mauNutSua);
            nhanNutSua.setPreferredSize(new Dimension(60, 28));
            nhanNutSua.setCursor(new Cursor(Cursor.HAND_CURSOR));

            khungThaoTac.add(nhanNutSua);
            return khungThaoTac;
        }
    }
}
