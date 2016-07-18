package com.ipcam.mailsender;

import android.util.Log;

import com.ipcam.helper.AsyncExecutor;
import com.ipcam.internalevent.IInternalEventInfo;
import com.ipcam.internalevent.InternalEvent;
import com.ipcam.mailsender.IResultReporter;

public class ResultReporterForInternalEvent<TDataToSend> implements IResultReporter<TDataToSend>
{
	private final String TAG = "AsyncMessageSender";
	private AsyncExecutor<IInternalEventInfo> resultHandler = null;

	public ResultReporterForInternalEvent(AsyncExecutor<IInternalEventInfo> r)
	{
		resultHandler = r;
	}
	@Override
	public void reportResult(TDataToSend data, ISender.SEND_RESULT sendingResult)
	{
		if (resultHandler != null)
		{
			IInternalEventInfo result = (IInternalEventInfo) data;
	
	    	if (sendingResult == ISender.SEND_RESULT.SUCCESS)
	    		result.setParameter(1);
	    	else
	    		result.setParameter(0);
	
	    	result.setInternalEvent(InternalEvent.TASK_COMPLETE);
	    	resultHandler.executeAsync(result);
		}
		else
		{
			Log.e(TAG, "ResultReporterForInternalEvent: reportResult error resultHandler is null");
		}
	}

}
