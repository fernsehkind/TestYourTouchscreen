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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;


public class TouchDrawView extends View implements OnTouchListener {
    public int touchDrawPointSize = 5;
    public boolean drawGrid = false;
    public int gridSize = 5;
    public PointerId[] pointerIds = new PointerId[NUMBER_OF_POINTER_ID];
    public boolean drawTouches = true;
    public int touchPointerTransparency = 0;
    public boolean evaluatePressure = false;
    public int pressureAmplification = 5;
    
    private static final int NUMBER_OF_POINTER_ID = 20;
    private List<List<TouchPoint>> pointerIdsToDraw = new ArrayList<List<TouchPoint>>();
    private List<Integer> colorList = new ArrayList<Integer>();
    
    private boolean initialized = false;
    private Paint paint = new Paint();
    private final int screenWidth;
    private final int screenHeight;
    private Canvas canvasBackbuffer;
    private Bitmap bitmapBackbuffer;
    private Canvas canvasTouches;
    private Bitmap bitmapTouches;
    
    
    public TouchDrawView(Context context) {
        super(context);
        
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
        
        for (int i = 0; i < NUMBER_OF_POINTER_ID; i++) {
            pointerIds[i] = new PointerId(i);
            List<TouchPoint> listTouchPoints = new ArrayList<TouchPoint>();
            pointerIdsToDraw.add(listTouchPoints);
        }
    }
    
