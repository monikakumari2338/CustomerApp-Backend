package com.deepanshu.request;

import com.deepanshu.modal.User;
import com.deepanshu.user.domain.SubscriptionType;

import java.time.LocalDate;

public class CreateSubscriptionRequest {
    private User user;
    private SubscriptionType type;
    private LocalDate startDate;
    private LocalDate endDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
