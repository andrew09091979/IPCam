package com.ipcam.asyncio;

import com.ipcam.asyncio.ISender.SEND_RESULT;

public interface IDataToSendTreatment<TDataToSend>
{
	void reportResult(TDataToSend data, SEND_RESULT result);
	void addString(TDataToSend data, String str);
}
