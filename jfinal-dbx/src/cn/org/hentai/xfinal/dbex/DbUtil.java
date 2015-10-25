package cn.org.hentai.xfinal.dbex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import com.jfinal.plugin.activerecord.Record;

public final class DbUtil
{
	public static void format(List<Record> sheet, Object... params)
	{
		if (params.length % 2 != 0) throw new RuntimeException("参数个数有误");
		
		for (int x = 0; x < sheet.size(); x++)
		{
			try
			{
				Record t = sheet.get(x);
				for (int i = 0; i < params.length; i += 2)
				{
					String field = (String)params[i];
					Formattable fmt = (Formattable)params[i + 1];
					t.set("fmt_" + field, fmt.format(t.get(field)));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static BigDecimal sum(List<Record> list, String field) throws RuntimeException
	{
		BigDecimal sum = new BigDecimal("0.00");
		for (int i = 0; i < list.size(); i++)
		{
			sum = sum.add(new BigDecimal(list.get(i).get(field).toString()));
		}
		return sum.setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	public static BigDecimal avg(List<Record> list, String field) throws RuntimeException
	{
		BigDecimal sum = new BigDecimal("0.00");
		if (list.size() == 0) return sum;
		for (int i = 0; i < list.size(); i++)
		{
			sum = sum.add(new BigDecimal(list.get(i).get(field).toString()));
		}
		return sum.divide(new BigDecimal(list.size()), BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	public static BigDecimal max(List<Record> list, String field)
	{
		BigDecimal max = new BigDecimal("0.00");
		for (int i = 0; i < list.size(); i++)
		{
			max = max.max(new BigDecimal(list.get(i).get(field).toString()));
		}
		return max;
	}
	
	public static BigDecimal min(List<Record> list, String field)
	{
		BigDecimal min = new BigDecimal("0.00");
		for (int i = 0; i < list.size(); i++)
		{
			min = min.min(new BigDecimal(list.get(i).get(field).toString()));
		}
		return min;
	}
	
	public static void join(List<Record> list, Object... params)
	{
		if (params.length % 3 != 0) throw new RuntimeException("参数个数有误");
		for (int x = 0; x < list.size(); x++)
		{
			Record t = list.get(x);
			try
			{
				for (int i = 0; i < params.length; i += 3)
				{
					String refField = (String)params[i];
					String newName = (String)params[i + 1];
					Formattable fmt = (Formattable)params[i + 2];
					t.set(newName, fmt.format(t.get(refField)));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	public static void remove(List<Record> list, String... fields)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Record rc = list.get(i);
			for (int k = 0; k < fields.length; k++) rc.remove(fields[k]);
		}
	}
	
	public static void combine(List<Record> list, Object... fields)
	{
		if (fields.length < 3) throw new RuntimeException("combine方法至少需要4个参数");
		if (!(fields[fields.length - 1] instanceof Formattable)) throw new RuntimeException("最后一个参数必须是Formattable实例");
		Formattable fmt = (Formattable)fields[fields.length - 1];
		for (int i = 0; i < list.size(); i++)
		{
			Record rc = list.get(i);
			HashMap<String, Object> pairs = new HashMap<String, Object>();
			for (int k = 0, s = 0, l = fields.length - 2; k < l; k++)
			{
				pairs.put((String)fields[k], rc.get((String)fields[k]));
			}
			Object val = fmt.combine(pairs);
			
			rc.set((String)fields[fields.length - 2], val);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		long time = System.currentTimeMillis() - 1053645000;
		ArrayList<Record> list = new ArrayList<Record>();
		list.add(new Record().set("name", "aaa").set("create_time", time));
		list.add(new Record().set("name", "bbb").set("create_time", time));
		list.add(new Record().set("name", "ccc").set("create_time", time));
		/*
		Sheet<Record> test = new Sheet<Record>(list, 1, 1, 1, 1);
		DbUtil.format(test, "create_time", FmtTimeElapse.class);
		DbUtil.join(test, "name", "xxfuck", new Formattable()
		{
			public String format(Object data)
			{
				return data.toString().toUpperCase();
			}
		});
		*/
		// DbUtil.remove(test, "create_time", "name");
		
		Sheet test = new Sheet(list);
		
		DbUtil.combine(test, "name", "create_time", "xxoo", new Formattable()
		{
			public Object combine(HashMap<String, Object> fields)
			{
				return fields.toString();
			}
		});
		
		System.out.println(test);
		// System.out.println(test.getList());
	}
}
