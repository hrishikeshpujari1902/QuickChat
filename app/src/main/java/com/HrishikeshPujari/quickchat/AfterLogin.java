package com.HrishikeshPujari.quickchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AfterLogin extends AppCompatActivity {
    private String mDisplayName;
    private ListView mListDisplayView;
    private String mEmail;
    private String User_UID;


    private DatabaseReference mDatabaseReference;
    private AfterLoginAdapter mAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Log.d("Quickchat","Oncreate() executed.");
        getDisplayName();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        mDatabaseReference= database.getReference("users");

        mListDisplayView=(ListView)findViewById(R.id.chat_list_list);
        SearchView searchView = (SearchView) findViewById(R.id.search_bar);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        mListDisplayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DisplayName displayName =mAdapter.getItem(position);
                Intent chatIntent=new Intent(AfterLogin.this,MainChatActivity.class);
                chatIntent.putExtra("Sender",mDisplayName);
                chatIntent.putExtra("Receiver_username",displayName.getDisplayName());
                chatIntent.putExtra("Receiver_email",displayName.getEmail());
                chatIntent.putExtra("Receiver_uid",displayName.getUid());
                startActivity(chatIntent);


            }
        });


    }

    private void getDisplayName(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mDisplayName=user.getDisplayName();



        if(mDisplayName==null){
            mDisplayName="Anonymous";
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d("Quickchat","Onstart() executed");
        mAdapter=new AfterLoginAdapter(mDisplayName,this);
        mListDisplayView.setAdapter(mAdapter);
    }
}
