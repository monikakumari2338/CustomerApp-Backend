package com.deepanshu.repository;

import com.deepanshu.modal.ContinueShoppingItems;
import com.deepanshu.modal.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinueShoppingItemsRepository extends JpaRepository<ContinueShoppingItems, Long> {

	List<ContinueShoppingItems> findAllByUser(User user);

}
