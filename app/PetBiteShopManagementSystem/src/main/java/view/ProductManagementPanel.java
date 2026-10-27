package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
 * Giao diện màn hình Quản lý sản phẩm - Thiết kế theo bản vẽ Figma (node 130:1506)
 * Toàn bộ các biến được đặt tên theo tiếng Việt chuẩn cú pháp camelCase.
 */
public class ProductManagementPanel extends JPanel {

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
    private JPanel theChuaDanhSach;
    private JPanel khungTieuDeKhoi;
    private JLabel nhanTieuDeDanhSach;
    private JLabel nhanMoTaDanhSach;
    private JPanel khungThanhCongCu;
    private JPanel khungBoLocBenTrai;
    private JTextField oNhapTimKiem;
    private JComboBox<String> hopChonTrangThai;
    private JComboBox<String> hopChonSapXep;
    private JButton nutThemSanPham;
    private JTable bangSanPham;
    private DefaultTableModel moHinhBang;
    private JScrollPane thanhCuonBang;
    private JPanel khungPhanTrang;
    private JLabel nhanThongKePhanTrang;
    private JPanel khungCacNutChuyenTrang;
    private JButton nutTrangTruoc;
    private JButton nutTrangSoMot;
    private JButton nutTrangSoHai;
    private JButton nutTrangKeTiep;

