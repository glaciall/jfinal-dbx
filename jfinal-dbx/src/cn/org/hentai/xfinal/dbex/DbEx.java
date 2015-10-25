package cn.org.hentai.xfinal.dbex;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public final class DbEx
{
	public DbEx()
	{
		// do nothing here...
	}
	
	private int toInt(Number val)
	{
		if (val instanceof Long)
		{
			System.err.println("Trying convert value from Long to Integer");
			return (int)val.longValue();
		}
		else return val.intValue();
	}
	
	private long toLong(Number val)
	{
		if (val instanceof Integer)
		{
			System.err.println("Trying convert value from Integer to Long");
			return val.intValue();
		}
		else return val.longValue();
	}
	
	public int queryInt(String sql, Object... params)
	{
		Number val = Db.queryInt(asterisk(sql, params), unpack(params));
		if (null == val) return 0;
		else return toInt(val);
	}
	
	public int queryInt(String sql)
	{
		Number val = Db.queryInt(sql);
		if (null == val) return 0;
		else return toInt(val);
	}
	
	public float queryFloat(String sql, Object... params)
	{
		Number val = Db.queryFloat(asterisk(sql, params), unpack(params));
		if (null == val) return 0f;
		else return val.floatValue();
	}
	
	public float queryFloat(String sql)
	{
		Number val = Db.queryFloat(sql);
		if (null == val) return 0f;
		else return val.floatValue();
	}
	
	public double queryDouble(String sql, Object... params)
	{
		Number val = Db.queryDouble(asterisk(sql, params), unpack(params));
		if (null == val) return 0d;
		else return val.doubleValue();
	}
	
	public double queryDouble(String sql)
	{
		Number val = Db.queryDouble(sql);
		if (null == val) return 0d;
		else return val.doubleValue();
	}
	
	public long queryLong(String sql, Object...params)
	{
		Number val = Db.queryLong(asterisk(sql, params), unpack(params));
		if (null == val) return 0L;
		else return toLong(val);
	}
	
	public long queryLong(String sql)
	{
		Number val = Db.queryLong(sql);
		if (null == val) return 0l;
		else return toLong(val);
	}
	
	public String queryStr(String sql, Object... params)
	{
		return Db.queryStr(asterisk(sql, params), unpack(params));
	}
	
	public String queryStr(String sql)
	{
		return Db.queryStr(sql);
	}
	
	public BigDecimal queryBigDecimal(String sql, Object... params)
	{
		return Db.queryBigDecimal(asterisk(sql, params), unpack(params));
	}
	
	public BigDecimal queryBigDecimal(String sql)
	{
		return Db.queryBigDecimal(sql);
	}
	
	public boolean queryBoolean(String sql, Object... params)
	{
		Boolean val = Db.queryBoolean(asterisk(sql, params), unpack(params));
		if (null == val) return false;
		else return val.booleanValue();
	}
	
	public boolean queryBoolean(String sql)
	{
		Boolean val = Db.queryBoolean(sql);
		if (null == val) return false;
		else return val.booleanValue();
	}
	
	public Sheet find(String sql, Object... params)
	{
		return new Sheet(Db.find(asterisk(sql, params), unpack(params)));
	}
	
	public Sheet find(String sql)
	{
		return new Sheet(Db.find(sql));
	}
	
	public Record findFirst(String sql, Object... params)
	{
		return Db.findFirst(asterisk(sql, params), unpack(params));
	}
	
	public Record findFirst(String sql)
	{
		return Db.findFirst(sql);
	}
	
	public int update(String sql, Object... params)
	{
		return Db.update(asterisk(sql, params), unpack(params));
	}
	
	public int update(String sql)
	{
		return Db.update(sql);
	}
	
	public boolean save(String tableName, Record record)
	{
		return Db.save(tableName, record);
	}
	
	public boolean tx(IAtom iAtom)
	{
		return Db.tx(iAtom);
	}
	
	public Sheet paginate(int pageIndex, int pageSize, String select, String sqlExceptSelect, Object... params)
	{
		com.jfinal.plugin.activerecord.Page<Record> p = com.jfinal.plugin.activerecord.Db.paginate(pageIndex, pageSize, select, sqlExceptSelect, params);
		return new Sheet(p.getList(), p.getPageNumber(), p.getPageSize(), p.getTotalPage(), p.getTotalRow());
	}
	
	public Sheet paginate(int pageIndex, int pageSize, String select, String sqlExceptSelect, Clause clause)
	{
		return paginate(pageIndex, pageSize, select, sqlExceptSelect.replace("{WHERE}", clause.toWhereClause()), clause.toParameters());
	}
	
	private static Object[] unpack(Object... params)
	{
		ArrayList<Object> values = new ArrayList<Object>();
		for (int i = 0; i < params.length; i++)
		{
			Object val = params[i];
			if (val == null || !val.getClass().isArray())
			{
				values.add(val);
				continue;
			}
			for (int k = 0; k < Array.getLength(val); k++)
				values.add(Array.get(val, k));
		}
		return values.toArray();
	}
	
	private static String asteriskEx(String sql, Object...params)
	{
		// 需要得到每个?和*的位置
		char lastChr = ' ';
		int idx = 0;
		StringBuilder str = new StringBuilder(sql.length());
		for (int i = 0; i < sql.length(); i++)
		{
			char chr = sql.charAt(i);
			if (chr == '?') idx += 1;
			if (chr == '*' && lastChr == '(' && sql.substring(0, i).matches("(?is)^[\\s\\S]+\\s+in\\s*\\($"))
			{
				Object array = params[idx];
				if (!array.getClass().isArray()) throw new RuntimeException("第" + idx + "个参数值为非Array类型");
				for (int k = 0, l = Array.getLength(array); k < l; k++)
				{
					str.append('?');
					if (k < l - 1) str.append(',');
				}
				idx += 1;
				continue;
			}
			str.append(chr);
			if (!Character.isWhitespace(chr)) lastChr = chr;
		}
		return str.toString();
	}
	
	private static String asterisk(String sql, Object...params)
	{
		if (sql.indexOf('*') == -1) return sql;
		
		// 确定参数是一个还是多个，主要是传入的单个数组参数会被展开
		for (int i = 0; i < params.length; i++)
		{
			if (params[i] == null) continue;
			if (params[i].getClass().isArray()) return asteriskEx(sql, params);
		}
		
		// 下面是只有一个数组参数的情况，也就是整个params只给一个*用
		// 需要得到每个?和*的位置
		char lastChr = ' ';
		int idx = 0;
		
		StringBuilder str = new StringBuilder(sql.length());
		for (int i = 0; i < sql.length(); i++)
		{
			char chr = sql.charAt(i);
			if (chr == '?') idx += 1;
			if (chr == '*' && lastChr == '(' && sql.substring(0, i).matches("(?is)^[\\s\\S]+\\s+in\\s*\\($"))
			{
				for (int k = 0, l = params.length; k < l; k++)
				{
					str.append('?');
					if (k < l - 1) str.append(',');
					idx ++;
				}
				continue;
			}
			str.append(chr);
			if (!Character.isWhitespace(chr)) lastChr = chr;
		}
		if (idx > params.length) throw new RuntimeException("单数组参数只能用于一个?参数占位");
		return str.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] arr = new String[] { "aaa", "bbb", "ccc" };
		Object users = new int[] { 333, 444, 555 };
		java.util.ArrayList<String> ss = new java.util.ArrayList<String>();
		ss.add("1111");
		ss.add("2222");
		// 如果只有一个参数，它就被展开了
		// 如果有多个参数，它是各自独立的，我勒个日
		// 那我确定一下是独立的还是展开的？
		// 如果参数集中没有数组类型，就是展开的
		// 如果是独立的，现在的这个程序就能很好的处理
		// 如果是展开的，得另外写程序处理
		int[] ids = new int[] { 3, 2 };
		String xx = asterisk("select count(*) as xx from by_goods where id in (*) and id = ?", ids, 33);
		System.out.println(xx);
	}
}
