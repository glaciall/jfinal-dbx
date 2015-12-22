package cn.org.hentai.xfinal.core;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil
{
    public static String getString(HttpServletRequest request, String name) throws RuntimeException
    {
        return request.getParameter(name);
    }
	
    public static HashMap<String, String> getParametersMap(HttpServletRequest request) throws RuntimeException
    {
        HashMap<String, String> map = new HashMap<String, String>();
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements())
        {
            String key = (String)enums.nextElement();
            String value = getString(request, key);

            map.put(key, value);
        }
        return map;
    }
	
    public static String formatUrl(HttpServletRequest request, String format, String name, int val) throws RuntimeException
    {
        return formatUrl(request, format, name, String.valueOf(val));
    }

    public static String formatUrl(HttpServletRequest request, String format, String name, String val) throws RuntimeException
    {
    	Pattern ptn = Pattern.compile("\\$\\w+");
		Matcher matcher = ptn.matcher(format);
		HashMap<String, String> map = getParametersMap(request);
		
		while (matcher.find())
		{
			String key=matcher.group(0);
			String value=map.get(key.substring(1));
			if(key.substring(1).equals(name))
			{
				
				value = val;
			}
			
			value = value == null ? "" : value;
		    format = format.replace(key, value);
		   
		}
        return format;
    }

    public static String getQueryString(HttpServletRequest request, String name, String val) throws RuntimeException
    {
        boolean exists = false;
        String url = "?", key, value;
        String[] vals;
        Enumeration enums = ((HttpServletRequest)request).getParameterNames();
        
        try
        {
	        while (enums.hasMoreElements())
	        {
	            key = (String)enums.nextElement();
	            if ("__uri__".equals(key)) continue;
	            vals = request.getParameterValues(key);
	            vals = vals == null ? new String[0] : vals;
	
	            for (int i = 0; i < vals.length; i++)
	            {
	                value = vals[i];
	                if (name.equals(key)) { value = val; exists = true; }
	                url += key + "=" + java.net.URLEncoder.encode(value, "UTF-8") + "&";
	            }
	        }
	        if (!exists) url += name + "=" + java.net.URLEncoder.encode(val, "UTF-8");
        }
        catch(UnsupportedEncodingException ex)
        {
        	throw new RuntimeException(ex);
        }
        
        return url;
    }
}
