package cn.org.hentai.xfinal.dbex;

import java.util.HashMap;

public interface Formattable
{
	default public Object format(Object data)
	{
		return null;
	}
	
	default public Object combine(HashMap<String, Object> fields)
	{
		return null;
	}
}
