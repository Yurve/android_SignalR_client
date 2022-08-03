package com.sample.mysignalr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;
    HubConnection hubConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        final Handler handler = new Handler();

        //signal R은 hub 를 통해서 송수신함 그래서 허브만들기
        String input = "http://****당신의 주소를 적으세요 ****";
        hubConnection = HubConnectionBuilder.create(input).build();

        //만든 허브 실행
        //안드로이드 9 이상부터는 http 주소를 그냥 막아버림 따라서 매니페스트에 어플부분에 android:usesCleartextTraffic="true" 를 추가해야 한다.
        hubConnection.start().blockingAwait();


        //버튼을 누르면 text 전송
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연결이 되어있다면 전송함.
                if (hubConnection.getConnectionState() != HubConnectionState.DISCONNECTED) {

                    String user = "android";
                    String message = editText.getText().toString();
                    editText.setText("");
                    hubConnection.send("SendMessage", user, message);
                } else {
                    Toast.makeText(getApplicationContext(), "전송 중 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }
        });



        hubConnection.on("ReceiveMessage", (user, message) -> {
            textView.append(user + " : " + message + "\n");
        }, String.class, String.class);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hubConnection.stop();

    }


}