package com.android.decipherstranger.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;

/**
 * Created by PengHaitao on 2015/2/10.
 */
public class RegisterActivityBase extends BaseActivity {

    boolean accountBool = false, passwordBool = false, rePawBool = false, nameBool = false,
            emailBool = false, phoneBool = false, birthBool = false;
    private Drawable OkIcon = null;
    private Drawable ErrorIcon = null;
    private ImageButton backButton = null;
    private Button nextStepButton = null;
    private EditText accountEdit = null;
    private EditText passwordEdit = null;
    private EditText rePawEdit = null;
    private EditText nameEdit = null;
    private RadioGroup sexGroup = null;
    private int userSex = 1;
    private RadioButton male = null;
    private RadioButton female = null;
    private EditText emailEdit = null;

    private EditText phoneEdit = null;
    private Button birthButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_base);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        OkIcon.setCallback(null);
        OkIcon = null;
        ErrorIcon.setCallback(null);
        ErrorIcon = null;
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {// 防止连续两次返回键
            //这你写你的返回处理
            onBackPressed();
            Intent intent = new Intent(RegisterActivityBase.this, LoginActivity.class);
            startActivity(intent);
            RegisterActivityBase.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {

        OkIcon = getResources().getDrawable(R.drawable.ok_icon);
        ErrorIcon = getResources().getDrawable(R.drawable.error_icon);
        OkIcon.setBounds(0, 0, OkIcon.getMinimumWidth(), OkIcon.getMinimumHeight()); //设置边界
        ErrorIcon.setBounds(0, 0, ErrorIcon.getMinimumWidth(), ErrorIcon.getMinimumHeight()); //设置边界

        this.backButton = (ImageButton) super.findViewById(R.id.register_back_button);
        this.nextStepButton = (Button) super.findViewById(R.id.next_step_btn);
        this.accountEdit = (EditText) super.findViewById(R.id.register_account_input);
        this.passwordEdit = (EditText) super.findViewById(R.id.register_password_input);
        this.rePawEdit = (EditText) super.findViewById(R.id.register_re_paw_input);
        this.nameEdit = (EditText) super.findViewById(R.id.register_username_input);
        this.sexGroup = (RadioGroup) super.findViewById(R.id.userSex);
        this.male = (RadioButton) super.findViewById(R.id.manRadio);
        this.female = (RadioButton) super.findViewById(R.id.womanRadio);
        this.emailEdit = (EditText) super.findViewById(R.id.register_email_input);
        this.phoneEdit = (EditText) super.findViewById(R.id.register_phone_input);
        this.birthButton = (Button) super.findViewById(R.id.register_birth_input);

        this.accountEdit.setOnFocusChangeListener(new accountOnFocusChangeListenerImpl());
        this.passwordEdit.setOnFocusChangeListener(new passwordOnFocusChangeListenerImpl());
        this.rePawEdit.setOnFocusChangeListener(new rePawOnFocusChangeListenerImpl());
        this.nameEdit.setOnFocusChangeListener(new nameOnFocusChangeListenerImpl());
        this.sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
        this.emailEdit.setOnFocusChangeListener(new emailOnFocusChangeListenerImpl());
        this.phoneEdit.setOnFocusChangeListener(new phoneOnFocusChangeListenerImpl());

    }

    public void RegisterBaseOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_back_button:
                onBackPressed();
                break;
            case R.id.register_account_input:
                accountEdit.setText("");
                break;
            case R.id.register_password_input:
                passwordEdit.setText("");
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.register_re_paw_input:
                rePawEdit.setText("");
                rePawEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.register_username_input:
                nameEdit.setText("");
                break;
            case R.id.register_email_input:
                emailEdit.setText("");
                break;
            case R.id.register_phone_input:
                phoneEdit.setText("");
                break;
            case R.id.register_birth_input:
                Dialog dialog = new DatePickerDialog(this,
                         new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month += 1;
                                birthButton.setText(year + "-" + month + "-" + day);
                                birthBool = true;
                            }
                        }, 1990, 9, 22);
                dialog.show();
                break;
            case R.id.next_step_btn:
                nextStepButton.setFocusable(true);
                nextStepButton.setFocusableInTouchMode(true);
                nextStepButton.requestFocus();
                nextStepButton.requestFocusFromTouch();
                if (accountBool && passwordBool && rePawBool && nameBool && emailBool && phoneBool && birthBool) {
                    Intent intentNext = new Intent(this, RegisterActivityPhoto.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("account", String.valueOf(accountEdit.getText()));
                    bundle.putString("possword", String.valueOf(passwordEdit.getText()));
                    bundle.putString("name", String.valueOf(nameEdit.getText()));
                    bundle.putString("sex", String.valueOf(userSex));
                    bundle.putString("email", String.valueOf(emailEdit.getText()));
                    bundle.putString("phone", String.valueOf(phoneEdit.getText()));
                    bundle.putString("birth", String.valueOf(birthButton.getText()));
                    intentNext.putExtras(bundle);
                    startActivity(intentNext);
                } else {
                    Toast toast = Toast.makeText(this, "请确认信息是否填写完整", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    private boolean checkAccountTop(String s) {
        return s.matches("^[a-zA-Z].{5,19}");    //     字母开头
    }

    private boolean checkAccount(String s) {
        return s.matches("^[a-zA-Z][a-zA-Z0-9_]{5,19}");    //字母开头，允许6-20字节，允许字母数字下划线
    }

    private boolean checkPassword(String s) {
        return s.matches("^[a-zA-Z0-9]{6,20}");       //数字和字母
    }

    private boolean checkEmail(String s) {
        return s.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");     //address@email.com
    }

    private boolean checkPhone(String s) {
        System.out.println("### phone = #" + s + "#");
        return s.matches("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,5-7,9])|(18[0-3,5-9]))[0-9]{8}$");     //手机号码
        //    return s.matches("^1[3|4|5|7|8][0-9]{9}$");     //手机号码
    }

    private class accountOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == accountEdit.getId()) {
                if (!focus) {
                    accountBool = false;
                    if (accountEdit.getText().length() == 0) {
                        accountEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        accountEdit.setText("用户名不能为空");
                    } else if (accountEdit.getText().length() < 6) {
                        accountEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        accountEdit.setText("长度不能小于6个字符");
                    } else if (!checkAccountTop(accountEdit.getText().toString())) {
                        accountEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        accountEdit.setText("用户名须以字母开头");
                    } else if (!checkAccount(accountEdit.getText().toString())) {
                        accountEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        accountEdit.setText("不能包含特殊符号");
                    } else {
                        accountBool = true;
                        accountEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                    }
                }
            }
        }
    }

    private class passwordOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == passwordEdit.getId()) {
                if (!focus) {
                    passwordBool = false;
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    if (passwordEdit.getText().length() == 0) {
                        passwordEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        passwordEdit.setText("密码不能为空");
                    } else if (passwordEdit.getText().length() < 6) {
                        passwordEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        passwordEdit.setText("长度不能小于6个字符");
                    } else if (!checkPassword(passwordEdit.getText().toString())) {
                        passwordEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        passwordEdit.setText("只能由数字和字母组成");
                    } else {
                        passwordBool = true;
                        passwordEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
            }
        }
    }

    private class rePawOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == rePawEdit.getId()) {
                if (!focus) {
                    rePawBool = false;
                    String rePawC = rePawEdit.getText().toString();
                    String passwordC = passwordEdit.getText().toString();
                    if (rePawEdit.getText().length() == 0) {
                        rePawEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        rePawEdit.setText("密码不能为空");
                    } else if (!rePawC.equals(passwordC)) {
                        rePawEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        rePawEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        rePawEdit.setText("两次密码不一致");
                    } else {
                        rePawBool = true;
                        rePawEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                    }
                }
            }
        }
    }

    private class nameOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == nameEdit.getId()) {
                if (!focus) {
                    nameBool = false;
                    if (nameEdit.getText().length() == 0 || nameEdit.getText().toString().equals("昵称不能为空")) {
                        nameEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        nameEdit.setText("昵称不能为空");
                    } else {
                        nameBool = true;
                        nameEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                    }
                }
            }
        }
    }

    private class OnCheckedChangeListenerImpl implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int Id) {
            if (male.getId() == Id) {
                userSex = 1;
                System.out.println("#### 男");
            } else {
                userSex = 0;
                System.out.println("#### 女");
            }
        }
    }

    private class emailOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == emailEdit.getId()) {
                if (!focus) {
                    emailBool = false;
                    if (emailEdit.getText().length() == 0) {
                        emailEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        emailEdit.setText("邮箱不能为空");
                    } else if (!checkEmail(emailEdit.getText().toString())) {
                        emailEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        emailEdit.setText("请输入正确的邮箱地址");
                    } else {
                        emailBool = true;
                        emailEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                    }
                }
            }
        }
    }

    private class phoneOnFocusChangeListenerImpl implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean focus) {
            if (view.getId() == RegisterActivityBase.this.phoneEdit.getId()) {
                if (!focus) {
                    phoneBool = false;
                    if (RegisterActivityBase.this.phoneEdit.getText().length() == 0) {
                        phoneEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        RegisterActivityBase.this.phoneEdit.setText("手机号不能为空");
                    } else if (!checkPhone(phoneEdit.getText().toString())) {
                        phoneEdit.setCompoundDrawables(null, null, ErrorIcon, null);//画在右边
                        phoneEdit.setText("请输入正确的手机号码");
                    } else {
                        phoneBool = true;
                        phoneEdit.setCompoundDrawables(null, null, OkIcon, null);//画在右边
                    }
                }
            }
        }
    }
}
