package org.au.client.utill;

import org.au.client.Main;

import java.awt.*;

public class DisplayResolution {
    public int[] returnResolution() {
        DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        //System.out.println(mode.getWidth() + " x " + mode.getHeight() + "(" + mode.getBitDepth() + ") : refresh rate " + mode.getRefreshRate());
        Main.x = mode.getWidth();
        Main.y = mode.getHeight();

        return new int[]{mode.getWidth(), mode.getHeight()};
    }
}
