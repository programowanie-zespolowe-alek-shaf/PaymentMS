package pl.agh.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.application.dto.TransactionDTO;
import pl.agh.payment.mysql.entity.Transaction;
import pl.agh.payment.mysql.repository.TransactionRepository;


@Service
public class TransactionService{

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction newTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionDTO.toEntity();
        return transactionRepository.save(transaction);
    }

    public Iterable<Transaction> findAll() { return  transactionRepository.findAll(); }

    public Iterable<Transaction> findAllById(Long id) { return transactionRepository.findAllById(id); }

    public Iterable<Transaction> findByShoppingCardID(Long id) { return transactionRepository.findByShoppingCardID(id);}


}
