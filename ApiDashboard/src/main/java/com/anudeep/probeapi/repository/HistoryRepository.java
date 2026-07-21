package com.anudeep.probeapi.repository;

import com.anudeep.probeapi.entity.HistoryEntry;
import com.anudeep.probeapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntry, Long> {
    Page<HistoryEntry> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    java.util.Optional<HistoryEntry> findByIdAndUser(Long id, User user);
    void deleteByUser(User user);
}
