package edu.umich.cliqus.event;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.umich.cliqus.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Event> events;
    private final int MAXEVENTS = 15;
    private static RecyclerViewClickListener mListener;

    public RecyclerAdapter(List<Event> events, RecyclerViewClickListener mListener){
        this.events = events;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_design, parent, false);

        if(view != null) {
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else
            return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.w("cliqus", "position " + position + " " + events.get(position).getDate() +
                " " + events.get(position).getTime());

        if(position < MAXEVENTS) {
            holder.descriptionView.setText(events.get(position).getDescription());
            holder.dateTimeView.setText(
                    events.get(position).getDate() + " " + events.get(position).getTime());

            holder.locationView.setText(events.get(position).getAddress());


            if (holder.imageView != null) {
                holder.imageView.setImageBitmap(events.get(position).getImageEvent());
                Log.w("cliqus", "Setting image ID " + events.get(position).getEventUID());
            }
        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView descriptionView;
        protected TextView dateTimeView;
        protected TextView locationView;
        protected ImageView imageView;
        protected RelativeLayout individualCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionView =  (TextView) itemView.findViewById(R.id.event_card_description);
            dateTimeView =  (TextView) itemView.findViewById(R.id.event_card_date_time);
            locationView =  (TextView) itemView.findViewById(R.id.event_card_location);
            imageView = (ImageView) itemView.findViewById(R.id.event_card_image);
            individualCardView = (RelativeLayout) itemView.findViewById(R.id.event_card_sub_view);
            individualCardView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mListener.recyclerViewListClicked(v, getLayoutPosition());
        }
    }

    public void removeEvents() {
        for(int i = 0; i < events.size() - 1; i++) {
            events.remove(0);
            notifyItemRemoved(0);
        }
    }

    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }

}