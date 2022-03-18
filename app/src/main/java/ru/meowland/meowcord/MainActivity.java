package ru.meowland.meowcord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    public static int MAX_LENGTH_MESSAGE = 1500;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    RecyclerView MessageRecycler;
    EditText EditTextMessage;
    Button SendButton;

    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SendButton = findViewById(R.id.send_message);
        EditTextMessage = findViewById(R.id.message_input);
        MessageRecycler = findViewById(R.id.messages_recucler);

        MessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        DateAdapter dateAdapter = new DateAdapter(this,messages);
        MessageRecycler.setAdapter(dateAdapter);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = EditTextMessage.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(getApplicationContext(), "Input message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length()>MAX_LENGTH_MESSAGE){
                    Toast.makeText(getApplicationContext(), "max message length: "+MAX_LENGTH_MESSAGE, Toast.LENGTH_SHORT).show();
                    return;
                }
                myRef.push().setValue(msg);
                EditTextMessage.setText("");
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg = snapshot.getValue(String.class);
                messages.add(msg);
                dateAdapter.notifyDataSetChanged();
                MessageRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}