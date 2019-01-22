package coin;
import java.util.*;
import java.io.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

@SpringBootApplication
@RequestMapping("/api/v1/data")
@EnableCaching
public class CoinController {

    private static String[] COINS = { "BTC_KRW", "LTC_KRW", "EOS_KRW", "BCH_KRW", "XRP_KRW" };
    private static String[] TRADERS = { "bithumb", "coinone", "korbit", "bitfinex" };

    @RequestMapping(value = "/currency/{currency}", method = RequestMethod.GET)
    public HashMap<String,Object> currency(String currency){
	HashMap<String, Object> map = new HashMap<>();	
	map.put("status", "success");
	map.put("data", getCurrency(currency));
	return map;
    }

    @Cacheable
    public JSONObject getCurrency(String currency){
        JSONParser parser = new JSONParser();
	JSONObject data = new JSONObject();
 
        try {
 
            Object obj = parser.parse(new FileReader(
                    "/Users/<username>/price.json"));
 
            JSONObject jsonObject = (JSONObject) obj;
 
	    if (currency.equals("")){
		String coinName = currency + "_KRW";
		JSONObject coinInfoObject = (JSONObject) getCoinInfo( (JSONObject) jsonObject.get(coinName));
		data.put("data", coinInfoObject);
	    } else {
		for (int i = 0; i < COINS.length; i++){
			// TODO
			System.out.println(COINS[i]);
		}
	    }
/*
            String name = (String) jsonObject.get("Name");
            String author = (String) jsonObject.get("Author");
            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
 
            System.out.println("Name: " + name);
            System.out.println("Author: " + author);
            System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
*/
	} catch(Exception e){
	    e.printStackTrace();
	}
	return data;
    }
    public JSONObject getCoinInfo(JSONObject orgObject){
	JSONObject coinInfoObject = new JSONObject();
	for (int i = 0; i < TRADERS.length; i++){
	    JSONObject traderInfoObject = new JSONObject();
	    JSONObject orgTraderInfoObject = (JSONObject)orgObject.get(TRADERS[i]);
	    traderInfoObject.put( "originPair", (String)orgTraderInfoObject.get("originPair") );
	    traderInfoObject.put( "last_price", (String)orgTraderInfoObject.get("last_price") );
	    coinInfoObject.put( TRADERS[i], traderInfoObject );
	}
	return coinInfoObject;
    }
}
