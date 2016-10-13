package viewpager.villa.com.newchannel;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class NewChannlActivity extends AppCompatActivity {
    private String[] mSelectedChannels = {"北京", "中国", "国际", "体育", "育人", "生活", "旅游", "军事", "汽车"};

    private String[] mUnSeletedChannels = {"时尚", "娱乐", "呵呵", "嘿嘿", "羞羞", "巴拿马"};
    private GridLayout mSelectedChannel;
    private GridLayout mUnSelectedChannel;
    private int itemMargin;
    private Rect[] mRects;
    private static final int NOT_AT_GRID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channl);
        initView();
        initSelectedChannel();
        initUnSelectedChannel();
        setEvent();
    }

    private void initRects() {
        mRects = new Rect[mSelectedChannel.getChildCount()];
        for (int i = 0; i < mSelectedChannel.getChildCount(); i++) {
            View child = mSelectedChannel.getChildAt(i);
            mRects[i] = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
    }

    //为选中的gridlayout设置拖拽的监听
    private void setEvent() {
        mSelectedChannel.setOnDragListener(mOnDragListener);
    }

    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                //拖拽结束
                case DragEvent.ACTION_DRAG_ENDED:
                    mDragView.setSelected(false);
                    break;
                //进入父容器的范围
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                //不再父容器范围内时调用
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                //拖拽位置改变过程中调用
                case DragEvent.ACTION_DRAG_LOCATION:
                    //实时的获取在gridlyout中的拖拽进入的孩子
                    int index = getChildIndex(event.getX(), event.getY());
                    //获取当前的需要移动到的目标孩子
                    View mTagart = mSelectedChannel.getChildAt(index);
                    //如果移动到了grid孩子中,就移除这个孩子
                    if (index != NOT_AT_GRID && mTagart != mDragView) {
                        //移动掉这个孩子
                        mSelectedChannel.removeView(mDragView);
                        //再把这个还在添加到移动到的位置,但是这里有个问题是移动到了自身的位置,所以前面需要加个判断
                        mSelectedChannel.addView(mDragView, index);
                    }
                    break;
                //拖拽开始,初始化矩形数组,以便判断孩子是不是在gridlayout范围内
                case DragEvent.ACTION_DRAG_STARTED:
                    initRects();
                    break;
                //拖拽终止
                case DragEvent.ACTION_DROP:
                    break;

            }
            return true;
        }
    };

    /**
     * 初始化没有选中的item
     */
    private void initUnSelectedChannel() {
        for (int i = 0; i < mUnSeletedChannels.length; i++) {
            TextView item = getTextView(mUnSeletedChannels[i]);
            item.setOnClickListener(mUnSelectedOnClickListener);
            mUnSelectedChannel.addView(item, 0);
        }
    }

    private View.OnClickListener mUnSelectedOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUnSelectedChannel.removeView(v);
            TextView mTextView = (TextView) v;
            TextView textView = getTextView(mTextView.getText().toString());
            addSelectedItem(textView);

        }
    };

    /**
     * 初始化选中的item
     */
    private void initSelectedChannel() {
        for (int i = 0; i < mSelectedChannels.length; i++) {
            TextView item = getTextView(mSelectedChannels[i]);
            addSelectedItem(item);

        }
    }

    private void addSelectedItem(TextView item) {
        //为创建的每一个item设置长按点击事件
        item.setOnLongClickListener(mOnLongClickListener);
        item.setOnClickListener(mSelectedOnclickListener);
        mSelectedChannel.addView(item);
    }

    private View.OnClickListener mSelectedOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectedChannel.removeView(v);
            TextView mTextView = (TextView) v;
            addUnselectedItem(mTextView);
        }
    };

    private void addUnselectedItem(TextView mTextView) {
        TextView textView = getTextView(mTextView.getText().toString());
        textView.setOnClickListener(mUnSelectedOnClickListener);
        mUnSelectedChannel.addView(textView);
    }

    /**
     * 初始化view
     */
    private void initView() {
        mSelectedChannel = (GridLayout) findViewById(R.id.selected_channel);
        mUnSelectedChannel = (GridLayout) findViewById(R.id.unSelected_channel);
        //获取dimen值
        itemMargin = getResources().getDimensionPixelOffset(R.dimen.item_margin);
    }


    private View mDragView;
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            /**
             *  * @param data A {@link android.content.ClipData} object pointing to the data to be
             * transferred by the drag and drop operation.
             * shadowBuilder,拖拽时的阴影
             * myLocalState, This Object is put into every DragEvent object sent by the system during the
             * current drag.
             * 最后一个标志位,我们不需要标志位,设为0
             */
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
            mDragView = v;
            mDragView.setSelected(true);
            return true;//表示我要消费这个事件
        }
    };

    /**
     * 获取一个item的方法
     *
     * @param msg
     * @return
     */
    @NonNull
    private TextView getTextView(String msg) {
        TextView item = new TextView(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        //这里不能直接去获取gridlayout的宽,因为这个方法在oncreate方法有调用,view还没有绘制完成,所以直接去获取屏幕的宽度像素
        layoutParams.width = widthPixels / 4 - 2 * itemMargin;
        layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins(itemMargin, itemMargin, itemMargin, itemMargin);
        item.setLayoutParams(layoutParams);
        //设置每个item的gravity,中心,
        item.setGravity(Gravity.CENTER);
        item.setBackgroundResource(R.drawable.item_back_selector);
        item.setTextColor(getResources().getColorStateList(R.color.item_text_selector));
        item.setText(msg);
        return item;
    }

    public int getChildIndex(float x, float y) {
        for (int i = 0; i < mRects.length; i++) {
            if (mRects[i].contains((int) x, (int) y)) {
                return i;
            }
        }
        return NOT_AT_GRID;
    }
}
