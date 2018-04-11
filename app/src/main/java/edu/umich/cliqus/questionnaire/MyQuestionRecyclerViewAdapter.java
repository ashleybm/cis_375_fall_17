package edu.umich.cliqus.questionnaire;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umich.cliqus.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyQuestionRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionRecyclerViewAdapter.ViewHolder> {

    private final List<Question> mValues;
    private final QuestionFragment.OnListFragmentInteractionListener mListener;

    public MyQuestionRecyclerViewAdapter(List<Question> items, QuestionFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
 //       holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).getQuestionStr());
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
 //               if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
 //                   mListener.onListFragmentInteraction(holder.mItem);
 //               }
 //           }
  //      });
    }

    @Override
    public int getItemCount() {
            return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Question mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.question_desc);
        }

    }
}
