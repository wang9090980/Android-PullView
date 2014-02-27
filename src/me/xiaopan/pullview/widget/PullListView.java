/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.pullview.widget;

import me.xiaopan.pullview.PullAdapterViewBase;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可拉的ListView
 */
public class PullListView extends PullAdapterViewBase<ListView> {
    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullListView(Context context) {
        super(context);
    }

    @Override
    protected ListView createPullView() {
        ListView listView = new ListView(getContext());
        listView.setId(android.R.id.list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setBackgroundColor(Color.WHITE);
        return listView;
    }
}
