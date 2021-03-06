package pl.agh.application.controller.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.application.dto.CouponDTO;
import pl.agh.application.utils.TestUtils;
import pl.agh.payment.mysql.entity.Coupon;
import pl.agh.payment.mysql.repository.CouponRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.application.utils.TestUtils.getIdFromResponse;


@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = {"DELETE FROM payment.transaction", "DELETE FROM payment.coupon"})
public class CouponControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void successHappyPath() throws Exception {
        String code = "successCode";
        int amountLeft = 1;
        float discountMultiplayer = 0.9f;

        //post test
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCode(code);
        couponDTO.setAmountLeft(amountLeft);
        couponDTO.setDiscountMultiplayer(discountMultiplayer);
        String requestJson = TestUtils.objectToJson(couponDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/coupon").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("code").value(code))
                .andExpect(jsonPath("amountLeft").value(1))
                .andExpect(jsonPath("discountMultiplayer").value(0.9f))
                .andReturn();


        Long couponID = getIdFromResponse(mvcResult);
        Coupon coupon = couponRepository.findById(couponID).orElse(null);
        assertNotNull(coupon);
        assertEquals(coupon.getId(), couponID);
        assertEquals(coupon.getCode(), code);
        assertEquals(coupon.getAmountLeft(), amountLeft);
        assertEquals(coupon.getDiscountMultiplayer(), discountMultiplayer);

        //put test
        amountLeft = 0;
        couponDTO.setAmountLeft(amountLeft);
        requestJson = TestUtils.objectToJson(couponDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.put("/coupon/" + couponID).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("code").value(code))
                .andExpect(jsonPath("amountLeft").value(amountLeft))
                .andExpect(jsonPath("discountMultiplayer").value(discountMultiplayer));

        //get test
        mvc.perform(MockMvcRequestBuilders.get("/coupon/" + couponID))
                .andExpect(status().is(200))
                .andExpect(jsonPath("code").value(code))
                .andExpect(jsonPath("amountLeft").value(amountLeft))
                .andExpect(jsonPath("discountMultiplayer").value(discountMultiplayer));


        //delete test
        mvc.perform(MockMvcRequestBuilders.delete("/coupon/" + couponID))
                .andExpect(status().is(204));

        coupon = couponRepository.findById(couponID).orElse(null);
        assertNull(coupon);

    }

    @Test
    public void duplicateCodeError() throws Exception {
        String code = "duplicate";
        int amountLeft = 1;
        float discountMultiplayer = 0.9f;

        CouponDTO duplicateCouponDTO = new CouponDTO();
        duplicateCouponDTO.setCode(code);
        duplicateCouponDTO.setAmountLeft(amountLeft);
        duplicateCouponDTO.setDiscountMultiplayer(discountMultiplayer);
        String requestJson = TestUtils.objectToJson(duplicateCouponDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.post("/coupon").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("code").value(code))
                .andExpect(jsonPath("amountLeft").value(1))
                .andExpect(jsonPath("discountMultiplayer").value(0.9f));


        mvc.perform(MockMvcRequestBuilders.post("/coupon").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(400));

    }

    @Test
    public void getAllCoupons() throws Exception {

        // add first coupon
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCode("successCode");
        couponDTO.setAmountLeft(1);
        couponDTO.setDiscountMultiplayer(0.9f);
        String requestJson = TestUtils.objectToJson(couponDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.post("/coupon").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201));

        // add second coupon
        couponDTO = new CouponDTO();
        couponDTO.setCode("anotherCode");
        couponDTO.setAmountLeft(3);
        couponDTO.setDiscountMultiplayer(0.4f);
        requestJson = TestUtils.objectToJson(couponDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.post("/coupon").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is(201));

        //get test
        mvc.perform(MockMvcRequestBuilders.get("/coupon")
                .param("offset", "0")
                .param("limit", "10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].code").value("successCode"))
                .andExpect(jsonPath("list[0].discountMultiplayer").value("0.9"))
                .andExpect(jsonPath("list[0].amountLeft").value("1"))
                .andExpect(jsonPath("list[1].code").value("anotherCode"))
                .andExpect(jsonPath("list[1].discountMultiplayer").value("0.4"))
                .andExpect(jsonPath("list[1].amountLeft").value("3"))
                .andExpect(jsonPath("count").value("2"));
    }
}
