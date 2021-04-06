// Android（サーバ側）

package com.example.android_port_test;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public abstract class MessageListener {

    private final int mPort;

    private final Handler mHandler;

    private final Runnable mAcceptTask = new Runnable() {
        public void run() {
            android.os.Debug.waitForDebugger();
            final ServerSocket serverSocket;

            try {
                serverSocket = new ServerSocket(mPort);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            final Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    serverSocket.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                final InputStream inputStream = socket.getInputStream();
                final String data = readAllLine(inputStream);

                mHandler.post(new Runnable() {
                    public void run() {
                        onReceivedData(data);
                    }
                });
            } catch (final IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public MessageListener(final int port) {
        mPort = port;
        mHandler = new Handler();
    }

    public void start() {
        try {
            Executors.newSingleThreadExecutor().execute(mAcceptTask);
        }catch (Exception e){

        }
    }

    protected abstract void onReceivedData(String data);

    private static String readAllLine(final InputStream inputStream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}