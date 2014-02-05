package com.alturos.adcup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

/**
 * @author berwil
 *
 */
public class JedisAutoComplete {
	
	private static final Log LOG = LogFactory.getLog(JedisAutoComplete.class);

	private Jedis jedis;
	
	private String hostName = "localhost";
	
	private static String FILE_NAME = "female-names.txt";

	public static void main(String[] args) {
		
		JedisAutoComplete ac = new JedisAutoComplete();
		
		if (args == null || args.length == 0) {
			LOG.warn("Usage:  pass auto complete string");
			return;
		}
			
		
		
		try {
			// load auto complete entries
			ac.loadEntriesIntoRedis();
			
			String prefix = args[0];
			// do some auto complete tests
			doTestComplete(ac, prefix );
//			doTestComplete(ac, "marcell");
//			doTestComplete(ac, "andr");
//			doTestComplete(ac, "lor");
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void doTestComplete(JedisAutoComplete ac, String prefix) {
		LOG.info("** Call complete for string :" + prefix);
		List<String> results = ac.complete(prefix, 50);
		LOG.info("Found " + results.size() + " results.");
		int i=0;
		for (String result : results) {
			LOG.info("result " + (i++) + ":" + result);
		}		
	}


	private List<String> complete(String prefix, int count) {
		LOG.debug(String.format("complete %s %s",  prefix, count));
		
		List<String> results = new ArrayList<>();
		int rangelen = 50;
		
		Long start = JEDIS().zrank(":compl", prefix);
		
		if (start == null)
			return results;
				
		while (results.size() != count) {
			Set<String> range = JEDIS().zrange(":compl", start, start + rangelen -1);
			
			if (range == null || range.size() == 0)
				break;
			
			for (String entry : range) {
				
				int minlen = Math.min(entry.length(), prefix.length());
				if (!entry.substring(0, minlen).equals(prefix.substring(0, minlen))) {
					count = results.size();
					break;
				}
					
				if (entry.endsWith("*") && results.size() != count) {
					results.add(entry.substring(0,entry.length()-1));
				}
			}
		}
		
		return results;
	}
	
	
	private void loadEntriesIntoRedis() throws IOException {
		
		LOG.info("Check if autocomplete entries must be loaded");
		if (!JEDIS().exists(":compl")) {
			LOG.info("****************************************");
			LOG.info("   Start loading entries into Redis");
			LOG.info("****************************************");
			
			
			BufferedReader br = new BufferedReader(new FileReader(new File(FILE_NAME)));
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				line.trim();
				
				for (int i=1; i<line.length(); i++) {
					String prefix = line.substring(0, i);
					LOG.debug("Add: " + prefix);
					
					JEDIS().zadd(":compl", 0, prefix);
					count++;
				}
				String stopper = line + "*";
				LOG.debug("Add >" + stopper);
				JEDIS().zadd(":compl", 0, stopper);
				count ++;
			}
			br.close();
						
			Long zcard = JEDIS().zcard(":compl");
			
			LOG.info("****************************************");
			LOG.info("Finished loading auto completion entries");
			LOG.info("Added >" + count + "< strings");
			LOG.info("The Redis sorted list \":comp\" contains " + zcard + " entries");
			LOG.info("****************************************");
			
		}
		else {
			LOG.info("Auto comlete entries are already loaded");
			Long zcard = JEDIS().zcard(":compl");
			LOG.info("The Redis sorted list \":comp\" contains " + zcard + " entries");

		}
	}
	
	

	private Jedis JEDIS() {
		if (jedis == null) {
			jedis = new Jedis(hostName);
		}
		return jedis;
	}

}
