package database;

import entity.ChatRoom;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ChatRoomDatabase {
    private List<ChatRoom> chatRoomList;

    public ChatRoomDatabase() {
        chatRoomList = new CopyOnWriteArrayList<>();
//        chatRoomList.add(new ChatRoom(1, "room1",2).setCurrentSize(1));
//        chatRoomList.add(new ChatRoom(2, "room2",2).setCurrentSize(2));
    }

    public ChatRoomDatabase setDataForTestOnly(Consumer<List<ChatRoom>> consumer) {
        consumer.accept(chatRoomList);
        return this;
    }

    public synchronized void createRoom(ChatRoom chatRoom){
        chatRoomList.add(chatRoom);
    }

    public synchronized void deleteRoom(ChatRoom chatRoom) {
        chatRoomList.remove(chatRoom);
    }

    public ChatRoom getRoom(int id) {
        return chatRoomList.get(id);
    }

    public List<ChatRoom> getRooms() {
        return List.copyOf(chatRoomList);
    }
}
