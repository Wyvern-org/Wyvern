package wyvern.util.pipes;

import java.net.Socket;

public interface SocketPipe
{
    void flush(Socket socket);
}
