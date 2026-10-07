package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_line_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long invoiceLineItemId;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private InvoiceLineItemType invoiceLineItemType;
    @Column(length = 300, nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    @Embedded
    @AttributeOverrides({ //o @AttributeOverride tells JPA: "For this particular use of Money, map amountInCents to the amount column."
            @AttributeOverride(
                    name = "amountInCents",
                    column = @Column(name = "amount", nullable = false)
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "currency", nullable = false, length = 3)
            )
    })
    private Money amount;
}
