package com.ipcam.mailsender;

import java.util.Vector;

public class LetterImpl implements ILetter 
{
	String headline = null;
	String message = null;
	Vector<String> file = null;

    @Override	
    public void setHeadline(String h)
    {
    	headline = h;
    }
    @Override
    public void addFile(String fn)
    {
    	if (file == null)
    		file = new Vector<String>();

    	file.add(fn);
    }
    @Override
    public void setMessage(String m)
    {
    	message = m;
    }
}
