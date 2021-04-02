package com.ruhul;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruhul.admin_app.R;
import com.ruhul.admin_app.model.StoryModel;

public class Story_Details_Show extends AppCompatActivity {

    private String part,name;
    private TextView S_name_txt,S_part_txt,S_full_Story_txt;


    private String parent_story_node="all_post";
    private String child_story_name_node="story_name";

    private String fullstory;


    private DatabaseReference rotref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story__details__show);


        S_name_txt=findViewById(R.id.story_name_id);
        S_part_txt=findViewById(R.id.story_part_id);
        S_full_Story_txt=findViewById(R.id.story_full_txt_id);

        Toast.makeText(this, "story details show call .....", Toast.LENGTH_SHORT).show();

        rotref= FirebaseDatabase.getInstance().getReference(parent_story_node);

        try {
            Intent intent=getIntent();
            part=intent.getStringExtra("part").trim().toString();
            name=intent.getStringExtra("story_name").trim().toString();

            Toast.makeText(this, "part: "+part+": name : "+name, Toast.LENGTH_LONG).show();

            Log.d("PART",part);
            Log.d("NAME",name);


        }catch (Exception e)
        {
            Log.d("error",e.getMessage().toString());
        }



        try {
            rotref.child(name).child(part).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    StoryModel storyModel=snapshot.getValue(StoryModel.class);
                    fullstory=storyModel.getStory().toString();

                    try {
                        S_name_txt.setText(name);
                        S_part_txt.setText(part);
                        S_full_Story_txt.setText(fullstory);
                    }catch (Exception e)
                    {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e)
        {

        }





    }
}