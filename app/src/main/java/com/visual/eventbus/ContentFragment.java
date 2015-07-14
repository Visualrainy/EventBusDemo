package com.visual.eventbus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * Created by pchen on 7/9/15.
 */
public class ContentFragment extends Fragment {

    private TextView contentView;
    private String content;

    public static ContentFragment newInstance(Bundle bundle) {
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            content = "content";
        } else {
            content = getArguments().getString("content");
        }
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        contentView = (TextView) rootView.findViewById(R.id.content);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        contentView.setText(content);
    }

    public void onEvent(Integer integer) {
        Log.d(BuildConfig.LOG_TAG , "Receive onEventPostThread(Integer) Thread ID: " + Thread.currentThread().getId());
    }

    /**
     * Default is ThreadMode is PostThread
     * @param title
     */
    public void onEvent(final String title) {
        Log.d(BuildConfig.LOG_TAG , "Receive onEventPostThread(String) Thread ID: " + Thread.currentThread().getId());
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setText(title);
            }
        });
    }

    public void onEventMainThread(String title) {
        Log.d(BuildConfig.LOG_TAG , "Receive onEventMainThread(String) ID: " + Thread.currentThread().getId());
    }

    public void onEventBackgroundThread(String title) {
        Log.d(BuildConfig.LOG_TAG , "Receive onEventBackgroundThread(String) ID: " + Thread.currentThread().getId());
    }

    public void onEventAsync(String title) {
        Log.d(BuildConfig.LOG_TAG , "Receive onEventAsync(String) ID: " + Thread.currentThread().getId());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
