package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Hộp thoại Popup: 06.1 - THÊM SẢN PHẨM (Chuẩn 100% theo thiết kế Figma node 130:1643)
 * Toàn bộ các biến được đặt tên theo tiếng Việt chuẩn cú pháp camelCase.
 */
public class AddProductDialog extends JDialog {

    // Bảng màu chuẩn trích xuất từ thiết kế Figma
    private final Color mauNenTheTrang        = Color.WHITE;
    private final Color mauChuTieuDe          = new Color(0x1C, 0x38, 0x4C);
    private final Color mauChuPhu             = new Color(0x7F, 0x90, 0x9E);
    private final Color mauVienHop            = new Color(0xE3, 0xE8, 0xEE);
    private final Color mauVienNgoaiModal     = new Color(0xD0, 0xD8, 0xE2);
    private final Color mauNenOData           = new Color(0xF8, 0xFB, 0xFD);
    private final Color mauChuPlaceholder     = new Color(0x94, 0xA5, 0xB1);
    private final Color mauNenCanhBao         = new Color(0xFF, 0xF8, 0xEC);
    private final Color mauVienCanhBao        = new Color(0xFF, 0xE7, 0xC3);
    private final Color mauChuCanhBao         = new Color(0x8B, 0x69, 0x2A);
    private final Color mauNutHuy             = new Color(0x60, 0x74, 0x83);
    private final Color mauNenNutHuy          = new Color(0xF5, 0xF8, 0xFB);
    private final Color mauCamChinh           = new Color(0xF4, 0xA3, 0x2C);
    private final Color mauNutDong            = new Color(0x69, 0x80, 0x8F);
    private final Color mauNenNutDong         = new Color(0xF7, 0xFA, 0xFD);

    // Font chữ chuẩn thiết kế
    private final Font phongTieuDeChinh       = new Font("Segoe UI", Font.BOLD, 28);
    private final Font phongChuPhu            = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongNhanTruong        = new Font("Segoe UI", Font.BOLD, 14);
    private final Font phongNhapLieu          = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font phongCanhBao           = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font phongNutThaoTac        = new Font("Segoe UI", Font.BOLD, 15);

    // Khai báo các thành phần giao diện
    private JPanel theChuaModal;
    private JLabel nhanBieuTuongLogo;
    private JLabel nhanChuThuongHieu;
    private JButton nutDongHopThoai;
    private JLabel nhanTieuDeModal;
    private JLabel nhanMoTaModal;

    private JLabel nhanMaSanPham;
    private JTextField oNhapMaSanPham;
    private JLabel nhanTenSanPham;
    private JTextField oNhapTenSanPham;

    private JLabel nhanLoaiSanPham;
    private JComboBox<String> hopChonLoaiSanPham;
    private JLabel nhanDonViTinh;
    private JTextField oNhapDonViTinh;
    private JLabel nhanGiaBan;
    private JTextField oNhapGiaBan;

    private JLabel nhanSoLuongTon;
    private JTextField oNhapSoLuongTon;
    private JLabel nhanTrangThai;
    private JComboBox<String> hopChonTrangThai;

    private JLabel nhanMoTa;
    private JTextArea vungNhapMoTa;
    private JScrollPane thanhCuonMoTa;
    private JLabel nhanKhungCanhBao;
    private JButton nutHuyBo;
    private JButton nutLuuSanPham;

    private Point diemBamChuot;

    public AddProductDialog(Frame cuaSoCha) {
        super(cuaSoCha, true);
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setUndecorated(true);
        setSize(720, 650);
        setLocationRelativeTo(getOwner());

        try {
            setShape(new RoundRectangle2D.Double(0, 0, 720, 650, 16, 16));
        } catch (Exception e) {
            // Trường hợp OS không hỗ trợ shape
        }

        theChuaModal = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenTheTrang);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.setColor(mauVienNgoaiModal);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        theChuaModal.setBackground(mauNenTheTrang);

