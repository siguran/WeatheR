package com.example.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class InputDialog
{
    private String mText;
    private Context mParent;
    private String mTitle;
    private DialogDelegate mDelegate = null;


    InputDialog(Context parent, String title)
    {
        mParent = parent;
        mTitle = title;
    }


    String getText()
    {
        return mText;
    }


    void setDelegate(DialogDelegate delegate)
    {
        mDelegate = delegate;
    }


    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
        builder.setTitle(mTitle);
        final EditText input = new EditText(mParent);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mText = input.getText().toString();
                dialog.dismiss();
                if(mDelegate != null)
                    mDelegate.onConfirm();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }







}
