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

package me.xiaopan.pullview.domain;

import me.xiaopan.pullview.adapter.TextAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityEntry implements TextAdapter.Text {
	private String name;
	private Class<?> targetClass;
	private Bundle bundle;
	private boolean checkLogined;
	private boolean checkNetwork;
	private boolean showArrow;
	private int requestCode;
	
	private ActivityEntry(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public String getText() {
		return getName();
	}

	@Override
	public boolean isShowArrow() {
		return showArrow;
	}

	public void setShowArrow(boolean showArrow) {
		this.showArrow = showArrow;
	}

	public boolean isCheckLogined() {
		return checkLogined;
	}

	public void setCheckLogined(boolean checkLogined) {
		this.checkLogined = checkLogined;
	}

	public boolean isCheckNetwork() {
		return checkNetwork;
	}

	public void setCheckNetwork(boolean checkNetwork) {
		this.checkNetwork = checkNetwork;
	}
	
	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public void clickHandle(Activity activity){
        if(targetClass != null){
            if(bundle != null){
                activity.startActivity(new Intent(activity, targetClass).putExtras(bundle));
            }else{
                activity.startActivity(new Intent(activity, targetClass));
            }
        }
	}
	
	public static class Build{
		private ActivityEntry activityEntry;
		
		public Build(String name, Class<?> targetClass){
			activityEntry = new ActivityEntry();
			setName(name);
			setTargetClass(targetClass);
		}
		
		public Build setName(String name) {
			activityEntry.setName(name);
			return this;
		}
		
		public Build setTargetClass(Class<?> targetClass) {
			activityEntry.setTargetClass(targetClass);
			return this;
		}

		public Build setBundle(Bundle bundle) {
			activityEntry.setBundle(bundle);
			return this;
		}

		public Build setShowArrow(boolean showArrow) {
			activityEntry.setShowArrow(showArrow);
			return this;
		}

		public Build setCheckLogined(boolean checkLogined) {
			activityEntry.setCheckLogined(checkLogined);
			return this;
		}

		public Build setCheckNetwork(boolean checkNetwork) {
			activityEntry.setCheckNetwork(checkNetwork);
			return this;
		}

		public Build setRequestCode(int requestCode) {
			activityEntry.setRequestCode(requestCode);
			return this;
		}
		
		public ActivityEntry create(){
			return activityEntry;
		}
	}
}