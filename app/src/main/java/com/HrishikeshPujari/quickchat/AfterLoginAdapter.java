package com.HrishikeshPujari.quickchat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AfterLoginAdapter extends BaseAdapter implements Filterable {
    private Activity mActivity;

    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;
    private DatabaseReference databaseref;

    private ArrayList<DataSnapshot> mSnapshotListfiltered;


    private ChildEventListener mListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(!snapshot.getValue(DisplayName.class).getDisplayName().equals(mDisplayName)){
            mSnapshotList.add(snapshot);
            mSnapshotListfiltered.add(snapshot);}

            Log.d("QuickChat","snapshot about user received "+snapshot);
            notifyDataSetChanged();
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
    };

    public AfterLoginAdapter(String displayName, Activity activity) {
        mActivity = activity;
        mDisplayName = displayName;
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        databaseref= database.getReference("users");
        String id=databaseref.push().getKey();
        databaseref.child(id);
        databaseref.addChildEventListener(mListener);

        mSnapshotList=new ArrayList<>();
        mSnapshotListfiltered=new ArrayList<>();
    }

    @Override
    public Filter getFilter() {
        Filter filter =new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                Log.d("constraint",constraint.toString());
                if(constraint==null){
                    filterResults.count=mSnapshotList.size();
                    filterResults.values=mSnapshotList;
                }else if(constraint.toString().isEmpty() || constraint.length()==0){
                    filterResults.count=mSnapshotList.size();
                    filterResults.values=mSnapshotList;
                }else{

                    String searchstr=constraint.toString().toLowerCase();
                    ArrayList<DataSnapshot> resultData=new ArrayList<>();
                    for(DataSnapshot dataSnapshot:mSnapshotList){

                        if (dataSnapshot.getValue(DisplayName.class).getDisplayName().toLowerCase().contains(searchstr) || dataSnapshot.getValue(DisplayName.class).getEmail().contains(searchstr)) {
                            resultData.add(dataSnapshot);
                        }}
                    filterResults.count=resultData.size();
                    filterResults.values=resultData;


                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mSnapshotList.clear();

                mSnapshotList= (ArrayList<DataSnapshot>) results.values;
                notifyDataSetChanged();


            }
        };
        return filter;
    }

    static class ViewHolder{
        TextView userName;
        TextView email;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public DisplayName getItem(int position) {
        DataSnapshot snapshot=mSnapshotList.get(position);
        return snapshot.getValue(DisplayName.class);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_list_displayusername, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.chatlist_displayname);
            holder.email = (TextView) convertView.findViewById(R.id.chatlist_email);
            convertView.setTag(holder);
        }
        final DisplayName listnames=getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        boolean isMe=listnames.getDisplayName().equals(mDisplayName);

        String author = listnames.getDisplayName();
        holder.userName.setText(author);
        String msg = listnames.getEmail();
        holder.email.setText(msg);



        return convertView;
    }

}
