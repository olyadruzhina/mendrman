package com.example.a123.space_football;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    Random rand = new Random();
    int[] x = new int[100];
    int[] y = new int[100];
    private CharacterSprite player1, player2, asteroid;
    int goal1 = 0, goal2 = 0, pointerId1= -1, pointerId2= -1, playerHeight, playerWidth, asteroidHeight, asteroidWidth;
    private final int edge = 10;
    private  boolean b_goal1, b_goal2;
    private int xVelocity = 5;
    private int yVelocity = 5;

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
        playerHeight = player1.image.getHeight();
        playerWidth = player1.image.getWidth();
        asteroidWidth = asteroid.image.getWidth();
        asteroidHeight = asteroid.image.getHeight();

        for (int i = 0; i < 100; i++) {
            x[i] = rand.nextInt(screenWidth);
            y[i] = rand.nextInt(screenHeight);
        }

        thread.setRunning(true);
        thread.start();
    }


    //@Override
    public boolean onTouchEvent(MotionEvent event) {

        int count = event.getPointerCount();
        int maskedAction = event.getActionMasked();

        for (int i = 0; i < count; i++) {
            switch (maskedAction) {

                case MotionEvent.ACTION_DOWN: {
                }
                case MotionEvent.ACTION_POINTER_DOWN: {

                    if (pointerIsOnPlayer(i, event, player1) && pointerId1 == -1) {
                        pointerId1 = event.getPointerId(event.getActionIndex());
                    }
                    if (pointerIsOnPlayer(i, event, player2) && pointerId2 == -1) {
                        pointerId2 = event.getPointerId(event.getActionIndex());
                    }
                }

                case MotionEvent.ACTION_MOVE: {
                    if (i == pointerId1) {
                        player1.x = (int) event.getX(event.findPointerIndex(pointerId1)) - playerWidth / 2;
                        player1.y = (int) event.getY(event.findPointerIndex(pointerId1)) - playerHeight / 2;

                        if (player1.y > screenHeight / 2 - playerHeight)
                            player1.y = screenHeight / 2 - playerHeight - edge;
                        if (player1.y < edge) player1.y = edge;
                        if (player1.x < edge) player1.x = edge;
                        if (player1.x + playerWidth > screenWidth - edge)
                            player1.x = screenWidth - playerWidth - edge;
                    }
                    if (i == pointerId2) {
                        player2.x = (int) event.getX(event.findPointerIndex(pointerId2)) - playerWidth / 2;
                        player2.y = (int) event.getY(event.findPointerIndex(pointerId2)) - playerHeight / 2;

                        if (player2.y <= screenHeight / 2) player2.y = screenHeight / 2;
                        if (player2.y + playerHeight > screenHeight - edge)
                            player2.y = screenHeight - playerHeight - edge;
                        if (player2.x < edge) player2.x = edge;
                        if (player2.x + playerWidth > screenWidth - edge)
                            player2.x = screenWidth - playerWidth - edge;
                    }
                    break;
                }

                case MotionEvent.ACTION_UP: {
                }
                case MotionEvent.ACTION_POINTER_UP: {

                    if (event.getPointerId(event.getActionIndex()) == pointerId1) {
                        pointerId1 = -1;
                    }
                    if (event.getPointerId(event.getActionIndex()) == pointerId2) {
                        pointerId2 = -1;
                    }
                    break;
                }
            }
        }
        return true;
    }

    public boolean pointerIsOnPlayer(int i, MotionEvent event, CharacterSprite player){
        if (event.getY(i) < player.y + playerHeight && event.getY(i) > player.y && event.getX(i) < player.x + playerWidth && event.getX(i) > player.x)
            return  true;
        else return  false;
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

public boolean isCollision(CharacterSprite asteroid, CharacterSprite hero) {

        if (hero.x < asteroid.x + asteroid.image.getWidth() &&
            hero.x + hero.image.getWidth() > asteroid.x &&
            hero.y < asteroid.y + asteroid.image.getHeight() &&
            hero.y + hero.image.getHeight() > asteroid.y) {
        xVelocity = xVelocity * -1;
        yVelocity = yVelocity * -1;
        return true;
    } else return false;
}

    public void update() {


        asteroid.x = asteroid.x + xVelocity/10;
        asteroid.y = asteroid.y + yVelocity/10;

        if (isCollision(asteroid, player1) ) {

            xVelocity = asteroid.x - player1.x ;
            yVelocity = asteroid.y - player1.y ;
        }

        if (isCollision(asteroid, player2)){
            xVelocity = asteroid.x - player2.x ;
            yVelocity = asteroid.y - player2.y ;
        }

        if (asteroid.x > screenWidth - asteroidWidth - edge || asteroid.x < edge) {
            xVelocity = xVelocity * -1;
        }

        if ((asteroid.y > screenHeight - asteroidHeight - edge || asteroid.y < edge) && (asteroid.x  < screenWidth / 2 - 150 || asteroid.x + asteroidWidth > screenWidth / 2 + 150)) {
            yVelocity = yVelocity * -1;
        }

        if (asteroid.y > screenHeight - asteroidHeight){
            b_goal2 = true;
        }

        if (asteroid.y < 0) {
            b_goal1 = true;
        }

        if (b_goal1 || b_goal2) {
            player1.x = screenWidth / 2 - player1.image.getWidth() / 2;
            player1.y = 150;
            player2.x = screenWidth / 2 - player2.image.getWidth() / 2;
            player2.y = screenHeight - player2.image.getHeight() - 150;
            asteroid.x = screenWidth / 2 - asteroidWidth / 2;
            asteroid.y = screenHeight / 2 - asteroidHeight / 2;
            xVelocity = 0;
            yVelocity = 0;
            pointerId1 = -1;
            pointerId2 = -1;
        }

        if (b_goal1) {
            goal2++;
            b_goal1 = false;
        }

        if (b_goal2) {
            goal1++;
            b_goal2 = false;
        }
    }
}
