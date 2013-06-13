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



public class PointerId {
    public int pointerId;
    private List<TouchPoint> presentPointerList = new ArrayList<TouchPoint>();
    private List<List<TouchPoint>> oldPointerLists = new ArrayList<List<TouchPoint>>();

    public PointerId(int pointerId) {
        this.pointerId = pointerId;
    }
    
    public void clear() {
        for (int i = 0; i < oldPointerLists.size(); i++) {
            oldPointerLists.get(i).clear();
        }
        oldPointerLists.clear();
        presentPointerList.clear();
    }
    
    public List<TouchPoint> getPresentPointerList() {
        return getTouchPointListCopy(presentPointerList);
    }
    
    public List<List<TouchPoint>> getOldPointerLists() {
        List<List<TouchPoint>> newList = new ArrayList<List<TouchPoint>>();
        for (int i=0; i < oldPointerLists.size(); i++) {
            newList.add(getTouchPointListCopy(oldPointerLists.get(i)));
        }
        return newList;
    }

    public void addTouchPoint(TouchPoint p) {
        if ((p.action == TouchPointAction.UP) ||
                (p.action == TouchPointAction.CANCEL)) {
            if (!presentPointerList.isEmpty()) {
                presentPointerList.add(p);
            }
        }
        else if (p.action == TouchPointAction.MOVE) {
            if (!presentPointerList.isEmpty()) {
                presentPointerList.add(p);
            }
        }
        else if (p.action == TouchPointAction.DOWN) {
            if (!presentPointerList.isEmpty()) {
                if ((presentPointerList.get(presentPointerList.size()-1).action == TouchPointAction.UP) ||
                        (presentPointerList.get(presentPointerList.size()-1).action == TouchPointAction.CANCEL)) {
                    oldPointerLists.add(getTouchPointListCopy(presentPointerList));
                    presentPointerList = new ArrayList<TouchPoint>();
                }
                else {
                    presentPointerList.clear();
                }
            }
            presentPointerList.add(p);
        }
    }
    
    private List<TouchPoint> getTouchPointListCopy(List<TouchPoint> list) {
        List<TouchPoint> newList = new ArrayList<TouchPoint>();
        for (int i = 0; i < list.size(); i++) {
            TouchPoint point = list.get(i);
            newList.add(new TouchPoint(point.x, point.y));
        }
        return newList;
    }
    
}
