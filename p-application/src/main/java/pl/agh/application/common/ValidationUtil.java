package pl.agh.application.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.agh.payment.mysql.entity.Coupon;

public class ValidationUtil {

    public static ResponseEntity<?> validateCoupon(Coupon coupon) {
        if (coupon == null) {
            return ResponseEntity.notFound().build();
        }
        if (coupon.getAmountLeft() <= 0) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("This coupon has no usages left.");
        }
        return ResponseEntity.ok(coupon);
    }
}
