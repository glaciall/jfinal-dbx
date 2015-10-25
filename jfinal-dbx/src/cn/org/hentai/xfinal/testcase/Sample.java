package cn.org.hentai.xfinal.testcase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import cn.org.hentai.xfinal.core.ControllerEx;
import cn.org.hentai.xfinal.dbex.Clause;
import cn.org.hentai.xfinal.dbex.Formattable;
import cn.org.hentai.xfinal.dbex.Sheet;

public class Sample extends ControllerEx
{
	public void index()
	{
		int page = getInt("page", 1);
		int pageSize = 1;
		
		// where从句
		Clause clause = Clause.compose();
		
		// *号支持，不然写个in得拼接到吐血
		clause.and("id in (*)", 1, 2, 3, 4);
		clause.and("sex = ?", 1);
		
		// 把where部分，用{WHERE}代替
		Sheet users = Db.paginate(page, pageSize, "select *", "from tb_users {WHERE} order by id desc", clause);
		
		// join，通过每行的某个字段联结其它的表
		// 如下根据id字段内容，查询每个用户的好友，新的字段名叫friends
		users.join("id", "friends", new Formattable()
		{
			public Object format(Object data)
			{
				return Db.find("select * from tb_friends where user_id = ?", data).join("friend_id", "friend_name", new Formattable()
				{
					public Object format(Object data)
					{
						return Db.queryStr("select name from tb_users where id = ?", data);
					}
				});
			}
		});
		
		// 字段格式化，参数为一个字符串一个Formattable，必须为偶数个参数
		// 对Sheet里的每一行的一个或多个字段进行格式化，格式化后的新字段名为fmt_xxxxx（在原字段名前加上fmt_前缀）
		
		users.format("reg_time", FmtDate.class);
		// 或是下面这样
		users.format("reg_time", new Formattable()
		{
			public Object format(Object data)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return sdf.format(new java.util.Date(Long.parseLong(data.toString()) * 1000));
			}
		});
		
		// combine，多字段整合
		// 假如共有N个参数，第N个是Formattable，第N-1个是整合后的字段名，N-1之前的参数为整合时需要用到的字段名
		// 以下例子将users里的每一行数据的id字段和name字段，通过Formattable整合成一个新的字段，名叫test
		users.combine("id", "name", "test", new Formattable()
		{
			public Object combine(HashMap<String, Object> fields)
			{
				// 当然你也可以用这几个字段去查询另外一张表的数据
				return fields.get("id") + ":" + fields.get("name");
			}
		});
		
		// 删除掉某些用不上的字段
		users.delete("reg_time");
		
		// 生成一个1 2 3 4 5 6 7 8 9的分页用的你懂的
		List<HashMap<String, String>> pages = users.getPageSelector(this.getRequest(), null, "page");
		
		this.renderJson(users);
	}
}
