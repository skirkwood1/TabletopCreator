package ChatServer;

import ChatServer.Messages.Message;

public interface MessageTypeExecutor {
    void execute(Message msg);
}
