package com.azerpost.app.repository;

import com.azerpost.app.model.Transfer;
import com.azerpost.app.model.TransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Page<Transfer> findByStatus(TransferStatus status, Pageable pageable);

}

