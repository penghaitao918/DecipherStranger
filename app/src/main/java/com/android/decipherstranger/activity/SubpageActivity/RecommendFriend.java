package com.android.decipherstranger.activity.SubpageActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.MyStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * *
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
 * Created by WangXin on 2015/8/11 0011.
 */
public class RecommendFriend extends BaseActivity {

    private ListView recommend = null;
    private SimpleAdapter adapter;
    private RecommendBroadcastReceiver receiver = null;
    private MyApplication application = null;
    private ArrayList<Map<String,Object>>dataList = null;
    private ImageButton back = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            recommend.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_recommend);
        application = MyApplication.getInstance();
        recommendBroadcas();
        initView();
        getData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(RecommendFriend.this.receiver);
        for(int i = 0;i < dataList.size(); i ++) {
            ((Bitmap) dataList.get(0).get(MyStatic.USER_PORTRAIT)).recycle();
        }
        dataList.clear();
        dataList = null;
        recommend = null;
        handler = null;
    }

    private void initView() {
        this.recommend = (ListView) findViewById(R.id.listView);
        this.recommend.setOnItemClickListener(new OnItemClickListenerImpl());
        this.back = (ImageButton) findViewById(R.id.btn_back);
        this.dataList = new ArrayList<Map<String,Object>>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.adapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_life_friends,
                new String[]{MyStatic.USER_PORTRAIT, MyStatic.USER_NAME, MyStatic.SHARE_SEX},
                new int[]{R.id.life_friends_portrait, R.id.life_friends_name, R.id.life_friends_sex}
        );
        /*实现ViewBinder()这个接口*/
        adapter.setViewBinder(new ViewBinderImpl());
        this.recommend.setAdapter(adapter);
        /*动态跟新ListView*/
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        if (NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"28";
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else{
            NetworkService.getInstance().closeConnection();
            Log.v("发布", "服务器连接失败");
        }
    }

    public void setData(String account, Bitmap portrait, String name, int sex){
        Map <String,Object> map = new HashMap<String,Object>();
        map.put(MyStatic.USER_ACCOUNT,account);
        map.put(MyStatic.USER_PORTRAIT,portrait);
        map.put(MyStatic.USER_NAME,name);
        map.put(MyStatic.USER_SEX,sex);
        if (sex == 0) {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(RecommendFriend.this.getResources(), R.drawable.ic_sex_female));
        } else {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(RecommendFriend.this.getResources(), R.drawable.ic_sex_male));
        }
        dataList.add(map);
    }

    private void recommendBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new RecommendBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.decipherstranger.RECOMMEND");
        this.registerReceiver(receiver, filter);
    }

    private class ViewBinderImpl implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            // TODO Auto-generated method stub
            if (view instanceof ImageView && data instanceof Bitmap) {
                ImageView i = (ImageView) view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(RecommendFriend.this, NearbyInfoActivity.class);
            intent.putExtra("account", (String) dataList.get(position).get(MyStatic.USER_ACCOUNT));
            intent.putExtra("photo", (Bitmap) dataList.get(position).get(MyStatic.USER_PORTRAIT));
            intent.putExtra("name", (String) dataList.get(position).get(MyStatic.USER_NAME));
            intent.putExtra("sex", (int) dataList.get(position).get(MyStatic.USER_SEX));
            startActivity(intent);
        }
    }

    public class RecommendBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.decipherstranger.RECOMMEND")){
                if (intent.getStringExtra("reResult").equals(MyStatic.resultTrue)){
                    setData(intent.getStringExtra("reAccount"), ChangeUtils.toBitmap(intent.getStringExtra("rePhoto")),
                            intent.getStringExtra("reName"),intent.getIntExtra("reGender", 0));
                }else if(intent.getStringExtra("reResult").equals("finish")){
                    handler.sendMessage(new Message());
                }else {
                    Toast.makeText(RecommendFriend.this,"未查询到相匹配的推荐好友",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
