package pl.agh.payment.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.agh.payment.mysql.entity.Transaction;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAll();

    List<Transaction> findByShoppingCardID(Long shoppingCardID);

    Optional<Transaction> findById(Long id);

}
