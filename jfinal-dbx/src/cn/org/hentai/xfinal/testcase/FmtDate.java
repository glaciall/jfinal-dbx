package cn.org.hentai.xfinal.testcase;

import java.text.SimpleDateFormat;

import cn.org.hentai.xfinal.dbex.Formattable;

public class FmtDate implements Formattable
{
	public String format(Object data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new java.util.Date(Long.parseLong(data.toString()) * 1000));
	}
}
