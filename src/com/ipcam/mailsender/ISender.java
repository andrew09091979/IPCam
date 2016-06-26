package com.ipcam.mailsender;

import com.ipcam.internalevent.IInternalEventInfo;

public interface ISender
{
	public enum SEND_LETTER_RESULT
	{
		SUCCESS,
		SERVER_ERROR,
		NETWORK_ERROR,
		UNKNOWN
	}	
	public SEND_LETTER_RESULT sendLetter(IInternalEventInfo letterToSend_);
}
