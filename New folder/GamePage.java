package com.sidm.suchgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 142328H on 11/23/2015.
 */
public class GamePage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(new GamePanelSurfaceView(this));
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}
