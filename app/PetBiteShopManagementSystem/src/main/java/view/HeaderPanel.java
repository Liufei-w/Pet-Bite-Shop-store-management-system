package view;

import java.awt.*;
import javax.swing.*;

public class HeaderPanel extends JPanel {
    private JLabel lblBreadcrumb;

    public HeaderPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 56));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));

        lblBreadcrumb = new JLabel(" Quản lý dịch vụ");
        lblBreadcrumb.setFont(AppTheme.FONT_PLAIN);
        lblBreadcrumb.setForeground(AppTheme.TEXT_SECONDARY);
        lblBreadcrumb.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        add(lblBreadcrumb, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        userPanel.setOpaque(false);
        
        JLabel lblUser = new JLabel("👤  Chú cửa hàng ▾");
        lblUser.setFont(AppTheme.FONT_BOLD);
        lblUser.setForeground(AppTheme.TEXT_PRIMARY);
        userPanel.add(lblUser);

        add(userPanel, BorderLayout.EAST);
    }

    public void setBreadcrumb(String title) {
        lblBreadcrumb.setText(" " + title);
    }
}