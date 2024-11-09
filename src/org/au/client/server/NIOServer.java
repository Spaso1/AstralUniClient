package org.au.client.server;

import org.au.client.Main;
import org.au.client.utill.SimpleLogger;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static org.au.client.utill.safe.AES256GCM.*;

public class NIOServer {
    private Selector selector;
    private int useId;
    private Map<String,String> used_mac;
    private String secure_code;
    private Map<SocketChannel, String> clientMap = new HashMap<>();
    private SecretKey secretKey ;
    private boolean isUSE_SSL = false;
    private String USE_SEC;
    private static SimpleLogger logger = new SimpleLogger("NIOServer", SimpleLogger.Level.INFO);

    public void initServer(int port) throws Exception {
        used_mac = new TreeMap<>();
        readProp();
        secretKey = generateKey();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("服务器启动成功，监听端口: " + port);
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
                }else {
                    used_mac.put(line.split(":")[0], line.split(":")[1]);
                }
            }
            br.close();
        }catch (Exception e) {
            logger.error("读取配置文件失败,检查配置文件内容和是否存在");
        }
    }

    public void listen() throws IOException {
        while (true) {
            selector.select();
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            try {
                while (ite.hasNext()) {
                    SelectionKey key = ite.next();
                    ite.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }catch (Exception e) {
                logger.error("服务器异常" + e.getStackTrace());
            }

        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        clientMap.put(channel, "");
        logger.info("客户端连接成功: " + channel.getRemoteAddress());
    }

    private void handleRead(SelectionKey key)  {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int read = 0;
            try {
                 read = channel.read(buffer);
            }catch (Exception e) {
            }
            if (read > 0) {
                buffer.flip();
                String msg = new String(buffer.array(), 0, read).trim();
//                if(isUSE_SSL) {
//                    if(USE_SEC.equals("AES256GCM")) {
//                        byte[] decryptedText = decrypt(msg.getBytes(), secretKey);
//                        msg = new String(decryptedText);
//                        System.out.println(msg);
//                    }
//                }
                if(msg.contains(secure_code)) {
                    msg = msg.replace(secure_code, "");
                    broadcastMessage(channel, msg);
                }else {
                    System.out.println("非法客户端" + channel.getRemoteAddress());
                    logger.error("非法客户端" + channel.getRemoteAddress());
                    key.cancel();
                    clientMap.remove(channel);
                }
                logger.debug(msg);
            } else {
                key.cancel();
                clientMap.remove(channel);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void broadcastMessage(SocketChannel sender, String msg) throws IOException {
        for (Map.Entry<SocketChannel, String> entry : clientMap.entrySet()) {
            SocketChannel channel = entry.getKey();
            ByteBuffer buffer = ByteBuffer.wrap((msg + "\n").getBytes());
            channel.write(buffer);
            //if (channel != sender) {
            //    ByteBuffer buffer = ByteBuffer.wrap((msg + "\n").getBytes());
            //    channel.write(buffer);
            //}
        }
    }
}