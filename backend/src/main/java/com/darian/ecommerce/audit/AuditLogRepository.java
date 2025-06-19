package com.darian.ecommerce.audit;

import com.darian.ecommerce.audit.entity.AuditLog;
import com.darian.ecommerce.auth.entity.User;
import com.darian.ecommerce.audit.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Save an audit log entry
    AuditLog save(AuditLog auditLog);

    // Count the number of delete actions by a user
    Integer countDeletesByUserId(Integer userId);


    Integer countByUserAndActionTypeAndTimestampAfter(User userById, ActionType actionType, LocalDateTime startOfDay);
}
