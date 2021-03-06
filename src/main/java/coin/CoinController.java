package coin;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileReader;
import java.util.HashMap;

@SpringBootApplication
@RequestMapping("/api/v1/data")
@EnableCaching
public class CoinController {

    private static final String[] COIN = {"BTC", "LTC", "EOS", "BCH", "XRP"};
    private static final String[] TRADER = {"bithumb", "coinone", "korbit", "bitfinex"};
    private static final String[] CURRENCY_SUFFIX = {"KRW", "JPY", "USD"};

    public static void main(String[] args) {
        SpringApplication.run(CoinController.class, args);
    }

    @RequestMapping(value = "/currency/")
    public @ResponseBody
    ResponseEntity<HashMap> currency() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("data", getData(""));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/currency/{curr}")
    public @ResponseBody
    ResponseEntity<HashMap> currency(@PathVariable String curr) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("data", getData(curr));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Cacheable
    public JSONObject getData(String currency) {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();

        if (currency == null) currency = "";

        try {
            Object obj = parser.parse(new FileReader("/price.json"));
            JSONObject jsonObject = (JSONObject) obj;

            if (!currency.equals("")) {
                String coin = currency;
                String coinWithCurrency = getCoinWithCurrency(coin);
                JSONObject valueInfoObject = (JSONObject) getCoinInfo((JSONObject) jsonObject.get(coinWithCurrency));
                data = valueInfoObject;
            } else {
                for (String coin : COIN) {
                    String coinWithCurrency = getCoinWithCurrency(coin);
                    JSONObject valueInfoObject = (JSONObject) getCoinInfo((JSONObject) jsonObject.get(coinWithCurrency));
                    data.put(coinWithCurrency, valueInfoObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public JSONObject getCoinInfo(JSONObject orgObject) {
        JSONObject valueInfoObject = new JSONObject();
        for (String trader : TRADER) {
            JSONObject traderInfoObject = new JSONObject();
            JSONObject orgTraderInfoObject = (JSONObject) orgObject.get(trader);
            if (orgTraderInfoObject == null) {
                valueInfoObject.put(trader, null);
            } else {
                traderInfoObject.put("originPair", (String) orgTraderInfoObject.get("originPair"));
                traderInfoObject.put("last", Double.parseDouble((String) orgTraderInfoObject.get("last")));
                valueInfoObject.put(trader, traderInfoObject);
            }
        }
        return valueInfoObject;
    }

    public static String getCoinWithCurrency(String coin) {
        return coin + "_" + CURRENCY_SUFFIX[0];
    }
}
