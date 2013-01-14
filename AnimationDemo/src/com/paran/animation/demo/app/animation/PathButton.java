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

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

/**
 * com.paran.animation.demo.app.animation.PathButton.java - Creation date: 2011. 12. 22. <br>
 * 하위 메뉴를 위한 Button 클래스
 * 
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class PathButton extends Button {
	private float x_offset = 0;
	private float y_offset = 0;
	
	public PathButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PathButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	//2012.1.17 일부단말에서 클릭 안되는 문제 해결을 위해 수정
//	@Override
//	public void getHitRect(Rect outRect) {
//		Rect curr = new Rect();
//	    super.getHitRect(curr);
//	    
//	    outRect.bottom = (int) (curr.bottom + y_offset);
//	    outRect.top = (int) (curr.top + y_offset);
//	    outRect.left = (int) (curr.left + x_offset);
//	    outRect.right = (int) (curr.right + x_offset);
//	    
//	    
//	    Log.e("brad", "Cur L:"+curr.left+" T:"+curr.top+" R:"+curr.right+" B:"+curr.bottom);
//	    Log.e("brad", "Out L:"+outRect.left+" T:"+outRect.top+" R:"+outRect.right+" B:"+outRect.bottom);
//	}
	
	public void setOffset(float endX, float endY) {
		x_offset = endX;
		y_offset = endY;
	}
	
	public float getXOffset() {
		return x_offset;
	}
	
	public float getYOffset() {
		return y_offset;
	}
}
