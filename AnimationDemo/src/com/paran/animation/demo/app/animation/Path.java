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

import com.paran.animation.demo.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Layout;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * com.paran.animation.demo.app.animation.Path.java - Creation date: 2011. 12. 21. <br>
 * Path 메뉴 화면 Demo
 * 
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class Path extends Activity implements OnClickListener {
	private Context context;

	/**
	 * 메뉴가 열렸을때 이동 거리
	 */
	private static final int length = 220;
	/**
	 * (+)버튼 동작 시간
	 */
	private static final int duration = 100;
	/**
	 * 하위 메뉴 동작 시간
	 */
	private static final int sub_duration = 200;
	/**
	 * 하위 메뉴 선택시 동작 시간
	 */
	private static final int sub_select_duration = 200;
	/**
	 * 하위 메뉴 동작시 각 버튼간의 시간 Gap
	 */
	private static final int sub_offset = 30;
	
	private Button plus_button;
	private ImageView plus;
	
	/**
	 * 하위 메뉴 버튼 리스트
	 */
	private ArrayList<PathButton> buttons;
	
	/**
	 * Menu 가 열려있는지 닫혀있는지 체크하기위한 flag
	 */
	private boolean isMenuOpened = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.path);
        
        context = this;
        
        plus_button = (Button)findViewById(R.id.plus_button);
        plus_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isMenuOpened) {
					isMenuOpened = true;
				} else {
					isMenuOpened = false;
				}
				startMenuAnimation(isMenuOpened);
			}
		});
        
        plus = (ImageView)findViewById(R.id.plus);
        
        buttons = new ArrayList<PathButton>();
        
        PathButton button = (PathButton)findViewById(R.id.camera);
        button.setOnClickListener(this);
        buttons.add(button);
        
        button = (PathButton)findViewById(R.id.with);
        button.setOnClickListener(this);
        buttons.add(button);
        
        button = (PathButton)findViewById(R.id.place);
        button.setOnClickListener(this);
        buttons.add(button);
        
        button = (PathButton)findViewById(R.id.music);
        button.setOnClickListener(this);
        buttons.add(button);
        
        button = (PathButton)findViewById(R.id.thought);
        button.setOnClickListener(this);
        buttons.add(button);
        
        button = (PathButton)findViewById(R.id.sleep);
        button.setOnClickListener(this);
        buttons.add(button);
    }
	
	/**
	 * <PRE>
	 * Comment : <br> 
	 * 하위 메뉴가 선택되었을 때, 애니메이션 처리
	 * 선택된 메뉴는 커지면서 점점 투명해지다가 사라지며, 
	 * 나머지 메뉴는 작아지면서 점점 투명해지다가 사라진다.
	 * (+)버튼은 닫힌 상태로 돌아간다.
	 * 
	 * @author kth
	 * @version 1.0
	 * @date 2011. 12. 22.
	 * </PRE>
	 * @param index 선택된 버튼의 index
	 */
	private void startSubButtonSelectedAnimation(int index) {
		
		for(int i = 0 ; i < buttons.size() ; i++) {
			if(index == i) {
				PathButton view = buttons.get(i);
				
				AnimationSet animation = new AnimationSet(false);
				
				//실제 버튼이 이동한것이 아니다. 다른 애니메이션을 실행시키기 전에 미리 이동시켜야한다. 
				Animation translate = new TranslateAnimation(
		        		0.0f, view.getXOffset()
		        		, 0.0f, view.getYOffset());
				translate.setDuration(0);
				
				Animation scale = new ScaleAnimation(
						1.0f, 2.5f
						, 1.0f, 2.5f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(sub_select_duration);
				
				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(sub_select_duration);
				
				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);
				
				view.startAnimation(animation);
			} else {
				PathButton view = buttons.get(i);
				
				AnimationSet animation = new AnimationSet(false);
				
				//실제 버튼이 이동한것이 아니다. 다른 애니메이션을 실행시키기 전에 미리 이동시켜야한다.
				Animation translate = new TranslateAnimation(
		        		0.0f, view.getXOffset()
		        		, 0.0f, view.getYOffset());
				translate.setDuration(0);
				
				Animation scale = new ScaleAnimation(
						1.0f, 0.0f
						, 1.0f, 0.0f
						, Animation.RELATIVE_TO_SELF, 0.5f
						, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(sub_select_duration);
				
				Animation alpha = new AlphaAnimation(1.0f, 0.0f);
				alpha.setDuration(sub_select_duration);
				
				animation.addAnimation(scale);
				animation.addAnimation(translate);
				animation.addAnimation(alpha);
				
				view.startAnimation(animation);
			}
		}
		
		if(isMenuOpened) {
			//(+)버튼은 닫힌 상태로 돌아가야한다.
			isMenuOpened = false;
			
			Animation rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
			
			rotate.setInterpolator(AnimationUtils.loadInterpolator(this,
	                        android.R.anim.anticipate_overshoot_interpolator));
			rotate.setFillAfter(true);
			rotate.setDuration(sub_select_duration);
			plus.startAnimation(rotate);
			
			//2012.1.17 일부단말에서 클릭 안되는 문제 해결을 위해 수정
			for(int i = 0 ; i < buttons.size() ; i++) {
				movePathButton(i, false);
			}			
		}
	}
	
	/**
	 * <PRE>
	 * Comment : <br>
	 * 하위 메뉴가 열리거나 닫힐때 애니메이션 처리
	 * 하위 메뉴는 회전하면서 이동한다.
	 * Dynamic한 효과를 위해 Interpolater를 사용한다.
	 * @author kth
	 * @version 1.0
	 * @date 2011. 12. 22.
	 * </PRE>
	 * @param index 버튼 index
	 * @param totalcount
	 * @param open
	 */
	private void startSubButtonAnimation(final int index, final boolean open) {
	
		PathButton view = buttons.get(index);
		
		float endX = length * FloatMath.cos(
				(float) (Math.PI * 1/2 * (index)/(buttons.size()-1))
				);
		float endY = length * FloatMath.sin(
				(float) (Math.PI * 1/2 * (index)/(buttons.size()-1))
				);
		
		AnimationSet animation = new AnimationSet(false);
		Animation translate;
		Animation rotate = new RotateAnimation(
				0, 360
				, Animation.RELATIVE_TO_SELF, 0.5f
				, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(sub_duration);
		rotate.setRepeatCount(1);
		rotate.setInterpolator(AnimationUtils.loadInterpolator(this,
	            android.R.anim.accelerate_interpolator));
		
		if(open) {
			translate = new TranslateAnimation(
	        		0.0f, endX
	        		, 0.0f, -endY);
	        translate.setDuration(sub_duration);
	        translate.setInterpolator(AnimationUtils.loadInterpolator(this,
	                android.R.anim.overshoot_interpolator));
	        translate.setStartOffset(sub_offset*index);
			
	        view.setOffset(endX, -endY);
		} else {
			translate = new TranslateAnimation(
					0, -endX
	        		, 0, endY);
			translate.setDuration(sub_duration);
			translate.setStartOffset(sub_offset*(buttons.size()-(index+1)));
			translate.setInterpolator(AnimationUtils.loadInterpolator(this,
	                android.R.anim.anticipate_interpolator));
			
			view.setOffset(-endX, endY);
		}
	    
		//애니메이션이 끝나고 그자리에 남아있어야 한다.
		//2012.1.17 일부단말에서 클릭 안되는 문제 해결을 위해 수정
//		animation.setFillAfter(true);
		
		//순서가 바뀌면 안된다.
		animation.addAnimation(rotate);
		animation.addAnimation(translate);
		
		//2012.1.17 일부단말에서 클릭 안되는 문제 해결을 위해 수정
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				movePathButton(index, open);
			}
		});
		view.startAnimation(animation);
	}
	
	//2012.1.17 일부단말에서 클릭 안되는 문제 해결을 위해 수정
	private int orgLeftMargin = -1;
	private int orgBottomMargin = -1;
	
	private void movePathButton(int index, boolean open) {
		PathButton view = buttons.get(index);
		
		float endX = length * FloatMath.cos(
				(float) (Math.PI * 1/2 * (index)/(buttons.size()-1))
				);
		float endY = length * FloatMath.sin(
				(float) (Math.PI * 1/2 * (index)/(buttons.size()-1))
				);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
		if(orgLeftMargin == -1) {
			orgLeftMargin = params.leftMargin;
			orgBottomMargin = params.bottomMargin;
		}
		
		if(open) {
			params.leftMargin = orgLeftMargin+(int)endX;
			params.bottomMargin = orgBottomMargin+(int)endY;
		} else {
			params.leftMargin = orgLeftMargin;
			params.bottomMargin = orgBottomMargin;
		}
		
		view.setLayoutParams(params);
	}
	/**
	 * <PRE>
	 * Comment : <br>
	 * (+)버튼을 눌렀을때 애니메이션 처리
	 * (+)버튼은 45도 회전하며
	 * 하위 메뉴는 startSubButtonAnimation를 각각 호출하여 화면에 나오게된다.
	 * @author kth
	 * @version 1.0
	 * @date 2011. 12. 22.
	 * </PRE>
	 * @param open
	 */
	private void startMenuAnimation(boolean open) {
		Animation rotate;
		
		if(open)
			rotate = new RotateAnimation(
					0, 45
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
		else
			rotate = new RotateAnimation(
					-45, 0
					, Animation.RELATIVE_TO_SELF, 0.5f
					, Animation.RELATIVE_TO_SELF, 0.5f);
		
		rotate.setInterpolator(AnimationUtils.loadInterpolator(this,
	                    android.R.anim.anticipate_overshoot_interpolator));
		rotate.setFillAfter(true);
		rotate.setDuration(duration);
		plus.startAnimation(rotate);
		
		for(int i = 0 ; i < buttons.size() ; i++) {
			startSubButtonAnimation(i, open);
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case  R.id.camera:
		{
			Toast.makeText(context, "Camera Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(0);
		}
		break;
		case R.id.with:
		{
			Toast.makeText(context, "With Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(1);
		}
		break;
		case R.id.place:
		{
			Toast.makeText(context, "Place Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(2);
		}
		break;
		case R.id.music:
		{
			Toast.makeText(context, "Music Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(3);
		}
		break;
		case R.id.thought:
		{
			Toast.makeText(context, "Thought Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(4);
		}
		break;
		case R.id.sleep:
		{
			Toast.makeText(context, "Sleep Clicked", Toast.LENGTH_SHORT).show();
			startSubButtonSelectedAnimation(5);
		}
		break;
		}
	}
}
