package com.nubar.jime.CalenderFragements;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nubar.jime.R;

public class daysOfWeek extends Fragment {
    private OnGreenFragmentListener mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_daysofweek, container, false);

        final TextView sat = v.findViewById(R.id.saturday);
        final TextView sunday = v.findViewById(R.id.sunday);
        final TextView monday = v.findViewById(R.id.monday);
        final TextView tuesday = v.findViewById(R.id.tuesday);
        final TextView wednesday = v.findViewById(R.id.wednesday);
        final TextView thursday = v.findViewById(R.id.thursday);
        final TextView friday = v.findViewById(R.id.friday);
        sat.setTextColor(Color.parseColor("#008000"));
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "saturday";
                mCallback.messageFromGreenFragment(message);
                sat.setTextColor(Color.parseColor("#008000"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));


            }
        });
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "sunday";
                mCallback.messageFromGreenFragment(message);
                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#008000"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "monday";
                mCallback.messageFromGreenFragment(message);

                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#008000"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "tuesday";
                mCallback.messageFromGreenFragment(message);

                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#008000"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "wednesday";
                mCallback.messageFromGreenFragment(message);

                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#008000"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "thursday";
                mCallback.messageFromGreenFragment(message);

                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#008000"));
                friday.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "friday";
                mCallback.messageFromGreenFragment(message);

                sat.setTextColor(Color.parseColor("#FFFFFF"));
                sunday.setTextColor(Color.parseColor("#FFFFFF"));
                monday.setTextColor(Color.parseColor("#FFFFFF"));
                tuesday.setTextColor(Color.parseColor("#FFFFFF"));
                wednesday.setTextColor(Color.parseColor("#FFFFFF"));
                thursday.setTextColor(Color.parseColor("#FFFFFF"));
                friday.setTextColor(Color.parseColor("#008000"));
            }
        });
        return v;
    }

    // This is the interface that the Activity will implement
    // so that this Fragment can communicate with the Activity.
    public interface OnGreenFragmentListener {
        void messageFromGreenFragment(String text);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGreenFragmentListener) {
            mCallback = (OnGreenFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGreenFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}
