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

package com.fernsehkind.testyourtouchscreen.helper;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenHelper {
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
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
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
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
}
