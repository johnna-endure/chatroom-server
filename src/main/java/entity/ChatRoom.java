package entity;

public class ChatRoom {
    private final int id;
    private final String name;
    private final int maxSize;
    private int currentSize;

    public ChatRoom(int id, String name, int maxSize) {
        this.id = id;
        this.name = name;
        this.maxSize = maxSize;
        this.currentSize = 0;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public ChatRoom setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
        return this;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxSize=" + maxSize +
                ", currentSize=" + currentSize +
                '}';
    }
}
