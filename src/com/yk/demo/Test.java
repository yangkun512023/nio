package com.yk.demo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        ServerSocketChannel serverChannel;
        Selector selector;
        try {
            // 打开监听信道,ServerSocketChannel最终实现的是channel接口
            serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket(); // 创建一个服务端socket
            InetSocketAddress address = new InetSocketAddress(7456);
            serverSocket.bind(address); // 绑定IP及端口
            serverChannel.configureBlocking(false);// 设置为非阻塞方式,如果为true 那么就为传统的阻塞方式
            selector = Selector.open();// 静态方法 实例化selector 创建选择器
            /*如果你注册不止一种事件，那么可以用“位或”操作符将常量连接起来
            SelectionKey.OP_READ |SelectionKey.OP_WRITE;*/
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端启动......");
            while (true) {
                // 等待某信道就绪(或超时) 监听注册通道，当其中有注册的 IO操作可以进行时，该函数返回
                // 并将对应的SelectionKey加入selected-key
                if (selector.select(3000) == 0) {
                    System.out.println("重新等待");
                    continue;
                }
                // selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                // Selected-key代表了所有通过select()方法监测到可以进行IO操作的channel
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {// 对每个信道进行一次循环，查看各个信道是否有事件需要处理
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) { // 有客户端连接请求时
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("接收到连接:" + client);
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector,
                                SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);// 分配一个新的1024字节的缓冲区
                        clientKey.attach(buffer);// 将给定的缓冲区附加到此键
                    }
                    if (key.isReadable()) {// 判断是否有数据发送过来
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        buffer.clear();
                        // 读取信息获得读取的字节数
                        long bytesRead = client.read(buffer);
                        if (bytesRead == -1) {
                            // 没有读取到内容的情况
                            client.close();
                        } else {
                            // 将缓冲区准备为数据传出状态
                            buffer.flip();
                            // 将字节转化为为UTF-8的字符串
                            String receivedString = Charset.forName("UTF-8").newDecoder()
                                    .decode(buffer).toString();
                            // 控制台打印出来
                            System.out.println(client.socket().getRemoteSocketAddress());
                            System.out.println("信息内容:" + receivedString);
                            // 准备发送的文本
                            String sendString = "你好,已经收到你的信息" + receivedString;
                            // 将byte数组包装到缓冲区中
                            buffer = ByteBuffer.wrap(sendString.getBytes("UTF-8"));
                            // 将字节序列从给定的缓冲区中写入此通道,并返回给客户端
                            client.write(buffer);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

