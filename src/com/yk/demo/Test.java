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
            // �򿪼����ŵ�,ServerSocketChannel����ʵ�ֵ���channel�ӿ�
            serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket(); // ����һ�������socket
            InetSocketAddress address = new InetSocketAddress(7456);
            serverSocket.bind(address); // ��IP���˿�
            serverChannel.configureBlocking(false);// ����Ϊ��������ʽ,���Ϊtrue ��ô��Ϊ��ͳ��������ʽ
            selector = Selector.open();// ��̬���� ʵ����selector ����ѡ����
            /*�����ע�᲻ֹһ���¼�����ô�����á�λ�򡱲�������������������
            SelectionKey.OP_READ |SelectionKey.OP_WRITE;*/
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("���������......");
            while (true) {
                // �ȴ�ĳ�ŵ�����(��ʱ) ����ע��ͨ������������ע��� IO�������Խ���ʱ���ú�������
                // ������Ӧ��SelectionKey����selected-key
                if (selector.select(3000) == 0) {
                    System.out.println("���µȴ�");
                    continue;
                }
                // selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                // Selected-key����������ͨ��select()������⵽���Խ���IO������channel
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {// ��ÿ���ŵ�����һ��ѭ�����鿴�����ŵ��Ƿ����¼���Ҫ����
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) { // �пͻ�����������ʱ
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("���յ�����:" + client);
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector,
                                SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);// ����һ���µ�1024�ֽڵĻ�����
                        clientKey.attach(buffer);// �������Ļ��������ӵ��˼�
                    }
                    if (key.isReadable()) {// �ж��Ƿ������ݷ��͹���
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        buffer.clear();
                        // ��ȡ��Ϣ��ö�ȡ���ֽ���
                        long bytesRead = client.read(buffer);
                        if (bytesRead == -1) {
                            // û�ж�ȡ�����ݵ����
                            client.close();
                        } else {
                            // ��������׼��Ϊ���ݴ���״̬
                            buffer.flip();
                            // ���ֽ�ת��ΪΪUTF-8���ַ���
                            String receivedString = Charset.forName("UTF-8").newDecoder()
                                    .decode(buffer).toString();
                            // ����̨��ӡ����
                            System.out.println(client.socket().getRemoteSocketAddress());
                            System.out.println("��Ϣ����:" + receivedString);
                            // ׼�����͵��ı�
                            String sendString = "���,�Ѿ��յ������Ϣ" + receivedString;
                            // ��byte�����װ����������
                            buffer = ByteBuffer.wrap(sendString.getBytes("UTF-8"));
                            // ���ֽ����дӸ����Ļ�������д���ͨ��,�����ظ��ͻ���
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

