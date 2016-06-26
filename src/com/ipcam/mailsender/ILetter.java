package com.ipcam.mailsender;

public interface ILetter 
{
    public void setHeadline(String headline);
    public void addFile(String headline);
    public void setMessage(String message);
}
