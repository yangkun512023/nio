package com.yk.demo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChannelClient {
    // �ŵ�ѡ����
    Selector selector;
    // �������ͨ�ŵ��ŵ�
    SocketChannel socketChannel;
    // Ҫ���ӵķ�����Ip��ַ
    String hostIp;
    // Ҫ���ӵ�Զ�̷������ڼ����Ķ˿�
    int hostListenningPort;

    public ChannelClient(String HostIp, int HostListenningPort) throws IOException {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;
        // �򿪼����ŵ�������Ϊ������ģʽ
        socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostListenningPort));
        socketChannel.configureBlocking(false);

        // �򿪲�ע��ѡ����(������)���ŵ�
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        // ������ȡ�߳�
        ChannelClientReadThread ccrt = new ChannelClientReadThread(selector);
        ccrt.start();
    }

    // �����ַ�����������
    public void sendMsg(String message) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        socketChannel.write(writeBuffer);
    }


    public static void main(String[] args) throws IOException {
        ChannelClient client = new ChannelClient("localhost", 7456);
        try {
            client.sendMsg("���ǿͻ���");
            while (true) {
                Scanner scan = new Scanner(System.in);// �ȴ�������������
                String string = scan.nextLine();
                client.sendMsg(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
