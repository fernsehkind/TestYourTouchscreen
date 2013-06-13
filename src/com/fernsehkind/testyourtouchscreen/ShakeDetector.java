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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
    public float relAccel;
    private ShakeEvent shakeEvent;
    private SensorManager sensorManager;
    private float absAccelCurrent;
    private float absAccelLast;

    public ShakeDetector(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        relAccel = 0f;
        absAccelCurrent = SensorManager.GRAVITY_EARTH;
        absAccelLast = SensorManager.GRAVITY_EARTH;
        shakeEvent = null;
    }
    
    public ShakeDetector(Context context, ShakeEvent shakeEvent) {
        this(context);
        this.shakeEvent = shakeEvent;  
    }
    
    public void resume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  SensorManager.SENSOR_DELAY_NORMAL);
        relAccel = 0f;
        absAccelCurrent = SensorManager.GRAVITY_EARTH;
        absAccelLast = SensorManager.GRAVITY_EARTH;
    }
    
    public void pause() {
        sensorManager.unregisterListener(this);
    }
    
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        absAccelLast = absAccelCurrent;
        absAccelCurrent = (float)Math.sqrt((double)(x*x + y*y + z*z));
        float delta = absAccelCurrent - absAccelLast;
        relAccel = relAccel * 0.65f + delta * 0.35f;
        if (relAccel > 3.0f) {
            if (shakeEvent != null) {
                shakeEvent.shakeTriggered();
            }
        }
    }
}
