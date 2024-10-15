package com.dung.UniStore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "inventory_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "inventory_item_id" , nullable = false)
    private InventoryItem inventoryItem;
    @Column(name = "quantity_change", nullable = false)
    private int quantityChange ;
    @Column(name = "transaction_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String reason;
    @Column(name = "reference_id")
    private String referenceId;
    @Column(name = "created_by",nullable = false)
    @Size(min = 5, message = "CreatedBy must be at least 5 characters")
    private String createdBy;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
