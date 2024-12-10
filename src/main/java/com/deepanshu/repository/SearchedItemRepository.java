package com.deepanshu.repository;

import com.deepanshu.modal.Order;
import com.deepanshu.modal.SearchedItem;
import com.deepanshu.modal.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchedItemRepository extends JpaRepository<SearchedItem, Long> {

	public List<SearchedItem> findTop5ByUserOrderByDateTimeDesc(User user);

}
