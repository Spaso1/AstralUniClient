package org.au.client.utill;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.au.client.Main;
import org.au.client.client.NIOClient;
import org.au.client.server.NIOServer;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.au.client.Main.*;
import static org.au.client.Main.x;
import static org.au.client.utill.WindowsNotification.displayTray;

public class CheckList {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static SimpleLogger logger = new SimpleLogger("CheckList", SimpleLogger.Level.INFO);
    private static List<String> list2;//Key-Computer映射
    public static void check() {
        list2 = new ArrayList<>();
        NIOClient.used_mac.size();
        for (String s : NIOClient.used_mac.keySet()) {
            list2.add(s);
        }
        while (true) {
            try {
                Thread.sleep(50);
                if(list.contains("Ctrl")) {
                    if(list.contains("Shift")) {
                        for (int x2 = 0; x2 < list2.size();x2 ++) {
                            String con = list2.get(x2);
                            if(list.contains(con)) {
                                NIOClient.addMessage("KeyReleased:" + "Ctrl");
                                NIOClient.addMessage("KeyReleased:" + "Shift");
                                NIOClient.a2 = true;
                                //System.out.println("A+Ctrl+Shift");
                                NIOClient.to_ = NIOClient.used_mac.get(con);
                                logger.info("2操作电脑切换为: " + NIOClient.to_);
                                displayTray("AstralUniClient","操作电脑切换为: " + NIOClient.to_);
                                NIOClient.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                NIOClient.robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                                NIOClient.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                                if(NIOClient.mac_.equals(NIOClient.to_)) {
                                    NIOClient.a2 = false;
                                    MouseShield.frame.dispose();
                                    MouseShield.shutdown();
                                    Robot robot = new Robot();
                                    int screenWidth = screenSize.width;
                                    int screenHeight = screenSize.height;
                                    double tagx = NIOClient.change_x;
                                    double tagy = NIOClient.change_y;
                                    double shijix = tagx  * screenWidth;
                                    double shijiy = tagy * screenHeight;
                                    robot.mouseMove((int)shijix, (int)shijiy);
                                    logger.info("鼠标恢复");
                                }
                            }
                        }
                    }
                }
                if(list.contains("Meta")) {
                    if(list.contains("D")) {
                        MouseShield.frame.dispose();
                        MouseShield.shutdown();
                        NIOClient.addMessage("KeyReleased:" + "Ctrl");
                        NIOClient.addMessage("KeyReleased:" + "Shift");
                        NIOClient.a2 = true;
                        NIOClient.to_ = NIOClient.mac_;
                        logger.info("操作电脑切换为: " + NIOClient.to_);
                        displayTray("AstralUniClient","操作电脑切换为: " + NIOClient.to_);
                        NIOClient.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        NIOClient.robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                        NIOClient.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        NIOClient.a2 = false;
                        Robot robot = new Robot();
                        int screenWidth = screenSize.width;
                        int screenHeight = screenSize.height;
                        double tagx = NIOClient.change_x;
                        double tagy = NIOClient.change_y;
                        double shijix = tagx  * screenWidth;
                        double shijiy = tagy  * screenHeight;
                        robot.mouseMove((int)shijix, (int)shijiy);
                        logger.info("鼠标恢复");
                        //NIOClient.mouseShield = new MouseShield();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
