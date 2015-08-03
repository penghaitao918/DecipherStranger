package com.android.decipherstranger.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.decipherstranger.R;
import com.android.decipherstranger.util.MyStatic;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
 * @Date 2015/7/31 12:01
 * @e-mail penghaitao918@163.com
 */
public class LifeShare {

    private SQLiteDatabase db = null;

    public LifeShare(SQLiteDatabase db){
        this.db = db;
    }

    // 存储数据
    public void insert(int id, Bitmap portrait, String account, String message, Bitmap photo, String time, int number) {
        ByteArrayOutputStream osPortrait = new ByteArrayOutputStream();
        portrait.compress(Bitmap.CompressFormat.PNG, 100, osPortrait);

        ByteArrayOutputStream osPhoto = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, osPhoto);

        String sql = "insert  into `life_share` values (?, ?, ?, ?, ?, ?, ?)";
        Object args[] = new Object[]{id, osPortrait.toByteArray(), account, message, osPhoto.toByteArray(), time, number};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    //  排序
    public ArrayList<Map<String, Object>> selectAll (Context context){
        String sql = "SELECT * FROM life_share ORDER BY id DESC";
        Cursor result = this.db.rawQuery(sql, null);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
        Bitmap bitmap = bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ds_icon);

/*        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.LIFE_ID,  result.getInt(0));
            map.put(MyStatic.LIFE_NAME, result.getString(1));
            map.put(MyStatic.LIFE_CLASSINT, result.getInt(2));
            map.put(MyStatic.LIFE_CLASS, bitmap);
            map.put(MyStatic.LIFE_SPACE, result.getString(5));
            map.put(MyStatic.LIFE_TIME, result.getString(6));
            all.add(map);
        }*/

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            long l = random.nextInt(10000);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.SHARE_PORTRAIT, bitmap);
            map.put(MyStatic.SHARE_NAME, "我是小涛啊" + l);
            map.put(MyStatic.SHARE_MESSAGE, "今天风好大，把我吹成了傻逼-。-凑点字数看能不能出现第二行QAQ");
            map.put(MyStatic.SHARE_PHOTO, bitmap);
            map.put(MyStatic.SHARE_TIME, "2015/07/28 18:14");
            all.add(map);
        }

        this.db.close();
        return all;
    }

    // 清空
    public void clear() {
        String clear = "DELETE FROM life_list";
        this.db.execSQL(clear);
        this.db.close();
    }

}
