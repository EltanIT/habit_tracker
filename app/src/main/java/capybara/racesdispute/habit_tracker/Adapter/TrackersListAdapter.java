package capybara.racesdispute.habit_tracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import capybara.racesdispute.habit_tracker.R;
import capybara.racesdispute.habit_tracker.TrackersClickListener;
import capybara.racesdispute.habit_tracker.trackers_settings.Models;

public class TrackersListAdapter extends  RecyclerView.Adapter<TrackersViewHolder> {

    Context context;
    List<Models> list;
    TrackersClickListener listener;

    public TrackersListAdapter(Context context, List<Models> list, TrackersClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrackersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackersViewHolder(LayoutInflater.from(context).inflate(R.layout.tracker_list,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull TrackersViewHolder holder, int position) {

        holder.textView_title.setText(list.get(position).getName());
        holder.textView_title.setSelected(true);

        holder.textView_trackers.setText(list.get(position).getPeriod());
        holder.textView_trackersDays.setText(list.get(position).getPeriodInt());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        holder.textView_description.setText(list.get(position).getDescription(),TextView.BufferType.EDITABLE);

        if (list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.ic_action_pin);
        }else{
            holder.imageView_pin.setImageResource(0);
        }
        int colorCode = getRandomColor();
        holder.trackers_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode,null));

        holder.trackers_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.trackers_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.trackers_container);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.tracker_color1);
        colorCode.add(R.color.tracker_color2);
        colorCode.add(R.color.tracker_color3);
        colorCode.add(R.color.tracker_color4);
        colorCode.add(R.color.tracker_color5);
        colorCode.add(R.color.tracker_color6);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());

        return colorCode.get(randomColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class TrackersViewHolder extends RecyclerView.ViewHolder {

CardView  trackers_container;
TextView textView_title, textView_trackers,textView_date, textView_description, textView_trackersDays;
ImageView imageView_pin;



    public TrackersViewHolder(@NonNull View itemView) {
        super(itemView);
        trackers_container = itemView.findViewById(R.id.trackers_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_trackers = itemView.findViewById(R.id.textView_trackers);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
        textView_description = itemView.findViewById(R.id.textView_discription);
        textView_trackersDays = itemView.findViewById(R.id. textView_trackersDays);
    }
}
