package coin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext; 
import org.springframework.boot.test.context.SpringBootTest;
import coin.CoinController;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CoinControllerTest{
    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetBTC() throws Exception {
        this.mvc.perform(get("/api/v1/data/currency/BTC"))
	    .andDo(print())
            .andExpect(jsonPath("$.data.bithumb.last", is(7471000.0)))
            .andExpect(jsonPath("$.data.bithumb.originPair", is("BTCKRW")))
            .andExpect(jsonPath("$.data.coinone.last", is(7503000.0)))
            .andExpect(jsonPath("$.data.coinone.originPair", is("BTCKRW")))
            .andExpect(jsonPath("$.data.korbit.last", is(7524500.0)))
            .andExpect(jsonPath("$.data.korbit.originPair", is("BTCKRW")))
            .andExpect(jsonPath("$.data.bitfinex", is(nullValue())))
            .andExpect(jsonPath("$.status", is("success")))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetLTC() throws Exception {
        this.mvc.perform(get("/api/v1/data/currency/LTC"))
	    .andDo(print())
            .andExpect(jsonPath("$.data.bithumb.last", is(107800.0)))
            .andExpect(jsonPath("$.data.bithumb.originPair", is("LTCKRW")))
            .andExpect(jsonPath("$.data.coinone.last", is(108050.0)))
            .andExpect(jsonPath("$.data.coinone.originPair", is("LTCKRW")))
            .andExpect(jsonPath("$.data.korbit", is(nullValue())))
            .andExpect(jsonPath("$.data.bitfinex", is(nullValue())))
            .andExpect(jsonPath("$.status", is("success")))
            .andExpect(status().isOk());
    }

    private Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

}
