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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TestYourTouchscreenActivity extends Activity {

    private Class<?> activityToStart;
    private SharedPreferences sharedPrefs;
    private TipDialog tipDialog;
    private TipDialogRandomizer tipDialogRandomizer = new TipDialogRandomizer();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_your_touchscreen);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_your_touchscreen, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_about:
            AboutDialog dialog = new AboutDialog(this);
            dialog.show();
            break;
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
        return true;
    }
    
    public void onStartMultiTouchDrawTest(View view) {
        prepareStartOfActivity(MultiTouchDrawTestActivity.class);
    }
    
    private void startGivenActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
    
    private void prepareStartOfActivity(Class<?> activity) {
        activityToStart = activity;
        
        if (!sharedPrefs.getBoolean("show_tips", true)) {
            startGivenActivity(activity);
            return;
        }
        else {
            tipDialog = tipDialogRandomizer.getTipDialog(this, activity);
            tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Editor editor = sharedPrefs.edit();
                    editor.putBoolean("show_tips", tipDialog.getCheckBoxShowHintValue());
                    editor.commit();
                    startGivenActivity(activityToStart);
                }
            });
            tipDialog.show();
        }
    }

}
