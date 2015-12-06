package com.sidm.suchgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
* Created by 142328H on 11/23/2015.
*/
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg,scaledbg;
    // 1b) Define Screen width and Screen height as integer
    int screenwidth, screenheight;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;

    //Monitor Image
    private Bitmap monitor, scaledmonitor;
    private boolean monitor_active = false;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] Spaceship = new Bitmap[4];
    // 4b) Variable as an index to keep track of the spaceship images
    private short SpaceshipIndex = 0;
    // Spaceship location
    private short mX = 0, mY = 0;
    // Asteroid
    private SpriteAnimation stone_anim;
    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenwidth = metrics.widthPixels;
        screenheight = metrics.heightPixels;
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        scaledbg = Bitmap.createScaledBitmap(bg, screenwidth, screenheight, true);

        // Load monitor
        monitor = BitmapFactory.decodeResource(getResources(), R.drawable.monitor);
        scaledmonitor = Bitmap.createScaledBitmap(monitor, screenwidth/4, screenheight - 90, true);
        //float ratio = monitor.getWidth() / monitor.getHeight();
        //monitor.setHeight(screenheight - 100);
        //monitor.setWidth((int)(monitor.getHeight() * ratio));

        // 4c) Load the images of the spaceships
        Spaceship[0] = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_1);
        Spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        Spaceship[2] = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_3);
        Spaceship[3] = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_4);
        // Load Asteroid
        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);
        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null)
        {
            return;
        }
        canvas.drawBitmap(scaledbg,bgX,bgY, null);
        canvas.drawBitmap(scaledbg,bgX+screenwidth,bgY,null);

        // Draw monitor
        if (monitor_active)
            canvas.drawBitmap(scaledmonitor, 0, 60, null);
        else
            canvas.drawBitmap(scaledmonitor, -300, 60, null);

        // 4d) Draw the spaceships
        canvas.drawBitmap(Spaceship[SpaceshipIndex],mX,mY,null);

        stone_anim.draw(canvas);

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("FPS: " + FPS, 130, 75, paint);
    }

    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                // 3) Update the background to allow panning effect
            bgX -= 500*dt;// Allow panning speed
                if(bgX < - screenwidth)
                {
                    bgX = 0;
                }

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                SpaceshipIndex++;
                SpaceshipIndex %= 4;

                //Update Asteroid
                stone_anim.update(System.currentTimeMillis());
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        monitor_active = !monitor_active;

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            short X = (short)event.getX();
            short Y = (short)event.getY();
            // New location where the image to land on
            mX = (short)(X - Spaceship[SpaceshipIndex].getWidth()/2);
            mY = (short)(Y - Spaceship[SpaceshipIndex].getHeight()/2);

            if (CheckCollision(mX, mY, Spaceship[SpaceshipIndex].getWidth(), Spaceship[SpaceshipIndex].getHeight(), stone_anim.getX(), stone_anim.getY(), stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight())){
                Random r = new Random();
                stone_anim.setX(r.nextInt(screenwidth));
                stone_anim.setY(r.nextInt(screenheight));
            }
        }

        return super.onTouchEvent(event);
    }

    private boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        if ((x1 - w1/2) > (x2 + w2/2) || (x1 + w1/2) < (x2 - w2/2) ||
            (y1 - h1/2) > (y2 + h2/2) || (y1 + h1/2) < (y2 - h2/2))
            return false;

        return true;
    }
}
