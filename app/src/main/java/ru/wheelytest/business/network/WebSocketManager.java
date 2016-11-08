package ru.wheelytest.business.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import ru.wheelytest.business.serialization.WebSocketMessagesConverter;
import ru.wheelytest.domain.entity.GpsPoint;
import ru.wheelytest.service.WebSocketService;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketManager implements RPCManager, WebSocketListener {

    private static final int NORMAL_CLOSURE = 1000;
    private static final String REASON_CLOSING_CONNECTION = "Closing connection";
    private static final long RECONNECT_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(5);

    private final OkHttpClient httpClient;
    private final WebSocketMessageListener socketListener;
    private final WebSocketMessagesConverter parser;

    private Request request;
    private WebSocket webSocket;
    private Runnable reconnectionRunnable = new Runnable() {
        @Override
        public void run() {
            connect();
        }
    };

    public WebSocketManager(@NonNull OkHttpClient httpClient, @NonNull WebSocketMessagesConverter messagesConverter, @NonNull WebSocketMessageListener socketListener) {
        this.httpClient = httpClient;
        this.parser = messagesConverter;
        this.socketListener = socketListener;
    }

    @Override
    public void send(GpsPoint gpsPoint) {
        if (isConnected()) {
            try {
                String message = parser.serialize(gpsPoint);
                webSocket.sendMessage(RequestBody.create(WebSocket.TEXT, message));
            } catch (Exception e) {
                reconnect();
            }
        }
    }

    @Override
    public void tryConnect(String url) {
        if (!isConnected()) {
            request = new Request.Builder()
                    .url(url)
                    .build();
            connect();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (isConnected()) {
                webSocket.close(NORMAL_CLOSURE, REASON_CLOSING_CONNECTION);
                webSocket = null;
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
        webSocket = null;

        httpClient.dispatcher().cancelAll();
        httpClient.connectionPool().evictAll();
        socketListener.onFailedConnection();

        reconnect();
    }

    @Override
    public void onMessage(ResponseBody response) throws IOException {
        Log.d(WebSocketService.TAG, "Response");
        List<GpsPoint> points = parser.deserialize(response.string());
        socketListener.onPointsReceived(points);
    }

    @Override
    public void onPong(Buffer payload) {
        // do nothing
    }

    @Override
    public void onClose(int code, String reason) {
        if (code != NORMAL_CLOSURE) {
            webSocket = null;
            reconnect();
        }
    }

    private void connect() {
        if (!isConnected()) {
            WebSocketCall.create(httpClient, request).enqueue(this);
        }
    }

    private void reconnect() {
        new Handler(Looper.getMainLooper()).postDelayed(reconnectionRunnable, RECONNECT_DELAY_MILLIS);
    }

    @Override
    public boolean isConnected() {
        return webSocket != null;
    }
}
