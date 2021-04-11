package com.HrishikeshPujari.quickchat;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class AfterLogin extends AppCompatActivity {
    private String mDisplayName;
    private ListView mChatDisplayView;
    private String mEmail;
    private AutoCompleteTextView mInputText;
    private ImageButton mSearchButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);


    }
    private void getDisplayName(){

    }
}
