package com.android.decipherstranger.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.decipherstranger.util.MyStatic;

import java.io.ByteArrayOutputStream;
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
 * @Date 2015/7/31 12:01
 * @e-mail penghaitao918@163.com
 */
public class LifeShare {

    private SQLiteDatabase db = null;

    public LifeShare(SQLiteDatabase db) {
        this.db = db;
    }

    // 存储数据
    public void insert(int id, String account, Bitmap portrait, String name, String message, Bitmap photo, String time, int number, int sex) {
        ByteArrayOutputStream osPortrait = new ByteArrayOutputStream();
        portrait.compress(Bitmap.CompressFormat.PNG, 100, osPortrait);

        ByteArrayOutputStream osPhoto = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, osPhoto);

        String sql = "insert  into `life_share` values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object args[] = new Object[]{id, account, osPortrait.toByteArray(), name, message, osPhoto.toByteArray(), time, number, String.valueOf(sex)};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    //  刷新
    public ArrayList<Map<String, Object>> refresh() {
        String sql = "SELECT * FROM `life_share` ORDER BY id DESC";
        Cursor result = this.db.rawQuery(sql, null);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.SHARE_ID, result.getInt(0));
            map.put(MyStatic.SHARE_ACCOUNT, result.getString(1));
            byte[] inPortrait = result.getBlob(2);
            Bitmap portrait = BitmapFactory.decodeByteArray(inPortrait, 0, inPortrait.length);
            map.put(MyStatic.SHARE_PORTRAIT, portrait);
            map.put(MyStatic.SHARE_NAME, result.getString(3));
            map.put(MyStatic.SHARE_MESSAGE, result.getString(4));
            byte[] inPhoto = result.getBlob(5);
            Bitmap photo = BitmapFactory.decodeByteArray(inPhoto, 0, inPhoto.length);
            map.put(MyStatic.SHARE_PHOTO, photo);
            map.put(MyStatic.SHARE_TIME, result.getString(6));
            map.put(MyStatic.SHARE_NUM, result.getInt(7));
            map.put(MyStatic.SHARE_SEX, result.getString(8));
            all.add(map);
        }
        this.db.close();
        return all;
    }

    //  加载
    public ArrayList<Map<String, Object>> load(int id) {
        String sql = "SELECT * FROM life_share WHERE id<? ORDER BY id DESC";
        String args[] = new String[]{String.valueOf(id)};
        Cursor result = this.db.rawQuery(sql, args);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.SHARE_ID, result.getInt(0));
            byte[] inPortrait = result.getBlob(1);
            Bitmap portrait = BitmapFactory.decodeByteArray(inPortrait, 0, inPortrait.length);
            map.put(MyStatic.SHARE_PORTRAIT, portrait);
            map.put(MyStatic.SHARE_NAME, result.getString(2));
            map.put(MyStatic.SHARE_MESSAGE, result.getString(3));
            byte[] inPhoto = result.getBlob(4);
            Bitmap photo = BitmapFactory.decodeByteArray(inPhoto, 0, inPhoto.length);
            map.put(MyStatic.SHARE_PHOTO, photo);
            map.put(MyStatic.SHARE_TIME, result.getString(5));
            map.put(MyStatic.SHARE_NUM, result.getInt(6));
            all.add(map);
        }
        this.db.close();
        return all;
    }

    //  获取当前最小ID
    public int getMinId() {
        int id = 0;
        String sql = "SELECT id FROM life_share ORDER BY id ASC LIMIT 1";
        Cursor result = this.db.rawQuery(sql, null);
        if (result.moveToNext()) {
            id = result.getInt(0);
        }
        return id;
    }

    // 清空
    public void clear() {
        String clear = "DELETE FROM life_share";
        this.db.execSQL(clear);
        this.db.close();
        System.out.println("### 删除成功");
    }

}
