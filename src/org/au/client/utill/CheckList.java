package org.au.client.utill;

import org.au.client.Main;
import org.au.client.client.NIOClient;
import org.au.client.server.NIOServer;

import java.awt.*;
import java.util.Arrays;

import static org.au.client.Main.*;

public class CheckList {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static SimpleLogger logger = new SimpleLogger("CheckList", SimpleLogger.Level.INFO);
    public static void check() {
        while (true) {
            try {
                Thread.sleep(50);
                if(list.contains("Ctrl")) {
                    if(list.contains("Shift")) {
                        if(list.contains("A")) {
                            NIOClient.a2 = true;
                            System.out.println("A+Ctrl+Shift");
                            NIOClient.to_ = NIOClient.used_mac.get("A");
                            logger.info("操作电脑切换为: " + NIOClient.to_);
                            if(NIOClient.mac_.equals(NIOClient.to_)) {
                                NIOClient.a2 = false;
                                Robot robot = new Robot();
                                int screenWidth = screenSize.width;
                                int screenHeight = screenSize.height;
                                double tagx = NIOClient.change_x;
                                double tagy = NIOClient.change_y;
                                double shijix = (tagx / x) * screenWidth;
                                double shijiy = (tagy / y) * screenHeight;
                                robot.mouseMove((int)shijix, (int)shijiy);
                                MouseShield.shutdown();
                                logger.info("鼠标恢复");

                            }
                        }
                        if(list.contains("B")) {
                            NIOClient.a2 = true;
                            System.out.println("B+Ctrl+Shift");
                            NIOClient.to_ = NIOClient.used_mac.get("B");
                            logger.info("操作电脑切换为: " + NIOClient.to_);
                            if(NIOClient.mac_.equals(NIOClient.to_)) {
                                NIOClient.a2 = false;
                                Robot robot = new Robot();
                                int screenWidth = screenSize.width;
                                int screenHeight = screenSize.height;
                                double tagx = NIOClient.change_x;
                                double tagy = NIOClient.change_y;
                                double shijix = (tagx / x) * screenWidth;
                                double shijiy = (tagy / y) * screenHeight;
                                robot.mouseMove((int)shijix, (int)shijiy);
                                MouseShield.shutdown();
                                logger.info("鼠标恢复");

                            }
                        }
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
