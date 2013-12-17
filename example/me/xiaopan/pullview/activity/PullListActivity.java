package me.xiaopan.pullview.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.adapter.TextAdapter;
import me.xiaopan.pullview.domain.ActivityEntry;
import me.xiaopan.pullview.example.R;

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
