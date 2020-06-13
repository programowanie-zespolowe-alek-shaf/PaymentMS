package pl.agh.application.controller.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.application.dto.CouponDTO;
import pl.agh.application.dto.TransactionDTO;
import pl.agh.application.service.CouponService;
import pl.agh.application.utils.TestUtils;
import pl.agh.payment.mysql.entity.Coupon;
import pl.agh.payment.mysql.entity.Transaction;
import pl.agh.payment.mysql.repository.CouponRepository;
import pl.agh.payment.mysql.repository.TransactionRepository;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.application.utils.TestUtils.getIdFromResponse;


@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void successHappyPathNoCode() throws Exception {
        float amount = 20.0f;
        String couponCode = null;
        Long shoppingCardID = 1L;

        //post test
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(amount);
        transactionDTO.setCouponCode(couponCode);
        transactionDTO.setShoppingCardID(shoppingCardID);
        String requestJson = TestUtils.objectToJson(transactionDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transaction").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("amount").value(amount))
                .andExpect(jsonPath("couponID").value(couponCode))
                .andExpect(jsonPath("shoppingCardID").value( shoppingCardID))
                .andReturn();


        Long transactionID =  getIdFromResponse(mvcResult);
        Transaction transaction = transactionRepository.findById(transactionID).orElse(null);
        assertNotNull(transaction);
        assertEquals(transaction.getId(), transactionID);
        assertEquals(transaction.getAmount(), amount);
        assertEquals(transaction.getShoppingCardID(), shoppingCardID);
        assertNull(transaction.getCouponID());

        //put test
        amount  = 0;
        transactionDTO.setAmount(amount);
        requestJson = TestUtils.objectToJson(transactionDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.put("/transaction/"+transactionID).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("amount").value(amount))
                .andExpect(jsonPath("couponID").value(couponCode))
                .andExpect(jsonPath("shoppingCardID").value( shoppingCardID))
                .andReturn();

        //get test
        mvc.perform(MockMvcRequestBuilders.get("/transaction/" + transactionID))
                .andExpect(status().is(200))
                .andExpect(jsonPath("amount").value(amount))
                .andExpect(jsonPath("couponID").value(couponCode))
                .andExpect(jsonPath("shoppingCardID").value( shoppingCardID));

        //delete test
        mvc.perform(MockMvcRequestBuilders.delete("/transaction/" + transactionID))
                .andExpect(status().is(204));

        transaction = transactionRepository.findById(transactionID).orElse(null);
        assertNull(transaction);

    }

    @Test
    public void addTransactionWithCode() throws Exception {
        String code = "WithCodeTest";
        int amountLeft = 1;
        float discountMultiplayer = 0.9f;

        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCode(code);
        couponDTO.setAmountLeft(amountLeft);
        couponDTO.setDiscountMultiplayer(discountMultiplayer);

        couponService.addCoupon(couponDTO);
        long couponID = couponService.findByCode(code).getId();
        float amount = 20.0f;

        Long shoppingCardID = 1L;

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(amount);
        transactionDTO.setCouponCode(code);
        transactionDTO.setShoppingCardID(shoppingCardID);
        String requestJson = TestUtils.objectToJson(transactionDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/transaction").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("amount").value(amount * discountMultiplayer))
                .andExpect(jsonPath("couponID").value(couponID))
                .andExpect(jsonPath("shoppingCardID").value( shoppingCardID))
                .andReturn();




    }
}
