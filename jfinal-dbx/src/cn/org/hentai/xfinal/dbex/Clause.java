package cn.org.hentai.xfinal.dbex;

import java.util.LinkedList;

public final class Clause
{
	private static final int LOGIC_AND = 1;
	private static final int LOGIC_OR = 2;
	
	private LinkedList<Statement> chain = null;
	private Clause()
	{
		this.chain = new LinkedList<Statement>();
	}
	
	public static Clause compose()
	{
		return new Clause();
	}
	
	public static Clause compose(String sql, Object val)
	{
		return new Clause().and(sql, val);
	}
	
	public static Clause compose(String sql)
	{
		return new Clause().and(sql);
	}
	
	public Clause and(String sql)
	{
		return this.and(sql, (Object)null);
	}
	
	public Clause and(String sql, Object val)
	{
		this.chain.add(new Statement(LOGIC_AND, sql, val));
		return this;
	}
	
	public Clause and(String sql, Object... vals)
	{
		String sequeue = "";
		for (int i = 0; i < vals.length; i++)
		{
			sequeue += "?";
			if (i + 1 < vals.length) sequeue += ",";
		}
		sql = sql.replace("*", sequeue);
		this.chain.add(new Statement(LOGIC_AND, sql, vals));
		return this;
	}
	
	public Clause or(String sql, Object val)
	{
		this.chain.add(new Statement(LOGIC_OR, sql, val));
		return this;
	}
	
	public String toWhereClause()
	{
		if (this.chain.size() == 0) return "";
		String where = " WHERE ";
		int logic = 0;
		for (int i = 0; i < this.chain.size(); i++)
		{
			Statement stmt = this.chain.get(i);
			logic = i == 0 ? 0 : stmt.type;
			if (logic == LOGIC_AND) where += " AND ";
			else if (logic == LOGIC_OR) where += " OR ";
			where += stmt.sql;
		}
		return where;
	}
	
	public Object[] toParameters()
	{
		int count = 0;
		for (int i = 0; i < this.chain.size(); i++)
		{
			Statement stmt = this.chain.get(i);
			if (stmt.value == null) continue;
			if (stmt.value.getClass().isArray()) count += ((Object[])stmt.value).length;
			else count += 1;
		}
		Object[] params = new Object[count];
		for (int i = 0, k = 0; i < this.chain.size(); i++)
		{
			Statement stmt = this.chain.get(i);
			if (stmt.value == null) continue;
			if (stmt.value.getClass().isArray())
			{
				Object[] list = (Object[])stmt.value;
				for (int s = 0; s < list.length; s++)
					params[k++] = list[s];
			}
			else params[k++] = stmt.value;
		}
		return params;
	}
	
	private static final class Statement
	{
		public int type;
		public String sql;
		public Object value;
		
		public Statement(int type, String sql, Object value)
		{
			this.type = type;
			this.sql = sql;
			this.value = value;
		}
	}
}