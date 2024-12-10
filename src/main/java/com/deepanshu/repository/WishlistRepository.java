package com.deepanshu.repository;

import com.deepanshu.modal.User;
import com.deepanshu.modal.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @Query("SELECT c From Wishlist c where c.user.id=:userId")
    public Wishlist findByUserId(@Param("userId") Long userId);

    Wishlist findByUser(User user);
}