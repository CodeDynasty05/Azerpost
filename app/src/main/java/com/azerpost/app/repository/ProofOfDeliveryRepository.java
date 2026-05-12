package com.azerpost.app.repository;

import com.azerpost.app.model.entity.ProofOfDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProofOfDeliveryRepository extends JpaRepository<ProofOfDelivery, Long>{

    Optional<ProofOfDelivery> getProofOfDeliveryByShipment_Id(Long shipmentId);
}
