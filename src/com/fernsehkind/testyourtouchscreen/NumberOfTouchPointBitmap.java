/*
 * Copyright 2013 Ralph Haussmann
 * https://github.com/fernsehkind/TestYourTouchscreen

 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.fernsehkind.testyourtouchscreen;

import java.util.Observable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class NumberOfTouchPointBitmap extends Observable implements TargetPatternBitmap {
    private Bitmap bitmap = null;
    private Canvas canvas = null;
    private Paint paint = new Paint();
    private Rect bitmapSize = null;
    private Bitmap.Config bitmapConfig;
    private int currentPointIndex;
    private final static int NUMBER_OF_POINTS = 16;
    
    public NumberOfTouchPointBitmap() {
        super();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        currentPointIndex = -1;
    }
    
    public boolean init(int width, int height, Bitmap.Config config) {
        this.bitmapConfig = config;
        bitmap = Bitmap.createBitmap(width, height, config);
        canvas = new Canvas(bitmap);
        bitmapSize = new Rect(0, 0, width, height);
        currentPointIndex = -1;
        
        for (int i = 0;  i < NUMBER_OF_POINTS; i++) {
            addNextPoint();
        }
        return true;
    }
    
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void clear() {
        currentPointIndex = -1;
        bitmap = Bitmap.createBitmap(bitmapSize.width(), bitmapSize.height(), bitmapConfig);
        canvas = new Canvas(bitmap);
        bitmapChanged();
    }
    
    public void addNextPoint() {
        if (currentPointIndex == NUMBER_OF_POINTS - 1) {
            return;
        }
        
        currentPointIndex++;
        
        final int numberOfPointsPerRow = (int)(Math.sqrt(NUMBER_OF_POINTS) + 0.5);
        final int offsetX = (bitmapSize.width() / ((numberOfPointsPerRow + 1) * 2)); 
        final int offsetY = (bitmapSize.height() / ((numberOfPointsPerRow + 1) * 2));
        final int spaceX = (bitmapSize.width() - (offsetX * 2)) / (numberOfPointsPerRow - 1);
        final int spaceY = (bitmapSize.height() - (offsetY * 2)) / (numberOfPointsPerRow - 1);
        final int x = offsetX + ((currentPointIndex / numberOfPointsPerRow) * spaceX);
        final int y = offsetY + ((currentPointIndex % numberOfPointsPerRow) * spaceY);
        final int radius = bitmapSize.width() < bitmapSize.height() ? 
                (bitmapSize.width() - offsetX) / (numberOfPointsPerRow - 1) / 4 : 
                    (bitmapSize.height() - offsetY) / (numberOfPointsPerRow - 1) / 4;
        
        canvas.drawCircle(x, y, radius, paint);
        bitmapChanged();
    }

    private void bitmapChanged() {
        super.setChanged();
        super.notifyObservers();
    }
}
