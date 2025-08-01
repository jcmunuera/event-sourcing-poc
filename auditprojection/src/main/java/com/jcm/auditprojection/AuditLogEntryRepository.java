package com.jcm.auditprojection;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuditLogEntryRepository extends JpaRepository<AuditLogEntry, UUID> {
}
