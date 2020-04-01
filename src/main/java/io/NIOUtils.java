package io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class NIOUtils {
    public Optional<SocketChannel> accept(ServerSocketChannel serverSocketChannel) {
        try {
            return Optional.of(serverSocketChannel.accept());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void write(String message, SocketChannel socketChannel) {
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
버퍼 넘는 경우도 모두 read 할 수 있도록 수정 필요.
 */
    public String read(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(200);
        int reads = 0;

        try { reads = socketChannel.read(buffer); }
        catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        byte[] dst = new byte[reads];
        buffer.get(dst);
        String msg = new String(dst);
        System.out.println("[서버] 클라이언트에게 받은 메세지 : "+ msg);
        return msg;
    }
}
