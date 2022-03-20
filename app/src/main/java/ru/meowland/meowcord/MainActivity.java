package ru.meowland.meowcord;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static  int SING_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SING_IN_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(activity_main, "You sing in now", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
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

        activity_main = findViewById(R.id.activity_main);
        sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textField = findViewById(R.id.message_field);
                if(textField.getText().toString().equals("") || textField.getText().toString().equals(" ") || textField.getText().toString().equals("  ") || textField.getText().toString().equals("   ") || textField.getText().toString().equals("    ")){
                    Snackbar.make(activity_main, "Message is not null", Snackbar.LENGTH_SHORT).show();
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

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SING_IN_CODE);
        }else {
            Snackbar.make(activity_main, "You sing in now", Snackbar.LENGTH_SHORT).show();

            displayAllMessages();
        }
    }

    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getNickName());
                mess_time.setText(DateFormat.format("dd-mm-yy HH:mm:ss", model.getMessageTime()));
                mess_text.setText(model.getTextMessage());
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}