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

public class SimpleDialog extends Dialog {
    private final CheckBox checkBox;
    private final Button closeButton;
    private final TextView textView;
    
    public SimpleDialog(final Context context, final String heading, final String textViewText, final String checkBoxText) {
        super(context);
        this.setContentView(R.layout.dialog_simple);
        checkBox = (CheckBox)this.findViewById(R.id.checkBox);
        closeButton = (Button)this.findViewById(R.id.dialogButtonOK);
        textView = (TextView)this.findViewById(R.id.text);
        this.setTitle(heading);
        setupCloseButton();
        setCheckBoxText(checkBoxText);
        setTextViewText(textViewText);
    }

    public void setChecked(boolean checkState) {
        checkBox.setChecked(checkState);
    }
    
    public boolean isCheckBoxChecked() {
        return checkBox.isChecked();
    }
    
    private void setupCloseButton() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setCheckBoxText(final String checkBoxText) {
        if (checkBoxText == null) {
            checkBox.setVisibility(View.GONE);
        }
        else {
            checkBox.setText(checkBoxText);
        }
    }
    
    private void setTextViewText(String textViewText) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(textViewText);
    }
}
