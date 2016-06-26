package com.ipcam.internalevent;

import java.io.File;

import com.ipcam.task.ITask;

import android.os.PowerManager.WakeLock;

public interface IInternalEventInfo
{
	public InternalEvent getInternalEvent();
	public int getID();
	public void setInternalEvent(InternalEvent ie);
    public void setHeadline(String headline);
    public void setMessage(String message);
    public void setWakeLock(WakeLock wakelock);
    public void setObject(Object obj);
    public void setResultNotifier(ITask t);
    public void addFile(String fullPath);
    public void removeAllFiles();
    public void setParameter(int parameter);
    public String getHeadline();
    public String getMessage();
    public ITask getResultNotifier();
    public void concatToMessage(String str);
    public WakeLock getWakeLock();
    public String getFile(int fileNum);
    public int getFilesNum();
    public int getParameter();
    public Object getObject();
}
