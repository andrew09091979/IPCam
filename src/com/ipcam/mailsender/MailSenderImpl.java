package com.ipcam.mailsender;

import android.util.Log;

import com.ipcam.helper.AsyncExecutor;
import com.ipcam.internalevent.IInternalEventInfo;
import com.ipcam.internalevent.InternalEvent;
import com.ipcam.task.ITask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MailSenderImpl extends AsyncExecutor<IInternalEventInfo>
{
	private final String TAG = "MailSenderImpl"; 
    private boolean bExitFlag = false;
    private ISender letterSender = null;
    private int networkFailCounter = 0;
    private int serverFailCounter = 0;
	private AsyncExecutor<IInternalEventInfo> eventHandler = null;

    public MailSenderImpl(AsyncExecutor<IInternalEventInfo> evh, ISender ls)
    {
    	eventHandler = evh;
    	letterSender = ls;
    }
    @Override
    public void executor(IInternalEventInfo info)
    {
    	Log.d(TAG, "executor: calling sendLetter");
    	sendLetter(info);
    }
	private boolean sendLetter(IInternalEventInfo infoForLetter)
	{
        SMTPSender.SEND_LETTER_RESULT res = SMTPSender.SEND_LETTER_RESULT.UNKNOWN;

        int maxAttemptsToSend = infoForLetter.getParameter();
        infoForLetter.concatToMessage("\nNetwork failures: " + networkFailCounter + "; Server failures: " + serverFailCounter);
        int attemptsToSend = 0;
        res = SMTPSender.SEND_LETTER_RESULT.UNKNOWN;

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
        			Log.e(TAG, "MailSenderImpl: InterruptedException in Thread.sleep: " + e.getMessage());
					e.printStackTrace();
				}
        	}
        	Log.d(TAG, "Trying to send letter, attempt #" + attemptsToSend);
            res = letterSender.sendLetter(infoForLetter);
            ++attemptsToSend;
        }
        while ((res != SMTPSender.SEND_LETTER_RESULT.SUCCESS) &&
        		(res != SMTPSender.SEND_LETTER_RESULT.SERVER_ERROR) &&
        		(attemptsToSend < maxAttemptsToSend));

        if (res == SMTPSender.SEND_LETTER_RESULT.SUCCESS)
        {
        	Log.d(TAG, "MailSenderImpl: mail successfully sent");
        }
        else if (res  == SMTPSender.SEND_LETTER_RESULT.SERVER_ERROR)
        {
        	++serverFailCounter;
        	Log.e(TAG, "MailSenderImpl: server error serverFailCounter = " + Integer.toString(serverFailCounter));
        }
        else if (res == SMTPSender.SEND_LETTER_RESULT.NETWORK_ERROR)
        {
        	++networkFailCounter;
        	Log.e(TAG, "MailSenderImpl: network error networkFailCounter = " + Integer.toString(networkFailCounter));
        }

        if ((eventHandler != null) && (infoForLetter != null)) 
        {
        	if (res == SMTPSender.SEND_LETTER_RESULT.SUCCESS)
        		infoForLetter.setParameter(1);
        	else
        		infoForLetter.setParameter(0);

        	infoForLetter.setInternalEvent(InternalEvent.TASK_COMPLETE);
        	eventHandler.executeAsync(infoForLetter);
        }
        else
        {
        	Log.e(TAG, "MailSenderImpl sendLetter: eventHandler or item is null");
        }

        return true;
	}
}
