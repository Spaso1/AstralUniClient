package org.au.client.utill;

import com.github.kwhat.jnativehook.mouse.*;
import org.au.client.client.NIOClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.au.client.Main.x;
import static org.au.client.Main.y;
import static org.au.client.client.NIOClient.screenSize;

public class GetMouse  implements NativeMouseListener, NativeMouseInputListener , NativeMouseWheelListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long lastRecordTime = System.currentTimeMillis();
    public static boolean isUse = true;
    public static boolean isMoveFrame = false;
    private static final long TIME_WINDOW_MS = 100; // 时间窗口，单位毫秒
    private int accumulatedRotation = 0;
    private Timer timer = new Timer();
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        //System.out.println("MouseClicked:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            NIOClient.addMessage("MouseClicked:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        //System.out.println("MousePressed:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            NIOClient.addMessage("MousePressed:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        //System.out.println("MouseReleased:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        if(isUse) {
            NIOClient.addMessage("MouseReleased:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        }
    }
    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        //System.out.println("MouseMove:" + e.getX() + "," + e.getY());
        if(isUse) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordTime >= 100) { // 100 毫秒 = 0.1 秒
                //System.out.println("Mouseposition: (" + e.getX() + ", " + e.getY() + ")");
                // 鼠标移动事件
                NIOClient.addMessage("MouseMove:" + e.getX() + "," + e.getY());
                lastRecordTime = currentTime;
            }
            if (isMoveFrame) {
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                double shijix = (((double) e.getX() / x) * screenWidth) -50 ;
                double shijiy =( ((double) e.getY() / y) * screenHeight) -50 ;
                NIOClient.mouseShield.move((int) shijix, (int) shijiy);
            }
        }
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        //System.out.println("MouseDragged:" + e.getButton() + " at " + e.getX() + "," + e.getY());
        //NIOClient.addMessage("MouseDragged:" + e.getButton() + " at " + e.getX() + "," + e.getY());
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
    }
}