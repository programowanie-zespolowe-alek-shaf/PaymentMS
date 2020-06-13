package pl.agh.payment.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.agh.payment.mysql.entity.Coupon;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Repository
public interface CouponRepository extends CrudRepository<Coupon, Long> {

    List<Coupon> findAll();

    List<Coupon> findAllById(Long id);

    Optional<Coupon> findById(Long id);

    Optional<Coupon> findByCode(String code);

}
