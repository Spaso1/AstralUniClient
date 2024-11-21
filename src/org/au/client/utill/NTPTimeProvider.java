package org.au.client.utill;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class NTPTimeProvider {

    private static final int NTP_PORT = 123; // NTP默认端口
    private static final int NTP_PACKET_SIZE = 48; // NTP数据包大小
    private static final long NTP_EPOCH_OFFSET = 2208988800L; // 1970年1月1日到1900年1月1日的秒数差
    private List<String> ntpServers;

    public NTPTimeProvider(List<String> ntpServers) {
        this.ntpServers = ntpServers;
    }

    public long getTime() {
        for (String server : ntpServers) {
            try {
                long time = getNtpTime(server);
                if (time > 0) {
                    return time;
                }
            } catch (IOException e) {
                // 忽略异常，尝试下一个服务器
            }
        }
        throw new RuntimeException("无法从任何NTP服务器获取时间");
    }

    private long getNtpTime(String ntpServer) throws IOException {
        byte[] buffer = new byte[NTP_PACKET_SIZE];
        DatagramSocket socket = null;
        try {
            InetAddress address = InetAddress.getByName(ntpServer);
            socket = new DatagramSocket();
            socket.setSoTimeout(5000); // 设置超时时间

            // 构建NTP请求数据包
            buffer[0] = 0x1B; // LI = 0, VN = 4, Mode = 3 (Client Mode)
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

            // 发送请求
            socket.send(request);

            // 接收响应
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            // 解析响应中的时间戳
            long seconds = ((long) (buffer[40] & 0xFF) << 24) |
                    ((long) (buffer[41] & 0xFF) << 16) |
                    ((long) (buffer[42] & 0xFF) << 8) |
                    (long) (buffer[43] & 0xFF);

            long fraction = ((long) (buffer[44] & 0xFF) << 24) |
                    ((long) (buffer[45] & 0xFF) << 16) |
                    ((long) (buffer[46] & 0xFF) << 8) |
                    (long) (buffer[47] & 0xFF);

            // 将NTP时间转换为Unix时间
            long milliseconds = (seconds - NTP_EPOCH_OFFSET) * 1000L + (fraction * 1000L / 0x100000000L);
            return milliseconds;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
