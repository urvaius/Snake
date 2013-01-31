package com.arne5.androidgames.framework;
import com.arne5.androidgames.framework.Graphics.PixmapFormat;
public interface Pixmap {
	
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
	

}
