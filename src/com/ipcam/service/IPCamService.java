package com.ipcam.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class IPCamService extends Service
{
    private final String TAG = "IPCamService";
    private IPCam ipcam = null;
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@Override
	public void onCreate()
	{
    	Log.d(TAG, "onCreate");
        ipcam = new IPCam();
	}

    @Override
    public int onStartCommand(Intent intent, int startId, int arg) 
    {
        Toast.makeText(this, "IPCamService starting", Toast.LENGTH_SHORT).show();
        ipcam.activate(intent, getResources(), getApplicationContext());

    	return START_STICKY;
    }

	@Override
    public void onDestroy() 
    {
        Toast.makeText(this, "IPCamService stopping", Toast.LENGTH_SHORT).show();
        ipcam.deactivate();

        super.onDestroy();
    }

}
