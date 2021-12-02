package com.example.eventified;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.Objects;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        EditText query = view.findViewById(R.id.club_search_bar);
                        fetchClubs(query.getText().toString());

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


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

        final RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());

        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("getClubSearch", "");

        serverUrl += "searchQuery=" + query;

        recyclerView = requireView().findViewById(R.id.clubs_recycler);

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