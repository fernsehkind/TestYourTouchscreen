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
import java.util.Random;

import android.content.Context;

public class TipDialogRandomizer {

    public TipDialog getTipDialog(Context context, Class<?> activity) {
        List<Tip> tips = new ArrayList<Tip>();
        
        for (Tip tip : Tip.values()) {
            if (tip.getActivity() == activity) {
                tips.add(tip);
            }
        }
        
        String textToShow = "";
        int max = tips.size();
        int index = 0;
        
        
        if (max == 0) {
            textToShow = Tip.SHAKE_YOUR_SCREEN.getText();
        }
        else {
            Random rand = new Random();
            try {
                index = rand.nextInt(max);
                textToShow = tips.get(index).getText();
            }
            catch (Exception e) {
                textToShow = "muh";
            }
            
        }

        TipDialog dialog = new TipDialog(context, textToShow);
        return dialog;
    }
}
