package com.android.decipherstranger.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.decipherstranger.R;
import com.android.decipherstranger.entity.Contacts;
import com.android.decipherstranger.util.MyStatic;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public class LifeList {

    private SQLiteDatabase db = null;

    public LifeList(SQLiteDatabase db){
        this.db = db;
    }

    // 存储数据
    public void insert(int id, String name, int type, double distance, int favorite, String space, String date) {
        String sql = "insert  into `life_list` values (?, ?, ?, ?, ?, ?, ?)";
        Object args[] = new Object[]{id, name, type, distance, favorite, space, date};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    //  排序
    public ArrayList<Map<String, Object>> selectAll (Context context, int type){
        String sql = "";
        switch (type) {
            case 1: sql = "SELECT * FROM 'life_list' ORDER BY 'life_class' ASC";
                break;
            case 2: sql = "SELECT * FROM 'life_list' ORDER BY 'life_distance' ASC";
                break;
            case 3: sql = "SELECT * FROM 'life_list' ORDER BY 'life_date' ASC";
                break;
            case 4: sql = "SELECT * FROM 'life_list' ORDER BY 'life_favorite' ASC";
                break;
        }
        Cursor result = this.db.rawQuery(sql, null);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.LIFE_ID,  result.getInt(0));
            map.put(MyStatic.LIFE_NAME, result.getString(1));
            Bitmap bitmap = null;
            switch (result.getInt(2)) {
                // 美食
                case 1:bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ds_icon);
                    break;
                // 旅游
                case 2:bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ds_icon);
                    break;
                // 休闲娱乐
                case 3:bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ds_icon);
                    break;
            }
            map.put(MyStatic.LIFE_CLASS, bitmap);
            map.put(MyStatic.LIFE_SPACE, result.getString(5));
            map.put(MyStatic.LIFE_TIME, result.getString(6));
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
