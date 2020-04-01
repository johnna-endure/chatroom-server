package handler;

import com.google.gson.Gson;
import database.ChatRoomDatabase;
import entity.ChatRoom;
import io.request.Method;
import io.request.Request;
import io.response.Response;

import java.util.List;
import java.util.Optional;

public class RequestHandler {
    ChatRoomDatabase database;
    Gson gson = new Gson();
    public void setDatabase(ChatRoomDatabase database) {
        this.database = database;
    }

    @RequestMapping(method = Method.GET, url = "/rooms")
    public Response getRooms(Request request) {
        List<ChatRoom> roomList = database.getRooms();

        Gson gson = new Gson();
        String responseBody = gson.toJson(roomList);

        return new Response(200, "success", responseBody);
    }

    @RequestMapping(method = Method.POST, url = "/room")
    public Response createRoom(Request request) {
        Optional<String> requestBodyOpt = request.getBodyOpt();
        if(requestBodyOpt.isEmpty())
            return new Response(400, "bad request", null);

        ChatRoom chatRoom = gson.fromJson(requestBodyOpt.get(), ChatRoom.class);
        database.createRoom(chatRoom);

        System.out.println(database.getRooms());
        return new Response(201, "created", null);
    }

    @RequestMapping(method = Method.GET, url = "/room/{id}")
    public Response canJoinRoom(Request request, int id) {
        ChatRoom chatRoom = database.getRoom(id);
        if(chatRoom.getCurrentSize() == chatRoom.getMaxSize()) {
            return new Response(200,
                    "room is full.",
                    "{\"canJoin\":false}");
        }
        return new Response(200,
                "this room is available.",
                "{\"canJoin\":true}");
    }
}
