package handler;

import com.google.gson.Gson;
import database.ChatRoomDatabase;
import entity.ChatRoom;
import io.request.Method;
import io.request.Request;
import io.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RequestHandler {
    private ChatRoomDatabase database;
    private Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    public RequestHandler setDatabase(ChatRoomDatabase database) {
        this.database = database;
        return this;
    }

    @RequestMapping(method = Method.GET, url = "/room/{id}")
    public Response getRoom(int id){
        logger.info("[getRoom] Request[method = GET, url = /room/{} ", id);
        Optional<ChatRoom> chatRoomOpt = database.getRoom(id);
        if(chatRoomOpt.isEmpty()){
            logger.info("[getRoom] no room");
            return new Response(200, "no room corresponds to this id");
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        logger.info("[getRoom] finding room success.");
        logger.debug("[getRoom] ChatRoom : " + chatRoom);
        return  new Response(200, "success", gson.toJson(chatRoom));
    }

    @RequestMapping(method = Method.GET, url = "/rooms")
    public Response getRooms() {
        logger.info("[getRooms] Request[method = GET, url = /rooms ");
        List<ChatRoom> roomList = database.getRooms();
        logger.debug("[getRooms] room list : " + roomList);

        return new Response(200, "success", gson.toJson(roomList));
    }

    @RequestMapping(method = Method.POST, url = "/room")
    public Response createRoom(Request request) {
        logger.info("[createRoom] Request[method = POST, url = /room");
        Optional<String> requestBodyOpt = request.getBodyOpt();
        if(requestBodyOpt.isEmpty()) {
            logger.info("[createRoom] bad request. create failed.");
            return new Response(400, "bad request");
        }
        ChatRoom chatRoom = gson.fromJson(requestBodyOpt.get(), ChatRoom.class);
        logger.info("[createRoom] create success");
        return new Response(201, "created",
                gson.toJson(database.createRoom(chatRoom)));
    }

    @RequestMapping(method = Method.POST, url = "/room/{id}")
    public Response joinRoom(int id) {
        logger.info("[joinRoom] Request[method = POST, url = /room/{}",id);
        Optional<ChatRoom> chatRoomOpt = database.getRoom(id);
        if(chatRoomOpt.isEmpty()){
            logger.info("[joinRoom] no room corresponds to id");
            return new Response(200, "no room corresponds to id");
        }

        ChatRoom chatRoom = chatRoomOpt.get();
        if(chatRoom.getCurrentSize() == chatRoom.getMaxSize()) {
            logger.info("[joinRoom] room is full");
            return new Response(200, "room is full");
        }
        chatRoom.setCurrentSize(chatRoom.getCurrentSize()+1);
        logger.info("[joinRoom] join success. ChatRoom : " + chatRoom);
        return new Response(200,
                "success", gson.toJson(chatRoom));
    }

    @RequestMapping(method = Method.DELETE, url = "/room/{id}")
    public Response deleteRoom(int id) {
        logger.info("[deleteRoom] method = DELETE, url = /room/{}", id);
        Optional<ChatRoom> chatRoomOpt = database.getRoom(id);
        if(chatRoomOpt.isEmpty()){
            logger.info("[deleteRoom] deletion failed. no room correspond to id");
            return new Response(200,"no room correspond to id");
        }
        boolean isDeleted = database.deleteRoom(chatRoomOpt.get());
        logger.info("[deleteRoom] deletion flag value::isDeleted = {}", isDeleted);
        return isDeleted ? new Response(200, "deletion success") :
                new Response(200, "deletion failed");
    }
}
