package com.android.decipherstranger.activity.GameOneActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;

/**
 * Created by acmer on 2015/3/20.
 */
public class FailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_fail);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.makefriend_lose);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("### 我释放了");
    }

    public void GameFailOnClick(View view) {
        onBackPressed();
        FailActivity.this.finish();
    }

}
