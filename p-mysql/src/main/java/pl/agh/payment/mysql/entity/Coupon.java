package pl.agh.payment.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name="coupon", schema = "payment")
public class Coupon {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String code;

    @NotNull
    @Column(name = "discount_multiplayer")
    private Float discountMultiplayer;

    @NotNull
    @Column(name = "amount_left")
    private int amountLeft;
}
