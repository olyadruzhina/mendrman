package com.example.a123.space_football;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class CharacterSprite {
    public Bitmap image;
    public int x,y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    boolean goal;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public  void  update(Rect hero1, Rect hero2)
    {
        if (hero1.left < x + image.getWidth() &&
                hero1.left + hero1.width() > x &&
                hero1.top < y + image.getHeight() &&
                hero1.height() + hero1.top > y) {
            xVelocity = xVelocity * -1;// collision detected!
            yVelocity = yVelocity * -1;
        }

        if (hero2.left < x + image.getWidth() &&
                hero2.left + hero2.width() > x &&
                hero2.top < y + image.getHeight() &&
                hero2.height() + hero2.top > y) {
            xVelocity = xVelocity * -1;// collision detected!
            yVelocity = yVelocity * -1;
        }

        x += xVelocity;
        y += yVelocity;
        if ((x > screenWidth - image.getWidth()-5) || (x < 5)) {
            xVelocity = xVelocity * -1;
        }
        if (((y > screenHeight - image.getHeight()-5) || (y < 5)) && x<screenWidth/2-150 && x>screenWidth/2+150) {
            yVelocity = yVelocity * -1;
        }
        if (y > screenHeight - image.getHeight()-5 || (y < 5)){
            goal = true;
        }
    }
}