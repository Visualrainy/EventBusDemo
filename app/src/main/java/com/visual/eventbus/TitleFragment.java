package com.visual.eventbus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by pchen on 7/9/15.
 */
public class TitleFragment extends Fragment {

    private Switch switchView;
    private ListView titleListView;
    private List<String> titles;

    public static TitleFragment newInstance(Bundle bundle) {
        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            initTitles();
        } else {
            titles = getArguments().getStringArrayList("title_list");
        }
    }

    private void initTitles() {
        titles = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            switch (i % 4) {
                case 0:
                    titles.add("PostThread" + i / 4);
                    break;
                case 1:
                    titles.add("MainThread" + i / 4);
                    break;
                case 2:
                    titles.add("BackgroundThread" + i / 4);
                    break;
                case 3:
                    titles.add("Async" + i / 4);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.title_list_layout, container, false);

        titleListView = (ListView) rootView.findViewById(R.id.title_list_view);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (switchView == null) {
            switchView = (Switch) getActivity().findViewById(R.id.switch_view);
        }
        populateData();
    }

    protected void populateData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(14);
                return view;
            }
        };
        titleListView.setAdapter(adapter);
        titleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                String title = (String) parent.getAdapter().getItem(position);
                if (switchView.isChecked()) {
                    postOnAsyncThread(title);
                } else {
                    postOnMainThread(title);
                }
            }
        });
    }

    private void postOnMainThread(String title) {
        Log.d(BuildConfig.LOG_TAG, "postEventOnMainThread ID: " + Thread.currentThread().getId());
        EventBus.getDefault().post(title);
    }

    public void postOnAsyncThread(final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(BuildConfig.LOG_TAG, "postEventOnAsyncThread ID: " + Thread.currentThread().getId());
                EventBus.getDefault().post(title);
            }
        }).start();
    }

}
