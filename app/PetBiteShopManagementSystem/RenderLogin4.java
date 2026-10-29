import view.LoginFrame;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import javax.swing.JPanel;

public class RenderLogin4 {
    public static void main(String[] args) throws Exception {
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
        
        ImageIO.write(img, "png", new File("login_preview.png"));
        System.out.println("RENDER_4_DONE");
    }
}
