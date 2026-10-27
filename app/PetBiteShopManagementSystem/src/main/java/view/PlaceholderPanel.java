package view;

import java.awt.*;
import javax.swing.*;

public class PlaceholderPanel extends JPanel {
    public PlaceholderPanel(String title) {
        setBackground(AppTheme.MAIN_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.PRIMARY);

        JLabel lblInfo = new JLabel("Chức năng đang được phát triển trong giai đoạn tiếp theo.");
        lblInfo.setFont(AppTheme.FONT_PLAIN);
        lblInfo.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(lblInfo);

        add(content, BorderLayout.NORTH);
    }
}