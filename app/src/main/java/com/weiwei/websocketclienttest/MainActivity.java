package com.weiwei.websocketclienttest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class MainActivity extends AppCompatActivity {

  private Button mButtonConnect;
  private Button mButtonSubmit;
  private RecyclerView mRecyclerView;
  private ListAdapter mAdapter;

  private static WebSocketClient mWebSocketClient;

  private boolean mConnectStatus = false;

  private String mUserName = "willy";
  private String mDefaultMsg = "{\"content\":\"香港3345678\"}";

  private Handler mHandler = new Handler(new Handler.Callback() {
    public boolean handleMessage(Message msg) {
      mAdapter.appendMsg(msg.obj.toString());
      mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
      return true;
    }
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
  }

  private void initView() {
    mRecyclerView = findViewById(R.id.recyclerView);

    mAdapter = new ListAdapter();
    mRecyclerView.setAdapter(mAdapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);

    mButtonConnect = findViewById(R.id.button_connect);
    mButtonConnect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mConnectStatus) {
          disconnect();
        } else {
          connect();
        }
        mConnectStatus = !mConnectStatus;
      }
    });

    mButtonSubmit = findViewById(R.id.button_submit);
    mButtonSubmit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mWebSocketClient.send(mDefaultMsg);
      }
    });
  }

  private void connect(){
    try {
      mWebSocketClient = new WebSocketClient(
          new URI("ws://192.168.1.80:8081/chat/" + mUserName),
          new Draft_6455() {},null,100000) {

        @Override
        public void onOpen(ServerHandshake handshakedata) {
          Log.d("ws", "onOpen");
        }

        @Override
        public void onMessage(String message) {
          Message handlerMessage = Message.obtain();
          handlerMessage.obj = message;
          mHandler.sendMessage(handlerMessage);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {}

        @Override
        public void onClosing(int code, String reason, boolean remote) {
          super.onClosing(code, reason, remote);
        }

        @Override
        public void onError(Exception ex) {
          Log.d("ws", "onError : " + ex.getMessage());
        }
      };
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    mWebSocketClient.connect();
    mButtonSubmit.setEnabled(true);
  }

  protected void disconnect() {
    if (mWebSocketClient != null)
      mWebSocketClient.close();
    mButtonSubmit.setEnabled(false);
  }
}
