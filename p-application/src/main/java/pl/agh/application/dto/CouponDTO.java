package pl.agh.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.payment.mysql.entity.Coupon;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @NotNull
    private String code;

    @NotNull
    private float discountMultiplayer;

    @NotNull
    private int amountLeft;

    public Coupon toEntity(){
        return Coupon.builder()
                .code(code)
                .discountMultiplayer(discountMultiplayer)
                .amountLeft(amountLeft)
                .build();
    }

    public CouponDTO(Coupon coupon) {
        code = coupon.getCode();
        discountMultiplayer = coupon.getDiscountMultiplayer();
        amountLeft = coupon.getAmountLeft();
    }
}
