package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import view.LoginFrame;

/**
 * Công cụ hỗ trợ chụp ảnh xem trước (preview) giao diện LoginFrame ra file ảnh.
 * (Được tối ưu và gộp từ 2 file thử nghiệm RenderLogin2.java và RenderLogin4.java)
 */
public class LoginPreviewRenderer {

    public static void main(String[] args) {
        try {
            LoginFrame frame = new LoginFrame();
            frame.pack();
            frame.setSize(1100, 720);
            frame.validate();

            JPanel pane = (JPanel) frame.getContentPane();
            pane.setBounds(0, 0, 1100, 720);
            pane.validate();

            BufferedImage img = new BufferedImage(1100, 720, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            pane.paint(g2);
            g2.dispose();

            File outputFile = new File("login_preview.png");
            ImageIO.write(img, "png", outputFile);
            System.out.println("Đã chụp và lưu ảnh xem trước giao diện tại: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

