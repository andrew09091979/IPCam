package com.ipcam.soundrecorder;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaRecorder;
import android.util.Log;

import com.ipcam.helper.AsyncExecutor;
import com.ipcam.helper.FileName;
import com.ipcam.internalevent.IInternalEventInfo;
import com.ipcam.task.ITask;

public class SoundRecorderImpl extends AsyncExecutor<IInternalEventInfo>
{
	private final String TAG = "SoundRecorder";
    private MediaRecorder mRecorder = null;
	private int recordingTime = 3;
	private Timer recordingTimer = null;
    private boolean bRecordingInProcess = false;

    synchronized
    private boolean lock()
    {
    	if (!bRecordingInProcess)
    	{
    		bRecordingInProcess = true;
    	    return true;
    	}
    	else
    	{
    	    return false;
    	}
    }
    synchronized
    private void unlock()
    {
		bRecordingInProcess = false;
    }

	@Override
	public void executor(IInternalEventInfo info)
	{
		if (!lock())
		{
			Log.d(TAG, "Already recording sound");
			return;
		}
	    recordingTime = info.getParameter();
        RecordingTimeFinisedHandler pth = new RecordingTimeFinisedHandler();
        recordingTimer = new Timer();
        recordingTimer.schedule(pth, recordingTime);
        startRecording();
	}

    private void startRecording()
    {
        mRecorder = new MediaRecorder();

        if (mRecorder != null)
        {
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

	        File adminStorageDir = new File(FileName.AdminConsoleCommandsFilePath());

	        if (! adminStorageDir.exists())
	        {
	            if (! adminStorageDir.mkdirs())
	            {
	                Log.d(TAG, "SoundRecorderImpl: failed to create directory");
	                return;
	            }
	        }
	        String mFileName = FileName.AdminConsoleCommandsFilePath() + "AUD_" + FileName.DateTimeFileName() + ".amr";
			mRecorder.setOutputFile(mFileName );
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	
	        try
	        {
	            mRecorder.prepare();
	        }
	        catch (IOException e)
	        {
	            Log.e(TAG, "prepare() failed");
	        }
	
	        mRecorder.start();
        }
        else
        {
        	Log.e(TAG, "SoundRecorderImpl: startRecording: mRecorder is null, can't start recording");
        }
    }

    private void stopRecording()
    {
        if (mRecorder != null)
        {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
        }
        else
        {
        	Log.e(TAG, "SoundRecorderImpl: stopRecording: mRecorder is null");
        }
        unlock();
    }
	class RecordingTimeFinisedHandler extends TimerTask
	{
		@Override
		public void run() 
		{
	        Log.d(TAG, "RecordingTimeFinisedHandler: started, calling stopRecording");
	        stopRecording();
		}
	}
}
