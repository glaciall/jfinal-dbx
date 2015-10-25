package cn.org.hentai.xfinal.dbex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.plugin.activerecord.Record;
import cn.org.hentai.xfinal.core.ServletUtil;

public class Sheet implements List<Record>
{
	private List<Record> records = null;
	private int pageIndex;
	private int pageSize;
	private int pageCount;
	private int recordCount;
	
	public Sheet(List<Record> list, int pageIndex, int pageSize, int pageCount, int recordCount)
	{
		this.records = list;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.recordCount = recordCount;
	}
	
	public Sheet(List<Record> list)
	{
		this.records = list;
	}
	
	/*
	public List<Map<String, Object>> toList()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Record> dataList = this.getList();
		for (int i = 0; i < dataList.size(); i++)
		{
			com.jfinal.plugin.activerecord.Record r = (com.jfinal.plugin.activerecord.Record) dataList.get(i);
			list.add(r.getColumns());
		}
		return list;
	}
	*/
	
	// 行级字段格式化
	public Sheet format(Object...args)
	{
		DbUtil.format(this, args);
		return this;
	}
	
	// 根据字段联结子表
	public Sheet join(Object...args)
	{
		DbUtil.join(this, args);
		return this;
	}
	
	// 对每行的多个字段作整理
	public Sheet combine(Object...args)
	{
		DbUtil.combine(this, args);
		return this;
	}
	
	// 删除每行的特定字段
	public Sheet delete(String...fields)
	{
		DbUtil.remove(this, fields);
		return this;
	}

	// 分页
	public int getPageIndex()
	{
		return this.pageIndex;
	}
	
	public int getFirstPage()
	{
		return 1;
	}
	
	public int getPrevPage()
	{
		return this.pageIndex > 1 ? this.pageCount - 1 : 1;
	}
	
	public int getNextPage()
	{
		return this.pageIndex < this.pageCount ? this.pageIndex + 1 : this.pageCount;
	}
	
	public int getLastPage()
	{
		return this.pageCount;
	}
	
    public ArrayList<HashMap<String, String>> getPageSelector(HttpServletRequest request, String format) throws RuntimeException
    {
        return getPageSelector(request, format, "page");
    }

    public ArrayList<HashMap<String, String>> getPageSelector(HttpServletRequest request) throws RuntimeException
    {
        return getPageSelector(request, null, "page");
    }
	
    public ArrayList<HashMap<String, String>> getPageSelector(HttpServletRequest request, String format, String paramName) throws RuntimeException
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> hm = new HashMap<String, String>();

        hm.put("url", format == null ? ServletUtil.getQueryString(request, paramName, String.valueOf(this.getPrevPage())) : ServletUtil.formatUrl(request, format, paramName, this.getPrevPage()));
        hm.put("text", "Prev");
        hm.put("label", "上一页");
        hm.put("index", "prev");
        hm.put("current", "false");
        list.add(hm);
        hm = null;

        for (int i = this.getPageIndex() - 5; i < this.getPageIndex() + 6; i++)
        {
            if (i < 1) continue;
            if (i > this.pageCount) break;

            hm = new HashMap<String, String>();
            hm.put("url", format == null ? ServletUtil.getQueryString(request, paramName, String.valueOf(i)) : ServletUtil.formatUrl(request, format, paramName, i));
            hm.put("text", String.valueOf(i));
            hm.put("label", String.valueOf(i));
            hm.put("index", String.valueOf(i));
            hm.put("current", String.valueOf(i == this.pageIndex));

            list.add(hm);
        }

        hm = new HashMap<String, String>();
        hm.put("url", format == null ? ServletUtil.getQueryString(request, paramName, String.valueOf(this.getNextPage())) : ServletUtil.formatUrl(request, format, paramName, this.getNextPage()));
        hm.put("text", "Next");
        hm.put("label", "下一页");
        hm.put("index", "next");
        hm.put("current", "false");
        list.add(hm);
        hm = null;

        return list;
    }

    // 实现List接口，这样Sheet类型的数据就可以直接在模板里使用了
    
	@Override
	public boolean add(Record o)
	{
		return false;
	}

	@Override
	public void add(int index, Record o)
	{
		// ...
	}

	@Override
	public boolean addAll(Collection c)
	{
		return false;
	}

	@Override
	public boolean addAll(int index, Collection c)
	{
		return false;
	}

	@Override
	public void clear()
	{
		// ...
	}

	@Override
	public boolean contains(Object o)
	{
		return false;
	}

	@Override
	public boolean containsAll(Collection c)
	{
		return false;
	}

	@Override
	public Record get(int index)
	{
		return this.records.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public Iterator iterator()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0, l = this.records.size(); i < l; i++)
		{
			list.add(this.records.get(i).getColumns());
		}
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return 0;
	}

	@Override
	public ListIterator listIterator()
	{
		return null;
	}

	@Override
	public ListIterator listIterator(int index)
	{
		return null;
	}

	@Override
	public boolean remove(Object arg0)
	{
		return false;
	}

	@Override
	public Record remove(int index)
	{
		return null;
	}

	@Override
	public boolean removeAll(Collection c)
	{
		return false;
	}

	@Override
	public boolean retainAll(Collection c)
	{
		return false;
	}

	@Override
	public Record set(int index, Record o)
	{
		return null;
	}

	@Override
	public int size()
	{
		return this.records.size();
	}

	@Override
	public List subList(int fromIndex, int toIndex)
	{
		return null;
	}

	@Override
	public Object[] toArray()
	{
		return this.records.toArray();
	}

	@Override
	public Object[] toArray(Object[] arg0)
	{
		return this.records.toArray(arg0);
	}
	
	public String toString()
	{
		return this.records.toString();
	}
}
