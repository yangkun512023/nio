package com.yk.demo;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ChannelClientReadThread extends Thread {
    private Selector selector;

    public ChannelClientReadThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (selector.select() > 0) {
                // 遍历每个有可用IO操作Channel对应的SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isReadable()) {
                        // 使用NIO读取Channel中的数据
                        SocketChannel sc = (SocketChannel) sk.channel();// 获取通道信息
                        ByteBuffer buffer = ByteBuffer.allocate(1024);// 分配缓冲区大小
                        sc.read(buffer);// 读取通道里面的数据放在缓冲区内
                        buffer.flip();// 调用此方法为一系列通道写入或相对获取 操作做好准备
                        // 将字节转化为为UTF-16的字符串
                        String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer)
                                .toString();
                        // 控制台打印返回信息
                        System.out.println("服务器:" + sc.socket().getRemoteSocketAddress());
                        System.out.println("信息内容:" + receivedString);
                        // 为下一次读取作准备
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                    // 删除正在处理的SelectionKey
                    selector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
