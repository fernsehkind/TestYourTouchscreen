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

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.fernsehkind.testyourtouchscreen.helper.StringHelper;

public class MultiTouchDrawTestActivity extends Activity implements ShakeEvent {

    private TouchDrawView drawView;
    private ShakeDetector shakeDetector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, 
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        initView();
        
        shakeDetector = new ShakeDetector(this, this);
    }
    
    private void initView() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        drawView = new TouchDrawView(this);
        drawView.touchDrawPointSize = StringHelper.TryParse(sharedPrefs.getString("touch_point_draw_size", "5"), 5);
        drawView.touchPointerTransparency = StringHelper.TryParse(sharedPrefs.getString("touch_point_transparency", "0"), 0);
        drawView.drawGrid = sharedPrefs.getBoolean("show_grid", false);
        drawView.gridSize = StringHelper.TryParse(sharedPrefs.getString("grid_size", "5"), 5);
        drawView.evaluatePressure = sharedPrefs.getBoolean("touch_point_evaluate_pressure", false);
        drawView.pressureAmplification = StringHelper.TryParse(sharedPrefs.getString("touch_point_pressure_amplification", "1"), 1);
        drawView.init();
        
        setContentView(drawView);
        drawView.requestFocus();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.pause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        shakeDetector.resume();
    }
    
    
    public void shakeTriggered() {
        drawView.reset();
    }
}
