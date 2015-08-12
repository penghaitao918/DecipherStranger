package com.android.decipherstranger.activity.GameOneActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.MainPageActivity.ServicePageActivity;

/**
 * Created by acmer on 2015/3/20.
 */
public class FailActivity extends BaseActivity {

    private MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_fail);
        mediaPlayer = MediaPlayer.create(this, R.raw.makefriend_lose);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void GameFailOnClick(View view) {
        Intent intent = new Intent(FailActivity.this, ServicePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        FailActivity.this.finish();
    }

}
