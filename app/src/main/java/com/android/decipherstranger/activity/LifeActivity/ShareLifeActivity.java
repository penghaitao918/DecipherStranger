package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.ImageCompression;
import com.android.decipherstranger.util.MyStatic;

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
 * @Date 2015/7/30 12:13
 * @e-mail penghaitao918@163.com
 */
public class ShareLifeActivity extends BaseActivity {

    private EditText editText = null;
    private ImageButton imageButton = null;
    private MyApplication application = null;
    private ShareLifeBroadcastReceiver receiver = null;
    private String photo = null;
    private String smallPhoto = null;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESULT_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_life_share_do);
        this.init();
        this.ShareLifeBroadcas();
    }

    private void init() {
        application = (MyApplication) getApplication();
        this.editText = (EditText) super.findViewById(R.id.editText);
        this.imageButton = (ImageButton) super.findViewById(R.id.imageButton);
    }

    private void send() {
        String account = application.getAccount();
        String message = editText.getText().toString();
        if(NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"23"+":"+"account"+":"+account+":"+"activitySpeech"+":"+message+":"+
                    "photo"+":"+photo+":"+"smallPhoto"+":"+smallPhoto;
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else {
            NetworkService.getInstance().closeConnection();
            Log.v("晒图", "服务器连接失败");
        }
    }

    public void SendShareOnclick(View view) {
        switch (view.getId()) {
            case R.id.sendShare_back_button:
                onBackPressed();
                break;
            case R.id.send_share:
                break;
            case R.id.imageButton:
                selectPhoto();
                break;
        }
    }

    public  void selectPhoto(){
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery,
                IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == IMAGE_REQUEST_CODE){
            try {
                startPhotoZoom(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            if (data!= null){
                getImageToView(data);
            }
        }
    }

    public void getImageToView(Intent data){
        Bundle extras = data.getExtras();
        if (extras != null){
            Bitmap selectPhoto = extras.getParcelable("data");
            photo = ChangeUtils.toBinary(selectPhoto);
            smallPhoto = ChangeUtils.toBinary(ImageCompression.compressSimplify(selectPhoto, 0.3f));
            Drawable drawable = new BitmapDrawable(this.getResources(), selectPhoto);
            imageButton.setImageDrawable(drawable);
        }

    }
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void ShareLifeBroadcas() {
        //动态方式注册广播接收者
        IntentFilter filter = new IntentFilter();
        this.receiver = new ShareLifeBroadcastReceiver();
        filter.addAction(MyStatic.LIFE_SHARE_DO);
        this.registerReceiver(receiver, filter);
    }

    public class ShareLifeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_SHARE_DO)) {
                if (intent.getBooleanExtra("reResult", true)){
                        //TODO 显示分享成功，跳转页面
                }else{
                        //TODO 显示分享失败
                }
            }
        }
    }

}
