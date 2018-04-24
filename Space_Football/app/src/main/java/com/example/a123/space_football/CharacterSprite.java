
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
