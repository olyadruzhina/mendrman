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

    //инициализация спрайтов
        player1 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.player1));
        player2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.player2));
        asteroid = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid));
        //положение белого космонавта при создании 
        player1.x = screenWidth / 2 - player1.image.getWidth() / 2;
        player1.y = 150;
        //положение красного космотнавта при создании
        player2.x = screenWidth / 2 - player2.image.getWidth() / 2;
        player2.y = screenHeight - player2.image.getHeight() - 150;
        //положение мячика при создании
        asteroid.x = screenWidth / 2 - asteroid.image.getWidth() / 2;
        asteroid.y = screenHeight / 2 - asteroid.image.getHeight() / 2;
        //ширина-высота для обоих космонавтов одинаковы
        playerHeight = player1.image.getHeight();
        playerWidth = player1.image.getWidth();
        //ширина-высота спрайта астероида
        asteroidWidth = asteroid.image.getWidth();
        asteroidHeight = asteroid.image.getHeight();

        //генерируем рандомные координаты для будущих звёздочек
        for (int i = 0; i < 100; i++) {
            x[i] = rand.nextInt(screenWidth);
            y[i] = rand.nextInt(screenHeight);
        }

        thread.setRunning(true);
        thread.start();
    }


    //@Override
    public boolean onTouchEvent(MotionEvent event) {

        int count = event.getPointerCount();//кол-во касаний на данный момент
        int maskedAction = event.getActionMasked();//индекс действия, не зависящий от индекс и ID касания

        for (int i = 0; i < count; i++) {
            switch (maskedAction) {

                case MotionEvent.ACTION_DOWN: {//в момент первого...
                }
                case MotionEvent.ACTION_POINTER_DOWN: {//...и всех последующих _нажатий_...
                //проверка, совершено ли нажатие пальцем с текущим индексом на одного из космонавтов, или нет
                    if (pointerIsOnPlayer(i, event, player1) && pointerId1 == -1) {
                        pointerId1 = event.getPointerId(event.getActionIndex());//если да, то запоминаем индекс этого пальца
                    }
                    if (pointerIsOnPlayer(i, event, player2) && pointerId2 == -1) {
                        pointerId2 = event.getPointerId(event.getActionIndex());//если да, то запоминаем индекс этого пальца
                    }
                }

                case MotionEvent.ACTION_MOVE: {
                    if (i == pointerId1) {//если текущее событие движения совершается пальцем, управляющим белым космонавтом
                        player1.x = (int) event.getX(event.findPointerIndex(pointerId1)) - playerWidth / 2;//координаты космонавта соответсвуют новым координатам пальца на экране
                        player1.y = (int) event.getY(event.findPointerIndex(pointerId1)) - playerHeight / 2;

                        //ограничения движения белого космонавта за пределами своей половины экрана
                        if (player1.y > screenHeight / 2 - playerHeight)
                            player1.y = screenHeight / 2 - playerHeight - edge;
                        if (player1.y < edge) player1.y = edge;
                        if (player1.x < edge) player1.x = edge;
                        if (player1.x + playerWidth > screenWidth - edge)
                            player1.x = screenWidth - playerWidth - edge;
                    }
                    //аналогично для красного космонавта
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

                case MotionEvent.ACTION_UP: {//для всех прерванных касаний экрана...
                }
                case MotionEvent.ACTION_POINTER_UP: {//и до последнего

                    if (event.getPointerId(event.getActionIndex()) == pointerId1) {//если с экрана убран палец, управляющий белым космонавтом, сбрасываем переменную индекса касания
                        pointerId1 = -1;
                    }
                    if (event.getPointerId(event.getActionIndex()) == pointerId2) {//если с экрана убран палец, управляющий белым космонавтом, сбрасываем переменную индекса касания
                        pointerId2 = -1;
                    }
                    break;
                }
            }
        }
        return true;
    }

    public boolean pointerIsOnPlayer(int i, MotionEvent event, CharacterSprite player){
        if (event.getY(i) < player.y + playerHeight && event.getY(i) > player.y && event.getX(i) < player.x + playerWidth && event.getX(i) > player.x)//если координаты касания находятся внутри текущих координат спрайта игрока, то true
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
            canvas.drawColor(Color.rgb(20, 0, 40));//фон

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);

            for (int i = 0; i < 100; i++) {
                canvas.drawCircle(x[i], y[i], 2, paint);//звёзды
            }
            //рисование разметки, вывод текста
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
            //отображение спрайтов
            player1.draw(canvas);
            player2.draw(canvas);
            asteroid.draw(canvas);
        }
    }

public boolean isCollision(CharacterSprite asteroid, CharacterSprite hero) {

        if (hero.x < asteroid.x + asteroid.image.getWidth() &&
            hero.x + hero.image.getWidth() > asteroid.x &&
            hero.y < asteroid.y + asteroid.image.getHeight() &&
            hero.y + hero.image.getHeight() > asteroid.y) {//если координаты астероида пересекаются с координатами игрока, то произошло его столкновение с игроком
        xVelocity = xVelocity * -1;//меняем направление вектора скорости астероида
        yVelocity = yVelocity * -1;
        return true;
    } else return false;
}

    public void update() {

    //новые координаты астероида
        asteroid.x = asteroid.x + xVelocity/10;
        asteroid.y = asteroid.y + yVelocity/10;

        if (isCollision(asteroid, player1) ) {

            xVelocity = asteroid.x - player1.x ;//вычисление длины вектора скорости астероида
            yVelocity = asteroid.y - player1.y ;
        }

        if (isCollision(asteroid, player2)){
            xVelocity = asteroid.x - player2.x ;//вычисление длины вектора скорости астероида
            yVelocity = asteroid.y - player2.y ;
        }

        if (asteroid.x > screenWidth - asteroidWidth - edge || asteroid.x < edge) {//если астероид ударился об длинную сторону экарана, он меняет своё направление
            xVelocity = xVelocity * -1;
        }

        if ((asteroid.y > screenHeight - asteroidHeight - edge || asteroid.y < edge) && (asteroid.x  < screenWidth / 2 - 150 || asteroid.x + asteroidWidth > screenWidth / 2 + 150)) {
        //если астероид ударился о бортик по одну из сторон от ворот, он меняет своё направление
            yVelocity = yVelocity * -1;
        }

        if (asteroid.y > screenHeight - asteroidHeight){//если астероид вылетел за пределы экрана через нижние ворота
            b_goal2 = true;
        }

        if (asteroid.y < 0) {//если астероид вылетел за пределы экрана через верхние ворота
            b_goal1 = true;
        }

        if (b_goal1 || b_goal2) {//если гол
            player1.x = screenWidth / 2 - player1.image.getWidth() / 2;//оба игрока возвращаются на свои исходные позиции
            player1.y = 150;
            player2.x = screenWidth / 2 - player2.image.getWidth() / 2;
            player2.y = screenHeight - player2.image.getHeight() - 150;
            asteroid.x = screenWidth / 2 - asteroidWidth / 2;//астероид возвращается в центр экрана
            asteroid.y = screenHeight / 2 - asteroidHeight / 2;
            xVelocity = 0;//астероид не движется, пока не получит импульс от столкновения
            yVelocity = 0;
            pointerId1 = -1;//если пальцы ещё на экране, касания с этими индексами больше не контролируют игроков
            pointerId2 = -1;
        }

        if (b_goal1) {
            goal2++;//если гол в верхние ворота, игроку 2 + 1 очко
            b_goal1 = false;
        }

        if (b_goal2) {
            goal1++;//если гол в нижние ворота, игроку 1 + 1 очко
            b_goal2 = false;
        }
    }
}
