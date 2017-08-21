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
                // ����ÿ���п���IO����Channel��Ӧ��SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    // �����SelectionKey��Ӧ��Channel���пɶ�������
                    if (sk.isReadable()) {
                        // ʹ��NIO��ȡChannel�е�����
                        SocketChannel sc = (SocketChannel) sk.channel();// ��ȡͨ����Ϣ
                        ByteBuffer buffer = ByteBuffer.allocate(1024);// ���仺������С
                        sc.read(buffer);// ��ȡͨ����������ݷ��ڻ�������
                        buffer.flip();// ���ô˷���Ϊһϵ��ͨ��д�����Ի�ȡ ��������׼��
                        // ���ֽ�ת��ΪΪUTF-16���ַ���
                        String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer)
                                .toString();
                        // ����̨��ӡ������Ϣ
                        System.out.println("������:" + sc.socket().getRemoteSocketAddress());
                        System.out.println("��Ϣ����:" + receivedString);
                        // Ϊ��һ�ζ�ȡ��׼��
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                    // ɾ�����ڴ����SelectionKey
                    selector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
