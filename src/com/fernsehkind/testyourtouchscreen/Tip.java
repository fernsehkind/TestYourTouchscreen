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

public enum Tip {
    SHAKE_YOUR_SCREEN("If you shake your device, your screen is cleared.", MultiTouchDrawTestActivity.class),
    CHANGE_DRAW_POINT_SIZE("You can change the draw size of a touch point in the settings screen.", MultiTouchDrawTestActivity.class),
    CHANGE_DRAW_POINT_TRANSPARENCY("You can change the transparency of a touch point in the settings screen.", MultiTouchDrawTestActivity.class),
    SHOW_GRID("A grid in the background of the screen can be very helpful. Go to the settings screen to activate the grid and adjust the grid size.", MultiTouchDrawTestActivity.class),
    EVALUATE_PRESSURE("Some touchscreens can evaluate the pressure of a touch. You can activate this feature by enabling the \"Evaluate pressure\" setting in the settings screen.", MultiTouchDrawTestActivity.class),
    ;
    
    
    private String text;
    private Class<?> activity;
    
    private Tip(String text, Class<?> activity) {
        this.text = text;
        this.activity = activity;
    }
    
    public String getText() {
        return this.text;
    }
    
    public Class<?> getActivity() {
        return this.activity;
    }
    
}

