package org.au.client.utill;

import org.au.client.client.NIOClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static org.au.client.Main.list;

public class RightSideTextArea {
    private static JFrame frame;
    private static JTextArea textArea;
    private static int finalX;
    private static int initialX;
    private static int y;
    public static boolean use;
    public static void updateText(String text) {
        textArea.setText(text);
    }
    public static void createAndShowGui() {
        frame = new JFrame("Right Side Text Area");
        frame.setUndecorated(true); // 去掉边框
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100, 800); // 调整窗口大小
        frame.setAlwaysOnTop(true); // 设置窗口始终在最上方
        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 初始位置在屏幕最右边
        initialX = screenWidth;
        finalX = screenWidth - 100;
        y = (screenHeight - 800) / 2;

        // 创建文本域
        textArea = new CustomTextArea(20, 10);
        textArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        textArea.setBackground(Color.WHITE); //
        textArea.setForeground(Color.DARK_GRAY); //
        textArea.setVisible(false); // 初始隐藏
        textArea.setFont(new java.awt.Font("宋体", 0, 14));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            private void contentChanged(DocumentEvent e) {
                NIOClient.addMessage("{Text:}" + textArea.getText());
                // 在这里执行你需要的操作
            }
        });

        // 创建一个滚动面板来放置文本域
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 移除滚动面板的边框
        scrollPane.setOpaque(false); // 使滚动面板透明

        // 设置滚动条样式
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        // 创建一个面板来放置滚动面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // 使面板透明

        // 创建一个按钮并添加到面板顶部
        JButton button = new JButton("显示/隐藏");
        button.setBackground(new Color(94, 187, 206));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.isVisible()) {
                    use = false;
                    hideAnimation();
                } else {
                    showAnimation();
                }
            }
        });
        panel.add(button, BorderLayout.NORTH);

        // 添加滚动面板到面板中心
        panel.add(scrollPane, BorderLayout.CENTER);

        // 添加面板到窗口
        frame.add(panel, BorderLayout.CENTER);

        // 设置窗口形状为圆角矩形
        frame.setShape(new RoundRectangle2D.Double(0, 0, 100, 800, 20, 20));
        frame.setLocation(1000,0);
        // 启动动画
        startAnimation(frame, initialX, finalX, y, textArea);
        frame.setVisible(false);
    }

    public static void startAnimation(JFrame frame, int initialX, int finalX, int y, JTextArea textArea) {
        Timer timer = new Timer(2, new ActionListener() {
            private int x = initialX;
            private int delay = 2; // 初始延迟

            @Override
            public void actionPerformed(ActionEvent e) {
                if (x <= finalX) {
                    ((Timer) e.getSource()).stop();
                    textArea.setVisible(true);
                } else {
                    x -= 10; // 每次移动10像素
                    frame.setLocation(x, y);
                    if (delay > 1) {
                        delay -= 1; // 减少延迟，实现加速效果
                        ((Timer) e.getSource()).setDelay(delay);
                    }
                }
                use = true;
            }
        });

        timer.start();
    }

    public static void showAnimation() {
        Timer timer = new Timer(2, new ActionListener() {
            private int x = frame.getX();
            private int delay = 1; // 初始延迟

            @Override
            public void actionPerformed(ActionEvent e) {
                use = true;
                if (x <= finalX) {
                    ((Timer) e.getSource()).stop();
                    textArea.setVisible(true);
                } else {
                    x -= 10; // 每次移动10像素
                    frame.setLocation(x, y);
                    if (delay > 1) {
                        delay -= 1; // 减少延迟，实现加速效果
                        ((Timer) e.getSource()).setDelay(delay);
                    }
                }
            }
        });

        timer.start();
    }

    public static void hideAnimation() {
        Timer timer = new Timer(1, new ActionListener() {
            private int x = frame.getX();
            private int delay = 2; // 初始延迟

            @Override
            public void actionPerformed(ActionEvent e) {
                use = false;
                if (x >= initialX) {
                    ((Timer) e.getSource()).stop();
                    textArea.setVisible(false);
                } else {
                    x += 10; // 每次移动10像素
                    frame.setLocation(x, y);
                    if (delay > 1) {
                        delay -= 1; // 减少延迟，实现加速效果
                        ((Timer) e.getSource()).setDelay(delay);
                    }
                }
            }
        });

        timer.start();
    }
}

// 自定义滚动条样式
class CustomScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        thumbColor = new Color(100, 105, 110); // 滚动条颜色
        trackColor = new Color(60, 63, 65); // 轨道颜色
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
