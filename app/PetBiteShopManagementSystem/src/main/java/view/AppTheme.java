package view;

import java.awt.Color;
import java.awt.Font;

public class AppTheme {
    public static final Color SIDEBAR_BG      = new Color(0x12, 0x39, 0x58); // #123958
    public static final Color MAIN_BG         = new Color(244, 246, 249); 
    public static final Color PRIMARY         = new Color(0xFF, 0xB1, 0x4B); // #FFB14B (Màu cam vàng chấm tròn active)
    public static final Color ACTIVE_BG       = new Color(0x20, 0x56, 0x77); // #205677 (Màu khi mục được active)
    public static final Color HOVER_BG        = new Color(0x1A, 0x47, 0x6A); // Màu hover nhẹ
    public static final Color TEXT_PRIMARY    = Color.WHITE;                  // Chữ trắng sidebar
    public static final Color TEXT_SECONDARY  = new Color(180, 195, 210);     // Chữ phụ
   
    public static final Color BORDER          = new Color(225, 230, 238);     
    public static final Color BORDER_LINE     = new Color(0x2A, 0x55, 0x78);  
    
    public static final Color CARD_BG         = Color.WHITE;

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_BOLD  = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_PLAIN = new Font("SansSerif", Font.PLAIN, 13);
}