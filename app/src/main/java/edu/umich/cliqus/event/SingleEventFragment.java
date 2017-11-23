package edu.umich.cliqus.event;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.umich.cliqus.R;

public class SingleEventFragment extends Fragment {

    private Event event;
    private static final String SELECTEDEVENT = "selected_event";

    private ImageView coverImage;
    private TextView eventName;
    private TextView eventDescription;

    private OnFragmentInteractionListener mListener;

    public SingleEventFragment() {
        // Required empty public constructor
    }

    public static SingleEventFragment newInstance(Event ev) {
        SingleEventFragment fragment = new SingleEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTEDEVENT, ev);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(SELECTEDEVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_event, container, false);
        coverImage = (ImageView) rootView.findViewById(R.id.event_cover_image);
        eventName = (TextView) rootView.findViewById(R.id.event_name);
        eventDescription = (TextView) rootView.findViewById(R.id.event_description);

        Log.e("cliqus", "event is " + event.toString());
        if(event != null) {
            coverImage.setImageBitmap(event.getImageEvent());
            eventName.setText("name of event"/*event.getTitle()*/);
            eventDescription.setText(event.getDescription());

        }

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
