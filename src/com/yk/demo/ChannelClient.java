package com.yk.demo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChannelClient {
    // 信道选择器
    Selector selector;
    // 与服务器通信的信道
    SocketChannel socketChannel;
    // 要连接的服务器Ip地址
    String hostIp;
    // 要连接的远程服务器在监听的端口
    int hostListenningPort;

    public ChannelClient(String HostIp, int HostListenningPort) throws IOException {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;
        // 打开监听信道并设置为非阻塞模式
        socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostListenningPort));
        socketChannel.configureBlocking(false);

        // 打开并注册选择器(监听读)到信道
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 启动读取线程
        ChannelClientReadThread ccrt = new ChannelClientReadThread(selector);
        ccrt.start();
    }

    // 发送字符串到服务器
    public void sendMsg(String message) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        socketChannel.write(writeBuffer);
    }


    public static void main(String[] args) throws IOException {
        ChannelClient client = new ChannelClient("localhost", 7456);
        try {
            client.sendMsg("我是客户端");
            while (true) {
                Scanner scan = new Scanner(System.in);// 等待键盘输入数据
                String string = scan.nextLine();
                client.sendMsg(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
