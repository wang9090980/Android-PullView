package me.xiaopan.pullview.sample.activity;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.ImageAdapter;
import me.xiaopan.pullview.widget.PullGridView;
import android.app.Activity;
import android.os.Bundle;

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