        // Kéo thả modal khi giữ chuột vùng trên
        theChuaModal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent suKien) {
                diemBamChuot = suKien.getPoint();
            }
        });
        theChuaModal.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent suKien) {
                Point viTriHienTai = getLocation();
                setLocation(viTriHienTai.x + suKien.getX() - diemBamChuot.x,
                            viTriHienTai.y + suKien.getY() - diemBamChuot.y);
            }
        });

        // 1. Logo Pet Bite Shop (x=34, y=18)
        URL duongDanAnhLogo = getClass().getResource("/logo_cua_hang.png");
        if (duongDanAnhLogo != null) {
            ImageIcon bieuTuongGoc = new ImageIcon(duongDanAnhLogo);
            Image anhDaCoTyLe = bieuTuongGoc.getImage().getScaledInstance(36, 35, Image.SCALE_SMOOTH);
            nhanBieuTuongLogo = new JLabel(new ImageIcon(anhDaCoTyLe));
        } else {
            nhanBieuTuongLogo = new JLabel();
        }
        nhanBieuTuongLogo.setBounds(34, 18, 36, 35);
        theChuaModal.add(nhanBieuTuongLogo);

        nhanChuThuongHieu = new JLabel("<html><span style='font-family:Arial;font-weight:bold;font-size:15px;color:#143958;'>PET BITE</span><br><span style='font-family:Arial;font-weight:bold;font-size:8px;color:#F8B44A;'>SHOP</span></html>");
        nhanChuThuongHieu.setBounds(76, 17, 120, 36);
        theChuaModal.add(nhanChuThuongHieu);

        // 2. Nút đóng modal góc phải (x=646, y=20, 40x40 bo góc)
        nutDongHopThoai = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenNutDong);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(mauVienHop);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.setColor(mauNutDong);
                g2.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
                g2.drawLine(cx + 5, cy - 5, cx - 5, cy + 5);
                g2.dispose();
            }
        };
        nutDongHopThoai.setBounds(646, 20, 40, 40);
        nutDongHopThoai.setFocusPainted(false);
        nutDongHopThoai.setContentAreaFilled(false);
        nutDongHopThoai.setBorderPainted(false);
        nutDongHopThoai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutDongHopThoai.addActionListener(suKien -> dispose());
        theChuaModal.add(nutDongHopThoai);

        // 3. Tiêu đề và phụ đề modal (x=34)
        nhanTieuDeModal = new JLabel("Thêm sản phẩm");
        nhanTieuDeModal.setBounds(34, 70, 450, 34);
        nhanTieuDeModal.setFont(phongTieuDeChinh);
        nhanTieuDeModal.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanTieuDeModal);

        nhanMoTaModal = new JLabel("Nhập đầy đủ thông tin trước khi lưu.");
        nhanMoTaModal.setBounds(34, 108, 450, 18);
        nhanMoTaModal.setFont(phongChuPhu);
        nhanMoTaModal.setForeground(mauChuPhu);
        theChuaModal.add(nhanMoTaModal);

        // 4. Hàng 1 (y=140): Mã sản phẩm (w=290) & Tên sản phẩm (w=290)
        nhanMaSanPham = new JLabel("Mã sản phẩm");
        nhanMaSanPham.setBounds(34, 140, 290, 18);
        nhanMaSanPham.setFont(phongNhanTruong);
        nhanMaSanPham.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanMaSanPham);

        oNhapMaSanPham = taoONhapLieuBoGoc("SP241018006 (tự động)", false, false);
        oNhapMaSanPham.setBounds(34, 162, 290, 40);
        theChuaModal.add(oNhapMaSanPham);

        nhanTenSanPham = new JLabel("Tên sản phẩm");
        nhanTenSanPham.setBounds(356, 140, 290, 18);
        nhanTenSanPham.setFont(phongNhanTruong);
        nhanTenSanPham.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanTenSanPham);

        oNhapTenSanPham = taoONhapLieuBoGoc("Nhập tên sản phẩm", true, true);
        oNhapTenSanPham.setBounds(356, 162, 290, 40);
        theChuaModal.add(oNhapTenSanPham);

        // 5. Hàng 2 (y=214): Loại sản phẩm (w=186), Đơn vị tính (w=186), Giá bán (w=186)
        nhanLoaiSanPham = new JLabel("Loại sản phẩm");
        nhanLoaiSanPham.setBounds(34, 214, 186, 18);
        nhanLoaiSanPham.setFont(phongNhanTruong);
        nhanLoaiSanPham.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanLoaiSanPham);

        String[] danhSachLoaiSP = {"Thức ăn", "Phụ kiện", "Vệ sinh", "Thuốc & bổ sung", "Đồ chơi"};
        hopChonLoaiSanPham = new JComboBox<>(danhSachLoaiSP);
        hopChonLoaiSanPham.setBounds(34, 236, 186, 40);
        hopChonLoaiSanPham.setFont(phongNhapLieu);
        hopChonLoaiSanPham.setBackground(mauNenOData);
        hopChonLoaiSanPham.setFocusable(false);
        theChuaModal.add(hopChonLoaiSanPham);

        nhanDonViTinh = new JLabel("Đơn vị tính");
        nhanDonViTinh.setBounds(247, 214, 186, 18);
        nhanDonViTinh.setFont(phongNhanTruong);
        nhanDonViTinh.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanDonViTinh);

        oNhapDonViTinh = taoONhapLieuBoGoc("Nhập đơn vị tính", true, true);
        oNhapDonViTinh.setBounds(247, 236, 186, 40);
        theChuaModal.add(oNhapDonViTinh);

        nhanGiaBan = new JLabel("Giá bán (VNĐ)");
        nhanGiaBan.setBounds(460, 214, 186, 18);
        nhanGiaBan.setFont(phongNhanTruong);
        nhanGiaBan.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanGiaBan);

        oNhapGiaBan = taoONhapLieuBoGoc("Nhập giá bán", true, true);
        oNhapGiaBan.setBounds(460, 236, 186, 40);
        theChuaModal.add(oNhapGiaBan);

        // 6. Hàng 3 (y=288): Số lượng tồn (w=186) & Trạng thái (w=186)
        nhanSoLuongTon = new JLabel("Số lượng tồn");
        nhanSoLuongTon.setBounds(34, 288, 186, 18);
        nhanSoLuongTon.setFont(phongNhanTruong);
        nhanSoLuongTon.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanSoLuongTon);

        oNhapSoLuongTon = taoONhapLieuBoGoc("Nhập số lượng tồn", true, true);
        oNhapSoLuongTon.setBounds(34, 310, 186, 40);
        theChuaModal.add(oNhapSoLuongTon);

        nhanTrangThai = new JLabel("Trạng thái");
        nhanTrangThai.setBounds(247, 288, 186, 18);
        nhanTrangThai.setFont(phongNhanTruong);
        nhanTrangThai.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanTrangThai);

        hopChonTrangThai = new JComboBox<>(new String[]{"Đang kinh doanh", "Ngừng kinh doanh"});
        hopChonTrangThai.setBounds(247, 310, 186, 40);
        hopChonTrangThai.setFont(phongNhapLieu);
        hopChonTrangThai.setBackground(mauNenOData);
        hopChonTrangThai.setFocusable(false);
        theChuaModal.add(hopChonTrangThai);

        // 7. Hàng 4 (y=362): Mô tả (w=612, h=72)
        nhanMoTa = new JLabel("Mô tả");
        nhanMoTa.setBounds(34, 362, 612, 18);
        nhanMoTa.setFont(phongNhanTruong);
        nhanMoTa.setForeground(mauChuTieuDe);
        theChuaModal.add(nhanMoTa);

        vungNhapMoTa = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D gPlaceholder = (Graphics2D) g.create();
                    gPlaceholder.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    gPlaceholder.setColor(mauChuPlaceholder);
                    gPlaceholder.setFont(phongNhapLieu);
                    Insets insets = getInsets();
                    FontMetrics fm = gPlaceholder.getFontMetrics();
                    gPlaceholder.drawString("Nhập mô tả sản phẩm", insets.left, insets.top + fm.getAscent());
                    gPlaceholder.dispose();
                }
            }
        };
        vungNhapMoTa.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent suKien) {
                vungNhapMoTa.repaint();
            }
            @Override
            public void focusLost(FocusEvent suKien) {
                vungNhapMoTa.repaint();
            }
        });
        vungNhapMoTa.setFont(phongNhapLieu);
        vungNhapMoTa.setForeground(mauChuTieuDe);
        vungNhapMoTa.setBackground(mauNenOData);
        vungNhapMoTa.setLineWrap(true);
        vungNhapMoTa.setWrapStyleWord(true);
        vungNhapMoTa.setBorder(new EmptyBorder(8, 12, 8, 12));

        thanhCuonMoTa = new JScrollPane(vungNhapMoTa);
        thanhCuonMoTa.setBounds(34, 384, 612, 72);
        thanhCuonMoTa.setBorder(BorderFactory.createLineBorder(mauVienHop, 1, true));
        theChuaModal.add(thanhCuonMoTa);

        // 8. Hàng 5 (y=470): Khung cảnh báo nghiệp vụ
        nhanKhungCanhBao = new JLabel("<html>• Giá bán và số lượng tồn không được âm. Hệ thống hỗ trợ cập nhật trạng thái kinh doanh của sản phẩm.</html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenCanhBao);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(mauVienCanhBao);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nhanKhungCanhBao.setOpaque(false);
        nhanKhungCanhBao.setBounds(34, 470, 612, 50);
        nhanKhungCanhBao.setFont(phongCanhBao);
        nhanKhungCanhBao.setForeground(mauChuCanhBao);
        nhanKhungCanhBao.setBorder(new EmptyBorder(0, 16, 0, 16));
        theChuaModal.add(nhanKhungCanhBao);

        // 9. Hàng 6 (y=544): Nút Hủy và Nút Lưu
        nutHuyBo = new JButton("Hủy") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNenNutHuy);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(mauVienHop);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nutHuyBo.setBounds(384, 544, 118, 46);
        nutHuyBo.setFont(phongNutThaoTac);
        nutHuyBo.setForeground(mauNutHuy);
        nutHuyBo.setFocusPainted(false);
        nutHuyBo.setContentAreaFilled(false);
        nutHuyBo.setBorderPainted(false);
        nutHuyBo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutHuyBo.addActionListener(suKien -> dispose());
        theChuaModal.add(nutHuyBo);

        nutLuuSanPham = new JButton("Lưu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauCamChinh);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nutLuuSanPham.setBounds(518, 544, 118, 46);
        nutLuuSanPham.setFont(phongNutThaoTac);
        nutLuuSanPham.setForeground(mauChuTieuDe);
        nutLuuSanPham.setFocusPainted(false);
        nutLuuSanPham.setContentAreaFilled(false);
        nutLuuSanPham.setBorderPainted(false);
        nutLuuSanPham.setCursor(new Cursor(Cursor.HAND_CURSOR));
        theChuaModal.add(nutLuuSanPham);

        // Đóng modal bằng phím ESC
        theChuaModal.setFocusable(true);
        theChuaModal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent suKien) {
                if (suKien.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });

        setContentPane(theChuaModal);
    }

    private JTextField taoONhapLieuBoGoc(String chuGoiY, boolean choPhepNhap, boolean laPlaceholder) {
        JTextField oNhap = new JTextField(laPlaceholder ? "" : chuGoiY) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(mauNenOData);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);

                if (laPlaceholder && getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D gPlaceholder = (Graphics2D) g.create();
                    gPlaceholder.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    gPlaceholder.setColor(mauChuPlaceholder);
                    gPlaceholder.setFont(phongNhapLieu);
                    Insets insets = getInsets();
                    FontMetrics fm = gPlaceholder.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    gPlaceholder.drawString(chuGoiY, insets.left, y);
                    gPlaceholder.dispose();
                }
            }
        };
        oNhap.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent suKien) {
                oNhap.repaint();
            }
            @Override
            public void focusLost(FocusEvent suKien) {
                oNhap.repaint();
            }
        });
        oNhap.setOpaque(false);
        oNhap.setFont(phongNhapLieu);
        oNhap.setForeground(mauChuTieuDe);
        oNhap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mauVienHop, 1, true),
            new EmptyBorder(0, 12, 0, 12)
        ));
        oNhap.setEditable(choPhepNhap);
        return oNhap;
    }
}
