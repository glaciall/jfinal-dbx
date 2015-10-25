package cn.org.hentai.xfinal.conf;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

public class TestConfig extends JFinalConfig
{
	@Override
	public void configConstant(Constants me)
	{
		this.loadPropertyFile("xfinal.conf", "UTF-8");
		me.setViewType(ViewType.FREE_MARKER);
	}

	@Override
	public void configHandler(Handlers me)
	{
		
	}

	@Override
	public void configInterceptor(Interceptors me)
	{
		// ...
	}

	@Override
	public void configPlugin(Plugins me)
	{
		
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("mysql_url"), getProperty("mysql_username"), getProperty("mysql_password"));
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
	}

	@Override
	public void configRoute(Routes me)
	{
		// ...
		me.add("/test", cn.org.hentai.xfinal.testcase.Sample.class);
	}
}
