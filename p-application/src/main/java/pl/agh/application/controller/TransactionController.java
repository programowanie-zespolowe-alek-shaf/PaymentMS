package pl.agh.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.application.common.ValidationUtil;
import pl.agh.application.dto.CouponDTO;
import pl.agh.application.dto.TransactionDTO;
import pl.agh.application.service.CouponService;
import pl.agh.application.service.TransactionService;
import pl.agh.payment.mysql.entity.Coupon;
import pl.agh.payment.mysql.entity.Transaction;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CouponService couponService;

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        Coupon coupon = couponService.findByCode(transactionDTO.getCouponCode());
        ResponseEntity<?> responseEntity = ValidationUtil.validateCoupon(coupon);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            transactionDTO.setAmount(
                    transactionDTO.getAmount() * coupon.getDiscountMultiplayer()
            );
            coupon.setAmountLeft(coupon.getAmountLeft() - 1);
            couponService.update(coupon.getId(), new CouponDTO(coupon));
            Transaction newTransaction = transactionService.newTransaction(
                    transactionDTO,
                    coupon.getId()
            );
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newTransaction.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(newTransaction);
        }
        Transaction newTransaction = transactionService.newTransaction(
                transactionDTO,
                null
        );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTransaction.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newTransaction);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTransactions() { return ResponseEntity.ok(transactionService.findAll()); }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Optional<Transaction> transaction = transactionService.find(id);
        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaction);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        Transaction deletedTransaction = transactionService.delete(id);
        if (deletedTransaction == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "{id}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateTransaction(@PathVariable("id") Long id, @RequestBody @Valid TransactionDTO transactionDTO){
        Transaction updatedTransaction = transactionService.update(id, transactionDTO);
        if (updatedTransaction == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedTransaction);
        }
    }

    @GetMapping(value = "/shoppingCardID/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByShoppingCardId(@PathVariable("id") Long ShoppingCardID) {
        List<?> transaction = transactionService.findByShoppingCardID(ShoppingCardID);
        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaction);
        }
    }

}
