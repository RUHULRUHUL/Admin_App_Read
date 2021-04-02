package com.ruhul.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ruhul.admin_app.R;
import com.ruhul.admin_app.model.StoryModel;

import java.util.List;

public class part_adapter extends RecyclerView.Adapter<part_adapter.ViewHolder> {

    private Context context;
    private List<StoryModel> partlist;
    private Itemsellect itemsellect;

    public part_adapter(Context context, List<StoryModel> partlist, Itemsellect itemsellect) {
        this.context = context;
        this.partlist = partlist;
        this.itemsellect=itemsellect;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.partlist_item_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        try {
            StoryModel storyModel=partlist.get(position);

            holder.story_name.setText(storyModel.getChild_story_name_node());
            holder.part_number.setText(storyModel.getPartnumber());
            Glide.with(context).load(storyModel.getImageuri()).into(holder.showimage);

        }catch (Exception e)
        {
            Log.d("part_adapter",e.getMessage().toString());

        }




    }

    @Override
    public int getItemCount() {
        return partlist.size();
    }


    public interface  Itemsellect{
        void sellectitem(String part,String story_name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView story_name,part_number;
        public ImageView showimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            story_name=itemView.findViewById(R.id.story_name_id);
            part_number=itemView.findViewById(R.id.story_part_number_id);
            showimage=itemView.findViewById(R.id.imageshow_id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemsellect.sellectitem(partlist.get(getAdapterPosition()).getPartnumber().toString(),partlist.get(getAdapterPosition()).getChild_story_name_node());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });




        }
    }
}
