package pl.agh.application.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.payment.mysql.entity.Coupon;

import java.net.URI;

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
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(coupon.getId())
                .toUri();
        return ResponseEntity.ok(uri);
    }
}
