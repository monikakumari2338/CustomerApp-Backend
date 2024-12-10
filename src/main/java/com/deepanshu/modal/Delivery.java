package com.deepanshu.modal;

import com.deepanshu.user.domain.DeliveryStatus;
import jakarta.persistence.*;

import java.security.PublicKey;
import java.time.LocalDate;

@Entity
@Table(name="deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;

    public Delivery()
    {

    }
    public Delivery(Long id, LocalDate deliveryDate) {
        this.id = id;
        this.deliveryDate = deliveryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
