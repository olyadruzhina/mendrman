<<<<<<< HEAD

package com.example.a123.space_football;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {
    public Bitmap image;
    public int x, y;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

}
=======

package com.example.a123.space_football;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class CharacterSprite {
    public Bitmap image;
    public int x,y;
    private int xVelocity = 5;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private  boolean collision;
    public boolean goal1, goal2;
    private Rect ball;
    int movingVectorX=0, movingVectorY=0;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public  void  update(Rect hero)
    {
        ball = new Rect(x/1,y/1, x+image.getWidth(), y+image.getHeight());

        x = x + xVelocity/20;
        y = y + yVelocity/20;

        if (hero.left < x + image.getWidth() &&
                hero.left + hero.width() > x &&
                hero.top < y + image.getHeight() &&
                hero.height() + hero.top > y) {

            xVelocity = xVelocity * -1;
            yVelocity = yVelocity * -1;
            collision=true;
        }

        if (collision) {

            xVelocity = x - hero.left ;
            yVelocity = y - hero.top ;

            collision=false;
        }
        if ((x > screenWidth - image.getWidth() - 5) || (x < 5)) {
            xVelocity = xVelocity * -1;
        }
        if (((y > screenHeight - image.getHeight() - 5) || (y < 5)) && x < screenWidth / 2 - 150 && x > screenWidth / 2 + 150) {
            yVelocity = yVelocity * -1;
        }
        if (y > screenHeight - image.getHeight() - 5){
            goal2 = true;
            collision=false;
            x=screenWidth/2-image.getWidth()/2;
            y=screenHeight/2-image.getHeight()/2;
            xVelocity=0;
            yVelocity=0;
        }
        if (y < 5) {
            goal1 = true;
            collision=false;
            x=screenWidth/2-image.getWidth()/2;
            y=screenHeight/2-image.getHeight()/2;
            xVelocity = 0;
            yVelocity=0;
        }
    }
}
>>>>>>> e2f06101a330019a9b1da6a8567ff7138d1ebd70
