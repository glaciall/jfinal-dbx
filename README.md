
##WHAT：这是个什么鬼？
>针对于jfinal框架，在数据库查询的相关api上做了一些修改，增强易用性。

1. 参数获取
```java
int id = getInt("id", 0);	// 如果有id参数并且值为整数类型，则转换为int类型，否则返回默认值0
float price = getFloat("price", 0);
long time = getLong("timestamp", 0L);
String name = getString("name");
String sex = getString("sex", "^(male|female)$", "male");
```
2. 分页查询的从句
```java
Clause clause = Clause.compose();
clause.and("id in (*)", 1, 2, 3, 4);	// 用*号代替需要指定确切数量的?
clause.and("is_delete = ?", false);
// 不再需要拼接又臭又长的where语句
Sheet users = Db.paginate(pageIndex, pageSize, "select *", "from tb_users {WHERE} order by id desc", clause);
```
3. 简化参数化SQL查询
```java
// 使用*号代替需要指定确切数量的?
Long count = Db.queryLong("select count(*) from table where id in (*)", 1, 2, 3, 4);
```
4. **数据格式化器，大大降低联合查询的复杂性，详见样例**
	1. 字段内容格式化
	2. 表联结
	3. 字段组合（联合计算或表联结）


##WHY：为什么要弄个这东西？
>首先当然是为了偷懒啦
>又臭又长的SQL语句，完全没有可读性
>繁复的表与表间的join，性能堪忧啊
>构建复杂结构的json输出，很麻烦啊
>轮子要重复的造，体力活不要重复的做


##HOW：这东西怎么使
1. 添加依赖库
	1. [jfinal：极简J2EE开发框架][2]
	2. [Google gson][3]
2. 构建得到jar或直接添加.java文件到项目中
>`ant build.xml`
3. 看看样例[Test.java][1]


##SAMPLE:样例
1. 测试表结构及数据
	1. tb_users 用户表
	```
    id		name		sex		reg_time			province_id
    1		zhangsan	1		1445218698			1
    2		lisi		1		1444749270			3
    3		lilan		0		1444809300			4
    4		huamulan	0		1443941012			7
    ```
	2. tb_friends 好友信息表
	```
    id		user_id		friend_id
    1		1				2
    2		1				3
    3		2				3
    4		2				4
    5		3				1
    6		4				1
    ```
	3. tb_provinces 省份信息表
    ```
    id		name
    1		beijing
    2		tianjing
    3		shanghai
    4		chongqing
    5		shanxi
    6		shandong
    7		hebei
    8		henan
    9		hunan
    10		hubei
    ```
2. 测试代码：[Test.java][1]
3. 测试代码的输出JSON
```json
[
	{
		"test": "2:lisi",
		"province_id": 3,
		"sex": true,
		"name": "lisi",
		"id": 2,
		"friends": [
			{
				"friend_name": "lilan",
				"friend_id": 3,
				"user_id": 2,
				"id": 3
			},
			{
				"friend_name": "huamulan",
				"friend_id": 4,
				"user_id": 2,
				"id": 4
			}
		],
		"fmt_reg_time": "2015-10-13 23:14"
	}
]
```

##TIPS:最佳实践
>整一大堆常用的Formattable，你值得拥有


[1]:https://github.com/glaciall/jfinal-dbx
[2]:https://www.jfinal.com/
[3]:https://github.com/google/gson
