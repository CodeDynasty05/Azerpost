package com.azerpost.app.specification;

import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.enums.ShipmentType;
import com.azerpost.app.model.enums.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShipmentSpecification {

    public static Specification<Shipment> withDynamicQuery(
            Status status,
            String receiverName,
            ShipmentType shipmentType,
            Integer minPrice,
            Integer maxPrice) {

        return (Root<Shipment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (receiverName != null && !receiverName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productName")),
                        "%" + receiverName.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (shipmentType != null) {
                predicates.add(cb.equal(root.get("shipmentType"), shipmentType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
