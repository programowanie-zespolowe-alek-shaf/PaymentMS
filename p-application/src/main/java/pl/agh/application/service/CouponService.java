package pl.agh.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.application.common.ListResponse;
import pl.agh.application.common.ListUtil;
import pl.agh.application.dto.CouponDTO;
import pl.agh.payment.mysql.entity.Coupon;
import pl.agh.payment.mysql.repository.CouponRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon addCoupon(CouponDTO couponDTO) {
        Coupon coupon = couponDTO.toEntity();
        return couponRepository.save(coupon);
    }

    public ListResponse findAll(int limit, int offset) {
        List<Coupon> coupons = couponRepository.findAll();
        int count = coupons.size();
        coupons = ListUtil.clampedSublist(coupons, limit, offset);
        return new ListResponse(coupons, count);
    }

    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    public Coupon delete(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (coupon.isEmpty()) {
            return null;
        }
        couponRepository.delete(coupon.get());
        return coupon.get();
    }

    public Coupon update(Long id, CouponDTO couponDTO) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (coupon.isEmpty()) {
            return null;
        }
        Coupon updatedCoupon = coupon.get();
        updatedCoupon.setAmountLeft(couponDTO.getAmountLeft());
        updatedCoupon.setCode(couponDTO.getCode());
        updatedCoupon.setDiscountMultiplayer(couponDTO.getDiscountMultiplayer());
        updatedCoupon = couponRepository.save(updatedCoupon);
        return updatedCoupon;
    }

    public Coupon findByCode(String code) {
        if (code == null) {
            return null;
        }
        return couponRepository.findByCode(code).orElse(null);
    }
}
