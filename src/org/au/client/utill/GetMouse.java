package org.au.client.utill;

import com.github.kwhat.jnativehook.mouse.*;
import org.au.client.Main;
import org.au.client.client.NIOClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.au.client.Main.*;
import static org.au.client.client.NIOClient.screenSize;
import static org.au.client.utill.RightSideTextArea.hideAnimation;
import static org.au.client.utill.RightSideTextArea.showAnimation;

public class GetMouse  implements NativeMouseListener, NativeMouseInputListener , NativeMouseWheelListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long lastRecordTime = System.currentTimeMillis();
    public static boolean isUse = true;
    public static boolean isMoveFrame = false;
    public static long TIME_WINDOW_MS = 100; // 时间窗口，单位毫秒
    private int accumulatedRotation = 0;
    private Timer timer = new Timer();
    private boolean isMove = false;
    private SimpleLogger logger = new SimpleLogger("GetMouse", SimpleLogger.Level.DEBUG);
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        //System.out.println("MouseClicked:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        //这里发送相对坐标

        if(isUse) {
            double tag_x = (double) e.getX() / x;
            double tag_y = (double) e.getY() / y;
            NIOClient.addMessage("MouseClicked:" + e.getButton() + " at " + tag_x + "," + tag_y);
            if(type==0) {
                logger.debug("MouseClicked: (" + e.getX() + ", " + e.getY() + ")");
            }
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        //System.out.println("MousePressed:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            double tag_x = (double) e.getX() / x;
            double tag_y = (double) e.getY() / y;
            NIOClient.addMessage("MousePressed:" + e.getButton() + " at " + tag_x + "," +  tag_y);
            if(type==0) {
                logger.debug("MouseClicked: (" + e.getX() + ", " + e.getY() + ")");
            }
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        //System.out.println("MouseReleased:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            double tag_x = (double) e.getX() / x;
            double tag_y = (double) e.getY() / y;
            NIOClient.addMessage("MouseReleased:" + e.getButton() + " at " + tag_x + "," +  tag_y);
            if(type==0) {
                logger.debug("MouseReleased: (" + e.getX() + ", " + e.getY() + ")");
            }
        }
    }
    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {

        //System.out.println("MouseMove:" + e.getX() + "," + e.getY());
        if(isUse) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordTime >= TIME_WINDOW_MS) { // 100 毫秒 = 0.1 秒
                if(Main.type==0) {
                    logger.debug("MouseMove: (" + e.getX() + ", " + e.getY() + ")");
                }
                // 鼠标移动事件
                double tag_x = (double) e.getX() / x;
                double tag_y = (double) e.getY() / y;
                NIOClient.addMessage("MouseMove:" + tag_x + "," + tag_y);
                lastRecordTime = currentTime;
                try {
                    if(tag_x>0.9) {
                        if(isMove) {
                            showAnimation();
                            isMove = false;
                        }
                    }else {
                        if(!isMove) {
                            isMove = true;
                            hideAnimation();
                        }
                    }
                }catch (NullPointerException npe) {

                }
            }
            if (isMoveFrame) {
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                double shijix = (((double) e.getX() / x) * screenWidth) -250 ;
                double shijiy =( ((double) e.getY() / y) * screenHeight) -250 ;
                NIOClient.mouseShield.move((int) shijix, (int) shijiy);
            }
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        //System.out.println("MouseDragged:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordTime >= TIME_WINDOW_MS) { // 100 毫秒 = 0.1 秒
                //System.out.println("Mouseposition: (" + e.getX() + ", " + e.getY() + ")");
                // 鼠标移动事件
                double tag_x = (double) e.getX() / x;
                double tag_y = (double) e.getY() / y;
                NIOClient.addMessage("MouseDragged:" + e.getButton() + ":" + tag_x + "," + tag_y);
                if(type==0) {
                    logger.debug("MouseDragged: (" + e.getX() + ", " + e.getY() + ")");
                }
                lastRecordTime = currentTime;
            }
        }
        // 鼠标拖动事件
    }
    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();

        // 累积滚轮旋转值
        accumulatedRotation += wheelRotation;

        // 取消之前的定时任务
        timer.cancel();
        timer.purge();

        // 重新设置定时任务
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleAccumulatedRotation(accumulatedRotation);
                accumulatedRotation = 0; // 重置累积值
            }
        }, TIME_WINDOW_MS);
    }

    private void handleAccumulatedRotation(int rotation) {
        if (rotation == 0) {
            return;
        }
        NIOClient.addMessage("MouseWheelEvent:" + rotation);
        if(type==0) {
            logger.debug("MouseWheelEvent: " + rotation);
        }
    }
}