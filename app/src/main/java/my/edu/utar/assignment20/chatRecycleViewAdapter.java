package my.edu.utar.assignment20;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chatRecycleViewAdapter extends RecyclerView.Adapter<chatRecycleViewAdapter.ViewHolder> {
    ArrayList<matchChatHelper> matchList;
    Context context;
    public chatRecycleViewAdapter(Context context,ArrayList<matchChatHelper> matchList) {
        this.matchList = matchList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        matchChatHelper match = matchList.get(position);
        holder.nameTextView.setText(match.getName());

        Picasso.get()
                .load(match.getImageURL())
                .placeholder(R.drawable.liam) // Placeholder image while loading
                .error(R.drawable.ic_baseline_person_24) // Error image if loading fails
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatPage.class);
                intent.putExtra("imageURL",match.getImageURL());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileimage);
            nameTextView = itemView.findViewById(R.id.nameonline);
            cardView=itemView.findViewById(R.id.chatCardView);
        }
    }
}
