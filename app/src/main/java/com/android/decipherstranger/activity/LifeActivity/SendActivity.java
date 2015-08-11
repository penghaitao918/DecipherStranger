package com.android.decipherstranger.activity.LifeActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

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
 * @Date 2015/7/28 11:29
 * @e-mail 785351408@qq.com
 */
public class SendActivity extends BaseActivity {

    private MyApplication application = null;
    private ProgressDialog progressDialog = null;
    private LifeSendBroadcastReceiver receiver = null;

    private RadioGroup classRadio = null;
    private EditText nameEdit = null;
    private EditText spaceEdit = null;
    private Button timeButton = null;
    private EditText numPeople = null;
    private Button endTime = null;
    private EditText rallySpace = null;
    private Button rallyTime = null;
    private EditText passwordEdit = null;

    private double mLatitude;
    private double mLongtitude;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;

    private int classRadInt = 3;
    private String timeBtnString = "";
    private int timeBtnInt = 0;
    private String endTimeString = "";
    private int endTimeInt = 30000000;
    private String rallyTimeString = "";

//    private PoiSearch mPoiSearch = null;// POI检索对象
//    private SuggestionSearch mSuggestionSearch = null; //联想词检索对象
//    private ArrayAdapter<String> sugAdapter = null;
//    private int load_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_life_send);
        //  application = (MyApplication) getApplication();
        application = MyApplication.getInstance();
        this.init();
//        this.initPOIListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(SendActivity.this.receiver);
//        mPoiSearch.destroy();
//        mSuggestionSearch.destroy();
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);

        this.classRadio = (RadioGroup) super.findViewById(R.id.classRadio);
        this.nameEdit = (EditText) super.findViewById(R.id.lifeName);
        this.spaceEdit = (EditText) super.findViewById(R.id.lifeSpace);
        this.timeButton = (Button) super.findViewById(R.id.lifeTime);
        this.numPeople = (EditText) super.findViewById(R.id.lifePeople);
        this.endTime = (Button) super.findViewById(R.id.timeEnd);
        this.rallySpace = (EditText) super.findViewById(R.id.rallySpace);
        this.rallyTime = (Button) super.findViewById(R.id.rallyTime);
        this.passwordEdit = (EditText) super.findViewById(R.id.lifePassword);

        this.LifeSendBroadcas();
        initLocation();

        /*锁定聚焦到顶部*/
        classRadio.setFocusable(true);
        classRadio.setFocusableInTouchMode(true);
        classRadio.requestFocus();


//        sugAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line);
//        spaceEdit.setAdapter(sugAdapter);
    }

    /**
     * 初始化搜索模块，注册搜索事件监听
     */
//    private void initPOIListener() {
//        // POI检索实例
//        mPoiSearch = PoiSearch.newInstance();
//        // 创建POI检索监听者
//        mPoiSearch.setOnGetPoiSearchResultListener(this);
//        // 联想词检索实例
//        mSuggestionSearch = SuggestionSearch.newInstance();
//        // 联想词检索监听者
//        mSuggestionSearch.setOnGetSuggestionResultListener(this);
//        /**
//         * 当输入关键字变化时，动态更新建议列表
//         */
//        spaceEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable arg0) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                      int arg3) {
//                if (cs.length() <= 0) {
//                    return;
//                }
////                String city = ((EditText) findViewById(R.id.city)).getText()
////                        .toString();
//                /**
//                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//                 */
//                mPoiSearch.searchInCity((new PoiCitySearchOption())
//                        .city("长春")// 根据城市
//                        .keyword(cs.toString())// 根据关键字
//                        .pageNum(load_Index));// 查询的页数
//            }
//        });
//    }
//    @Override
//    public void onGetSuggestionResult(SuggestionResult res) {
//        if (res == null || res.getAllSuggestions() == null) {
//            return;
//        }
//        sugAdapter.clear();
//        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
//            if (info.key != null)
//                sugAdapter.add(info.key);
//        }
//        sugAdapter.notifyDataSetChanged();

//    }

