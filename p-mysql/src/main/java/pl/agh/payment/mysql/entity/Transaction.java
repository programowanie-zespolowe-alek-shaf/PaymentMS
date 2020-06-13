package pl.agh.payment.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name="transaction", schema = "payment")
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
    @PositiveOrZero
    private Float amount;

    @NotNull
    private Status status;


    @Column(name = "coupon_id")
    private Long couponID;

    public enum Status {
        PENDING, COMPLETED, CANCELED
    }

}
