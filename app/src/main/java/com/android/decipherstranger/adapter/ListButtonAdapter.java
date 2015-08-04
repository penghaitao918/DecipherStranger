package com.android.decipherstranger.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.decipherstranger.util.MyStatic;

import java.util.ArrayList;
import java.util.Map;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　?●　?　●　　??〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>? ?_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　?＜| ＼＼        比卡丘~
 * 　 ヽ_?　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`?―＿
 *
 * @author penghaitao
 * @version V1.1
 * @Date 2015/8/4 9:09
 * @e-mail penghaitao918@163.com
 */
public class ListButtonAdapter extends BaseAdapter {

    private int[] mTo;
    private String[] mFrom;

    private int mResource;
    private LayoutInflater mInflater;

    ViewHolder holder;
    private ArrayList<Map<String, Object>> mData;

    public ListButtonAdapter(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to) {
        mData = data;
        mResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.sharePortrait = (ImageView)view.findViewById(mTo[0]);
            holder.shareName = (TextView)view.findViewById(mTo[1]);
            holder.shareMessage = (TextView)view.findViewById(mTo[2]);
            holder.sharePhoto = (ImageView)view.findViewById(mTo[3]);
            holder.shareTime = (TextView)view.findViewById(mTo[4]);
            holder.shareNum = (TextView)view.findViewById(mTo[5]);
            holder.numButton = (ImageButton)view.findViewById(mTo[6]);
            holder.friendButton = (ImageButton)view.findViewById(mTo[7]);
            view.setTag(holder);
        }

        bindView(position);

        return view;
    }

    private void bindView(int position) {
        Map<String, Object> itemData = mData.get(position);
        if (itemData != null) {
            int id = (Integer)itemData.get(MyStatic.SHARE_ID);
            String account = (String) itemData.get(MyStatic.SHARE_ACCOUNT);
            Bitmap portrait = (Bitmap)itemData.get(MyStatic.SHARE_PORTRAIT);
            String name = (String) itemData.get(MyStatic.SHARE_NAME);
            String message = (String) itemData.get(MyStatic.SHARE_MESSAGE);
            Bitmap photo = (Bitmap)itemData.get(MyStatic.SHARE_PHOTO);
            String time = (String) itemData.get(MyStatic.SHARE_TIME);
            int num = (Integer)itemData.get(MyStatic.SHARE_NUM);

            holder.sharePortrait.setBackground(new BitmapDrawable(portrait));
            holder.shareName.setText(name);
            holder.shareMessage.setText(message);
            holder.sharePhoto.setBackground(new BitmapDrawable(photo));
            holder.shareTime.setText(time);
            if(num == 0) {
                holder.shareNum.setText("");
            } else {
                holder.shareNum.setText(String.valueOf(num));
            }

            holder.numButton.setOnClickListener(new AddNumOnClickListenerImpl(position, num));
            holder.friendButton.setOnClickListener(new AddNumOnClickListenerImpl(position));
        }
    }


    private class ViewHolder {
        ImageView sharePortrait;
        TextView shareName;
        TextView shareMessage;
        ImageView sharePhoto;
        TextView shareTime;
        TextView shareNum;
        ImageButton numButton;
        ImageButton friendButton;
    }

    private class AddNumOnClickListenerImpl implements View.OnClickListener {

        private int position;
        private int count;

        AddNumOnClickListenerImpl(int pos) {
            position = pos;
        }

        AddNumOnClickListenerImpl(int pos, int c) {
            position = pos;
            count = c;
        }

        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == holder.numButton.getId())
                itemDo(position, count);;
        }
    }

    public void itemDo(int position, int count){
        mData.get(position).put(MyStatic.SHARE_NUM, count + 1);
        this.notifyDataSetChanged();
    }
}