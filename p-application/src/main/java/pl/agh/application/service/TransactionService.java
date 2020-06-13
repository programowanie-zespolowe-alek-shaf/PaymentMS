package pl.agh.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.application.dto.TransactionDTO;
import pl.agh.payment.mysql.entity.Coupon;
import pl.agh.payment.mysql.entity.Transaction;
import pl.agh.payment.mysql.repository.CouponRepository;
import pl.agh.payment.mysql.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionService{

    private final TransactionRepository transactionRepository;

    private final CouponRepository couponRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CouponRepository couponRepository) {
        this.transactionRepository = transactionRepository;
        this.couponRepository = couponRepository;
    }

    public Transaction newTransaction(TransactionDTO transactionDTO, Long couponID) {
        Transaction transaction = transactionDTO.toEntity(couponID);
        return transactionRepository.save(transaction);
    }

    public Iterable<Transaction> findAll() { return  transactionRepository.findAll(); }

    public Optional<Transaction> find(Long id) { return transactionRepository.findById(id); }

    public List<Transaction> findByShoppingCardID(Long id) { return transactionRepository.findByShoppingCardID(id);}

    public Transaction update(Long id, TransactionDTO transactionDTO) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            return null;
        }
        Transaction updatedTransaction = transaction.get();
        updatedTransaction.setShoppingCardID(transactionDTO.getShoppingCardID());
        updatedTransaction.setAmount(transactionDTO.getAmount());
        Coupon coupon = couponRepository.findByCode(transactionDTO.getCouponCode()).orElse(null);
        if (coupon != null) {
            updatedTransaction.setCouponID(coupon.getId());
        } else {
            updatedTransaction.setCouponID(null);
        }
        updatedTransaction = transactionRepository.save(updatedTransaction);
        return updatedTransaction;
    }

    public Transaction delete(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isEmpty()) {
            return null;
        }
        transactionRepository.delete(transaction.get());
        return transaction.get();
    }
}
