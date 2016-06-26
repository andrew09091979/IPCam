package com.ipcam.service;

import com.example.ipcam.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class BootCompleteReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{
    	Intent intent = new Intent(arg0, IPCamService.class);
    	Resources res = arg0.getResources();
    	String startedFromIntentExtraName = res.getString(R.string.started_from_intent_extra_name);
    	String periodToPhotoPropertyName = res.getString(R.string.photo_period_intent_extra_name);
    	String photoQualityPropertyName = res.getString(R.string.photo_quality_index_intent_extra_name);
    	String mailLoginPropertyName = res.getString(R.string.mail_login_intent_extra_name);
    	String mailPasswordPropertyName = res.getString(R.string.mail_password_intent_extra_name);
    	String mailPortalPropertyName = res.getString(R.string.mail_portal_intent_extra_name);
    	String smtpServerAddrPropertyName = res.getString(R.string.smtp_server_addr_intent_extra_name);
    	String smtpServerPortPropertyName = res.getString(R.string.smtp_server_port_intent_extra_name);
    	String mailBoxDestPropertyName = res.getString(R.string.mail_box_to_send_to_intent_extra_name);
    	startedFromIntentExtraName = res.getString(R.string.started_from_intent_extra_name);
    	SharedPreferences Prefs = arg0.getSharedPreferences(periodToPhotoPropertyName, 1);
    	String periodToPhotoStr = Prefs.getString(periodToPhotoPropertyName, "10");
    	String photoQualityStr = Prefs.getString(photoQualityPropertyName, "3");
    	String mailLogin = Prefs.getString(mailLoginPropertyName, "");
    	String mailPassword = Prefs.getString(mailPasswordPropertyName, "");
    	String mailPortal = Prefs.getString(mailPortalPropertyName, "");
    	String smtpServerAddr = Prefs.getString(smtpServerAddrPropertyName, "");
    	String smtpServerPort = Prefs.getString(smtpServerPortPropertyName, "");
    	String mailBoxToSendTo = Prefs.getString(mailBoxDestPropertyName, "");
    	intent.putExtra(periodToPhotoPropertyName, periodToPhotoStr);
    	intent.putExtra(photoQualityPropertyName, photoQualityStr);
    	intent.putExtra(startedFromIntentExtraName, "BootCompleteReceiver");
    	intent.putExtra(mailLoginPropertyName, mailLogin);
    	intent.putExtra(mailPasswordPropertyName, mailPassword);
    	intent.putExtra(mailPortalPropertyName, mailPortal);
    	intent.putExtra(smtpServerAddrPropertyName, smtpServerAddr);
    	intent.putExtra(smtpServerPortPropertyName, smtpServerPort);
    	intent.putExtra(mailBoxDestPropertyName, mailBoxToSendTo);
    	arg0.startService(intent);		
	}
}