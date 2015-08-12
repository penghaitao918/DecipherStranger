package com.android.decipherstranger.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.activity.MainPageActivity.MainPageActivity;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.db.UserTabOperate;
import com.android.decipherstranger.entity.User;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.util.SharedPreferencesUtils;
import com.android.decipherstranger.util.StringUtils;

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
 * @Date 2015/2/10.
 * @e-mail 785351408@qq.com
 */
public class LoginActivity extends BaseActivity {

    private static final String FILENAME = "Login_CheckBox";
    private MyApplication application = null;
    private Intent it = null;
    private ArrayAdapter<String> adapter = null;
    private StringUtils stringUtils = null;
    private SQLiteOpenHelper helper = null;
    private UserTabOperate userInfo = null;
    private LoginBroadcastReceiver receiver = null;
    private ProgressDialog progressDialog = null;
    private SharedPreferencesUtils sharedPreferencesUtils = null;

    private AutoCompleteTextView accountEdit = null;
    private EditText pawEdit = null;
    private Button loginButton = null;
    private CheckBox checkBox = null;
    private Button registerButton = null;
    private String account = null;
    private String passwordMD5 = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    progressDialog.onStart();
                    progressDialog.show();
                    break;
                case 1:
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "服务器连接失败~(≧▽≦)~啦啦啦", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        application = MyApplication.getInstance();
        this.helper = new DATABASE(this);
        this.sharedPreferencesUtils = new SharedPreferencesUtils(this, MyStatic.FILENAME_USER);
        initView();
        getCheckBox();
        loginBroadcas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(LoginActivity.this.receiver);
        application = null;
        it = null;
        adapter.clear();
        stringUtils = null;
        helper = null;
        userInfo = null;
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        sharedPreferencesUtils.recycle();
        accountEdit = null;
        pawEdit = null;
        loginButton = null;
        checkBox = null;
        registerButton = null;
        account = null;
        passwordMD5 = null;
    }

    private void initView() {
        this.progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login...");

        LoginActivity.this.userInfo = new UserTabOperate(LoginActivity.this.helper.getReadableDatabase());
        this.adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LoginActivity.this.userInfo.accountInfo());

        this.accountEdit = (AutoCompleteTextView) super.findViewById(R.id.login_edit_account);
        this.accountEdit.setAdapter(adapter);
        this.pawEdit = (EditText) super.findViewById(R.id.login_edit_password);
        this.loginButton = (Button) super.findViewById(R.id.login_button);
        this.checkBox = (CheckBox) super.findViewById(R.id.auto_save_password);
        this.registerButton = (Button) super.findViewById(R.id.register_button);

        this.accountEdit.setOnItemClickListener(new OnItemClickListenerImpl());
        this.pawEdit.setOnClickListener(new passwordOnClickListenerImpl());
        this.loginButton.setOnClickListener(new loginOnClickListenerImpl());
        this.registerButton.setOnClickListener(new registerOnClickListenerImpl());
    }

    /**
     * Created by Feng on 2015/3/24.
     */
    private void accountCheckByWeb(String account, String password) {

        NetworkService.getInstance().closeConnection();
        NetworkService.getInstance().onInit(LoginActivity.this);
        NetworkService.getInstance().setupConnection();
        if (NetworkService.getInstance().getIsConnected()) {
            String userInfo = "type" + ":" + Integer.toString(GlobalMsgUtils.msgLogin) + ":" + "account" + ":" + account + ":" + "password" + ":" + password;
            NetworkService.getInstance().sendUpload(userInfo);
        } else {
            NetworkService.getInstance().closeConnection();
        //    Handler mhandler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Message m = new Message();
                    m.what = 3;
                    handler.sendMessage(m);
                    Log.v("Login", "已经执行T（）方法");
                }
            }, 2000);
        }
    }

    private void getCheckBox() {
        SharedPreferences shared = getSharedPreferences(FILENAME, LoginActivity.MODE_PRIVATE);
        LoginActivity.this.checkBox.setChecked(shared.getBoolean("Checked", true));
    }

    private void loginBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LoginBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.decipherstranger.LOGIN");
        this.registerReceiver(receiver, filter);
    }

    private void saveUserInfo() {
        sharedPreferencesUtils.set(MyStatic.USER_LOGIN, true);
        sharedPreferencesUtils.set(MyStatic.USER_ACCOUNT, account);
        sharedPreferencesUtils.set(MyStatic.USER_PASSWORD, passwordMD5);
        sharedPreferencesUtils.set(MyStatic.USER_NAME, application.getName());
        sharedPreferencesUtils.set(MyStatic.USER_PORTRAIT, ChangeUtils.toBinary(application.getPortrait()));
        sharedPreferencesUtils.set(MyStatic.USER_SEX, application.getSex());
        sharedPreferencesUtils.set(MyStatic.USER_BIRTH, application.getBirth());
        sharedPreferencesUtils.set(MyStatic.USER_EMAIL, application.getEmail());
        sharedPreferencesUtils.set(MyStatic.USER_PHONE, application.getPhone());
        //       sharedPreferencesUtils.set(MyStatic.USER_SIGNATURE, application.getSignature());
    }

    private class passwordOnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            LoginActivity.this.pawEdit.setText("");
        }
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String userAccount = adapter.getItem(position);
            userInfo = new UserTabOperate(LoginActivity.this.helper.getReadableDatabase());
            User user = LoginActivity.this.userInfo.userTabInfo(userAccount);
            pawEdit.setText(user.getPassword());
        }
    }

    private class loginOnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Message m = new Message();
            m.what = 0;
            handler.sendMessage(m);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message m = new Message();
                    account = LoginActivity.this.accountEdit.getText().toString();
                    String password = LoginActivity.this.pawEdit.getText().toString();
                    passwordMD5 = stringUtils.MD5(password);
                    if (account.equals("")) {
                        m.what = 1;
                        handler.sendMessage(m);
                    } else if (password.equals("")) {
                        m.what = 2;
                        handler.sendMessage(m);
                    } else {
                        LoginActivity.this.userInfo = new UserTabOperate(LoginActivity.this.helper.getReadableDatabase());
                        User user = LoginActivity.this.userInfo.userTabInfo(account);

                        LoginActivity.this.userInfo = new UserTabOperate(LoginActivity.this.helper.getWritableDatabase());
                        SharedPreferences share = getSharedPreferences(FILENAME, LoginActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = share.edit();

                        if (LoginActivity.this.checkBox.isChecked()) {
                            editor.putBoolean("Checked", true);
                            if (!user.getAccount().equals("")) {
                                LoginActivity.this.userInfo.update(account, password);
                            } else {
                                LoginActivity.this.userInfo.insert(account, password);
                            }
                        } else {
                            editor.putBoolean("Checked", false);
                            if (!user.getAccount().equals("")) {
                                LoginActivity.this.userInfo.update(account, "");
                            } else {
                                LoginActivity.this.userInfo.insert(account, "");
                            }
                        }
                        editor.commit();
                        accountCheckByWeb(account, passwordMD5);
                    }
                }
            }).start();

        }
    }

    private class registerOnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            it = new Intent(LoginActivity.this, RegisterActivityBase.class);
            startActivity(it);
            finish();
        }
    }

    public class LoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.decipherstranger.LOGIN")) {
                if (intent.getStringExtra("result").equals(MyStatic.resultTrue)) {
                    application.setAccount(account);
                    saveUserInfo();
                    progressDialog.dismiss();
                    Intent it = new Intent(LoginActivity.this, MainPageActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                    finish();
                } else if (intent.getStringExtra("result").equals("same")) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "账号已经登录了！", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
