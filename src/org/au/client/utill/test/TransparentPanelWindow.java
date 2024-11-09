package org.au.client.utill.test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
public class TransparentPanelWindow {
    public static void main(String[] args) {
        // 创建 JWindow 窗口
        JWindow window = new JWindow();

        // 设置窗口大小
        int width = 1000;
        int height = 200;
        window.setSize(width, height);

        // 设置窗口位置为屏幕中心
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        window.setLocation(x, y);

        // 创建一个透明的面板
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f)); // 设置透明度为 0
                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false);

        // 设置窗口背景为透明
        window.setBackground(new Color(0, 0, 0, 0));

        // 设置鼠标指针为透明
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisibleCursor");
        window.setCursor(invisibleCursor);

        // 屏蔽鼠标操作
        window.addMouseListener(new MouseAdapter() {});
        window.addMouseMotionListener(new MouseMotionAdapter() {});

        // 将面板添加到窗口
        window.getContentPane().add(panel);

        // 将窗口锁定在最上层
        window.setAlwaysOnTop(true);

        // 显示窗口
        window.setVisible(true);
    }
}
