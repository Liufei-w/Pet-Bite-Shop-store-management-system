import view.LoginFrame;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import javax.swing.JComponent;

public class RenderLogin2 {
    public static void main(String[] args) throws Exception {
        LoginFrame frame = new LoginFrame();
        frame.setSize(1100, 720);
        frame.addNotify();
        frame.validate();
        
        JComponent pane = (JComponent) frame.getContentPane();
        pane.setSize(1100, 720);
        pane.doLayout();
        
        BufferedImage img = new BufferedImage(1100, 720, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        pane.paintAll(g2);
        g2.dispose();
        
        ImageIO.write(img, "png", new File("login_preview.png"));
        System.out.println("RENDER_SUCCESS_2");
    }
}
