package com.ruhul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruhul.admin_app.R;

import java.util.List;


public class Story_Name_Adapter extends RecyclerView.Adapter<Story_Name_Adapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private Itemsellect itemsellect;

    public Story_Name_Adapter(Context context, List<String> list, Itemsellect itemsellect) {
        this.context = context;
        this.list = list;
        this.itemsellect=itemsellect;
    }

    public Story_Name_Adapter() {
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.adapter_item_lay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.storyname.setText(list.get(position).toString());
       // Glide.with(context).load(list.get(position)).into(holder.showimage);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface Itemsellect{

        void sellectitem(String name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView storyname,part_number;
       //public ImageView showimage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storyname=itemView.findViewById(R.id.story_name_id);
            part_number=itemView.findViewById(R.id.story_part_number_id);
            //showimage=itemView.findViewById(R.id.imageshow_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemsellect.sellectitem(list.get(getAdapterPosition()).toString());
                }
            });
        }
    }
}
