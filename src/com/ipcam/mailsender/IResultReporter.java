package com.ipcam.mailsender;

import com.ipcam.mailsender.ISender.SEND_RESULT;

public interface IResultReporter<TDataToSend>
{
	void reportResult(TDataToSend data, SEND_RESULT result);
}
