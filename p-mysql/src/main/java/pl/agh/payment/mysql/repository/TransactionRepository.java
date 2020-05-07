package pl.agh.payment.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.agh.payment.mysql.entity.Transaction;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAll();

    List<Transaction> findByShoppingCardID(Long shoppingCardID);

    List<Transaction> findAllById(Long id);

}
