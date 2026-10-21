package view;

import java.awt.*;
import javax.swing.*;

public class HeaderPanel extends JPanel {
    private static final int HEADER_HEIGHT  = 96;
    private static final int LEFT_PADDING   = 36;
    private static final int RIGHT_PADDING  = 80;

    private static final Color SUBTITLE_COLOR = new Color(0x7A, 0x8C, 0xA0);
    private static final Color TODAY_BG       = new Color(0xED, 0xF1, 0xF5);

    private JLabel lblTitle;
    private JLabel lblSubtitle;
    private JLabel lblAvatar;

    public HeaderPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, HEADER_HEIGHT));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));

        // ===== Bên trái: tiêu đề + mô tả =====
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, 0, 0));

        lblTitle = new JLabel("Quản lý khách hàng");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(AppTheme.SIDEBAR_BG);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblSubtitle = new JLabel("");
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitle.setForeground(SUBTITLE_COLOR);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBox.add(Box.createVerticalGlue());
        titleBox.add(lblTitle);
        titleBox.add(Box.createVerticalStrut(2));
        titleBox.add(lblSubtitle);
        titleBox.add(Box.createVerticalGlue());
        add(titleBox, BorderLayout.WEST);

        // ===== Bên phải: nút "Hôm nay" + avatar =====
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, RIGHT_PADDING));

        RoundPanel todayBtn = new RoundPanel(TODAY_BG, 12);
        todayBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
        todayBtn.setPreferredSize(new Dimension(150, 40));
        todayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblClock = new JLabel("◷");
        lblClock.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblClock.setForeground(AppTheme.SIDEBAR_BG);

        JLabel lblToday = new JLabel("Hôm nay");
        lblToday.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblToday.setForeground(AppTheme.SIDEBAR_BG);

        // FlowLayout đặt component sát đỉnh -> bọc lại để căn giữa dọc
        todayBtn.setLayout(new GridBagLayout());
        GridBagConstraints tg = new GridBagConstraints();
        tg.gridy = 0;
        tg.gridx = 0;
        tg.insets = new Insets(0, 0, 0, 4);
        todayBtn.add(lblClock, tg);
        tg.gridx = 1;
        tg.insets = new Insets(0, 0, 0, 0);
        todayBtn.add(lblToday, tg);

        RoundPanel avatar = new RoundPanel(AppTheme.PRIMARY, 40);
        avatar.setLayout(new GridBagLayout());
        avatar.setPreferredSize(new Dimension(40, 40));
        lblAvatar = new JLabel("P");
        lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAvatar.setForeground(AppTheme.SIDEBAR_BG);
        avatar.add(lblAvatar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        rightPanel.add(todayBtn, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 42, 0, 0);
        rightPanel.add(avatar, gbc);

        add(rightPanel, BorderLayout.EAST);
    }

    /** Đặt tiêu đề + mô tả của màn hình hiện tại. */
    public void setHeader(String title, String subtitle) {
        lblTitle.setText(title);
        lblSubtitle.setText(subtitle == null ? "" : subtitle);
    }

    /** Giữ lại để tương thích code cũ: chỉ đổi tiêu đề, xóa mô tả. */
    public void setBreadcrumb(String title) {
        setHeader(title, "");
    }

    public void setAvatarInitial(String initial) {
        lblAvatar.setText(initial);
    }

    /** Panel bo góc dùng chung cho nút "Hôm nay" và avatar tròn. */
    private static class RoundPanel extends JPanel {
        private final Color fill;
        private final int arc;

        RoundPanel(Color fill, int arc) {
            this.fill = fill;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}