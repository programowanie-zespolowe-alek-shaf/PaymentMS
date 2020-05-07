package pl.agh.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.application.dto.TransactionDTO;
import pl.agh.application.service.TransactionService;
import pl.agh.payment.mysql.entity.Transaction;

import javax.validation.Valid;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newTransaction(@RequestBody @Valid TransactionDTO transactionDTO) throws Exception {
        Transaction newTransaction = transactionService.newTransaction(transactionDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTransaction.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newTransaction);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTransactions() { return ResponseEntity.ok(transactionService.findAll()); }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllById(@PathVariable("id") Long id) {
        Iterable<?> transaction = transactionService.findAllById(id);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaction);
        }
    }
}
