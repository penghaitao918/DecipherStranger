package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.db.LifeList;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.MyScrollView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　ｲ●　､　●　　⊂⊃〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>ｰ ､_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　ﾉ＜| ＼＼        比卡丘~
 * 　 ヽ_ﾉ　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`ｰ―＿
 *
 * @author penghaitao
 * @version V1.1
 * @Date 2015/7/27 17:55
 * @e-mail 785351408@qq.com
 */
public class PartakeActivity extends BaseActivity implements MyScrollView.OnScrollListener {

    private RelativeLayout topLayout = null;
    private ListView dataList = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> list = null;

    private SQLiteOpenHelper helper = null;
    private LifeList lifeList = null;
    private LifePartakeBroadcastReceiver receiver = null;

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
        this.initData();
        this.getData();
        this.LifePartakeBroadcas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.lifeList.clear();
        super.unregisterReceiver(PartakeActivity.this.receiver);
    }

    private void init() {

        this.helper = new DATABASE(this);
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

    private void initData() {
        this.lifeList = new LifeList(this.helper.getReadableDatabase());
        this.list.addAll(lifeList.selectAll(this,3));
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
    //    this.fixListViewHeight(dataList);
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

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int lifeId = (int) list.get(position).get(MyStatic.LIFE_ID);
            int lifeType = (int) list.get(position).get(MyStatic.LIFE_CLASSINT);
            Intent intent = new Intent(PartakeActivity.this, DetailsActivity.class);
            intent.putExtra(MyStatic.LIFE_ID, lifeId);
            intent.putExtra(MyStatic.LIFE_CLASSINT,lifeType);
            startActivity(intent);
        }
    }

/*    private void fixListViewHeight(ListView listView) {
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
    }*/

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

    private void getData() {
        /**
         * 此处提交获取服务器数据请求
         */
    }

    //    lifeList.setData(id, name, type, distance, favorite, space, date);
    private void setData (int lifeId, String name, int type, double distance, int favorite, String space, String date){
        Bitmap bitmap = null;
        switch (type) {
            // 美食
            case 1:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
                break;
            // 旅游
            case 2:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
                break;
            // 休闲娱乐
            case 3:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
                break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.LIFE_ID, lifeId);
        map.put(MyStatic.LIFE_CLASSINT, type);
        map.put(MyStatic.LIFE_DISTANCE,distance);
        map.put(MyStatic.LIFE_FAVORITE, favorite);
        map.put(MyStatic.LIFE_TIME, date);

        map.put(MyStatic.LIFE_CLASS, bitmap);
        map.put(MyStatic.LIFE_NAME, name);
        map.put(MyStatic.LIFE_SPACE, space);
        list.add(map);
        //    System.out.println(list);
    }

    private void sort(final String flag) {
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                Map<String, Object> map1 = (Map<String, Object>) o1;
                Map<String, Object> map2 = (Map<String, Object>) o2;
                //  降序排序
                return ((String) map1.get(flag)).compareTo((String) map2.get(flag));
            }
        };
        Collections.sort(list, comp);
        simpleAdapter.notifyDataSetChanged();

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
                sort(MyStatic.LIFE_CLASSINT);
                break;
            /*距离*/
            case R.id.distance:
                sort(MyStatic.LIFE_DISTANCE);
                break;
            /*时间*/
            case R.id.time:
                sort(MyStatic.LIFE_TIME);
                break;
            /*欢迎度*/
            case R.id.favorite:
                sort(MyStatic.LIFE_FAVORITE);
                break;
        }
    }

    private void LifePartakeBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifePartakeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_PARTAKE);
        this.registerReceiver(receiver, filter);
    }

    public class LifePartakeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_PARTAKE)) {
                // TODO 将获取的数据赋值到本地
            //    insert(id, name, type, distance, favorite, space, date);
            }
        }
    }
}
