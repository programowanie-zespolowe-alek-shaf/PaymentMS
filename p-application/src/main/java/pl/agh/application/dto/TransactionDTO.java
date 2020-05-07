package pl.agh.application.dto;

import lombok.Getter;
import lombok.Setter;
import pl.agh.payment.mysql.entity.Transaction;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {

    @NotNull
    private Long shoppingCardID;

    @NotNull
    private Float amount;

    @NotNull
    private String methodOfPayment;

    private String couponCode;

    public Transaction toEntity() {
        return Transaction.builder()
                .shoppingCardID(shoppingCardID)
                .timestampOfTransaction(new Date())
                .amount(amount)
                .status("Completed")
                .methodOfPayment(methodOfPayment)
                .build();
    }

}
