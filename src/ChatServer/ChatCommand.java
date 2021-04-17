package ChatServer;

import ChatServer.Messages.Message;

public interface ChatCommand {
    void execute(Message msg);
}
