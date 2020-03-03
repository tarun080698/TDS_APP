package com.example.tds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tds.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    public String id;
    public String uid;
    TextView menuOptions;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Data> options;
    FirebaseRecyclerAdapter<Data, MyViewHolder> adapter;

    boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//Toolbar work here
        Toolbar tlBar = findViewById(R.id.toolbar);
        setSupportActionBar(tlBar);
        tlBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tool_title));

        Objects.requireNonNull(getSupportActionBar()).setTitle("To-Do");

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData();
            }
        });
//        menuOptions = findViewById(R.id.menuOptions);
//------------------------------------------------------------------------------
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        uid = mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All Data").child(uid);
        recyclerView = findViewById(R.id.recyclerId);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        options = new FirebaseRecyclerOptions.
                Builder<Data>().setQuery(databaseReference, Data.class).build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final MyViewHolder myViewHolder, final int i, Data data) {
                myViewHolder.setFinalData(data);

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mOptionDialog = new AlertDialog.Builder(getBaseContext());
                        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                        View vOptn = inflater.inflate(R.layout.options_on_click, null);
                        mOptionDialog.setView(vOptn);
                        final AlertDialog dOption = mOptionDialog.create();
                        dOption.setCancelable(false);
                        dOption.show();
                    }
                });
//                myViewHolder.mOptions.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        PopupMenu popup = new PopupMenu(getBaseContext(), myViewHolder.mOptions);
//                        //inflating menu from xml resource
//                        popup.inflate(R.menu.item_menu);
//                        //adding click listener
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()) {
//                                    case R.id.menu1:
//                                        //handle menu1 click
//                                        break;
//                                    case R.id.menu2:
//                                        //handle menu2 click
//                                        break;
//                                    case R.id.menu3:
//                                        //handle menu3 click
//                                        break;
//                                }
//                                return false;
//                            }
//                        });
//                        //displaying the popup
//                        popup.show();
//                    }
//                });
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout, parent, false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
//-------------------------------------------------------------------------------------
    }

    private void AddData() {
        final AlertDialog.Builder mAddDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.inputlayout, null);
        mAddDialog.setView(v);
        final AlertDialog dial = mAddDialog.create();
        dial.setCancelable(false);
        dial.show();

        final EditText addName = v.findViewById(R.id.add_name);
        final EditText addDesc = v.findViewById(R.id.add_desc);

        Button cnc = v.findViewById(R.id.cancel_data);
        Button sav = v.findViewById(R.id.save_data);

        cnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial.dismiss();
            }
        });

        sav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalName = addName.getText().toString().trim();
                String finalDesc = addDesc.getText().toString().trim();
                if (TextUtils.isEmpty(finalName)) {
                    addName.setError("Required field");
                    return;
                }
                if (TextUtils.isEmpty(finalDesc)) {
                    addDesc.setError("Required field");
                    return;
                }

                id = databaseReference.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data dt = new Data(finalName, finalDesc, id, mDate);
                databaseReference.child(id).setValue(dt);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                dial.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mNme, mDcp, mDate, mOptions;

        MyViewHolder(View itemView) {
            super(itemView);
            mNme = itemView.findViewById(R.id.name_dis);
            mDcp = itemView.findViewById(R.id.desc_dis);
            mDate = itemView.findViewById(R.id.date);

        }
        void setFinalData(Data data) {
            String rName = data.getmName();
            mNme.setText(rName);
            String rDesc = data.getmDesc();
            mDcp.setText(rDesc);
            String rDate = data.getmDate();
            mDate.setText(rDate);
        }
//            mOptions = itemView.findViewById(R.id.menuOptions);
    }
}
//    public static class doneAll(){}
