import controller.ChatRoomController;
import io.NIOUtils;
import parser.reqeuest.RequestParser;
import parser.response.ResponseParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.concurrent.*;

public class ChatRoomMain {
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService executorService;
    private ChatRoomController controller;
    private NIOUtils nioUtils = new NIOUtils();

    public static void main(String[] args) {
        ChatRoomMain chatRoomMain = new ChatRoomMain();
        chatRoomMain.init();

        ChatRoomController controller = chatRoomMain.getController();
        while(true) {
            Optional<SocketChannel> socketChannelOpt =
                    chatRoomMain.accept(chatRoomMain.getServerSocketChannel());
            if(socketChannelOpt.isEmpty()) continue;

            try {
                chatRoomMain.processRequest(socketChannelOpt.get(), controller)
                        .get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
    private CompletableFuture<Void> processRequest(
            SocketChannel socketChannel, ChatRoomController controller){
        return CompletableFuture.supplyAsync(() -> nioUtils.read(socketChannel))
                .thenApply(msg -> RequestParser.parse(msg))
                .thenApply(controller::dispatch)
                .thenApply(ResponseParser::parse)
                .thenAccept(message -> nioUtils.write(message, socketChannel));
    }

    private void init() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", 9001));
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            controller = new ChatRoomController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<SocketChannel> accept(ServerSocketChannel serverSocketChannel) {
        return nioUtils.accept(serverSocketChannel);
    }

    public ChatRoomController getController() {
        return controller;
    }
    public ExecutorService getExecutorService() {
        return executorService;
    }
    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }
}
