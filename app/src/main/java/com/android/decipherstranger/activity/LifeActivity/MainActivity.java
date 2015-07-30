package com.android.decipherstranger.activity.LifeActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.util.MyStatic;

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
 * @Date 2015/7/30 9:54
 * @e-mail penghaitao918@163.com
 */
public class MainActivity extends BaseActivity {

    private RelativeLayout topLayout = null;
    private ListView dataList = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_main);
        this.init();
        this.setData();
    }

    private void init() {
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

    public void fixListViewHeight(ListView listView) {
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
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
        }
    }

    public void LifeMainOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            /*广告*/
            case R.id.advertisement:
                Uri uri = Uri.parse(MyStatic.ADVERTISEMENT);
                Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent0);
                break;
            /*删除广告*/
/*            case R.id.exitButton:
                RelativeLayout layout = (RelativeLayout) super.findViewById(R.id.advertLayout);
                layout.setVisibility(View.GONE);
                break;*/
            /*我的活动*/
            case R.id.mylife:
                break;
            /*发起*/
            case R.id.sendLife:
                Intent intent2 = new Intent(this,SendActivity.class);
                startActivity(intent2);
                break;
            /*参团*/
            case R.id.partakeLife:
                Intent intent3 = new Intent(this,PartakeActivity.class);
                startActivity(intent3);
                break;
            /*分享*/
            case R.id.share:
                Intent intent4 = new Intent(this,ShareActivity.class);
                startActivity(intent4);
                break;
        }
    }
}