package me.xiaopan.pullview.activity;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.pullview.PullExpandableListView;
import me.xiaopan.pullview.adapter.GroupAadpter;
import me.xiaopan.pullview.adapter.TextAdapter;
import me.xiaopan.pullview.domain.ActivityEntry;
import me.xiaopan.pullview.example.R;

/**
 * Created by xiaopan on 13-12-17.
 */
public class PullExpandableListActivity extends ExpandableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list);

        List<TextAdapter.Text> entrys = new ArrayList<TextAdapter.Text>();
        for(int w = 0; w < 25; w++){
            entrys.add(new ActivityEntry.Build("第"+(w+1)+"天", null).create());
        }

        List<GroupAadpter.Group> groups = new ArrayList<GroupAadpter.Group>();

        for(int w = 0; w < 7; w++){
            String[] texts = new String[10];
            for(int w2 = 0; w2 < texts.length; w2++){
                texts[w2] = "第"+(w+1)+"组的第"+(w2+1)+"个条目";
            }
            groups.add(new GroupAadpter.Group("第"+(w+1)+"组", texts));
        }

        getExpandableListView().setAdapter(new GroupAadpter(getBaseContext(), groups));
        getExpandableListView().setGroupIndicator(null);
    }
}
