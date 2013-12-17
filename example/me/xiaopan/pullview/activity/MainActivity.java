package me.xiaopan.pullview.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.adapter.TextAdapter;
import me.xiaopan.pullview.domain.ActivityEntry;
import me.xiaopan.pullview.example.R;

/**
 * Created by xiaopan on 13-12-17.
 */
public class MainActivity extends ListActivity{
    private TextAdapter textAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullWebView), PullWebViewActivity.class).create());
        entrys.add(new ActivityEntry.Build(getString(R.string.activty_pullScrollView), PullScrollViewActivity.class).create());
        getListView().setAdapter(textAdapter = new TextAdapter(getBaseContext(), entrys));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ActivityEntry) textAdapter.getTexts().get(position - getListView().getHeaderViewsCount())).clickHandle(MainActivity.this);
            }
        });
    }
}
