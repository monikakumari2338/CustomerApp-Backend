package com.deepanshu.repository;

import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c From Cart c where c.user.id=:userId")
    public Cart findByUserId(@Param("userId") Long userId);

    Cart findByUser(User user);
}
