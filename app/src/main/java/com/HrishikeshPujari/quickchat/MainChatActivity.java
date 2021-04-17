package com.HrishikeshPujari.quickchat;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private String Ref1;
    private String Ref2;
    private String mSenderEmail;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private String mReceiver_username;
    private String mReceiver_email;
    private String mReceiver_uid;
    private String mSender_uid;
    private TextView mDisplayReceiverUsername;
    private TextView mDisplayReceiverEmail;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        getDisplayName();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        mDatabaseReference= database.getReference();


        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);
        mDisplayReceiverUsername=(TextView)findViewById(R.id.displayname_mainchat);
        mDisplayReceiverEmail=(TextView)findViewById(R.id.email_mainchat);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });


        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void getDisplayName(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mDisplayName=user.getDisplayName();
        mSenderEmail=user.getEmail();
        Log.d("Quickchat","email:"+mSenderEmail);


        if(mDisplayName==null){
            mDisplayName="Anonymous";
        }
    }


    private void sendMessage() {
        Log.d("Flashchat","I sent something");
        String input=mInputText.getText().toString();
        if(!input.equals("")){
            InstantMessage chat=new InstantMessage(input,mDisplayName);

            mDatabaseReference.child("messages").child(Ref1).push().setValue(chat);
            mDatabaseReference.child("messages").child(Ref2).push().setValue(chat);

            mInputText.setText("");

        }


        // TODO: Grab the text the user typed in and push the message to Firebase

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.
    @Override
    public void onStart(){
        super.onStart();
        Intent chatIntent=getIntent();
        mReceiver_username=chatIntent.getStringExtra("Receiver_username");
        mReceiver_email=chatIntent.getStringExtra("Receiver_email");
        mReceiver_uid=chatIntent.getStringExtra("Receiver_uid");
        Log.d("Quickchat","Mainchat Receiver uid:"+mReceiver_uid);
        mDisplayReceiverUsername.setText(mReceiver_username);
        mDisplayReceiverEmail.setText(mReceiver_email);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mSender_uid=user.getUid();

        Ref1=mSender_uid+"_"+mReceiver_uid;
        Ref2=mReceiver_uid+"_"+mSender_uid;
        Log.d("Quickchat","ref1"+Ref1);
        Log.d("Quickchat","ref2"+Ref2);
        mAdapter=new ChatListAdapter(this,mDisplayName,Ref1);
        mChatListView.setAdapter(mAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myintent=new Intent(MainChatActivity.this,AfterLogin.class);
        startActivity(myintent);
    }

    @Override
    public void onStop() {
        super.onStop();


        // TODO: Remove the Firebase event listener on the adapter.

    }

}
