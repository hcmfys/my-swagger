package org.springbus.ws;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientRun  extends WebSocketClient  {

    public WebSocketClientRun(URI serverUri) {
        super(serverUri);
    }


    public static void main(String[] args) throws URISyntaxException {
        WebSocketClientRun webSocketClientRun=   new WebSocketClientRun(new URI("ws://127.0.0.1:8080/doMsg"));
        webSocketClientRun.connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println( " onOpen  " + handshakedata);
    }

    @Override
    public void onMessage(String message) {
        System.out.println( " recv:  " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( " onClose:  " + code  +" " +reason +" "+ remote);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println( " ex"+ ex.getMessage());
    }
}
