package me.xiaopan.pullview.sample.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.R;
import me.xiaopan.pullview.sample.adapter.TextAdapter;
import me.xiaopan.pullview.sample.domain.ActivityEntry;
import android.app.ListActivity;
import android.os.Bundle;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullListActivity extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();
        for(int w = 0; w < 25; w++){
            entrys.add(new ActivityEntry.Build("第"+(w+1)+"天", null).create());
        }
        getListView().setAdapter(new TextAdapter(getBaseContext(), entrys));
    }
}