    public ProductManagementPanel() {
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setBackground(mauNenChinh);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 24, 24));

        theChuaDanhSach = new JPanel(new BorderLayout());
        theChuaDanhSach.setBackground(mauTheTrang);
        theChuaDanhSach.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienNhe, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        ));

        // ===== 1. Khung tiêu đề khối & mô tả =====
        khungTieuDeKhoi = new JPanel(new GridLayout(2, 1, 0, 4));
        khungTieuDeKhoi.setOpaque(false);

        nhanTieuDeDanhSach = new JLabel("Danh sách sản phẩm");
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
                    FontMetrics fm = gPlaceholder.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    gPlaceholder.drawString("  🔎 Tìm kiếm theo mã hoặc tên", insets.left, y);
                    gPlaceholder.dispose();
                }
            }
        };
        oNhapTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent suKien) {
                oNhapTimKiem.repaint();
            }
            @Override
            public void focusLost(FocusEvent suKien) {
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
        String[] danhSachTrangThai = {"Trạng thái", "Đang kinh doanh", "Ngừng kinh doanh"};
        hopChonTrangThai = new JComboBox<>(danhSachTrangThai);
        hopChonTrangThai.setPreferredSize(new Dimension(150, 38));
        hopChonTrangThai.setFont(phongChuMoTa);
        hopChonTrangThai.setForeground(mauChuBoLoc);
        hopChonTrangThai.setBackground(mauNenOData);
        hopChonTrangThai.setFocusable(false);

        // Hộp chọn thứ tự sắp xếp
        String[] danhSachSapXep = {"Sắp xếp: Mới nhất", "Sắp xếp: Cũ nhất", "Tên A-Z", "Tên Z-A", "Giá tăng dần", "Giá giảm dần"};
        hopChonSapXep = new JComboBox<>(danhSachSapXep);
        hopChonSapXep.setPreferredSize(new Dimension(160, 38));
        hopChonSapXep.setFont(phongChuMoTa);
        hopChonSapXep.setForeground(mauChuBoLoc);
        hopChonSapXep.setBackground(mauNenOData);
        hopChonSapXep.setFocusable(false);

        khungBoLocBenTrai.add(oNhapTimKiem);
        khungBoLocBenTrai.add(hopChonTrangThai);
        khungBoLocBenTrai.add(hopChonSapXep);

        // Nút thêm mới sản phẩm bên phải
        nutThemSanPham = new JButton("+ Thêm") {
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
        nutThemSanPham.setPreferredSize(new Dimension(120, 38));
        nutThemSanPham.setFont(phongNutChucNang);
        nutThemSanPham.setForeground(mauChuTieuDe);
        nutThemSanPham.setFocusPainted(false);
        nutThemSanPham.setContentAreaFilled(false);
        nutThemSanPham.setBorderPainted(false);
        nutThemSanPham.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Mở popup Thêm sản phẩm
        nutThemSanPham.addActionListener(suKien -> {
            Frame cuaSoCha = (Frame) SwingUtilities.getWindowAncestor(this);
            AddProductDialog hopThoaiThem = new AddProductDialog(cuaSoCha);
            hopThoaiThem.setVisible(true);
        });

        khungThanhCongCu.add(khungBoLocBenTrai, BorderLayout.WEST);
        khungThanhCongCu.add(nutThemSanPham, BorderLayout.EAST);

        // Ghép phần tiêu đề và thanh công cụ lên phía trên
        JPanel khungPhiaTren = new JPanel();
        khungPhiaTren.setLayout(new BoxLayout(khungPhiaTren, BoxLayout.Y_AXIS));
        khungPhiaTren.setOpaque(false);
        khungPhiaTren.add(khungTieuDeKhoi);
        khungPhiaTren.add(khungThanhCongCu);
        theChuaDanhSach.add(khungPhiaTren, BorderLayout.NORTH);

        // ===== 3. Bảng dữ liệu sản phẩm =====
        String[] tieuDeCacCot = {"Mã SP", "Tên sản phẩm", "Loại SP", "Đơn vị tính", "Giá bán", "SL tồn", "Trạng thái", "Thao tác"};
        Object[][] duLieuMauTuFigma = {
            {"SP241018001", "Royal Canin Mini Adult", "Thức ăn", "Gói", "185.000", "24", "Đang kinh doanh", "Sửa"},
            {"SP241018002", "Vòng cổ phản quang", "Phụ kiện", "Cái", "95.000", "15", "Đang kinh doanh", "Sửa"},
            {"SP241018003", "Cát vệ sinh hương trà xa…", "Vệ sinh", "Túi", "72.000", "30", "Đang kinh doanh", "Sửa"},
            {"SP241018004", "Vitamin tổng hợp cho chó", "Thuốc & bổ sung", "Hộp", "128.000", "8", "Ngừng kinh doanh", "Sửa"},
            {"SP241018005", "Đồ chơi gặm xương cao su", "Đồ chơi", "Cái", "59.000", "18", "Đang kinh doanh", "Sửa"}
        };

        moHinhBang = new DefaultTableModel(duLieuMauTuFigma, tieuDeCacCot) {
            @Override
            public boolean isCellEditable(int hang, int cot) {
                return false;
            }
        };

        bangSanPham = new JTable(moHinhBang);
        bangSanPham.setFont(phongBangDuLieu);
        bangSanPham.setRowHeight(52);
        bangSanPham.setShowGrid(true);
        bangSanPham.setGridColor(mauDuongKeBang);
        bangSanPham.setSelectionBackground(new Color(0xF2, 0xF7, 0xFC));
        bangSanPham.setSelectionForeground(mauChuTieuDe);
        bangSanPham.setFillsViewportHeight(true);

        // Tùy chỉnh thanh tiêu đề của bảng (JTableHeader)
        JTableHeader dauBang = bangSanPham.getTableHeader();
        dauBang.setPreferredSize(new Dimension(0, 46));
        dauBang.setFont(phongTieuDeBang);
        dauBang.setBackground(mauNenTieuDeCot);
        dauBang.setForeground(mauTieuDeCot);
        dauBang.setReorderingAllowed(false);
        dauBang.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, mauDuongKeBang));

        // Căn lề và kích thước các cột
        DefaultTableCellRenderer boCanLeTrai = new DefaultTableCellRenderer();
        boCanLeTrai.setBorder(new EmptyBorder(0, 12, 0, 12));
        boCanLeTrai.setForeground(mauChuTieuDe);

        DefaultTableCellRenderer boCanLePhai = new DefaultTableCellRenderer();
        boCanLePhai.setHorizontalAlignment(SwingConstants.RIGHT);
        boCanLePhai.setBorder(new EmptyBorder(0, 12, 0, 12));
        boCanLePhai.setForeground(mauChuTieuDe);

        DefaultTableCellRenderer boCanLeGiua = new DefaultTableCellRenderer();
        boCanLeGiua.setHorizontalAlignment(SwingConstants.CENTER);
        boCanLeGiua.setForeground(mauChuTieuDe);

        bangSanPham.getColumnModel().getColumn(0).setPreferredWidth(110);
        bangSanPham.getColumnModel().getColumn(0).setCellRenderer(boCanLeTrai);

        bangSanPham.getColumnModel().getColumn(1).setPreferredWidth(210);
        bangSanPham.getColumnModel().getColumn(1).setCellRenderer(boCanLeTrai);

        bangSanPham.getColumnModel().getColumn(2).setPreferredWidth(130);
        bangSanPham.getColumnModel().getColumn(2).setCellRenderer(boCanLeTrai);

        bangSanPham.getColumnModel().getColumn(3).setPreferredWidth(90);
        bangSanPham.getColumnModel().getColumn(3).setCellRenderer(boCanLeGiua);

        bangSanPham.getColumnModel().getColumn(4).setPreferredWidth(100);
        bangSanPham.getColumnModel().getColumn(4).setCellRenderer(boCanLePhai);

        bangSanPham.getColumnModel().getColumn(5).setPreferredWidth(80);
        bangSanPham.getColumnModel().getColumn(5).setCellRenderer(boCanLeGiua);

        // Cột Trạng thái: hiển thị badge bo góc màu xanh/đỏ
        bangSanPham.getColumnModel().getColumn(6).setPreferredWidth(150);
        bangSanPham.getColumnModel().getColumn(6).setCellRenderer(new BoKetXuatTrangThai());

        // Cột Thao tác: hiển thị nút "Sửa" màu cam nhạt
        bangSanPham.getColumnModel().getColumn(7).setPreferredWidth(90);
        bangSanPham.getColumnModel().getColumn(7).setCellRenderer(new BoKetXuatThaoTac());

        // Bắt sự kiện click vào cột "Thao tác" (cột 7)
        bangSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent suKien) {
                int cotDuocChon = bangSanPham.columnAtPoint(suKien.getPoint());
                if (cotDuocChon == 7) {
                    Frame cuaSoCha = (Frame) SwingUtilities.getWindowAncestor(ProductManagementPanel.this);
                    UpdateProductDialog hopThoaiCapNhat = new UpdateProductDialog(cuaSoCha);
                    hopThoaiCapNhat.setVisible(true);
                }
            }
        });

        thanhCuonBang = new JScrollPane(bangSanPham);
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
        nutTrangSoMot = taoNutChuyenTrang("1", true);
        nutTrangSoHai = taoNutChuyenTrang("2", false);
        nutTrangKeTiep = taoNutChuyenTrang(">", false);

        khungCacNutChuyenTrang.add(nutTrangTruoc);
        khungCacNutChuyenTrang.add(nutTrangSoMot);
        khungCacNutChuyenTrang.add(nutTrangSoHai);
        khungCacNutChuyenTrang.add(nutTrangKeTiep);

        khungPhanTrang.add(nhanThongKePhanTrang, BorderLayout.WEST);
        khungPhanTrang.add(khungCacNutChuyenTrang, BorderLayout.EAST);

        theChuaDanhSach.add(khungPhanTrang, BorderLayout.SOUTH);

        add(theChuaDanhSach, BorderLayout.CENTER);
    }

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

    private class BoKetXuatTrangThai extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String giaTri = value != null ? value.toString() : "";
            boolean laDangKinhDoanh = giaTri.equalsIgnoreCase("Đang kinh doanh");

            JPanel khungBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            khungBadge.setOpaque(false);

            JLabel nhanBadge = new JLabel(giaTri, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(laDangKinhDoanh ? mauNenXanhTrangThai : mauNenDoTrangThai);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            nhanBadge.setFont(phongBadgeNho);
            nhanBadge.setForeground(laDangKinhDoanh ? mauXanhTrangThai : mauDoTrangThai);
            nhanBadge.setPreferredSize(new Dimension(laDangKinhDoanh ? 125 : 135, 28));

            khungBadge.add(nhanBadge);
            return khungBadge;
        }
    }

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
