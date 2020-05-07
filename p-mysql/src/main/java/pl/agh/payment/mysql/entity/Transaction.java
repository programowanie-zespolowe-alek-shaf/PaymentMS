package pl.agh.payment.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name="Transaction", schema = "payment")
public class Transaction {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "shopping_card_id")
    private long shoppingCardID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp_of_transaction")
    private Date timestampOfTransaction;

    @NotNull
    private Float amount;

    @NotNull
    private String status;

    @NotNull
    @Column(name = "method_of_payment")
    private String methodOfPayment;

    public enum Status {
        PENDING, COMPLETED, CANCELED
    }

    public enum PaymentMethod {
        CREDIT_CARD, BLIK, PAYPAL
    }
}
