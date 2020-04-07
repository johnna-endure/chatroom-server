package database;

import com.google.common.collect.Lists;
import entity.ChatRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ChatRoomDatabase {
    private int idCounter;
    private List<ChatRoom> chatRoomList;

    private static final Logger logger = LogManager.getLogger(ChatRoomDatabase.class);

    public ChatRoomDatabase() {
        idCounter = 0;
        chatRoomList = new CopyOnWriteArrayList<>();
    }

    /**
     * 테스트에만 사용되어야 함.
     * 초기에 항상 비어있는 데이터베이스를 제공함.
     * @param settingData 데이터를 세팅하는 람다
     * @return
     */
    public ChatRoomDatabase setDataForTestOnly(Consumer<List<ChatRoom>> settingData) {
        chatRoomList = new ArrayList<>();
        settingData.accept(chatRoomList);
        logger.debug("[setDataForTestOnly] chatRoomList  "+ chatRoomList);
        return this;
    }

    public synchronized ChatRoom createRoom(ChatRoom chatRoom){
        chatRoom.setId(idCounter++);
        chatRoomList.add(chatRoom);
        return chatRoom;
    }

    public synchronized boolean deleteRoom(ChatRoom chatRoom) {
        return chatRoomList.remove(chatRoom);
    }

    public Optional<ChatRoom> getRoom(int id) {
        return chatRoomList.stream().filter(room -> room.getId() == id)
                .findFirst();
    }

    public List<ChatRoom> getRooms() {
        return List.copyOf(chatRoomList);
    }
}
