package edu.umich.cliqus.questionnaire;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import edu.umich.cliqus.R;
import edu.umich.cliqus.profile.Profile;

public class SingleQuestionFragment extends Fragment {
    private TextView questionDesc;
    private LinearLayout questionAnswerLayout;

    private Profile profile;
    private Question question;
    private static final String SELECTEDQUESTION = "selected_question";
    private static final String PROFILEDATA = "profiledata";

    private OnFragmentInteractionListener mListener;

    public SingleQuestionFragment() {

    }


    public static SingleQuestionFragment newInstance(Questionnaire que) {
        SingleQuestionFragment fragment = new SingleQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(SELECTEDQUESTION);
            profile = (Profile) getArguments().getSerializable(PROFILEDATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_question, container, false);

        questionDesc = (TextView) rootView.findViewById(R.id.question_desc);
        questionAnswerLayout = (LinearLayout) rootView.findViewById(R.id.question_answer_layout);

        if(question != null) {
            questionDesc.setText(question.getQuestionStr());
            final List<String> questionAnsChoicesList = question.getQuestionAnsChoicesList();
            for(int i = 0; i < question.getQuestionAnsChoicesList().size(); i++) {
                Button button = new Button(getActivity());
                button.setText(questionAnsChoicesList.get(i));
                final int index = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quesAns = questionAnsChoicesList.get(index);
                        profile.addDesiredEventTag(
                                quesAns.substring(quesAns.indexOf('(') + 1, quesAns.indexOf(')') -1));
                    }
                });
            }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
