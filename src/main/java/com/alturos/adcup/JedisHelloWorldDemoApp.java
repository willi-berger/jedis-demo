package com.alturos.adcup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static java.lang.String.format;
import redis.clients.jedis.Jedis;

/**
 * Hello world!
 * updated in WS2
 *
 */
public class JedisHelloWorldDemoApp 
{
	static final Log LOG = LogFactory.getLog(JedisHelloWorldDemoApp.class);
	
	public void hello() {
		int x=0;
	}
	
    public static void main( String[] args )
    {
    	String hostName = "localhost";
    	LOG.debug(format("Instantiating Jedis at %s", hostName));
    	    	
		Jedis jedis = new Jedis(hostName);
    	
		String key = "jedis.key";
		String value = "Hello Jedis World ...";
		
		LOG.debug(format("JEDIS.set   key = %s value = %s", key, value));
		jedis.set(key, value);
        
		LOG.debug(format("JEDIS.get %s", key));
		String v = jedis.get(key);
		
		
		jedis.hset("users:willi", "name", "willie");
		jedis.hset("users:willi", "password", "password");
		
		
		
		LOG.debug(format("retrieved >%s<", v));

    }
}
