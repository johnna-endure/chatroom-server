import com.google.gson.Gson;
import entity.ChatRoom;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class TestPlayground {
    @Test
    public void test() {
        List<ChatRoom> l = new ArrayList<>();
        l.add(new ChatRoom(1,"1",2));
        l.add(new ChatRoom(2,"223",2));

        l.remove(new ChatRoom(1,"",3));
        System.out.println(l);
    }
}
