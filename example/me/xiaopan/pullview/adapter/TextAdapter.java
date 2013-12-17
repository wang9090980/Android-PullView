/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.xiaopan.pullview.adapter;

import java.util.List;

import me.xiaopan.pullview.example.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TextAdapter extends BaseAdapter{
	private Context conetxt;
	private List<Text> texts;
	private Text text;
	
	public TextAdapter(Context conetxt, List<Text> texts){
		this.conetxt = conetxt;
		this.texts = texts;
	}
	
	@Override
	public Object getItem(int position) {
		return texts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return texts.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(conetxt).inflate(R.layout.list_item_text, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.text_textItem_text);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		text = texts.get(position);
		viewHolder.text.setText(text.getText());
		return convertView;
	}
	
	class ViewHolder{
		private TextView text;
	}
	
	public interface Text{
		/**
		 * 获取文本
		 * @return
		 */
		public String getText();
		
		/**
		 * 是否显示箭头
		 * @return
		 */
		public boolean isShowArrow();
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
}