package pl.agh.application.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.application.common.ValidationUtil;
import pl.agh.application.dto.CouponDTO;
import pl.agh.application.service.CouponService;
import pl.agh.payment.mysql.entity.Coupon;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCoupon(@RequestBody @Valid CouponDTO couponDTO) {
        if (couponService.findByCode(couponDTO.getCode()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon with given code already exists.");
        }
        if (couponDTO.getDiscountMultiplayer() <= 0 || couponDTO.getDiscountMultiplayer() >= 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon with incorrect multiplayer");
        }
        Coupon newCoupon = couponService.addCoupon(couponDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCoupon.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newCoupon);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCoupons(@RequestParam int limit, @RequestParam int offset) {
        return ResponseEntity.ok(couponService.findAll(limit, offset));
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Coupon coupon = couponService.findById(id);
        if (coupon == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(coupon);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        Coupon deletedCoupon = couponService.delete(id);
        if (deletedCoupon == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateCoupon(@PathVariable("id") Long id, @RequestBody @Valid CouponDTO couponDTO) {
        Coupon updatedCoupon = couponService.update(id, couponDTO);
        if (updatedCoupon == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedCoupon);
        }
    }

    @GetMapping(value = "validate/{code}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> validateCoupon(@PathVariable String code) {
        Coupon coupon = couponService.findByCode(code);
        return ValidationUtil.validateCoupon(coupon);

    }
}
