package com.markellowww.ingestion.repositories;

import com.markellowww.ingestion.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Markelloww
 */

public interface OrderRepository extends MongoRepository<Order, String> {
}
