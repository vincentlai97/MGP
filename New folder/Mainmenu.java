package com.sidm.suchgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Mainmenu extends Activity implements OnClickListener {

    private Button btn_start;
    private Button btn_help;
    private Button btn_options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.mainmenu);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_help = (Button)findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
    }

    public void onClick(View V)
    {
        Intent intent = new Intent();

        if(V == btn_start)
        {
            intent.setClass(this, GamePage.class);
        }

        if(V == btn_help)
        {
            intent.setClass(this, Helppage.class);
        }

        if(V == btn_options)
        {
            intent.setClass(this,Optionspage.class);
        }

        startActivity(intent);
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
