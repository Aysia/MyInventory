package com.linux_girl.myinventory;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lani on 8/17/2016.
 * Simple Utils for quick use
 */
public class Utils {

    /**
     * Method to validate edit text is not empty
     */
    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
    public static void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
