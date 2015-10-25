package cn.org.hentai.xfinal.core;

import com.google.gson.GsonBuilder;

import cn.org.hentai.xfinal.dbex.DbEx;
import cn.org.hentai.xfinal.dbex.Sheet;

public class ControllerEx extends com.jfinal.core.Controller
{
	protected final int getInt(String name, int defaultVal)
	{
		String val = this.getPara(name);
		if (val == null || !val.matches("^-?\\d+$")) return defaultVal;
		return Integer.parseInt(val);
	}
	
	protected final float getFloat(String name, float defaultVal)
	{
		String val = this.getPara(name);
		if (val == null || !val.matches("^-?\\d+(\\.\\d+)?$")) return defaultVal;
		return Float.parseFloat(val);
	}
	
	protected final long getLong(String name, long defaultVal)
	{
		String val = this.getPara(name);
		if (val == null || !val.matches("^-?\\d+$")) return defaultVal;
		return Long.parseLong(val);
	}
	
	protected final String getString(String name)
	{
		String val = this.getPara(name);
		if ("".equals(val)) return null;
		return val;
	}
	
	protected final String getString(String name, String format, String defaultVal)
	{
		String value = this.getString(name);
		if (null == value) return defaultVal;
		if (!value.matches(format)) return defaultVal;
		return value;
	}
	
	public void renderJson(Sheet sheet)
	{
		this.renderText(new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(sheet), "application/json");
	}
	
	protected final void put(String name, Object value)
	{
		this.setAttr(name, value);
	}
	
	// Db类的扩展
	protected DbEx Db = new DbEx();
}