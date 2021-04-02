package com.ruhul.admin_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ruhul.All_Part_Show;
import com.ruhul.adapter.Story_Name_Adapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Story_Name_Adapter.Itemsellect {
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    public DatabaseReference mrootref;

    private List<String> storyModelList;
    private Story_Name_Adapter admin_adapter;

    private String name;

    private String parent_story_node="all_post";
    private String child_story_name_node="story_name";
    private String child_story_part_node="part";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton=findViewById(R.id.file_upload_id);

        mrootref=FirebaseDatabase.getInstance().getReference();


        recyclerView=findViewById(R.id.recycular_id);
        storyModelList=new ArrayList<String>();
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
//        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        admin_adapter=new Story_Name_Adapter(MainActivity.this,storyModelList,this::sellectitem);
        recyclerView.setAdapter(admin_adapter);

        //push notification in android from firebase database -------------------

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "done";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });





        mrootref.child("all_post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                storyModelList.clear();
                    for (DataSnapshot dataSnapshot1:snapshot.getChildren())
                    {


                       // StoryModel storyModel=dataSnapshot1.getValue(StoryModel.class);

                        String story_name=dataSnapshot1.getKey().toString();
                        Log.d("name",story_name);

                        storyModelList.add(story_name);

                    }

                    admin_adapter.notifyDataSetChanged();






            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "data read problemes[] "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Admin_Post_Upload.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void sellectitem(String name) {

        Log.d("adapter_item_position",name);

        Intent intent=new Intent(MainActivity.this, All_Part_Show.class);
        intent.putExtra("story_name",name);
        startActivity(intent);

    }
}