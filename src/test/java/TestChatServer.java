import com.google.gson.Gson;
import entity.ChatRoom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestChatServer {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel =
                    SocketChannel.open(new InetSocketAddress("localhost", 9001));
            Gson gson = new Gson();
            String body = gson.toJson(new ChatRoom(3,"hello",2));
            socketChannel.write(ByteBuffer.wrap(("POST /room\\n\\n"+body).getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(300);
            int reads = socketChannel.read(buffer);
            byte[] dst = new byte[reads];

            buffer.flip();
            buffer.get(dst);

            System.out.println(new String(dst));
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
