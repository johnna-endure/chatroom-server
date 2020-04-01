package integration;

import database.ChatRoomDatabase;
import entity.ChatRoom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/*
method = Method.GET, url = "/room/{id}" 테스트
 */
public class TestCanJoinRoom {
    SocketChannel dummyClient;
    ChatRoomDatabase dummyDatabase; // 테스트 메서드마다 setDataForTestOnly 메서드 이용해서 데이터 초기화할 것.

    @Before
    public void init() {
        try {
            dummyClient = SocketChannel.open(new InetSocketAddress("localhost", 9001));
            dummyDatabase = new ChatRoomDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCanJoinRoom_WhenRoomIsFull() {
        dummyDatabase.setDataForTestOnly(
                list -> list.add(new ChatRoom(1, "testRoom", 2).setCurrentSize(2)));
//        dummyClient.
        System.out.println(dummyDatabase.getRooms());
    }
}
