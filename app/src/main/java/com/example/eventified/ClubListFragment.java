package com.example.eventified;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ClubListFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_club_list, container, false);
        recyclerView = view.findViewById(R.id.student_club_recycler);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {

                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    Log.i("tests", "Refreshing Page");
                    fetchClubs(email);

                    recyclerView.removeAllViewsInLayout();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        return view;
    }

    @Override
    @MainThread
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        fetchClubs(email);
    }

    public void fetchClubs(String query)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());

        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("memberClubList", "");

        serverUrl += "searchQuery=" + query;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        JSONArray names = response.getJSONArray("name");
                        JSONArray descriptions = response.getJSONArray("desc");
                        JSONArray locations = response.getJSONArray("location");
                        requestQueue.stop();
                        Log.i("test", "Made it to the try");

                        ClubSearchAdapter adapter = new ClubSearchAdapter(getContext(), names, descriptions, locations);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);

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