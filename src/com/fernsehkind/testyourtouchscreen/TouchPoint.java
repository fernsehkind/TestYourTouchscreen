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


public class TouchPoint {
    public float x;
    public float y;
    public float pressure;
    public TouchPointAction action;
    
    public TouchPoint() {
        this(0, 0, 1, TouchPointAction.CANCEL);
    }
    
    public TouchPoint(float x, float y) {
        this(x, y, 1, TouchPointAction.CANCEL);
        this.x = x;
        this.y = y;
    }
    
    public TouchPoint(float x, float y, TouchPointAction action) {
        this(x, y, 1, action);
    }
    
    public TouchPoint(float x, float y, float pressure, TouchPointAction action) {
        this.x = x;
        this.y = y;
        this.pressure = pressure;
        this.action = action;
    }
    
    public TouchPoint(TouchPoint p) {
        this.x = p.x;
        this.y = p.y;
        this.action = p.action;
    }
}
