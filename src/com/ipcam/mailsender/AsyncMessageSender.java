package com.ipcam.mailsender;

import android.util.Log;

import com.ipcam.helper.AsyncExecutor;

public class AsyncMessageSender<TDataToSend, TDataToReportResult> extends AsyncExecutor<TDataToSend>
{
	private final String TAG = "AsyncMessageSender"; 
    private ISender<TDataToSend> sender = null;
    private int networkFailCounter = 0;
    private int serverFailCounter = 0;
    private IResultReporter<TDataToSend> resultReporter = null;

    public AsyncMessageSender(IResultReporter<TDataToSend> rh, ISender<TDataToSend> ls)
    {
    	resultReporter = rh;
    	sender = ls;
    }
    @Override
    public void executor(TDataToSend info)
    {
    	Log.d(TAG, "executor: calling sendLetter");
    	sendMessage(info);
    }
	private boolean sendMessage(TDataToSend infoForLetter)
	{
		ISender.SEND_RESULT res = ISender.SEND_RESULT.UNKNOWN;

        int maxAttemptsToSend = 5;//infoForLetter.getParameter();
        //infoForLetter.concatToMessage("\nNetwork failures: " + networkFailCounter + "; Server failures: " + serverFailCounter);
        int attemptsToSend = 0;
        res = ISender.SEND_RESULT.UNKNOWN;

        do
        {
        	if (attemptsToSend > 0)
        	{
        		try
        		{
					Thread.sleep(2000 * attemptsToSend);
				}
        		catch (InterruptedException e)
        		{
        			Log.e(TAG, "AsyncMessageSender: InterruptedException in Thread.sleep: " + e.getMessage());
					e.printStackTrace();
				}
        	}
        	Log.d(TAG, "Trying to send letter, attempt #" + attemptsToSend);
            res = sender.send(infoForLetter);
            ++attemptsToSend;
        }
        while ((res != ISender.SEND_RESULT.SUCCESS) &&
        		(res != ISender.SEND_RESULT.SERVER_ERROR) &&
        		(attemptsToSend < maxAttemptsToSend));

        if (res == ISender.SEND_RESULT.SUCCESS)
        {
        	Log.d(TAG, "AsyncMessageSender: mail successfully sent");
        }
        else if (res  == ISender.SEND_RESULT.SERVER_ERROR)
        {
        	++serverFailCounter;
        	Log.e(TAG, "AsyncMessageSender: server error serverFailCounter = " + Integer.toString(serverFailCounter));
        }
        else if (res == ISender.SEND_RESULT.NETWORK_ERROR)
        {
        	++networkFailCounter;
        	Log.e(TAG, "AsyncMessageSender: network error networkFailCounter = " + Integer.toString(networkFailCounter));
        }

        if ((resultReporter != null) && (infoForLetter != null)) 
        {
            resultReporter.reportResult(infoForLetter, res);//report sending result to IPCam
        }
        else
        {
        	Log.e(TAG, "AsyncMessageSender sendLetter: eventHandler or item is null");
        }

        return true;
	}
}
