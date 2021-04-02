package com.ruhul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruhul.adapter.part_adapter;
import com.ruhul.admin_app.R;
import com.ruhul.admin_app.model.StoryModel;

import java.util.ArrayList;
import java.util.List;

public class All_Part_Show extends AppCompatActivity implements part_adapter.Itemsellect {


    private RecyclerView recyclerView;

    private List<StoryModel> partlist;


    private String parent_story_node="all_post";
    private String child_story_name_node="story_name";
    private String child_story_part_node="part";

    private DatabaseReference rotref;
    private String storyname;

    private part_adapter part_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__part__show);


        partlist=new ArrayList<>();
        recyclerView=findViewById(R.id.recycularview_id);



        try {
            Intent intent=getIntent();
            storyname=intent.getStringExtra("story_name").toString();
        }catch (Exception e)
        {
            Log.d("error",e.getMessage().toString());
        }


        part_adapter =new part_adapter(All_Part_Show.this,partlist,this::sellectitem);
        recyclerView.setLayoutManager(new GridLayoutManager(All_Part_Show.this,1));
        recyclerView.setAdapter(part_adapter);






        rotref= FirebaseDatabase.getInstance().getReference();

        rotref.child(parent_story_node).child(storyname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                partlist.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    StoryModel storyModel=dataSnapshot.getValue(StoryModel.class);
                    partlist.add(storyModel);

                }
                part_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("data_retrive",error.getMessage().toString());
            }
        });

    }

    @Override
    public void sellectitem(String part,String story_name) {
        Intent intent=new Intent(All_Part_Show.this, Story_Details_Show.class);
        intent.putExtra("story_name",story_name);
        intent.putExtra("part",part);
        startActivity(intent);
    }

}