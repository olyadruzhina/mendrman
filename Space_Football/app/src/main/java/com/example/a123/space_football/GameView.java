package com.example.a123.space_football;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    Random rand = new Random();
    int [] x = new int [100];
    int [] y = new int [100];
    private CharacterSprite player1, player2, asteroid, star1, star2, star3, star4;
    boolean moving = false;
    private int mActivePointerId;



    int upPI = 0;
    int downPI = 0;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        player1 = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.player1));
        player2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.player2));
        asteroid = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.asteroid));
        player1.x=screenWidth/2-player1.image.getWidth()/2 ;
        player1.y=150;
        player2.x=screenWidth/2-player2.image.getWidth()/2;
        player2.y=screenHeight-player2.image.getHeight()-150;
        asteroid.x=screenWidth/2-asteroid.image.getWidth()/2;
        asteroid.y=screenHeight/2-asteroid.image.getHeight()/2;

        for(int i = 0; i< 100; i++){
            x[i] = rand.nextInt(screenWidth);
            y[i] = rand.nextInt(screenHeight);
        }


        thread.setRunning(true);
        thread.start();

    }


    //@Override
    public boolean onTouchEvent(MotionEvent event) {

        for (int i = 0; i < event.getPointerCount(); i++) {
            mActivePointerId = event.getPointerId(0);
            int action = event.getActionMasked();

            int pointerIndex = event.findPointerIndex(mActivePointerId);
            float x = event.getX(pointerIndex);
            float y = event.getY(pointerIndex);

            if (y >= screenHeight / 2) {
                player2.x = (int) x - player2.image.getWidth() / 2;
                player2.y = (int) y - player2.image.getHeight() / 2;
                if (player2.y < screenHeight / 2)
                    player2.y = screenHeight / 2;
            }
        }
        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.rgb(20,0,40));

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);

            for (int i = 0; i<100; i++){
                canvas.drawCircle(x[i],y[i],2,paint);
            }

            paint.setStrokeWidth(10f);
            paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
            canvas.drawCircle(screenWidth/2,screenHeight/2,120,paint);
            paint.setColor(Color.rgb(20,0,40));
            canvas.drawCircle(screenWidth/2,screenHeight/2,110,paint);
            paint.setColor(Color.WHITE);
            canvas.drawLine(0,screenHeight/2,screenWidth,screenHeight/2,paint);


            player1.draw(canvas);
            player2.draw(canvas);
            asteroid.draw(canvas);
        }
    }



    public void update(){
        //asteroid.update();
    }


}
