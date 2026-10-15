package view;

import java.awt.*;
import javax.swing.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setBackground(AppTheme.MAIN_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("DASHBOARD");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.PRIMARY);
        
        JLabel lblSub = new JLabel("Tổng quan hệ thống cửa hàng thú cưng PET BITE SHOP");
        lblSub.setFont(AppTheme.FONT_PLAIN);
        lblSub.setForeground(AppTheme.TEXT_SECONDARY);
        
        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);
        add(headerPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        cardsPanel.add(createCard("Tổng sản phẩm", "124", "📦"));
        cardsPanel.add(createCard("Tổng dịch vụ", "12", "✂️"));
        cardsPanel.add(createCard("Khách hàng", "48", "👥"));
        cardsPanel.add(createCard("Lịch hẹn hôm nay", "5", "📅"));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppTheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppTheme.FONT_PLAIN);
        lblTitle.setForeground(AppTheme.TEXT_SECONDARY);
        
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblVal.setForeground(AppTheme.TEXT_PRIMARY);
        
        textPanel.add(lblTitle);
        textPanel.add(lblVal);

        card.add(lblIcon, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }
}