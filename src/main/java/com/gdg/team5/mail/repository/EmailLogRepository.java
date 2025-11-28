package com.gdg.team5.mail.repository;

import com.gdg.team5.mail.domain.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Integer> {

    List<EmailLog> findByUserId(String userId);

    List<EmailLog> findBySentDateBetween(LocalDateTime start, LocalDateTime end);

    List<EmailLog> findByStatus(String status);

    List<EmailLog> findByUserIdOrderBySentDateDesc(String userId);
}
