package org.au.client;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import org.au.client.client.NIOClient;
import org.au.client.server.NIOServer;
import org.au.client.utill.*;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class Main {
    public static RobotController controller ;
    public static GetKey getKey;
    public static GetMouse getMouse;
    public static Deque<String> queue;
    public static String last;
    private static NIOClient client;
    public static List<String> list;
    public static int x;
    public static int y;

    public static void main(String[] args) throws Exception {
        SimpleLogger logger = new SimpleLogger("Main", SimpleLogger.Level.DEBUG);
        logger.info("AstralUniClient 启动开始,参数:" + Arrays.toString(args));
        Locale.setDefault(Locale.ENGLISH);
        logger.info("底层识别语言已换成en");
        list = new ArrayList<>();
        if(args[0].contains("-server")) {
            logger.info("服务端模式");
            startServer(Integer.parseInt(args[0].split(":")[1]));
        }else if(args[0].contains("-client")){
            logger.info("客户端模式");
            try {
                if(args[1].equals("-main")) {
                    // 注册键盘监听器
                    logger.info("作为主客户端");
                    logger.info("注册键盘监听器");
                    DisplayResolution resolution = new DisplayResolution();
                    x = resolution.returnResolution()[0];
                    y = resolution.returnResolution()[1];
                    logger.info("分辨率:" + x + "x" + y);
                    //System.out.println(Arrays.toString(resolution.returnResolution()));
                    GlobalScreen.registerNativeHook();
                    getKey = new GetKey();
                    getMouse = new GetMouse();
                    GlobalScreen.addNativeKeyListener(getKey);
                    logger.info("注册鼠标监听器");
                    // 注册鼠标监听器
                    GlobalScreen.addNativeMouseListener(getMouse);
                    GlobalScreen.addNativeMouseMotionListener(getMouse);
                    GlobalScreen.addNativeMouseWheelListener(getMouse);
                    client = new NIOClient();
                    logger.info("启动客户端");
                    startClient(args[0].split(":")[1], Integer.parseInt(args[0].split(":")[2]));
                    NIOClient.maincode = true;
                    logger.info("组合键检测启动");
                    CheckList.check();
                }else {
                    DisplayResolution resolution = new DisplayResolution();
                    x = resolution.returnResolution()[0];
                    y = resolution.returnResolution()[1];
                    logger.info("分辨率:" + x + "x" + y);
                    NIOClient.maincode = false;
                    //System.out.println(Arrays.toString(resolution.returnResolution()));
                    client = new NIOClient();
                    startClient(args[0].split(":")[1], Integer.parseInt(args[0].split(":")[2]));
                    CheckList.check();
                }
            }catch (Exception e) {
                logger.error("作为从客户端启动");
                DisplayResolution resolution = new DisplayResolution();
                x = resolution.returnResolution()[0];
                y = resolution.returnResolution()[1];
                logger.info("分辨率:" + x + "x" + y);
                NIOClient.maincode = false;
                //System.out.println(Arrays.toString(resolution.returnResolution()));
                client = new NIOClient();
                startClient(args[0].split(":")[1], Integer.parseInt(args[0].split(":")[2]));
            }


        }else if(args[0].equals("-test")){
            logger.info("测试模式");
            logger.debug("启动");
            try {
                DisplayResolution resolution = new DisplayResolution();
                System.out.println(Arrays.toString(resolution.returnResolution()));
                logger.debug("分辨率:" + Arrays.toString(resolution.returnResolution()));
                controller = new RobotController();
                queue = new LinkedList<String>();
                logger.debug("注册键盘监听器");
                // 注册键盘监听器
                GlobalScreen.registerNativeHook();
                getKey = new GetKey();
                getMouse = new GetMouse();

                GlobalScreen.addNativeKeyListener(getKey);
                // 注册鼠标监听器
                logger.debug("注册鼠标监听器");
                GlobalScreen.addNativeMouseListener(getMouse);
                GlobalScreen.addNativeMouseWheelListener(getMouse);
                GlobalScreen.addNativeMouseMotionListener(getMouse);
                logger.debug("组合键检测启动");
                CheckList.check();
            } catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
                System.exit(1);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void startServer(int port) throws Exception {
        NIOServer server = new NIOServer();
        server.initServer(port);
        server.listen();
    }

    public static void startClient(String ip,int port) throws IOException {
        client.connect(ip,port);
        client.start();
    }
    public static int getType(String key) throws NativeHookException {
        int value= 0;
        try {
            // 组合类名和变量名
            String className = "java.awt.event.KeyEvent";
            String cmd = key.toUpperCase().replace(" ","");
            if(cmd.equals("BACKSPACE")) {
                cmd = "BACK_SPACE";
            }
            String fieldName = "VK_" + cmd;

            // 获取类对象
            Class<?> clazz = Class.forName(className);

            // 获取字段对象
            Field field = clazz.getField(fieldName);

            // 获取静态变量的值
            value = (int) field.get(null); // 静态变量不需要实例对象
            SimpleLogger logger = new SimpleLogger("Main-GetType", SimpleLogger.Level.DEBUG);
            logger.debug("获取到按键类型:" + value);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
}
