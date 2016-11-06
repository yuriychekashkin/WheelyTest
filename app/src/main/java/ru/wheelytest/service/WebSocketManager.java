package ru.wheelytest.service;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import ru.wheelytest.business.WebSocketMessagesConverter;
import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketManager implements WebSocketListener {

    private static final int NORMAL_CLOSURE = 1000;
    private static final String REASON_CLOSING_CONNECTION = "Closing connection";

    private Request request;
    private final OkHttpClient httpClient;
    private final WebSocketMessageListener socketListener;

    private final WebSocketMessagesConverter parser;
    private WebSocket webSocket;

    public WebSocketManager(@NonNull OkHttpClient httpClient, @NonNull WebSocketMessagesConverter messagesConverter, @NonNull WebSocketMessageListener socketListener) {
        this.httpClient = httpClient;
        this.parser = messagesConverter;
        this.socketListener = socketListener;
    }

    public void send(GpsPoint gpsPoint) throws Exception {
        String message = parser.serialize(gpsPoint);
        webSocket.sendMessage(RequestBody.create(WebSocket.TEXT, message));
    }

    public void tryConnect(String webSocketUrl) {
        request = new Request.Builder()
                .url(webSocketUrl)
                .build();
        connect();
    }

    public void disconnect() {
        try {
            if (webSocket != null) {
                webSocket.close(NORMAL_CLOSURE, REASON_CLOSING_CONNECTION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
        socketListener.onSuccessConnection();
    }

    @Override
    public void onFailure(IOException e, Response response) {
        if (response != null && response.body() != null) {
            response.body().close();
        }
        httpClient.dispatcher().cancelAll();
        httpClient.connectionPool().evictAll();
        socketListener.onFailedConnection();
    }

    @Override
    public void onMessage(ResponseBody response) throws IOException {
        List<GpsPoint> points = parser.deserialize(response.string());
        socketListener.onPointsReceived(points);
    }

    @Override
    public void onPong(Buffer payload) {
        // do nothing
    }

    @Override
    public void onClose(int code, String reason) {
        webSocket = null;
        connect();
    }

    private void connect() {
        WebSocketCall.create(httpClient, request).enqueue(this);
    }

    public interface WebSocketMessageListener {
        void onSuccessConnection();

        void onFailedConnection();

        void onPointsReceived(List<GpsPoint> gpsPoints);
    }
}
