package com.android.decipherstranger.activity.GameOneActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.activity.MainPageActivity.MainPageActivity;
import com.android.decipherstranger.db.ContactsList;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.entity.User;
import com.android.decipherstranger.util.GlobalMsgUtils;
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
 * @version V1.0
 * @Date 2015/3/20.
 * @e-mail 785351408@qq.com
 */
public class SuccessActivity extends BaseActivity {

    private SQLiteOpenHelper helper = null;
    private ContactsList contactsList = null;
    private Bitmap backGroundBitmap = null;
    private ImageView imageView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_success);
        this.imageView = (ImageView) super.findViewById(R.id.imageView);

        Intent intent = getIntent();
        String Type = intent.getStringExtra("Type");
        if (Type.equals("AddFriend")) {
            //Toast.makeText(this, "已添加" + MyStatic.friendName + "为好友！",Toast.LENGTH_LONG).show();
            this.helper = new DATABASE(this);
            this.SendToWeb();
            this.SendToLocal();
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.makefriend_success);
        mediaPlayer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
/*        this.backGroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.friend_get_success);
        this.imageView.setImageBitmap(backGroundBitmap);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
/*        this.backGroundBitmap.recycle();
        this.backGroundBitmap = null;
        this.imageView = null;*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        helper = null;
    }

    public void GameSuccessOnClick(View view) {
        Intent intent = new Intent(SuccessActivity.this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        SuccessActivity.this.finish();
    }

    private void SendToWeb() {
        MyApplication application = MyApplication.getInstance();
        if (NetworkService.getInstance().getIsConnected()) {
            String addUser = "type" + ":" + Integer.toString(GlobalMsgUtils.msgAddFriend) + ":" +
                    "account" + ":" + application.getAccount() + ":" +
                    "friend" + ":" + MyStatic.friendAccount;
            NetworkService.getInstance().sendUpload(addUser);
        } else {
            NetworkService.getInstance().closeConnection();
            Toast.makeText(SuccessActivity.this, "服务器连接失败~(≧▽≦)~啦啦啦", Toast.LENGTH_SHORT).show();
            Log.v("Login", "已经执行T（）方法");
        }
    }

    private void SendToLocal() {
        User user = new User();
        user.setUsername(MyStatic.friendName);
        user.setAccount(MyStatic.friendAccount);
        user.setPortrait(MyStatic.friendPhoto);
        user.setUserSex(MyStatic.friendSex);
        contactsList = new ContactsList(this.helper.getWritableDatabase());
        contactsList.insert(user);
        Intent intent = new Intent("com.android.decipherstranger.FRIEND");
        intent.putExtra("reFresh", "reFresh");
        sendBroadcast(intent);
    }

}