    public void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint.setAntiAlias(true);
        initCanvasBackbuffer();
        initCanvasTouches();
        initColorList();
        initialized = true;
    }
    
    public void reset() {
        pointerIdsToDraw.clear();
        for (int i = 0; i < NUMBER_OF_POINTER_ID; i++) {
            pointerIds[i] = new PointerId(i);
            List<TouchPoint> listTouchPoints = new ArrayList<TouchPoint>();
            pointerIdsToDraw.add(listTouchPoints);
        }
        initCanvasBackbuffer();
        initCanvasTouches();
        invalidate();
    }
      
    public boolean onTouch(View view, MotionEvent ev) {
        
        int pointerCount = ev.getPointerCount();
        
        addHistoricalPointersToPointerIdsArray(ev);

        int actionMasked = ev.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_CANCEL) {
            for (int i = 0; i < NUMBER_OF_POINTER_ID; i++) {
                pointerIds[i].addTouchPoint(new TouchPoint(0,0, TouchPointAction.CANCEL));
            }
        }
        else if (actionMasked == MotionEvent.ACTION_DOWN) {
            getTouchPointAndAddItToPointerIds(ev, ev.getPointerId(0), 0, TouchPointAction.DOWN);
            storeCoordsOfNonEventTouchPoints(ev, pointerCount, 0);
        }
        else if (actionMasked == MotionEvent.ACTION_UP) {
            getTouchPointAndAddItToPointerIds(ev, ev.getPointerId(0), 0, TouchPointAction.UP);
        }
        else if (actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            int pointerIndex = ev.getActionIndex();
            int pointerId = ev.getPointerId(pointerIndex);
            getTouchPointAndAddItToPointerIds(ev, pointerId, pointerIndex, TouchPointAction.DOWN);
            storeCoordsOfNonEventTouchPoints(ev, pointerCount, pointerIndex);            
        }
        else if (actionMasked == MotionEvent.ACTION_POINTER_UP) {
            int pointerIndex = ev.getActionIndex();
            int pointerId = ev.getPointerId(pointerIndex);
            getTouchPointAndAddItToPointerIds(ev, pointerId, pointerIndex, TouchPointAction.UP);
            storeCoordsOfNonEventTouchPoints(ev, pointerCount, pointerIndex);
        }
            
        else if (actionMasked == MotionEvent.ACTION_MOVE) {
            storeCoordsOfNonEventTouchPoints(ev, pointerCount, -1);
        }
    
        drawTouchPointerIds(canvasTouches);
        invalidate();
        return true;
    }

    private void getTouchPointAndAddItToPointerIds(MotionEvent ev, 
            int pointerId, int pointerIndex, TouchPointAction action) {
        if (isValidPointerId(pointerId)) {
            TouchPoint p = new TouchPoint();
            p.x = ev.getX(pointerIndex);
            p.y = ev.getY(pointerIndex);
            p.pressure = ev.getPressure(pointerIndex);
            p.action = action;
            pointerIds[pointerId].addTouchPoint(p);
            pointerIdsToDraw.get(pointerId).add(p);
        }
    }
    
    private void storeCoordsOfNonEventTouchPoints(MotionEvent ev,
            int pointerCount, int pointerIndexIgnore) {
        int pointerId;
        for (int i = 0; i < pointerCount; i++) {
            pointerId = ev.getPointerId(i);
            if (i != pointerIndexIgnore) {
                if (isValidPointerId(pointerId)) {
                    TouchPoint p = new TouchPoint();
                    p.x = ev.getX(i);
                    p.y = ev.getY(i);
                    p.pressure = ev.getPressure(i);
                    p.action = TouchPointAction.MOVE;
                    pointerIds[pointerId].addTouchPoint(p);
                    pointerIdsToDraw.get(pointerId).add(p);
                }
            }
        }
    }
    
    private void addHistoricalPointersToPointerIdsArray(MotionEvent ev) {
        int pointerCount = ev.getPointerCount();
        int historyCount = ev.getHistorySize();
        
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = ev.getPointerId(i);
            if (isValidPointerId(pointerId)) {
                for (int j = 0; j < historyCount; j++) {
                    TouchPoint p = new TouchPoint();
                    p.x = ev.getHistoricalX(i, j);
                    p.y = ev.getHistoricalY(i, j);
                    p.pressure = ev.getHistoricalPressure(i, j);
                    p.action = TouchPointAction.MOVE;
                    pointerIds[pointerId].addTouchPoint(p);
                    pointerIdsToDraw.get(pointerId).add(p);
                }
            }
        }
    }
    
    private void initColorList() {
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFF, 0x00, 0x00));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x0, 0xFF, 0x00));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x0, 0x0, 0xFF));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFF, 0xFF, 0x00));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x0, 0xFF, 0xFF));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFF, 0x00, 0xFF));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x7F, 0x0, 0x0));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x00, 0x7F, 0x00));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x0, 0x0, 0x7F));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFD, 0xD9, 0xB5));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x87, 0xA9, 0x6B));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x9F, 0x81, 0x70));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x73, 0x66, 0xBD));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xB4, 0x67, 0x4D));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xCE, 0xFF, 0x1D));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xF8, 0xD5, 0x68));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFC, 0x74, 0xFD));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xDB, 0xD7, 0xD2));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0x00, 0xFF, 0xA1));
        colorList.add(Color.argb((100 - touchPointerTransparency) * 255 / 100, 0xFF, 0x75, 0x38));
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (initialized) {
            canvas.drawBitmap(bitmapBackbuffer, 0, 0, null);
            canvas.drawBitmap(bitmapTouches, 0, 0, null);
        }
    }

    private void initCanvasBackbuffer() {
        
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmapBackbuffer = Bitmap.createBitmap(screenWidth, screenHeight, conf);
        canvasBackbuffer = new Canvas(bitmapBackbuffer);
        
        drawBackbuffer();
    }
    
    private void drawBackbuffer() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(Color.WHITE);
        canvasBackbuffer.drawRect(0, 0, screenWidth, screenHeight, paint);
        if (drawGrid) {
            drawGrid(canvasBackbuffer);
        }
    }

    private void drawGrid(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        paint.setColor(Color.rgb(0xAA, 0xAA, 0xAA));
        for (int indexRow = 0; (indexRow * gridSize) < screenHeight; indexRow++){
            canvas.drawLine(0, indexRow * gridSize, screenWidth-1, indexRow * gridSize, paint);
        }
        for (int indexCol = 0; (indexCol * gridSize) < screenWidth; indexCol++){
            canvas.drawLine(indexCol * gridSize, 0, indexCol * gridSize, screenHeight-1, paint);
        }
    }
    
    private void initCanvasTouches() {
        bitmapTouches = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvasTouches = new Canvas(bitmapTouches);
    }

    private void drawTouchPointerIds(Canvas canvas) {
        float radius = touchDrawPointSize;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        for (int i = 0; i < pointerIdsToDraw.size(); i++) {
            paint.setColor(colorList.get(i));
            for (TouchPoint p : pointerIdsToDraw.get(i)) {
                if (evaluatePressure) {
                    radius = touchDrawPointSize + ((p.pressure - 0.5f) * pressureAmplification * 10);
                }
                canvas.drawCircle(p.x, p.y,  radius, paint);
            }
            pointerIdsToDraw.get(i).clear();
        }
    }

    @SuppressWarnings("deprecation")
    private int getScreenWidth() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point outSize = new Point();
        int screenWidth = 0;
        try {
            display.getSize(outSize);
            screenWidth = outSize.x;
        }
        catch (java.lang.NoSuchMethodError ignore) {
            screenWidth = display.getWidth();
        }
        return screenWidth;
    }

    @SuppressWarnings("deprecation")
    private int getScreenHeight() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point outSize = new Point();
        int screenHeight = 0;
        try {
            display.getSize(outSize);
            screenHeight = outSize.y;
        }
        catch (java.lang.NoSuchMethodError ignore) {
            screenHeight = display.getHeight();
        }
        return screenHeight;
    }
    
    private boolean isValidPointerId(int pointerId) {
        if (pointerId < 0) {
            return false;
        }
        else if (pointerId >= NUMBER_OF_POINTER_ID) {
            return false;
        }
        else {
            return true;
        }
    }
}
