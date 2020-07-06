package org.springbus.ws;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServerRun extends  WebSocketServer {
    private static ConcurrentHashMap<Integer, WebSocket> onlineClientList=new ConcurrentHashMap<>();

    public WebSocketServerRun() {
        super();
    }

    public WebSocketServerRun(InetSocketAddress address) {
        super(address);
    }

    public static void main(String[] args) {
        WebSocketServerRun webSocketServerRun=
                new WebSocketServerRun(new InetSocketAddress("127.0.0.1", 8080));
        webSocketServerRun.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println( "open :"+conn.getLocalSocketAddress());
        onlineClientList.put( conn.hashCode(), conn);
        conn.send("hello websocket client");
        broadcastMsg(conn.getLocalSocketAddress()+"服务上线了");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("close :" + conn);
        onlineClientList.remove(conn.hashCode());
        broadcastMsg(conn.getLocalSocketAddress()+"服务下线了");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println( "recv :"+message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
    private  void broadcastMsg(String msg) {
        Collection<WebSocket>  users= onlineClientList.values();
        users.stream().forEach( item->{
            item.send(msg);
        });
    }

    @Override
    public void onStart() {
        System.out.println(" server started ");
    }
}
