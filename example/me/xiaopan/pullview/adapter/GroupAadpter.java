package me.xiaopan.pullview.adapter;

import java.util.List;

import me.xiaopan.pullview.example.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by xiaopan on 13-12-19.
 */
public class GroupAadpter extends BaseExpandableListAdapter{
    private Context context;
    private List<Group> groupList;

    public GroupAadpter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return groupList != null?groupList.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList!=null?groupList.get(groupPosition).getTexts().length:0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getTexts()[childPosition];
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_text, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_textItem_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(groupList.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_text, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_textItem_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(groupList.get(groupPosition).getTexts()[childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        private TextView text;
    }

    class ChildViewHolder{
        private TextView text;
    }

    public static class Group{
        private String name;
        private String[] texts;

        public Group(String name, String[] texts) {
            this.name = name;
            this.texts = texts;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getTexts() {
            return texts;
        }

        public void setTexts(String[] texts) {
            this.texts = texts;
        }
    }
}
