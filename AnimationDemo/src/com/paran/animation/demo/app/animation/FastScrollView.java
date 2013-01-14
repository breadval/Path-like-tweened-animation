/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.paran.animation.demo.app.animation;

import java.util.Date;
import java.util.HashMap;

import com.paran.animation.demo.app.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * FastScrollView is meant for embedding {@link ListView}s that contain a large number of 
 * items that can be indexed in some fashion. It displays a special scroll bar that allows jumping
 * quickly to indexed sections of the list in touch-mode. Only one child can be added to this 
 * view group and it must be a {@link ListView}, with an adapter that is derived from 
 * {@link BaseAdapter}.
 * 
 * <p>This file was copied from the Contacts application.  In the future it
 * should be provided as a standard part of the Android framework.
 */
/**
 * com.paran.animation.demo.app.animation.FastScrollView.java - Creation date: 2012. 1. 5. <br>
 *
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class FastScrollView extends FrameLayout 
        implements OnScrollListener, OnHierarchyChangeListener {

    /**
     * Time Line Thumb의 Layout
     */
    private RelativeLayout mThumbLayout;
    /**
     * Time Line Thumb Layout이 가지고 있는 아날로그 시계
     */
    private AnalogClock mClock;
    /**
     * Time Line Thumb Layout이 가지고 있는 시간이 표시될 텍스트
     */
    private TextView mText;
    
    /**
     * Overlay 배경이 될 Drawable
     */
    private Drawable mOverlayDrawable;

    /**
     * Thumb의 높이
     */
    private int mThumbH;
    /**
     * Thumb의 넓이
     */
    private int mThumbW;
    /**
     * Thumb의 Y 위치
     */
    private int mThumbY;

    /**
     * Section Indexer을 사용할 경우 Section 정보를 표시해 줄 Overlay의 위치
     */
    private RectF mOverlayPos;
    
    /**
     * Section 정보를 표시해 줄 Overlay의 크기이며 Hard coding되어 있다.
     */
    private int mOverlaySize = 104;

    /**
     * Thumb가 Drag되고 있는지 체크하기위한 Flag
     */
    private boolean mDragging;
    
    /**
     * FastScrollView가 가지고 있는 ListView
     */
    private ListView mList;
    /**
     * Scroll이 되고 있는지 체크하기 위한 Flag
     */
    private boolean mScrollCompleted;
    /**
     * First Visible Item의 Index
     */
    private int mVisibleItem;
    /**
     * Section Indexer를 사용할경우 Section 정보를 표시해주기위한 Paint 객체
     */
    private Paint mPaint;
    /**
     * ListView에서 Header를 사용할 경우 Header만큼 Offset을 설정 
     */
    private int mListOffset;
    
    /**
     * Section 정보의 Array
     */
    private Object [] mSections;
    /**
     * Section 정보가 표시될 String
     */
    private String mSectionText;
    /**
     * Section 정보가 표시되고 있는지 체크하기 위한 flag
     */
    private boolean mDrawOverlay;
    
    /**
     * Scroll이나 Drag가 끝났을 때, Thumb를 Fade out처리 하기 위한 handler
     */
    private Handler mHandler = new Handler();
    
    /**
     * List의 Adapter
     */
    private BaseAdapter mListAdapter;

    /**
     * 아이템이 추가된적이 있는지 체크하여, 처음으로 체크된 경우에만 첫번째 인덱스의 내용으로 Thumb 레이아웃의 시간을 표시 해주기위한 Flag
     */
    private boolean mAddedBefore;

    /**
     * 스크롤이 멈췄을 때, Thumb를 Fade out 처리하기 위한 Runnable
     */
    private ScrollFade mScrollFade;
    /**
     * Thumb의 Click처리를 위한 Flag
     */
    private boolean mThumbVisible;
    
    interface SectionIndexer {
        Object[] getSections();
        
        int getPositionForSection(int section);
        
        int getSectionForPosition(int position);
    }
    
    public FastScrollView(Context context) {
        super(context);

        init(context);
    }


    public FastScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context);
    }

    public FastScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    /**
     * <PRE>
     * Comment : <br>
     * Thumb의 크기를 가지고 온다. 기존 샘플 코드에서는 Hard coding되어 있었으나, measureView를 통해 정확한 Thumb 크기를 가지고 온다. 
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     */
    private void setThumbSize() {
    	measureView(mThumbLayout);
    	
        mThumbW = mThumbLayout.getMeasuredWidth(); //mCurrentThumb.getIntrinsicWidth();
        mThumbH = mThumbLayout.getMeasuredHeight(); //mCurrentThumb.getIntrinsicHeight();
    }
    
    /**
     * <PRE>
     * Comment : <br>
     * View의 크기를 계산한다. 
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    /**
     * <PRE>
     * Comment : <br>
     * Thumb의 위치를 설정한다. Layout의 top margin을 변경하여 위치를 조정한다.
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     * @param position Thumb의 새로운 Y좌표
     */
    public void setThumbPosition(int position) {
    	FrameLayout.LayoutParams plControl = (FrameLayout.LayoutParams) mThumbLayout.getLayoutParams();
    	plControl.topMargin = position;
    	mThumbLayout.setLayoutParams(plControl);
    }
    
    /**
     * <PRE>
     * Comment : <br>
     * ListView를 초기화 한다. 
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     * @param context
     */
    private void init(Context context) {
        // Get both the scrollbar states drawables
        final Resources res = context.getResources();
        
        mOverlayDrawable = res.getDrawable(android.R.drawable.alert_dark_frame);
        
        mScrollCompleted = true;
        setWillNotDraw(false);
        
        // Need to know when the ListView is added
        setOnHierarchyChangeListener(this);
        
        mOverlayPos = new RectF();
        mScrollFade = new ScrollFade();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mOverlaySize / 2);
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        // If user is dragging the scroll bar, draw the alphabet overlay
        if (mDragging && mDrawOverlay) {
            mOverlayDrawable.draw(canvas);
            final Paint paint = mPaint;
            float descent = paint.descent();
            final RectF rectF = mOverlayPos;
            canvas.drawText(mSectionText, (int) (rectF.left + rectF.right) / 2,
                    (int) (rectF.bottom + rectF.top) / 2 + mOverlaySize / 4 - descent, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        final RectF pos = mOverlayPos;
        pos.left = (w - mOverlaySize) / 2;
        pos.right = pos.left + mOverlaySize;
        pos.top = h / 10; // 10% from top
        pos.bottom = pos.top + mOverlaySize;
        mOverlayDrawable.setBounds((int) pos.left, (int) pos.top,
                (int) pos.right, (int) pos.bottom);
    }
    
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
	
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, 
            int totalItemCount) {
        
        if (totalItemCount - visibleItemCount > 0 && !mDragging) {
            mThumbY = ((getHeight() - mThumbH) * firstVisibleItem) / (totalItemCount - visibleItemCount);
            setThumbPosition(mThumbY);
        }
        mScrollCompleted = true;
        if (firstVisibleItem == mVisibleItem) {
            return;
        }
        
        startThumbAnimation(true);
        mVisibleItem = firstVisibleItem;
        
        updateThumbDisplay(firstVisibleItem);
        
        mHandler.removeCallbacks(mScrollFade);
        
        if (!mDragging) {
            mHandler.postDelayed(mScrollFade, 1500);
        }
    }

    /**
     * <PRE>
     * Comment : <br>
     * ListItem의 Position의 아이템에 해당하는 시간으로 Thumb를 갱신한다. 
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     * @param firstVisibleItem
     */
    private void updateThumbDisplay(int firstVisibleItem) {
    	if(mList == null)
    		return;
    	
    	HashMap<String, Object> map = (HashMap<String, Object>)mList.getItemAtPosition(firstVisibleItem);
        
    	if(map == null)
    		return;
    	
        Date date = (Date)map.get("item");
        mClock.setFixedTime(date);
        
        String times = DateFormat.format("a hh:mm", date).toString();
        mText.setText(times);
    }
    
    /**
     * <PRE>
     * Comment : <br>
     * ListView에서 Adapter를 가지고 온다. Section을 표시해주기 위함.
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     */
    private void getSections() {
        Adapter adapter = mList.getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            mListOffset = ((HeaderViewListAdapter)adapter).getHeadersCount();
            adapter = ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        }
        if (adapter instanceof SectionIndexer) {
            mListAdapter = (BaseAdapter) adapter;
            mSections = ((SectionIndexer) mListAdapter).getSections();
        }
    }
    
    
    /* (non-Javadoc)
     * @see android.view.ViewGroup.OnHierarchyChangeListener#onChildViewAdded(android.view.View, android.view.View)
     */
    public void onChildViewAdded(View parent, View child) {
        if (child instanceof ListView) {
            mList = (ListView)child;
            
            mList.setOnScrollListener(this);
            mList.setOnHierarchyChangeListener(this);
            getSections();
        } else if(child instanceof RelativeLayout) {
        	if(child.getId() == R.id.thumb) {
        		mThumbLayout = (RelativeLayout)child;
        		
        		for(int i = 0; i < mThumbLayout.getChildCount(); i++) {
        			View childOfThumb = mThumbLayout.getChildAt(i);
        			if(childOfThumb instanceof AnalogClock) {
        	        	if(childOfThumb.getId() == R.id.clock) {
        	        		mClock = (AnalogClock)childOfThumb;
        	        	}
        	        } else if(childOfThumb instanceof TextView) {
        	        	if(childOfThumb.getId() == R.id.text) {
        	        		mText = (TextView)childOfThumb;
        	        	}
        	        }
        		}
        		
        		setThumbSize();
        	}
        } else {
        	if(parent instanceof ListView) {
        		if(!mAddedBefore) {
        			mAddedBefore = true;
        			updateThumbDisplay(0);
        		}
        	}
        }
    }

    public void onChildViewRemoved(View parent, View child) {
        if (child == mList) {
            mList = null;
            mListAdapter = null;
            mSections = null;
        } else if(child == mThumbLayout) {
        	mClock = null;
        	mText = null;
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mThumbVisible && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (ev.getX() > getWidth() - mThumbW && ev.getY() >= mThumbY &&
                    ev.getY() <= mThumbY + mThumbH) {
                mDragging = true;
                return true;
            }            
        }
        return false;
    }

    /**
     * <PRE>
     * Comment : <br>
     * position에 해당하는 List의 Item으로 scroll한다.
     * @author Brad
     * @version 1.0
     * @date 2012. 1. 5.
     * </PRE>
     * @param position
     */
    private void scrollTo(float position) {
        int count = mList.getCount();
        mScrollCompleted = false;
        final Object[] sections = mSections;
        int sectionIndex;
        if (sections != null && sections.length > 1) {
            final int nSections = sections.length;
            int section = (int) (position * nSections);
            if (section >= nSections) {
                section = nSections - 1;
            }
            sectionIndex = section;
            final SectionIndexer baseAdapter = (SectionIndexer) mListAdapter;
            int index = baseAdapter.getPositionForSection(section);
            
            // Given the expected section and index, the following code will
            // try to account for missing sections (no names starting with..)
            // It will compute the scroll space of surrounding empty sections
            // and interpolate the currently visible letter's range across the
            // available space, so that there is always some list movement while
            // the user moves the thumb.
            int nextIndex = count;
            int prevIndex = index;
            int prevSection = section;
            int nextSection = section + 1;
            // Assume the next section is unique
            if (section < nSections - 1) {
                nextIndex = baseAdapter.getPositionForSection(section + 1);
            }
            
            // Find the previous index if we're slicing the previous section
            if (nextIndex == index) {
                // Non-existent letter
                while (section > 0) {
                    section--;
                     prevIndex = baseAdapter.getPositionForSection(section);
                     if (prevIndex != index) {
                         prevSection = section;
                         sectionIndex = section;
                         break;
                     }
                }
            }
            // Find the next index, in case the assumed next index is not
            // unique. For instance, if there is no P, then request for P's 
            // position actually returns Q's. So we need to look ahead to make
            // sure that there is really a Q at Q's position. If not, move 
            // further down...
            int nextNextSection = nextSection + 1;
            while (nextNextSection < nSections &&
                    baseAdapter.getPositionForSection(nextNextSection) == nextIndex) {
                nextNextSection++;
                nextSection++;
            }
            // Compute the beginning and ending scroll range percentage of the
            // currently visible letter. This could be equal to or greater than
            // (1 / nSections). 
            float fPrev = (float) prevSection / nSections;
            float fNext = (float) nextSection / nSections;
            index = prevIndex + (int) ((nextIndex - prevIndex) * (position - fPrev) 
                    / (fNext - fPrev));
            // Don't overflow
            if (index > count - 1) index = count - 1;
            
            mList.setSelectionFromTop(index + mListOffset, 0);
        } else {
            int index = (int) (position * count);
            mList.setSelectionFromTop(index + mListOffset, 0);
            sectionIndex = -1;
        }

        if (sectionIndex >= 0) {
            String text = mSectionText = sections[sectionIndex].toString();
            mDrawOverlay = (text.length() != 1 || text.charAt(0) != ' ') &&
                    sectionIndex < sections.length;
        } else {
            mDrawOverlay = false;
        }
    }

    private void cancelFling() {
        // Cancel the list fling
        MotionEvent cancelFling = MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0);
        mList.onTouchEvent(cancelFling);
        cancelFling.recycle();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            if (me.getX() > getWidth() - mThumbW
                    && me.getY() >= mThumbY 
                    && me.getY() <= mThumbY + mThumbH) {
                
                mDragging = true;
                if (mListAdapter == null && mList != null) {
                    getSections();
                }

                cancelFling();
                return true;
            }
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
            if (mDragging) {
                mDragging = false;
                final Handler handler = mHandler;
                handler.removeCallbacks(mScrollFade);
                handler.postDelayed(mScrollFade, 1500);
                return true;
            }
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            if (mDragging) {
                final int viewHeight = getHeight();
                mThumbY = (int) me.getY() - mThumbH + 10;
                if (mThumbY < 0) {
                    mThumbY = 0;
                } else if (mThumbY + mThumbH > viewHeight) {
                    mThumbY = viewHeight - mThumbH;
                }
                setThumbPosition(mThumbY);
                // If the previous scrollTo is still pending
                if (mScrollCompleted) {
                	float position = (float) mThumbY / (viewHeight - mThumbH);
                	Log.e("brad", "position: "+ position);
                    scrollTo((float) mThumbY / (viewHeight - mThumbH));
                }
                return true;
            }
        }
        
        return super.onTouchEvent(me);
    }
    
	/**
	 * <PRE>
	 * Comment : <br>
	 * Thumb가 나타나거나 사라지는 Animation을 시작한다.
	 * @author Brad
	 * @version 1.0
	 * @date 2012. 1. 5.
	 * </PRE>
	 * @param bShow
	 */
	private void startThumbAnimation(boolean bShow) {
		AnimationSet set = new AnimationSet(false);
		Animation preTranslate;
		Animation translate;
		Animation alpha;
		
		if(bShow) {
			if(mThumbVisible)
				return;
		
			mThumbVisible = true;
			
			preTranslate = new TranslateAnimation(
		    		0.0f, 30.0f
		    		, 0.0f, 0.0f);
			preTranslate.setDuration(0);
			
			mThumbLayout.setVisibility(VISIBLE);
			translate = new TranslateAnimation(
		    		30.0f, 0.0f
		    		, 0.0f, 0.0f);
			
			alpha = new AlphaAnimation(0.0f, 1.0f);
		} else {
			if(!mThumbVisible)
				return;
			
			mThumbVisible = false;
			
			translate = new TranslateAnimation(
		    		0.0f, 30.0f
		    		, 0.0f, 0.0f);
			
			alpha = new AlphaAnimation(1.0f, 0.0f);
		}
		translate.setDuration(500);
		alpha.setDuration(500);
		set.setFillAfter(true);
		set.addAnimation(translate);
		set.addAnimation(alpha);

		mThumbLayout.startAnimation(set);
	}
	
	public class ScrollFade implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startThumbAnimation(false);
		}
		
	}
}
