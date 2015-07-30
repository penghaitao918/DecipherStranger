package com.android.decipherstranger.activity.LifeActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.MyScrollView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　?●　?　●　　??〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>? ?_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　?＜| ＼＼        比卡丘~
 * 　 ヽ_?　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`?―＿
 *
 * @author penghaitao
 * @version V1.0
 * @Date 2015/7/27 17:55
 * @e-mail 785351408@qq.com
 */
public class PartakeActivity extends BaseActivity implements MyScrollView.OnScrollListener {

    private RelativeLayout topLayout = null;
    private ListView dataList = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> list = null;

    private MyScrollView myScrollView;
    private LinearLayout mBuyLayout;
    private WindowManager mWindowManager;
    /* 手机屏幕宽度 */
    private int screenWidth;
    /** 悬浮框View  */
    private static View suspendView;
    /** 悬浮框的参数 */
    private static WindowManager.LayoutParams suspendLayoutParams;
    /** 排序布局的高度 */
    private int buyLayoutHeight;
    /** myScrollView与其父类布局的顶部距离 */
    private int myScrollViewTop;
    /** 排序布局与其父类布局的顶部距离 */
    private int buyLayoutTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_partake);

        this.init();
        this.setData();
    }

    private void init() {

        myScrollView = (MyScrollView) findViewById(R.id.view);
        mBuyLayout = (LinearLayout) findViewById(R.id.sort);

        myScrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();

        this.list = new ArrayList<Map<String, Object>>();
        this.dataList = (ListView) super.findViewById(R.id.listView);
        this.dataList.setOnItemClickListener(new OnItemClickListenerImpl());

        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();
    }

    private void setData() {
        this.list.addAll(this.selectAll());
        this.simpleAdapter = new SimpleAdapter(this,
                this.list,
                R.layout.list_item_life,
                new String[] {MyStatic.LIFE_CLASS, MyStatic.LIFE_NAME, MyStatic.LIFE_TIME, MyStatic.LIFE_SPACE},
                new int[] {R.id.life_class, R.id.life_name, R.id.life_time, R.id.life_space}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.dataList.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
        simpleAdapter.notifyDataSetChanged();
        /*动态计算ListView的高度*/
        this.fixListViewHeight(dataList);
    }

    private class ViewBinderImpl implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            // TODO Auto-generated method stub
            if(view instanceof ImageView && data instanceof Bitmap){
                ImageView i = (ImageView)view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }
    }

    private ArrayList<Map<String, Object>> selectAll (){
        Bitmap  bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; ++ i) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.LIFE_CLASS, bitmap);
            map.put(MyStatic.LIFE_NAME, "一起去吃饭啊");
            map.put(MyStatic.LIFE_TIME, "2015/07/28 9:16");
            map.put(MyStatic.LIFE_SPACE, "长春工业大学");
            all.add(map);
        }
        return all;
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PartakeActivity.this, DetailsActivity.class);
            startActivity(intent);
        }
    }

    private void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; ++ index) {
            View listViewItem = listAdapter.getView(index , null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            buyLayoutHeight = mBuyLayout.getHeight();
            buyLayoutTop = mBuyLayout.getTop();

            myScrollViewTop = myScrollView.getTop();
        }
    }

    /**
     * 滚动的回调方法，当滚动的Y距离大于或者等于 购买布局距离父类布局顶部的位置，就显示购买的悬浮框
     * 当滚动的Y的距离小于 购买布局距离父类布局顶部的位置加上购买布局的高度就移除购买的悬浮框
     *
     */
    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= buyLayoutTop){
            if(suspendView == null){
                showSuspend();
            }
        }else if(scrollY <= buyLayoutTop + buyLayoutHeight){
            if(suspendView != null){
                removeSuspend();
            }
        }
    }


    /**
     * 显示购买的悬浮框
     */
    private void showSuspend(){
        if(suspendView == null){
            suspendView = LayoutInflater.from(this).inflate(R.layout.winpop_life_sort, null);
            if(suspendLayoutParams == null){
                suspendLayoutParams = new WindowManager.LayoutParams();
                suspendLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                suspendLayoutParams.gravity = Gravity.TOP;
                suspendLayoutParams.width = screenWidth;
                suspendLayoutParams.height = buyLayoutHeight;
                suspendLayoutParams.x = 0;
                suspendLayoutParams.y = myScrollViewTop;
            }
        }

        mWindowManager.addView(suspendView, suspendLayoutParams);
    }

    /**
     * 移除购买的悬浮框
     */
    private void removeSuspend(){
        if(suspendView != null){
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
    }

    public void LifePartakeOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            /*广告*/
            case R.id.advertisement:
                break;
            /*删除广告*/
/*            case R.id.exitButton:
                RelativeLayout layout = (RelativeLayout) super.findViewById(R.id.advertLayout);
                layout.setVisibility(View.GONE);
                break;*/
            /*类别*/
            case R.id.lifeClass:
                break;
            /*距离*/
            case R.id.distance:
                break;
            /*时间*/
            case R.id.time:
                break;
            /*欢迎度*/
            case R.id.favorite:
                break;
        }
    }

}
