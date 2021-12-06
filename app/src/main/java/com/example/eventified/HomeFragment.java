package com.example.eventified;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @MainThread
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        fetchEventInfo(email);
    }

    private void fetchEventInfo(String email) {

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        getContext();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString("getHomeEvents", "");

        serverUrl += "email=" + email;

        recyclerView = requireView().findViewById(R.id.recyclerView);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null,

                response -> {
                    try {
                        JSONArray titles = response.getJSONArray("title");
                        JSONArray descriptions = response.getJSONArray("desc");
                        JSONArray locations = response.getJSONArray("location");
                        JSONArray dates = response.getJSONArray("date");
                        JSONArray times = response.getJSONArray("time");
                        JSONArray repeating = response.getJSONArray("repeating");
                        JSONArray personal = response.getJSONArray("personal");
                        JSONArray name = response.getJSONArray("name");
                        requestQueue.stop();

                        HomeAdapter adapter = new HomeAdapter(getContext(), titles, descriptions,
                                locations, dates, times, repeating, personal, name);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something with the request is wrong, or there are no events for this club", Toast.LENGTH_LONG).show();
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