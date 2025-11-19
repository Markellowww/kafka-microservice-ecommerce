package com.markellowww.processing.repositories;

import com.markellowww.processing.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Markelloww
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
