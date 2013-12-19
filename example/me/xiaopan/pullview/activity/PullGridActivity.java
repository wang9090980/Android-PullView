package me.xiaopan.pullview.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.PullGridView;
import me.xiaopan.pullview.adapter.ImageAdapter;
import me.xiaopan.pullview.adapter.TextAdapter;
import me.xiaopan.pullview.domain.ActivityEntry;
import me.xiaopan.pullview.example.R;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullGridActivity extends Activity {

    private PullGridView pullGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        pullGridView = (PullGridView) findViewById(R.id.pullGrid_grid);

        pullGridView.getPullView().setAdapter(new ImageAdapter(getBaseContext(), getResources().getStringArray(R.array.urls_small)));
        pullGridView.getPullView().setNumColumns(2);
    }
}
