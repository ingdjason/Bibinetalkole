package com.bibinetalkole.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bibinetalkole.app.R;
import com.bibinetalkole.app.adapter.ShowArrayAdapter;
import com.bibinetalkole.app.models.Shows;
import com.bibinetalkole.app.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

import static com.bibinetalkole.app.utils.settings.API_URL;
import static com.bibinetalkole.app.utils.settings.getSslContext;

public class FragmentShow extends Fragment {

    ArrayList<Shows> shows;
    ShowArrayAdapter showArrayAdapter;

    RecyclerView rvList;
    private EndlessRecyclerViewScrollListener scrollListener;
    ProgressBar progressBar3;
    Button btnReload;
    LinearLayout lnReload;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        AndroidNetworking.initialize(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show, container, false);
        progressBar3 = v.findViewById(R.id.progressBar3);
        //lvShows = v.findViewById(R.id.lvShows);
        btnReload = v.findViewById(R.id.btnReload);
        lnReload = v.findViewById(R.id.lnReload);
        rvList = v.findViewById(R.id.rvList);
        swipeContainer = v.findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                progressBar3.setVisibility(View.VISIBLE);
                lnReload.setVisibility(View.GONE);
                findListShow();
            }
        });

        lnReload.setVisibility(View.GONE);
        // Set layout manager to position the items
 LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        rvList.setLayoutManager(linearLayoutManager);
        ////
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        // Attach the layout manager to the recycler view
        rvList.setLayoutManager(linearLayoutManager);
        ////


        rvList.setItemAnimator(new SlideInUpAnimator());

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar3.setVisibility(View.VISIBLE);
                lnReload.setVisibility(View.GONE);
                findListShow();
            }
        });

        // Retain an instance so that you can call `resetState()` for fresh searches
        //scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int pages, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progressBar3.setVisibility(View.VISIBLE);
                swipeContainer.setRefreshing(true);
                findListShow();
            }
        };

        findListShow();
        // Adds the scroll listener to RecyclerView
        rvList.addOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //LifeCycle
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    private void findListShow() {
        int cacheSize = 10 * 1024 * 1024; // 10MB
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .cache(new Cache(getActivity().getCacheDir(), cacheSize))
                .sslSocketFactory(getSslContext().getSocketFactory())
                .readTimeout(300, TimeUnit.SECONDS)
                . writeTimeout(300, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.get(API_URL+"posts?categories=20&_embed")
                .setPriority(Priority.IMMEDIATE)
                .setOkHttpClient(okHttpClient)
                .setMaxAgeCacheControl(3 , TimeUnit.HOURS)
                .getResponseOnlyIfCached()
                .setMaxStaleCacheControl(3, TimeUnit.HOURS)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        JSONArray showsJsonPosts = null;
                        showsJsonPosts = response;
                        System.out.println("DATA-BIBI-> "+response);
                        shows = Shows.fromJSONArray(showsJsonPosts);
                        // Create adapter passing in the sample user data
                        showArrayAdapter = new ShowArrayAdapter(shows);
                        // Attach the adapter to the recyclerview to populate items
                        rvList.setAdapter(showArrayAdapter);
                        //page +=1;
                        progressBar3.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        lnReload.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println("ERROR-BIBI->"+error.getErrorDetail()+" <-> "+error.getMessage());
                        progressBar3.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        lnReload.setVisibility(View.VISIBLE);
                    }
                });
    }

}
