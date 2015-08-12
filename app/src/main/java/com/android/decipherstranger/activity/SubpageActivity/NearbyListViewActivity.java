package com.android.decipherstranger.activity.SubpageActivity;


import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

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
 * Created by WangXin on 2015/4/6 0006.
 */
public class NearbyListViewActivity extends BaseActivity {

    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;

    private double mLatitude;
    private double mLongtitude;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;

    private MyApplication application = null;
    private ProgressDialog progressDialog = null;

    private NearbyBroadcastReceiver receiver = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            progressDialog.dismiss();
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_nearby_listview);
        this.application = MyApplication.getInstance();
        this.initLocation();
        this.init();
        this.initData();
        this.nearbyBroadcas();
        mLocationClient.start();
        System.out.println("#### 啦啦啦我进来了。。。");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(NearbyListViewActivity.this.receiver);
        mLocationClient.stop();
        dataList.clear();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        dataList = null;
        listView = null;
        System.out.println("#### 再见我退出了。。。");
    }

    private void init() {
        this.progressDialog = new ProgressDialog(NearbyListViewActivity.this);

        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (ListView) super.findViewById(R.id.nearby_list_view);
        this.listView.setOnItemClickListener(new OnItemClickListenerImpl());
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
    }

    private void initData() {
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.nearby_view_item,
                new String[]{MyStatic.USER_PORTRAIT, MyStatic.USER_NAME, MyStatic.SHARE_SEX, MyStatic.USER_DISTANCE},
                new int[]{R.id.nearby_list_view_user_photo, R.id.nearby_list_view_user_name, R.id.nearby_list_view_sex, R.id.nearby_list_view_distance}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
        simpleAdapter.notifyDataSetChanged();
    }

    private void getData() {
        //创建我们的进度条
        progressDialog.setMessage("正在搜寻附近的人");
        progressDialog.onStart();
        progressDialog.show();
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + Integer.toString(GlobalMsgUtils.msgNearBy) + ":" +
                    "account" + ":" + application.getAccount() + ":" + "latitude" + ":" + mLatitude + ":" +
                    "longtitude" + ":" + mLongtitude;
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("Login", "已经执行T（）方法");
        }
    }

    private void setData(String account, Bitmap portrait, String name, int sex, String distance) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.USER_ACCOUNT, account);
        map.put(MyStatic.USER_PORTRAIT, portrait);
        map.put(MyStatic.USER_NAME, name);
        map.put(MyStatic.USER_SEX, sex);
        map.put(MyStatic.USER_DISTANCE, Math.round(Double.parseDouble(distance)) + "米");
        if (sex == 0) {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(NearbyListViewActivity.this.getResources(), R.drawable.ic_sex_female));
        } else {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(NearbyListViewActivity.this.getResources(), R.drawable.ic_sex_male));
        }
        dataList.add(map);
    }

    private void nearbyBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new NearbyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.decipherstranger.NEARBY");
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
            Intent intent = new Intent(NearbyListViewActivity.this, NearbyInfoActivity.class);
            intent.putExtra("account", (String) dataList.get(position).get(MyStatic.USER_ACCOUNT));
            intent.putExtra("photo", (Bitmap) dataList.get(position).get(MyStatic.USER_PORTRAIT));
            intent.putExtra("name", (String) dataList.get(position).get(MyStatic.USER_NAME));
            intent.putExtra("sex", (int) dataList.get(position).get(MyStatic.USER_SEX));
            startActivity(intent);
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            NearbyListViewActivity.this.getData();
        }
    }

    public class NearbyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.decipherstranger.NEARBY")) {
                if (intent.getBooleanExtra("reResult", false)) {
/*                    NearbyUserInfo info = new NearbyUserInfo();
                    info.setUserAccount(intent.getStringExtra("reAccount"));
                    info.setUserName(intent.getStringExtra("reName"));
                    info.setImgId(ChangeUtils.toBitmap(intent.getStringExtra("rePhoto")));
                    info.setSex(intent.getIntExtra("reGender", 1));
                    info.setDistance(intent.getStringExtra("reDistance"));*/
                    System.out.println("### reDistance = " + intent.getStringExtra("reDistance"));
                    setData(intent.getStringExtra("reAccount"), ChangeUtils.toBitmap(intent.getStringExtra("rePhoto")), intent.getStringExtra("reName") , intent.getIntExtra("reGender", 1), intent.getStringExtra("reDistance"));
                } else if (intent.getBooleanExtra("isfinish", false)) {
                    handler.sendMessage(new Message());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(NearbyListViewActivity.this, "附近好像还没有人哦( ⊙ o ⊙ )！啦啦啦~~", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
