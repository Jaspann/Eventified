package com.example.eventified;

import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i("Location: ","Start of Fetch Club Info");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button button = view.findViewById(R.id.search_button);
        button.setOnClickListener(v -> {
            EditText query = view.findViewById(R.id.club_search_bar);
            fetchClubs(query.getText().toString());
        });


        return view;
    }

    @MainThread
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        EditText query = view.findViewById(R.id.club_search_bar);
        fetchClubs(query.getText().toString());
    }

    public void fetchClubs(String query)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String serverUrl = ""; //Inputs GetClubSearch URL

        serverUrl += "searchQuery=" + query;

        recyclerView = getView().findViewById(R.id.clubs_recycler);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        JSONArray names = response.getJSONArray("name");
                        JSONArray descriptions = response.getJSONArray("desc");
                        JSONArray locations = response.getJSONArray("location");
                        requestQueue.stop();

                        ClubSearchAdapter adapter = new ClubSearchAdapter(getContext(), names, descriptions, locations);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: something with the request is wrong", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        requestQueue.stop();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error: something with Volley is wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    requestQueue.stop();
                }) {
        };
        requestQueue.add(stringRequest);
    }
}