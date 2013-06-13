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

import android.app.Dialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TipDialog extends Dialog {
    
    public TipDialog(final Context context, final String tip) {
        super(context);
        this.setContentView(R.layout.dialog_tip);
        this.setTitle(R.string.dialog_tip_heading);
        setupCloseButton();
        setCheckBoxShowHintToTrue();
        setTextViewText(tip);
    }

    public boolean getCheckBoxShowHintValue() {
        CheckBox showTipsCheckBox = (CheckBox)this.findViewById(R.id.checkBoxShowHints);
        return showTipsCheckBox.isChecked();
        
    }
    
    private void setupCloseButton() {
        Button closeButton = (Button)this.findViewById(R.id.dialogButtonOK);
        closeButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setCheckBoxShowHintToTrue() {
        CheckBox showTipsCheckBox = (CheckBox)this.findViewById(R.id.checkBoxShowHints);
        showTipsCheckBox.setChecked(true);
    }
    
    private void setTextViewText(String tip) {
        TextView textView = (TextView)this.findViewById(R.id.text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(tip);
    }
}