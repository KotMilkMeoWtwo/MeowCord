package ru.meowland.meowcord;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static int SING_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FloatingActionButton sendButton;
    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendButton = findViewById(R.id.buttonSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textField = findViewById(R.id.messageField);
                if(textField.getText().toString().equals("")){
                    return;
                }
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        textField.getText().toString()
                    )
                );
                textField.setText("");
            }

        });

        if(requestCode == SING_IN_CODE){
            if(requestCode == RESULT_OK){
                Snackbar.make(activity_main, "You sing in now", Snackbar.LENGTH_SHORT).show();
                displayALLMessages();
            }else {
                Snackbar.make(activity_main, "You not sing in now", Snackbar.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = findViewById(R.id.main_activity);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SING_IN_CODE);
        }
        else{
            Snackbar.make(activity_main, "You sing in now", Snackbar.LENGTH_SHORT).show();

            displayALLMessages();
        }
    }

    private void displayALLMessages() {
        ListView listMessages = findViewById(R.id.messages_list);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()){
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getNickName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));
            }
        };

        listMessages.setAdapter(adapter);
    }
    /*
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

     */

}