//    @Override
//    public void onGetPoiDetailResult(PoiDetailResult result) {
//        // 未找到了结果
//        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
//                    .show();
//        } else {
//            Toast.makeText(MainActivity.this,
//                    result.getName() + ": " + result.getAddress(),
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    public void onGetPoiResult(PoiResult result) {
//        // 未找到结果
//        if (result == null
//                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//            return;
//        }
//        // 结果没有异常，找到了结果
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////            mBaiduMap.clear();
////            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
////            mBaiduMap.setOnMarkerClickListener(overlay);
////            overlay.setData(result);
////            overlay.addToMap();
////            overlay.zoomToSpan();
//            sugAdapter.clear();
//            for (PoiInfo info: result.getAllPoi()) {
//                if (info.name!= null)
//                    sugAdapter.add(info.address);
//            }
//            sugAdapter.notifyDataSetChanged();
//            return;
//        }
        // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

//            String strInfo = "在";
//            for (CityInfo cityInfo : result.getSuggestCityList()) {
//                strInfo += cityInfo.city;
//                strInfo += ",";
//            }
//            strInfo += "找到结果";
//            Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG)
//                    .show();
//        }
//    }
    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 发送数据到服务器
     */
    private void send() {
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + Integer.toString(GlobalMsgUtils.msgSendActivity) + ":" +
                    "account" + ":" + application.getAccount() + ":" + "activity_type" + ":" + classRadInt + ":" + "activity_name" + ":" +
                    nameEdit.getText().toString() + ":" + "activity_place" + ":" + spaceEdit.getText().toString() + ":" + "activity_time" +
                    ":" + timeBtnString + ":" + "people_num" + ":" + Integer.parseInt(numPeople.getText().toString()) + ":" + "end_time" + ":" +
                    endTimeString + ":" + "set_place" + ":" + rallySpace.getText().toString() + ":" + "set_time" + ":" + rallyTimeString + ":" + "activity_password" +
                    ":" + passwordEdit.getText().toString() + ":" + "mLatitude" + ":" + mLatitude + ":" + "mLongtitude" + ":" + mLongtitude;
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("发布", "服务器连接失败");
        }
    }

    private boolean check() {
        if (classRadInt == -1) {
            classRadio.requestFocus();
            Toast.makeText(this, "请选择活动类别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nameEdit.getText().toString().equals("")) {
            nameEdit.requestFocus();
            Toast.makeText(this, "请输入活动名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spaceEdit.getText().toString().equals("")) {
            spaceEdit.requestFocus();
            Toast.makeText(this, "请输入活动地点", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timeBtnString.equals("")) {
            classRadio.requestFocus();
            Toast.makeText(this, "请选择活动日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numPeople.getText().toString().equals("")) {
            numPeople.requestFocus();
            Toast.makeText(this, "请输入活动人数", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endTimeString.equals("")) {
            classRadio.requestFocus();
            Toast.makeText(this, "请选择截止日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rallySpace.getText().toString().equals("")) {
            rallySpace.requestFocus();
            Toast.makeText(this, "请输入集合地点", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rallyTimeString.equals("")) {
            rallyTime.requestFocus();
            Toast.makeText(this, "请选择集合时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordEdit.getText().toString().equals("")) {
            passwordEdit.requestFocus();
            Toast.makeText(this, "请输入活动口令", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void LifeSendOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
             /*美食*/
            case R.id.food:
                classRadInt = 1;
                break;
            /*旅行*/
            case R.id.travel:
                classRadInt = 2;
                break;
            /*休闲娱乐*/
            case R.id.others:
                classRadInt = 3;
                break;
            /*活动日期*/
            case R.id.lifeTime:
                Dialog dialog0 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month += 1;
                                timeButton.setTextColor(Color.parseColor("#ffa79c88"));
                                timeButton.setText(year + "-" + month + "-" + day);
                                timeBtnString = timeButton.getText().toString();
                                timeBtnInt = (year * 100 + month) * 100 + day;
                            }
                        }, 2015, 9, 22);
                dialog0.show();
                break;
            /*截止日期*/
            case R.id.timeEnd:
                Dialog dialog1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month += 1;
                                endTime.setTextColor(Color.parseColor("#ffa79c88"));
                                endTime.setText(year + "-" + month + "-" + day);
                                endTimeString = endTime.getText().toString();
                                endTimeInt = (year * 100 + month) * 100 + day;
                            }
                        }, 2015, 9, 22);
                dialog1.show();
                break;
            /*集合时间*/
            case R.id.rallyTime:
                Dialog dialog2 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                rallyTime.setTextColor(Color.parseColor("#ffa79c88"));
                                rallyTime.setText(hour + " ： " + minute);
                                rallyTimeString = rallyTime.getText().toString();
                            }
                        }, 9, 0, true);
                dialog2.show();
                break;
            /*发布*/
            case R.id.publish:
                if (this.check()) {
                    progressDialog.setMessage("正在提交数据,请稍后...");
                    progressDialog.onStart();
                    progressDialog.show();
                    this.send();
                }
                break;
        }
    }

    private void LifeSendBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifeSendBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_SEND);
        this.registerReceiver(receiver, filter);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();
        }
    }

    public class LifeSendBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_SEND)) {
                if (intent.getBooleanExtra("reResult", true)) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(SendActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            SendActivity.this.finish();
                            onBackPressed();
                        }
                    }, 1500);
                }
            }
        }
    }

}
