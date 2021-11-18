package com.example.eventified;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class ClubListFragment extends Fragment {

    TextView textView;
    String serverUrl = "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/testemail";

    public ClubListFragment() {
        // Required empty public constructor
    }


    public static ClubListFragment newInstance() {
        ClubListFragment fragment = new ClubListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        textView = textView.findViewById(R.id.volley_test);

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        String email = response.getString("email");
                        textView.setText(email);
                        requestQueue.stop();
                    } catch (JSONException e) {
                        textView.setText("something with Request is wrong");
                        e.printStackTrace();
                        requestQueue.stop();
                    }
                },
                error -> {
                    textView.setText("something with Volley is wrong");
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_club_list, container, false);
    }
}