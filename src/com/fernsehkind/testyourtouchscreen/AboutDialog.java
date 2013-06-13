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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutDialog extends Dialog {

    public AboutDialog(final Context context) {
        super(context);
        this.setContentView(R.layout.dialog_about);
        
        this.setTitle(R.string.dialog_about_heading);
        this.SetTextViewText();
        
        Button closeButton = (Button)this.findViewById(R.id.dialogButtonOK);
        closeButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
    private void SetTextViewText() {
        Resources res = this.getContext().getResources();
        String text = String.format(res.getString(R.string.dialog_about_content),
                GetVersionString(),
                res.getString(R.string.dialog_about_authors),
                res.getString(R.string.dialog_about_thanks),
                GetLicenseString());

        TextView textView = (TextView)this.findViewById(R.id.text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(text);
                
                
    }
    
    private String GetVersionString() {
        try
        {
            return this.getContext().getPackageManager().getPackageInfo("com.fernsehkind.testyourtouchscreen", 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Resources res = this.getContext().getResources();
            return res.getString(R.string.dialog_about_version_unknown);
        }
    }
    
    private String GetLicenseString() {
        InputStream inputStream = this.getContext().getResources().openRawResource(R.raw.license);
         
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         
         int i;
         try {
             i = inputStream.read();
             while (i != -1) {
                 byteArrayOutputStream.write(i);
                 i = inputStream.read();
             }
             inputStream.close();
         } 
         catch (IOException e) {
             return this.getContext().getResources().getString(R.string.dialog_about_license_read_failed);
         }
      
         return byteArrayOutputStream.toString();        
    }
        
}
