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
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.fernsehkind.testyourtouchscreen.helper.StringHelper;

public class NumberOfTouchPointTestActivity extends Activity {

    private TouchDrawView drawView;
    private NumberOfTouchPointBitmap bitmap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, 
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        showInstructions();
        initView();        
        initBackgroundBitmap();
        drawView.setTargetPatternBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        int numberOfTouchPointerIdsDetected = getNumberOfDetectedPointerIds();
        
        String resultText = String.format(this.getString(R.string.activity_number_of_touch_point_test_end_dialog_text), numberOfTouchPointerIdsDetected);
        
        SimpleDialog simpleDialog = new SimpleDialog(this, 
                this.getString(R.string.activity_number_of_touch_point_test_end_dialog_title),
                resultText,
                null);
        simpleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            
            @Override
            public void onDismiss(DialogInterface dialog) {
                NumberOfTouchPointTestActivity.super.onBackPressed();
            }
        });
        
        simpleDialog.show();
    }

    private int getNumberOfDetectedPointerIds() {
        int numberOfTouchPointerIdsDetected = 0;
        
        PointerId[] pointerIds = drawView.pointerIds;
        
        for (int i=0; i < pointerIds.length; i++) {
            PointerId x = pointerIds[i];
            if (x == null) {
                continue;
            }
            if ((x.getOldPointerLists().size() > 0) || (x.getPresentPointerList().size() > 0)) {
                numberOfTouchPointerIdsDetected++;
            }
        }
        return numberOfTouchPointerIdsDetected;
    }
    
    private void initBackgroundBitmap() {
        bitmap = new NumberOfTouchPointBitmap();
        bitmap.init(drawView.viewWidth, drawView.viewHeight, Bitmap.Config.ARGB_8888);
    }

    private void showInstructions() {
        SimpleDialog simpleDialog = new SimpleDialog(this, 
                this.getString(R.string.activity_number_of_touch_point_test_start_dialog_title),
                this.getString(R.string.activity_number_of_touch_point_test_start_dialog_text),
                null);
        simpleDialog.show();
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
}
