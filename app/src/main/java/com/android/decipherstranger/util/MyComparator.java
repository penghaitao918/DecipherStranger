package com.android.decipherstranger.util;

import java.util.Comparator;
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
 * @Date 2015/8/2 16:16
 * @e-mail penghaitao918@163.com
 */
public class MyComparator implements Comparator {

    public int flag = 0;

    public MyComparator(int flag) {
        this.flag = flag;
    }

    public int compare(Object o1, Object o2) {
        Map<String, Object> map1 = (Map<String, Object>) o1;
        Map<String, Object> map2 = (Map<String, Object>) o2;
        switch (flag) {
            case 1:
                //  升序排序
                return ((int) map1.get(MyStatic.LIFE_CLASSINT) - (int) map2.get(MyStatic.LIFE_CLASSINT));
            case 2:
                //  升序排序
                return ((int) map1.get(MyStatic.LIFE_DISTANCE) - (int) map2.get(MyStatic.LIFE_DISTANCE));
            case 3:
                //  升序排序
                return ((String) map1.get(MyStatic.LIFE_TIME)).compareTo((String) map2.get(MyStatic.LIFE_TIME));
            case 4:
                //  降序排序
                return ((int) map2.get(MyStatic.LIFE_FAVORITE) - (int) map1.get(MyStatic.LIFE_FAVORITE));
        }
        return ((int) map1.get(MyStatic.LIFE_FAVORITE) - (int) map2.get(MyStatic.LIFE_FAVORITE));
    }

}