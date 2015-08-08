package com.android.decipherstranger.activity.LifeActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private String message = null;
    private ProgressDialog progressDialog = null;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESULT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_life_share_do);
        this.init();
        this.ShareLifeBroadcas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(ShareLifeActivity.this.receiver);
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("正在提交数据,请稍后...");
        application = MyApplication.getInstance();
        this.editText = (EditText) super.findViewById(R.id.editText);
        this.imageButton = (ImageButton) super.findViewById(R.id.imageButton);
    }

    private void send() {
        String account = application.getAccount();
        if(NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"23"+":"+"account"+":"+account+":"+"activitySpeech"+":"+message+":"+
                    "photo"+":"+photo+":"+"smallPhoto"+":"+smallPhoto;
            Log.v("### aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else {
            NetworkService.getInstance().closeConnection();
            Log.v("### 晒图", "服务器连接失败");
        }
    }

    public void SendShareOnclick(View view) {
        switch (view.getId()) {
            case R.id.sendShare_back_button:
                onBackPressed();
                break;
            case R.id.send_share:
                progressDialog.onStart();
                progressDialog.show();
                message = editText.getText().toString();
                if (message == null || message.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(this,"分享内容不能为空！",Toast.LENGTH_SHORT).show();
                }else if (photo == null) {
                    progressDialog.dismiss();
                    Toast.makeText(this,"请选择图片！",Toast.LENGTH_SHORT).show();
                } else {
                    send();
                }
                break;
            case R.id.imageButton:
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
            Bitmap photo = ImageCompression.compressSimplify(selectPhoto, 2f);
            smallPhoto = ChangeUtils.toBinary(photo);
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
        //    selectPhoto.recycle();
        //    photo.recycle();
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
                progressDialog.dismiss();
                if (intent.getBooleanExtra("reResult", true)){
                    //TODO 显示分享成功，跳转页面
                    System.out.println("### 晒图返回接收成功");
                    onBackPressed();
                }else{
                    //TODO 显示分享失败
                    Toast.makeText(ShareLifeActivity.this,"分享失败，请检查网络连接~",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
