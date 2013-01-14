/**
 * KTH Developed by Java <br>
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc. <br>
 * You shall not disclose such Confidential Information and shall use it only <br>
 * in accordance with the terms of the license agreement you entered into with KTH.
 */
package com.paran.animation.demo.app.animation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paran.animation.demo.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

/**
 * com.paran.animation.demo.app.animation.PathList.java - Creation date: 2012. 1. 2. <br>
 *
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class PathList extends Activity {
	private ListView list; 
	private SimpleAdapter adapter;
	private FastScrollView scrollview;
	
	private List<HashMap<String, Object>> arList = new ArrayList<HashMap<String, Object>>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathlist);
        
        long cur = System.currentTimeMillis();
        for(int i = 0 ; i < 50 ; i++) {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	
        	Date date = new Date(cur - 1000*60*60*i);
        	map.put("item", date);
        	map.put("item2", date.toLocaleString());
        	arList.add(map);
        }
        
        list = (ListView)findViewById(R.id.list);
        scrollview = (FastScrollView)findViewById(R.id.listContainer);
        
        adapter = new SimpleAdapter(
        		this
        		, arList
        		, R.layout.list_item
        		, new String[]{"item2"}
        		, new int[] {R.id.title});
        list.setAdapter(adapter);
	}
}
