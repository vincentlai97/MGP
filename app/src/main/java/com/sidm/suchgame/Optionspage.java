package com.sidm.suchgame;

/**
 * Created by JunYan on 29/11/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Optionspage extends Activity implements OnClickListener {

    private Button btn_return1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.optionspage);

        btn_return1 = (Button)findViewById(R.id.btn_returnoptions);
        btn_return1.setOnClickListener(this);
    }

    public void onClick(View V)
    {
        Intent intent = new Intent();

        if(V == btn_return1)
        {
            intent.setClass(this, Mainmenu.class);
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
