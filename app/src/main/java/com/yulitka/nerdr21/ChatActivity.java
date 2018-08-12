package com.yulitka.nerdr21;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

public class ChatActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout chatActivity;
    FloatingActionButton fab;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Snackbar.make(chatActivity,"Successfully signed in",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else {
                Snackbar.make(chatActivity,"Error signing in",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivity = (RelativeLayout) findViewById(R.id.chatActivity);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText input = (EditText) findViewById(R.id.message);
//                FirebaseDatabase.getInstance().getReference("messages").push().setValue(new ChatMessage(input.getText().toString(),
//                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//                input.setText("");
//            }
//        });

        // Check sign in
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // redirect to sign in
        }
        else {
            Snackbar.make(chatActivity,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();

        }

        // Load content
        displayChatMessage();
    }

    private void displayChatMessage() {
        ListView listOfMessages = (ListView) findViewById(R.id.messageList);


//    Query ref = new Query()
//            FirebaseDatabase.getInstance().getReference().
//////        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {
//          adapter = new FirebaseListAdapter<ChatMessage>(ChatActivity.this,ChatMessage.class, R.layout.list_item,ref) {
//              @Override
//              protected void populateView(View view, ChatMessage chatMessage) {
//
//              }
//          }


        TextView messageText, messageUser,messageTime;
        messageText =(TextView) findViewById(R.id.messageText);
        messageUser =(TextView) findViewById(R.id.messageUser);
        messageTime =(TextView) findViewById(R.id.messageTime);

        messageText.setText("message");

        messageUser.setText("user");
//        messageTime.setText(android.text.format.DateFormat.format("dd-MM-yyy (HH:mm:ss),","12-08-2018 16:30:00"));
    }
}
