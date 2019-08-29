package khairy.com.jakegithub.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import khairy.com.jakegithub.R;
import khairy.com.jakegithub.adapter.RecyclerViewAdapter;
import khairy.com.jakegithub.viewmodels.ViewModelProviderFactory;

public class JakeGithubFragment extends DaggerFragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout lodingFrame;
    private LinearLayout emptyLinear;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private JakeGithubViewModel viewModel;
    private TextView textView;
    private int currentPage = 1;
    private Boolean isEnd = false;
    private Boolean isloading = false;
    private int swipeCount = 0;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jake_github_fargment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this, providerFactory).get(JakeGithubViewModel.class);
        initView(view);
        setRecyclerView(view);
        observeData();
        refresh();
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {

            swipeCount += 1;
            currentPage = 1;
            if (swipeCount > 0) {
                viewModel.getJakeRepo(1, true);
            }

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initView(View view) {
        textView = (TextView) view.findViewById(R.id.error_message);
        emptyLinear = (LinearLayout) view.findViewById(R.id.empty);
        lodingFrame = (FrameLayout) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }


    private void observeData() {

        viewModel.getJakeRepo(currentPage, false);
        viewModel.observeUser().observe(this, apiResource -> {

            switch (apiResource.status) {
                case LOADING: {
                    showProgressBar(true);
                    break;
                }

                case SUCCESS: {

                    showProgressBar(false);
                    if (apiResource.data != null && apiResource.data.size() > 0) {
                        recyclerViewAdapter.addItems(apiResource.data, apiResource.isEnded);
                        isEnd = apiResource.isEnded;
                        isloading = false;
                        if (apiResource.isConnected) {
                            showEmptyMessage(false);
                        } else {
                            showEmptyMessage(true
                            );
                        }
                        break;
                    }
                    showEmptyMessage(true);
                    break;
                }

                case ERROR: {
                    recyclerViewAdapter.addItems(apiResource.data, apiResource.isEnded);
                    textView.setText(apiResource.message);
                    showEmptyMessage(true);
                    showProgressBar(false);
                    break;
                }
            }

        });
    }


    private void setRecyclerView(View view) {

        LinearLayoutManager rvManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(rvManager);
        recyclerView.setAdapter(recyclerViewAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = rvManager.getChildCount();
                int totalItemCount = rvManager.getItemCount();
                int firstVisibleItemPosition = rvManager.findFirstVisibleItemPosition();


                if (!isEnd && !isloading) {

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 3 && firstVisibleItemPosition >= 0 && totalItemCount < 108) {
                        showProgressBar(true);
                        isloading = true;
                        currentPage += 1;
                        viewModel.getJakeRepo(currentPage, false);

                    } else if (currentPage == 8 && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        Snackbar.make(view, "The End!", Snackbar.LENGTH_SHORT).show();
                    }

                } else if (isEnd && currentPage == 1) {

                    textView.setText("You Are Offline");
                    showEmptyMessage(true);
                    if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
                        Snackbar.make(view, "The End!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            lodingFrame.setVisibility(View.VISIBLE);
        } else {
            lodingFrame.setVisibility(View.GONE);
        }
    }

    private void showEmptyMessage(boolean isEmpty) {
        if (isEmpty) {
            emptyLinear.setVisibility(View.VISIBLE);
        } else {
            emptyLinear.setVisibility(View.GONE);
        }
    }

}
