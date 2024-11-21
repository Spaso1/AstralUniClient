package org.au.client.utill;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import org.au.client.client.NIOClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class MouseShield {
    protected static JFrame frame;
    private static SimpleLogger logger = new SimpleLogger(MouseShield.class.getName(), SimpleLogger.Level.INFO);
    public MouseShield() throws Exception {
        // 创建1像素点的JFrame窗口
        frame = new JFrame();
        frame.setSize(400, 400);
        frame.setUndecorated(true); // 设置为无边框
        frame.setBackground(new Color(0, 0, 0, 10)); // 透明背景
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 不关闭程序
        frame.setAlwaysOnTop(true); // 设置窗口始终在最上方
        // 添加窗口关闭监听器，以便调试
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing");
                System.exit(0);
            }
        });
        // 创建一个透明的图像
        BufferedImage transparentImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = transparentImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, 16, 16);
        g2d.dispose();
        // 创建自定义鼠标指针
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(transparentImage, new Point(0, 0), "invisible");
// 设置自定义鼠标指针V
        frame.setCursor(customCursor);
        // 确保窗口在屏幕上可见
        frame.setVisible(true);
        // 将窗口移到屏幕中央
        frame.setLocation(0,0);
        logger.info("已成功");
    }
    public void move(int x, int y) {
        frame.setLocation(x, y);
    }
    public static void shutdown() {
        try {
            frame.dispose();
        }catch (Exception e) {
            logger.info("您是否已经在本桌面");
        }
    }
}
