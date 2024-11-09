package org.au.client.utill;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

public class RobotController {
    private final Robot robot;

    public RobotController() throws AWTException {
        this.robot = new Robot();
    }

    /**
     * 模拟按下指定的键
     *
     * @param keyCode 键码
     */
    public void pressKey(int keyCode) {
        robot.keyPress(keyCode);
    }

    /**
     * 模拟释放指定的键
     *
     * @param keyCode 键码
     */
    public void releaseKey(int keyCode) {
        robot.keyRelease(keyCode);
    }

    /**
     * 模拟按下并释放指定的键
     *
     * @param keyCode 键码
     */
    public void simulateKeyPressAndRelease(int keyCode) {
        pressKey(keyCode);
        releaseKey(keyCode);
    }

    /**
     * 模拟输入一个字符
     *
     * @param character 字符
     */
    public void typeCharacter(char character) {
        int keyCode = KeyEvent.getExtendedKeyCodeForChar(character);
        if (keyCode != KeyEvent.VK_UNDEFINED) {
            simulateKeyPressAndRelease(keyCode);
        } else {
            System.err.println("Unsupported character: " + character);
        }
    }

    /**
     * 模拟输入一个字符串
     *
     * @param text 字符串
     */
    public void typeString(String text) {
        for (char c : text.toCharArray()) {
            typeCharacter(c);
        }
    }

    /**
     * 模拟鼠标移动到指定位置
     *
     * @param x X坐标
     * @param y Y坐标
     */
    public void moveMouse(int x, int y) {
        robot.mouseMove(x, y);
    }

    /**
     * 模拟鼠标点击
     *
     * @param button 鼠标按钮（InputEvent.BUTTON1_DOWN_MASK, InputEvent.BUTTON2_DOWN_MASK, InputEvent.BUTTON3_DOWN_MASK）
     */
    public void clickMouse(int button) {
        robot.mousePress(button);
        robot.mouseRelease(button);
    }

    /**
     * 模拟鼠标双击
     *
     * @param button 鼠标按钮（InputEvent.BUTTON1_DOWN_MASK, InputEvent.BUTTON2_DOWN_MASK, InputEvent.BUTTON3_DOWN_MASK）
     */
    public void doubleClickMouse(int button) {
        clickMouse(button);
        clickMouse(button);
    }

    /**
     * 模拟鼠标滚轮滚动
     *
     * @param wheelAmount 滚轮滚动量
     */
    public void scrollMouse(int wheelAmount) {
        robot.mouseWheel(wheelAmount);
    }
}
