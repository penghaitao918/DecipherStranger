package com.android.decipherstranger.activity.GameOneActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.MainPageActivity.MainPageActivity;
import com.android.decipherstranger.activity.MainPageActivity.ServicePageActivity;

/**
 * Created by acmer on 2015/3/20.
 */
public class FailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_fail);
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.makefriend_lose);
        mediaPlayer.start();
    }
    
    public void GameFailOnClick(View view) {
/*        Intent intent = new Intent(FailActivity.this, ServicePageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        FailActivity.this.finish();
    }

}
