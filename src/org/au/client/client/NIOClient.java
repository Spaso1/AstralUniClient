package org.au.client.client;
import com.github.kwhat.jnativehook.NativeHookException;
import org.au.client.Main;
import org.au.client.utill.GetMouse;
import org.au.client.utill.MouseShield;
import org.au.client.utill.SimpleLogger;

import javax.crypto.SecretKey;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.au.client.Main.*;
import static org.au.client.utill.safe.AES256GCM.*;

public class NIOClient {
    private SocketChannel channel;
    private static ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    public static String mac_;
    public static String to_;
    private Robot robot ;
    public static int change_x;
    public static int change_y;
    public static String useId;
    public static Map<String,String> used_mac;
    public static boolean maincode = true;
    public static MouseShield mouseShield;
    private String secure_code;
    public static boolean a2 = true;
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private SecretKey secretKey ;
    private boolean isUSE_SSL = false;
    private String USE_SEC;
    private static SimpleLogger logger = new SimpleLogger("NIOClient", SimpleLogger.Level.DEBUG);
    public void connect(String host, int port) {
        //获得mac
        try {
            logger.info("客户端初始化");
            used_mac = new TreeMap<>();
            secretKey = generateKey();
            readProp();
            robot = new Robot();
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                logger.info("MAC Address: " + mac[i]);
            }
            //System.out.println("MAC Address: " + sb.toString());
            logger.info("本机MAC地址:" + sb.toString());
            mac_ = sb.toString();
            to_ = mac_;
            useId = mac_;
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            logger.info("尝试连接到服务器: " + host + ":" + port);

            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
            channel.configureBlocking(false);
            logger.info("连接成功");
        }catch (Exception e) {
            long x = 100;
            while (true) {
                try {
                    Thread.sleep(x);
                    channel.close();
                    logger.info("尝试(" +x +"ms)重新连接到服务器: " + host + ":" + port);

                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(host, port));
                    channel.configureBlocking(false);
                    logger.info("连接成功");
                    break;
                }catch (Exception e2) {
                    x *= 2;
                }
            }
        }

    }

    public void start() throws IOException {
        // 启动一个线程来处理从服务器接收到的消息
        new Thread(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (true) {
                    buffer.clear();
                    int read = channel.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        String msg = new String(buffer.array(), 0, read, StandardCharsets.UTF_8).trim();
                        //System.out.println("收到消息: " + msg);
                        if(msg.split(":")[1].equals("MouseMove")) {
                            if(msg.split(":")[0].equals(mac_)) {
                                change_x = Integer.parseInt(msg.split(":")[2].split(",")[0]);
                                change_y = Integer.parseInt(msg.split(":")[2].split(",")[1]);
                            }
                        }
                        out(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 启动一个线程来处理队列中的消息并发送到服务器
        new Thread(() -> {
            try {
                while (true) {
                    String msg = messageQueue.poll();
                    if (msg != null) {
                        msg = msg + secure_code;
                        String[] res = msg.split(":");

//                        if(isUSE_SSL) {
//                            if(USE_SEC.equals("AES256GCM")) {
//                                byte[] ciphertext = encrypt(msg.getBytes(), secretKey);
//                                msg = new String(ciphertext);
//                            }
//                        }

                        ByteBuffer buffer = ByteBuffer.wrap((msg +"\n").getBytes(StandardCharsets.UTF_8));
                        channel.write(buffer);
                        //System.out.println(msg);
                    }
                    Thread.sleep(100); // 避免忙等待
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 启动一个线程来读取用户输入并添加到队列
        new Thread(() -> {
            try {
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = stdin.readLine()) != null) {
                    messageQueue.offer(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 提供一个方法供其他地方添加消息到队列
    public static void addMessage(String msg) {
        messageQueue.offer(to_ + ":" + msg);
    }

    public void out(String cmd) throws Exception {
        String[] res = cmd.split(":");
        if(mac_.equals(res[0])) {
            if(maincode) {
                GetMouse.isMoveFrame = false;
            }

            if(!maincode) {
                try {
                    logger.info("对" + res[0] + "执行:" + res[1] + ":" + res[2]);
                    if(res[1].equals("MouseMove")) {
                        System.out.println("对本机器执行:" + res[1] + ":" + res[2]);
                        String[] db = res[2].split(",");
                        int screenWidth = screenSize.width;
                        int screenHeight = screenSize.height;
                        double tagx = Integer.parseInt(db[0]);
                        double tagy = Integer.parseInt(db[1]);
                        double shijix = (tagx / x) * screenWidth;
                        double shijiy = (tagy / y) * screenHeight;
                        robot.mouseMove((int)shijix, (int)shijiy);
                    }else if(res[1].equals("MousePressed")){
                        int buttontype = Integer.parseInt(res[2].split(" ")[0]);
                        double tagx = Integer.parseInt(res[2].split(" ")[2].split(",")[0]);
                        double tagy = Integer.parseInt(res[2].split(" ")[2].split(",")[1]);
                        int screenWidth = screenSize.width;
                        int screenHeight = screenSize.height;
                        double shijix = (tagx / (double)x) * (double)screenWidth;
                        double shijiy = (tagy / (double)y) * (double)screenHeight;
                        robot.mouseMove((int)shijix, (int)shijiy);
                        if(buttontype==1) {
                            buttontype = InputEvent.BUTTON1_DOWN_MASK;
                        }else if(buttontype == 2) {
                            buttontype = InputEvent.BUTTON3_DOWN_MASK;
                        }else if(buttontype == 3) {
                            buttontype = InputEvent.BUTTON2_DOWN_MASK;
                        }
                        robot.mousePress(buttontype);
                    }else if(res[1].equals("MouseReleased")){
                        int buttontype = Integer.parseInt(res[2].split(" ")[0]);
                        double tagx = Integer.parseInt(res[2].split(" ")[2].split(",")[0]);
                        double tagy = Integer.parseInt(res[2].split(" ")[2].split(",")[1]);
                        int screenWidth = screenSize.width;
                        int screenHeight = screenSize.height;
                        double shijix = (tagx / (double)x) * (double) screenWidth;
                        double shijiy = (tagy / (double)y) * (double) screenHeight;
                        robot.mouseMove((int)shijix, (int)shijiy);
                        if(buttontype==1) {
                            buttontype = InputEvent.BUTTON1_DOWN_MASK;
                        }else if(buttontype == 2) {
                            buttontype = InputEvent.BUTTON3_DOWN_MASK;
                        }else if(buttontype == 3) {
                            buttontype = InputEvent.BUTTON2_DOWN_MASK;
                        }
                        robot.mouseRelease(buttontype);
                    }else if(res[1].equals("KeyPressed")) {
                        int v = Main.getType(res[2]);
                        System.out.println(v);
                        robot.keyPress(v);
                    }else if(res[1].equals("KeyReleased")) {
                        int v = Main.getType(res[2]);
                        robot.keyRelease(v);
                    }else if(res[1].equals("MouseWheelEvent")) {
                        int v = Integer.parseInt(res[2]);
                        robot.mouseWheel(v);
                    }
                }catch (Exception e) {
                    System.out.println("未预设");
                }
            }
        }else {
            System.out.println(res[0]);
            if (maincode) {
                GetMouse.isUse = false;
                if(a2) {
                    logger.info("屏蔽鼠标");
                    if(!GetMouse.isMoveFrame) {
                        mouseShield  = new MouseShield();
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    //System.out.println("发送对象" + res[0]);
                    logger.info("操作对象" + res[0]);
                    GetMouse.isMoveFrame = true;

                }
                GetMouse.isUse = true;
            }
        }
    }
    private void readProp() throws IOException {
        logger.info("读取配置文件");
        try {
            File file = new File(".//auc//server.prop");
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("SECURE")) {
                    secure_code = line.split(":")[1];
                }else if(line.contains("USE_SSL")){
                    isUSE_SSL = Boolean.parseBoolean(line.split(":")[1]);
                }else if(line.contains("USE_SEC")){
                    USE_SEC = line.split(":")[1];
                    //System.out.println(USE_SEC);
                    logger.info("USE_SEC:" + USE_SEC);
                }else {
                    used_mac.put(line.split(":")[0], line.split(":")[1]);

                }
            }
            br.close();
        }catch (Exception e) {
            logger.error("读取配置文件失败,检查配置文件内容和是否存在");
        }
    }

}
