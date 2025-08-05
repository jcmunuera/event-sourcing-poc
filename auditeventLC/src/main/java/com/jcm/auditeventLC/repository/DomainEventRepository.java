package com.jcm.auditeventLC.repository;

import com.jcm.auditeventLC.dbo.DomainEventDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DomainEventRepository extends JpaRepository<DomainEventDbo, UUID> {
}
