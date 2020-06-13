package pl.agh.application.dto;

import lombok.Getter;
import lombok.Setter;
import pl.agh.payment.mysql.entity.Transaction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {

    @NotNull
    private Long shoppingCardID;

    @NotNull
    @PositiveOrZero
    private Float amount;

    private String couponCode;

    public Transaction toEntity(Long couponID) {
        return Transaction.builder()
                .shoppingCardID(shoppingCardID)
                .timestampOfTransaction(new Date())
                .amount(amount)
                .status(Transaction.Status.COMPLETED)
                .couponID(couponID)
                .build();
    }

}
