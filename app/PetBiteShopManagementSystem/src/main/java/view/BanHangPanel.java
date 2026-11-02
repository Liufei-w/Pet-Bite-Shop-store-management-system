package view;

import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import dao.SanPham_DAO;
import dao.HoaDon_DAO;
import entity.KhachHang;
import entity.SanPham;
import entity.HoaDon;
import entity.NhanVien;
import entity.ChiTietHoaDon_SanPham;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class BanHangPanel extends JPanel implements ActionListener {
    private JTextField txtSearchKH, txtSearchSP, txtTienKhachDua;
    private JButton btnThemKH, btnThemSP, btnXemDS, btnTangSL, btnGiamSL, btnXoaDong, btnXacNhanTT;
    private JRadioButton rbTienMat, rbChuyenKhoan;
    private JTable tableHoaDonTam;
    private DefaultTableModel modelHoaDonTam;
    
    private JLabel lblTienHang, lblTienThue, lblTongTien, lblTienThua, lblInfoKH;
    private JPanel panelTienMatInput;

    private KhachHang_DAO kh_dao;
    private SanPham_DAO sp_dao;
    private HoaDon_DAO hd_dao;
    private KhachHang khachHangHienTai = null;

    public BanHangPanel() {
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        kh_dao = new KhachHang_DAO();
        sp_dao = new SanPham_DAO();
        hd_dao = new HoaDon_DAO();

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(244, 246, 249));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // ===== 1. TIÊU ĐỀ PHÍA TRÊN =====
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setOpaque(false);

        JLabel lblTitle = new JLabel("Bán hàng & Thanh toán");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(18, 57, 88));

        JLabel lblSub = new JLabel("Lập hóa đơn tạm, tự động tính toán thuế VAT 10% và xử lý thanh toán chính xác.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(122, 140, 160));

        panelTop.add(lblTitle);
        panelTop.add(Box.createVerticalStrut(4));
        panelTop.add(lblSub);
        add(panelTop, BorderLayout.NORTH);

        // ===== 2. KHU VỰC TRUNG TÂM (Chia 2 cột) =====
        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCenter.setOpaque(false);

        // --- CỘT TRÁI ---
        Box boxLeft = Box.createVerticalBox();
        boxLeft.setOpaque(false);

        // Card 1: Khách hàng
        JPanel cardKH = createCardPanel("1. Chọn khách hàng (Nhập SĐT rồi nhấn Enter)");
        txtSearchKH = new JTextField();
        txtSearchKH.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearchKH.setPreferredSize(new Dimension(210, 40));
        txtSearchKH.setToolTipText("Nhập số điện thoại khách hàng");
        
        btnThemKH = createLargeOrangeButton("+ Thêm KH mới");
        
        JPanel pKHInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pKHInput.setOpaque(false);
        pKHInput.add(txtSearchKH);
        pKHInput.add(Box.createHorizontalStrut(8));
        pKHInput.add(btnThemKH);
        cardKH.add(pKHInput);

        lblInfoKH = new JLabel("<html><b><font size='3'>Chưa chọn khách hàng nào!</font></b><br/><font color='#7A8CA0' size='3'>Vui lòng nhập số điện thoại để tìm kiếm hoặc thêm mới.</font></html>");
        lblInfoKH.setBorder(new EmptyBorder(8, 0, 8, 0));
        cardKH.add(lblInfoKH);

        boxLeft.add(cardKH);
        boxLeft.add(Box.createVerticalStrut(15));

        // Card 2: Sản phẩm
        JPanel cardSP = createCardPanel("2. Thêm sản phẩm (Nhập Mã/Tên hoặc bấm Xem DS)");
        txtSearchSP = new JTextField();
        txtSearchSP.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearchSP.setPreferredSize(new Dimension(170, 40));
        txtSearchSP.setToolTipText("Nhập mã hoặc tên sản phẩm");
        
        btnThemSP = createLargeOrangeButton("+ Thêm SP");
        
        btnXemDS = new JButton("Xem DS");
        btnXemDS.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnXemDS.setMargin(new Insets(8, 14, 8, 14));
        btnXemDS.setFocusPainted(false);
        btnXemDS.setBackground(new Color(230, 235, 245));

        JPanel pSPInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pSPInput.setOpaque(false);
        pSPInput.add(txtSearchSP);
        pSPInput.add(Box.createHorizontalStrut(5));
        pSPInput.add(btnThemSP);
        pSPInput.add(Box.createHorizontalStrut(5));
        pSPInput.add(btnXemDS);
        cardSP.add(pSPInput);

        JTextArea txtGoiY = new JTextArea(
            " Hướng dẫn nhanh:\n" +
            " • Nhập SĐT khách hàng nhấn Enter để tra cứu hoặc thêm mới.\n" +
            " • Nhập Mã SP hoặc Tên SP rồi nhấn Enter để chọn nhanh."
        );
        txtGoiY.setEditable(false);
        txtGoiY.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtGoiY.setForeground(new Color(90, 100, 110));
        txtGoiY.setBackground(new Color(250, 252, 255));
        txtGoiY.setBorder(BorderFactory.createLineBorder(new Color(230, 235, 245)));
        
        JScrollPane spGoiY = new JScrollPane(txtGoiY);
        spGoiY.setPreferredSize(new Dimension(0, 95));
        cardSP.add(spGoiY);

        boxLeft.add(cardSP);
        panelCenter.add(boxLeft);

        // --- CỘT PHẢI ---
        Box boxRight = Box.createVerticalBox();
        boxRight.setOpaque(false);

        // Card 3: Dịch vụ tự động
        JPanel cardDV = createCardPanel("3. Dịch vụ phát sinh (Tự động nạp)");
        JTextArea txtDVAuto = new JTextArea(
            " Hệ thống sẽ tự động lọc các lịch hẹn 'Hoàn thành' nhưng chưa thanh toán của khách hàng."
        );
        txtDVAuto.setEditable(false);
        txtDVAuto.setLineWrap(true);
        txtDVAuto.setWrapStyleWord(true);
        txtDVAuto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDVAuto.setForeground(new Color(18, 57, 88));
        txtDVAuto.setBackground(new Color(235, 243, 255));
        txtDVAuto.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        cardDV.add(txtDVAuto);

        boxRight.add(cardDV);
        boxRight.add(Box.createVerticalStrut(15));

        // Card 4: Hóa đơn tạm & Thanh toán
        JPanel cardHD = createCardPanel("4. Chi tiết Hóa đơn tạm & Thanh toán");
        
        String[] cols = {"Loại", "Mã / Diễn giải", "SL", "Đơn giá", "Thành tiền"};
        modelHoaDonTam = new DefaultTableModel(new Object[][]{}, cols);
        tableHoaDonTam = new JTable(modelHoaDonTam);
        tableHoaDonTam.setRowHeight(28);
        tableHoaDonTam.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableHoaDonTam.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JScrollPane spTable = new JScrollPane(tableHoaDonTam);
        spTable.setPreferredSize(new Dimension(0, 110));
        cardHD.add(spTable);

        JPanel pActionTable = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 4));
        pActionTable.setOpaque(false);
        btnTangSL = new JButton("+ Tăng SL");
        btnGiamSL = new JButton("- Giảm SL");
        btnXoaDong = new JButton("Xóa dòng");
        btnXoaDong.setForeground(new Color(200, 0, 0));
        
        pActionTable.add(btnTangSL);
        pActionTable.add(btnGiamSL);
        pActionTable.add(btnXoaDong);
        cardHD.add(pActionTable);

        // Khu vực tiền
        Box boxTien = Box.createVerticalBox();
        boxTien.setAlignmentX(Component.LEFT_ALIGNMENT);
        boxTien.setBorder(new EmptyBorder(5, 0, 5, 0));
        lblTienHang = new JLabel("Tiền hàng (chưa VAT): 0 đ");
        lblTienHang.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        lblTienThue = new JLabel("Thuế VAT (10%): 0 đ");
        lblTienThue.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        lblTongTien = new JLabel("<html><b>TỔNG THANH TOÁN: <font color='red' size='5'>0 đ</font></b></html>");
        
        boxTien.add(lblTienHang);
        boxTien.add(Box.createVerticalStrut(3));
        boxTien.add(lblTienThue);
        boxTien.add(Box.createVerticalStrut(3));
        boxTien.add(lblTongTien);
        cardHD.add(boxTien);

        JPanel pPayment = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pPayment.setOpaque(false);
        rbTienMat = new JRadioButton("Tiền mặt", true);
        rbChuyenKhoan = new JRadioButton("Chuyển khoản");
        rbTienMat.setOpaque(false);
        rbChuyenKhoan.setOpaque(false);
        rbTienMat.setFont(new Font("SansSerif", Font.BOLD, 13));
        rbChuyenKhoan.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbTienMat);
        bg.add(rbChuyenKhoan);

        JLabel lblHinhThuc = new JLabel("Hình thức:");
        lblHinhThuc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pPayment.add(lblHinhThuc);
        pPayment.add(Box.createHorizontalStrut(10));
        pPayment.add(rbTienMat);
        pPayment.add(Box.createHorizontalStrut(10));
        pPayment.add(rbChuyenKhoan);
        cardHD.add(pPayment);

        panelTienMatInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelTienMatInput.setOpaque(false);
        
        JLabel lblKhachDua = new JLabel("Tiền khách đưa:");
        lblKhachDua.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        txtTienKhachDua = new JTextField(12);
        txtTienKhachDua.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtTienKhachDua.setPreferredSize(new Dimension(140, 32));
        
        lblTienThua = new JLabel("Tiền thừa: 0 đ");
        lblTienThua.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTienThua.setForeground(new Color(0, 120, 0));
        
        panelTienMatInput.add(lblKhachDua);
        panelTienMatInput.add(Box.createHorizontalStrut(10));
        panelTienMatInput.add(txtTienKhachDua);
        panelTienMatInput.add(Box.createHorizontalStrut(10));
        panelTienMatInput.add(lblTienThua);
        cardHD.add(panelTienMatInput);

        txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { tinhTienThuaThoiGianThuc(); }
            @Override
            public void removeUpdate(DocumentEvent e) { tinhTienThuaThoiGianThuc(); }
            @Override
            public void changedUpdate(DocumentEvent e) { tinhTienThuaThoiGianThuc(); }
        });

        btnXacNhanTT = new JButton("XÁC NHẬN THANH TOÁN & TẠO HÓA ĐƠN");
        btnXacNhanTT.setBackground(new Color(255, 177, 75));
        btnXacNhanTT.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnXacNhanTT.setFocusPainted(false);
        btnXacNhanTT.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnXacNhanTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Đăng ký sự kiện
        btnThemKH.addActionListener(this);
        btnThemSP.addActionListener(this);
        btnXemDS.addActionListener(this);
        btnTangSL.addActionListener(this);
        btnGiamSL.addActionListener(this);
        btnXoaDong.addActionListener(this);
        btnXacNhanTT.addActionListener(this);
        rbTienMat.addActionListener(this);
        rbChuyenKhoan.addActionListener(this);

        txtSearchKH.addActionListener(e -> timKiemKhachHang());
        txtSearchSP.addActionListener(e -> themSanPhamVaoBangTam());

        cardHD.add(Box.createVerticalStrut(5));
        cardHD.add(btnXacNhanTT);

        boxRight.add(cardHD);
        panelCenter.add(boxRight);

        add(panelCenter, BorderLayout.CENTER);

        // Footer
        JLabel lblFooter = new JLabel("• Quy định: Hóa đơn tự động tính thuế VAT 10% và giảm tồn kho khi thanh toán thành công.");
        lblFooter.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(180, 130, 40));
        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFooter.setBackground(new Color(255, 248, 220));
        pFooter.setBorder(BorderFactory.createLineBorder(new Color(245, 222, 179)));
        pFooter.add(lblFooter);
        add(pFooter, BorderLayout.SOUTH);
    }

    /**
     * Hàm tạo Card Panel chuẩn căn trái toàn bộ các thành phần bên trong
     */
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 238), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(new Color(18, 57, 88));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); // Đảm bảo tiêu đề card bám sát lề trái
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(8));
        return panel;
    }

    private JButton createLargeOrangeButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(255, 177, 75));
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setMargin(new Insets(8, 14, 8, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private void timKiemKhachHang() {
        String sdt = txtSearchKH.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại hoặc mã khách hàng cần tìm!");
            return;
        }

        KhachHang kh = kh_dao.getKhachHangTheoSDT(sdt);
        if (kh != null) {
            khachHangHienTai = kh;
            lblInfoKH.setText("<html><b><font size='3'>Đã chọn: " + kh.getMaKH() + " - " + kh.getHoTen() + "</font></b><br/><font color='#7A8CA0' size='3'>SĐT: " + kh.getSoDienThoai() + " • Địa chỉ: " + kh.getDiaChi() + "</font></html>");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Không tìm thấy khách hàng với từ khóa: " + sdt + "\nBạn có muốn thêm mới khách hàng này không?", 
                "Thông báo", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                moFormThemKhachHang(sdt);
            }
        }
    }

    private void moFormThemKhachHang(String sdtGoiY) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khách hàng mới", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField txtTen = new JTextField();
        txtTen.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JTextField txtSdt = new JTextField(sdtGoiY);
        txtSdt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cbGioiTinh.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JTextField txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 13));

        dialog.add(new JLabel("Họ tên khách hàng (*):"));
        dialog.add(txtTen);
        dialog.add(new JLabel("Số điện thoại (*):"));
        dialog.add(txtSdt);
        dialog.add(new JLabel("Giới tính:"));
        dialog.add(cbGioiTinh);
        dialog.add(new JLabel("Địa chỉ:"));
        dialog.add(txtDiaChi);

        JButton btnLuu = createLargeOrangeButton("Lưu khách hàng");
        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("SansSerif", Font.BOLD, 13));

        dialog.add(btnHuy);
        dialog.add(btnLuu);

        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            String hoTen = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String gioiTinh = (String) cbGioiTinh.getSelectedItem();
            String diaChi = txtDiaChi.getText().trim();

            if (hoTen.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Họ tên và số điện thoại không được để trống!");
                return;
            }

            if (!sdt.matches("^0[3|5|7|8|9]\\d{8}$")) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại không hợp lệ (gồm 10 số, bắt đầu bằng 03, 05, 07, 08, 09)!");
                return;
            }

            String yyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            int stt = kh_dao.demSoLuongKhachHangTrongNgay(yyMMdd) + 1;
            String maKH = "KH" + yyMMdd + String.format("%03d", stt);
            
            KhachHang khMoi = new KhachHang(maKH, hoTen, sdt, gioiTinh, diaChi, true);
            boolean kq = kh_dao.createKhachHang(khMoi);

            if (kq) {
                JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thành công!");
                khachHangHienTai = khMoi;
                lblInfoKH.setText("<html><b><font size='3'>Đã chọn: " + khMoi.getMaKH() + " - " + khMoi.getHoTen() + "</font></b><br/><font color='#7A8CA0' size='3'>SĐT: " + khMoi.getSoDienThoai() + " • Địa chỉ: " + khMoi.getDiaChi() + "</font></html>");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thất bại! (Trùng số điện thoại hoặc lỗi kết nối)");
            }
        });

        dialog.setVisible(true);
    }

    private void themSanPhamVaoBangTam() {
        String keyword = txtSearchSP.getText().trim();
        if (keyword.isEmpty()) {
            return;
        }

        ArrayList<SanPham> dsSP = sp_dao.timSanPhamTheoMaHoacTen(keyword);
        
        if (dsSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào phù hợp với từ khóa: " + keyword);
            return;
        }

        SanPham spChon = null;
        if (dsSP.size() == 1) {
            spChon = dsSP.get(0);
        } else {
            Object[] options = new Object[dsSP.size()];
            for (int i = 0; i < dsSP.size(); i++) {
                options[i] = dsSP.get(i).getMaSP() + " - " + dsSP.get(i).getTenSP() + " (Tồn: " + dsSP.get(i).getSoLuongTon() + ")";
            }
            String selected = (String) JOptionPane.showInputDialog(
                this, "Tìm thấy nhiều sản phẩm, vui lòng chọn một sản phẩm:", 
                "Chọn sản phẩm", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            if (selected != null) {
                String maChon = selected.split(" - ")[0];
                for (SanPham sp : dsSP) {
                    if (sp.getMaSP().equals(maChon)) {
                        spChon = sp;
                        break;
                    }
                }
            }
        }

        if (spChon != null) {
            if (!spChon.isTrangThai()) {
                JOptionPane.showMessageDialog(this, "Sản phẩm này đang ở trạng thái ngừng kinh doanh!");
                return;
            }
            if (spChon.getSoLuongTon() <= 0) {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng trong kho!");
                return;
            }

            boolean daCo = false;
            for (int i = 0; i < modelHoaDonTam.getRowCount(); i++) {
                String maTrongBang = (String) modelHoaDonTam.getValueAt(i, 1);
                if (maTrongBang.contains(spChon.getMaSP())) {
                    int slCu = Integer.parseInt(modelHoaDonTam.getValueAt(i, 2).toString());
                    if (slCu + 1 > spChon.getSoLuongTon()) {
                        JOptionPane.showMessageDialog(this, "Vượt quá số lượng tồn kho cho phép (" + spChon.getSoLuongTon() + ")!");
                        return;
                    }
                    modelHoaDonTam.setValueAt(slCu + 1, i, 2);
                    BigDecimal donGia = spChon.getGiaBan();
                    BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(slCu + 1));
                    modelHoaDonTam.setValueAt(thanhTien.toString(), i, 4);
                    daCo = true;
                    break;
                }
            }

            if (!daCo) {
                BigDecimal donGia = spChon.getGiaBan();
                Object[] row = {"SP", spChon.getMaSP() + " • " + spChon.getTenSP(), 1, donGia.toString(), donGia.toString()};
                modelHoaDonTam.addRow(row);
            }

            txtSearchSP.setText("");
            tinhToanTienHoaDon();
        }
    }

    private void hienThiBangChonTatCaSanPham() {
        ArrayList<SanPham> dsSP = sp_dao.getAllSanPham();
        if (dsSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong kho!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Danh sách sản phẩm tồn kho", true);
        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(new EmptyBorder(12, 12, 12, 12));

        String[] cols = {"Mã SP", "Tên sản phẩm", "ĐVT", "Giá bán", "Tồn kho"};
        Object[][] data = new Object[dsSP.size()][5];
        for (int i = 0; i < dsSP.size(); i++) {
            SanPham sp = dsSP.get(i);
            data[i][0] = sp.getMaSP();
            data[i][1] = sp.getTenSP();
            data[i][2] = sp.getDonViTinh();
            data[i][3] = sp.getGiaBan();
            data[i][4] = sp.getSoLuongTon();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnChon = createLargeOrangeButton("Chọn sản phẩm này");
        pBottom.add(btnChon);
        dialog.add(pBottom, BorderLayout.SOUTH);

        btnChon.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String maSP = table.getValueAt(row, 0).toString();
                txtSearchSP.setText(maSP);
                dialog.dispose();
                themSanPhamVaoBangTam();
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một sản phẩm trong danh sách!");
            }
        });

        dialog.setVisible(true);
    }

    private void tinhToanTienHoaDon() {
        BigDecimal tongThanhToanGomVAT = BigDecimal.ZERO;
        for (int i = 0; i < modelHoaDonTam.getRowCount(); i++) {
            BigDecimal thanhTienRow = new BigDecimal(modelHoaDonTam.getValueAt(i, 4).toString());
            tongThanhToanGomVAT = tongThanhToanGomVAT.add(thanhTienRow);
        }

        BigDecimal tienHangChuaVAT = tongThanhToanGomVAT.divide(new BigDecimal("1.1"), 2, RoundingMode.HALF_UP);
        BigDecimal tienThueVAT = tongThanhToanGomVAT.subtract(tienHangChuaVAT);

        lblTienHang.setText("Tiền hàng (chưa VAT): " + tienHangChuaVAT + " đ");
        lblTienThue.setText("Thuế VAT (10%): " + tienThueVAT + " đ");
        lblTongTien.setText("<html><b>TỔNG THANH TOÁN: <font color='red' size='5'>" + tongThanhToanGomVAT + " đ</font></b></html>");

        tinhTienThuaThoiGianThuc();
    }

    private void tinhTienThuaThoiGianThuc() {
        try {
            BigDecimal tongTien = layTongTienTam();
            String sTienDua = txtTienKhachDua.getText().trim();
            if (sTienDua.isEmpty() || tongTien.compareTo(BigDecimal.ZERO) == 0) {
                lblTienThua.setText("Tiền thừa: 0 đ");
                return;
            }
            BigDecimal tienDua = new BigDecimal(sTienDua);
            BigDecimal tienThua = tienDua.subtract(tongTien);
            if (tienThua.compareTo(BigDecimal.ZERO) >= 0) {
                lblTienThua.setText("Tiền thừa: " + tienThua + " đ");
                lblTienThua.setForeground(new Color(0, 120, 0));
            } else {
                lblTienThua.setText("Tiền thừa: Chưa đủ tiền");
                lblTienThua.setForeground(Color.RED);
            }
        } catch (Exception e) {
            lblTienThua.setText("Tiền thừa: 0 đ");
        }
    }

    private BigDecimal layTongTienTam() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < modelHoaDonTam.getRowCount(); i++) {
            BigDecimal thanhTien = new BigDecimal(modelHoaDonTam.getValueAt(i, 4).toString());
            tong = tong.add(thanhTien);
        }
        return tong;
    }

    private String layMaNhanVienMacDinh() {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        String maNV = null;
        try {
            String sql = "SELECT TOP 1 maNV FROM NhanVien";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maNV = rs.getString("maNV");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (maNV != null) ? maNV : "NV001";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnThemKH)) {
            moFormThemKhachHang(txtSearchKH.getText().trim());
        } else if (o.equals(btnThemSP)) {
            themSanPhamVaoBangTam();
        } else if (o.equals(btnXemDS)) {
            hienThiBangChonTatCaSanPham();
        } else if (o.equals(btnTangSL)) {
            int row = tableHoaDonTam.getSelectedRow();
            if (row >= 0) {
                String maVaTen = modelHoaDonTam.getValueAt(row, 1).toString();
                String maSP = maVaTen.split(" • ")[0];
                SanPham sp = sp_dao.getSanPhamTheoMa(maSP);
                int slCu = Integer.parseInt(modelHoaDonTam.getValueAt(row, 2).toString());
                
                if (sp != null && slCu + 1 > sp.getSoLuongTon()) {
                    JOptionPane.showMessageDialog(this, "Vượt quá số lượng tồn kho cho phép (" + sp.getSoLuongTon() + ")!");
                    return;
                }
                
                int slMoi = slCu + 1;
                modelHoaDonTam.setValueAt(slMoi, row, 2);
                BigDecimal donGia = new BigDecimal(modelHoaDonTam.getValueAt(row, 3).toString());
                modelHoaDonTam.setValueAt(donGia.multiply(BigDecimal.valueOf(slMoi)).toString(), row, 4);
                tinhToanTienHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng sản phẩm cần tăng số lượng!");
            }
        } else if (o.equals(btnGiamSL)) {
            int row = tableHoaDonTam.getSelectedRow();
            if (row >= 0) {
                int slCu = Integer.parseInt(modelHoaDonTam.getValueAt(row, 2).toString());
                if (slCu > 1) {
                    int slMoi = slCu - 1;
                    modelHoaDonTam.setValueAt(slMoi, row, 2);
                    BigDecimal donGia = new BigDecimal(modelHoaDonTam.getValueAt(row, 3).toString());
                    modelHoaDonTam.setValueAt(donGia.multiply(BigDecimal.valueOf(slMoi)).toString(), row, 4);
                    tinhToanTienHoaDon();
                } else {
                    int choice = JOptionPane.showConfirmDialog(this, "Số lượng là 1, bạn có muốn xóa sản phẩm này khỏi hóa đơn tạm không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        modelHoaDonTam.removeRow(row);
                        tinhToanTienHoaDon();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng sản phẩm cần giảm số lượng!");
            }
        } else if (o.equals(btnXoaDong)) {
            int row = tableHoaDonTam.getSelectedRow();
            if (row >= 0) {
                modelHoaDonTam.removeRow(row);
                tinhToanTienHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa trên bảng tạm!");
            }
        } else if (o.equals(rbTienMat)) {
            panelTienMatInput.setVisible(true);
            revalidate();
            repaint();
        } else if (o.equals(rbChuyenKhoan)) {
            panelTienMatInput.setVisible(false);
            revalidate();
            repaint();
        } else if (o.equals(btnXacNhanTT)) {
            if (khachHangHienTai == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng trước khi thanh toán!");
                txtSearchKH.requestFocus();
                return;
            }
            if (modelHoaDonTam.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Hóa đơn tạm chưa có sản phẩm hoặc dịch vụ nào!");
                return;
            }

            // Kiểm tra tiền khách đưa nếu chọn Tiền mặt
            if (rbTienMat.isSelected()) {
                String sTienDua = txtTienKhachDua.getText().trim();
                if (sTienDua.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!");
                    txtTienKhachDua.requestFocus();
                    return;
                }
                try {
                    BigDecimal tienDua = new BigDecimal(sTienDua);
                    BigDecimal tongTien = layTongTienTam();
                    if (tienDua.compareTo(tongTien) < 0) {
                        JOptionPane.showMessageDialog(this, "Số tiền khách đưa không đủ để thanh toán!");
                        txtTienKhachDua.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số tiền khách đưa không hợp lệ!");
                    txtTienKhachDua.requestFocus();
                    return;
                }
            }

            try {
                String yyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
                int stt = hd_dao.demSoLuongHoaDonTrongNgay(yyMMdd) + 1;
                String maHD = "HD" + yyMMdd + String.format("%03d", stt);

                LocalDateTime ngayLap = LocalDateTime.now();
                String ptTT = rbTienMat.isSelected() ? HoaDon.TIEN_MAT : HoaDon.CHUYEN_KHOAN;
                
                NhanVien nv = new NhanVien();
                nv.setMaNV(layMaNhanVienMacDinh());

                HoaDon hd = new HoaDon();
                hd.setMaHD(maHD);
                hd.setNgayLap(ngayLap);
                hd.setTrangThai("Đã thanh toán"); // Đảm bảo trạng thái hóa đơn
                hd.setPhuongThucTT(ptTT);
                hd.setKhachHang(khachHangHienTai);
                hd.setNhanVien(nv);

                // Nếu là tiền mặt, gán thêm tiền khách đưa và tiền thừa
                if (rbTienMat.isSelected()) {
                    BigDecimal tienDua = new BigDecimal(txtTienKhachDua.getText().trim());
                    hd.setTienKhachDua(tienDua);
                    hd.setTienThua(tienDua.subtract(layTongTienTam()));
                }

                // Gán tổng tiền thanh toán (đã gồm VAT) vào đối tượng HoaDon để DAO tính tiền hàng chưa VAT
                hd.setTongTien(layTongTienTam());

                // Thu thập danh sách chi tiết sản phẩm từ bảng tạm
                ArrayList<Object[]> dsChiTietSP = new ArrayList<>();
                ArrayList<Object[]> dsChiTietDV = new ArrayList<>(); // Tạm thời để trống, lát nữa chúng ta sẽ tích hợp phần Dịch vụ sau

                for (int i = 0; i < modelHoaDonTam.getRowCount(); i++) {
                    String loai = modelHoaDonTam.getValueAt(i, 0).toString();
                    String maVaTen = modelHoaDonTam.getValueAt(i, 1).toString();
                    String maSP = maVaTen.split(" • ")[0];
                    int soLuong = Integer.parseInt(modelHoaDonTam.getValueAt(i, 2).toString());
                    BigDecimal donGia = new BigDecimal(modelHoaDonTam.getValueAt(i, 3).toString());

                    if (loai.equals("SP")) {
                        Object[] ctsp = {maSP, soLuong, donGia};
                        dsChiTietSP.add(ctsp);
                    }
                }

                // Gọi DAO thực hiện Transaction lưu hóa đơn, trừ tồn kho
                boolean kq = hd_dao.themHoaDon(hd, dsChiTietSP, dsChiTietDV);

                if (kq) {
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nMã hóa đơn: " + maHD);
                    
                    // Reset lại form bán hàng cho giao dịch tiếp theo
                    modelHoaDonTam.setRowCount(0);
                    khachHangHienTai = null;
                    txtSearchKH.setText("");
                    txtSearchSP.setText("");
                    txtTienKhachDua.setText("");
                    lblInfoKH.setText("<html><b><font size='3'>Chưa chọn khách hàng nào!</font></b><br/><font color='#7A8CA0' size='3'>Vui lòng nhập số điện thoại để tìm kiếm hoặc thêm mới.</font></html>");
                    tinhToanTienHoaDon();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Lỗi lưu cơ sở dữ liệu.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình thanh toán: " + ex.getMessage());
            }
        }
    }
}