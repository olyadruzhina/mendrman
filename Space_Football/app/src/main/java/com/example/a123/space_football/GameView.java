
package com.example.a123.space_football;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    Random rand = new Random();
    int[] x = new int[100];
    int[] y = new int[100];
    private CharacterSprite player1, player2, asteroid, star1, star2, star3, star4;
    boolean moving = false;
    Rect hero1, hero2;
    int goal1 = 0, goal2 = 0;
    boolean update;
    private int mActivePointerId;
    private long lastDrawNanoTime = -1;


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

        player1 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.player1));
        player2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.player2));
        asteroid = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid));
        player1.x = screenWidth / 2 - player1.image.getWidth() / 2;
        player1.y = 150;
        player2.x = screenWidth / 2 - player2.image.getWidth() / 2;
        player2.y = screenHeight - player2.image.getHeight() - 150;
        asteroid.x = screenWidth / 2 - asteroid.image.getWidth() / 2;
        asteroid.y = screenHeight / 2 - asteroid.image.getHeight() / 2;

        for (int i = 0; i < 100; i++) {
            x[i] = rand.nextInt(screenWidth);
            y[i] = rand.nextInt(screenHeight);
        }


        thread.setRunning(true);
        thread.start();

    }


    //@Override
    public boolean onTouchEvent(MotionEvent event) {

        for (int i = 0; i < event.getPointerCount(); i++) {
            //   mActivePointerId = event.getPointerId(0);
            //    int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();

            // get pointer ID
            int pointerId = event.getPointerId(pointerIndex);

            // get masked (not specific to a pointer) action
            int maskedAction = event.getActionMasked();

            //int pointerIndex = event.findPointerIndex(mActivePointerId);
            float x = event.getX(pointerIndex);
            float y = event.getY(pointerIndex);

            switch (maskedAction) {

                case MotionEvent.ACTION_DOWN: {
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: {
                    // TODO use data
                    break;
                }
                case MotionEvent.ACTION_MOVE: { // a pointer was moved
                    // TODO use data
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    break;
                }
            }


            if (pointerId == 0) {
                player2.x = (int) x - player2.image.getWidth() / 2;
                player2.y = (int) y - player2.image.getHeight() / 2;
                if (player2.y <= screenHeight / 2) player2.y = screenHeight / 2;
                if (player2.y + player2.image.getHeight() > screenHeight)
                    player2.y = screenHeight - player2.image.getHeight();
                if (player2.x < 0) player2.x = 0;
                if (player2.x + player2.image.getWidth() > screenWidth)
                    player2.x = screenWidth - player2.image.getWidth();
            } else {
               player1.x = (int) x - player1.image.getWidth() / 2;
                player1.y = (int) y - player1.image.getHeight() / 2;
                    if (player1.y > screenHeight / 2)
                   player1.y = screenHeight / 2 - player2.image.getHeight();
                if (player1.y < 0) player1.y = 0;
                if (player1.x < 0) player1.x = 0;
                if (player1.x + player1.image.getWidth() > screenWidth)
                    player1.x = screenWidth - player1.image.getWidth();
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
            canvas.drawColor(Color.rgb(20, 0, 40));

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);

            for (int i = 0; i < 100; i++) {
                canvas.drawCircle(x[i], y[i], 2, paint);
            }

            paint.setStrokeWidth(10f);
            paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
            canvas.drawCircle(screenWidth / 2, screenHeight / 2, 120, paint);
            paint.setColor(Color.rgb(20, 0, 40));
            canvas.drawCircle(screenWidth / 2, screenHeight / 2, 110, paint);
            paint.setColor(Color.WHITE);
            canvas.drawLine(0, screenHeight / 2, screenWidth + 0, screenHeight / 2, paint);
            canvas.drawLine(0, 5, screenWidth / 2 - 150, 5, paint);
            canvas.drawLine(screenWidth / 2 + 150, 5, screenWidth / 1, 5, paint);
            canvas.drawLine(0, screenHeight - 5, screenWidth / 2 - 150, screenHeight - 5, paint);
            canvas.drawLine(screenWidth / 2 + 150, screenHeight - 5, screenWidth / 1, screenHeight - 5, paint);
            canvas.drawLine(5, 5, 5, screenHeight - 5, paint);
            canvas.drawLine(screenWidth - 5, 5, screenWidth - 5, screenHeight - 5, paint);


            if (asteroid.goal1) {
                goal2++;
                asteroid.goal1 = false;
            }
            if (asteroid.goal2) {
                goal1++;
                asteroid.goal2 = false;
            }

            paint.setTextSize(50);
            paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
            canvas.drawText("GOALS", screenWidth - 200, 70, paint);
            canvas.drawText("GOALS", 30, screenHeight - 40, paint);
            canvas.drawText(String.valueOf(goal1), screenWidth - 125, 120, paint);
            canvas.drawText(String.valueOf(goal2), 95, screenHeight - 100, paint);

            player1.draw(canvas);
            player2.draw(canvas);
            asteroid.draw(canvas);
        }
    }


    public void update() {

        hero1 = new Rect(player1.x / 1, player1.y / 1, player1.x + player1.image.getWidth(), player1.y + player1.image.getHeight());
        hero2 = new Rect(player2.x / 1, player2.y / 1, player2.x + player2.image.getWidth(), player2.y + player1.image.getHeight());
        asteroid.update(hero1);
        asteroid.update(hero2);




    }
}
