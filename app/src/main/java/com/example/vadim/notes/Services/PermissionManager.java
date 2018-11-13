package com.example.vadim.notes.Services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager
{
    private static final int REQUEST_PERMISSION = 1;
    private Activity targetActivity;

    public PermissionManager(Activity targetActivity)
    {
        this.targetActivity = targetActivity;
    }

    public boolean isReadingAccesGranted()
    {
        if (ContextCompat.checkSelfPermission

                (targetActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(targetActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)

        {return true;}

        else
            {
              ActivityCompat.requestPermissions

                      (targetActivity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                                     Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                     REQUEST_PERMISSION);}

     return false;
    }
}
