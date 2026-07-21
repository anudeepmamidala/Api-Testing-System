package com.anudeep.probeapi.repository;

import com.anudeep.probeapi.entity.RequestJob;
import com.anudeep.probeapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestJobRepository extends JpaRepository<RequestJob, Long> {
    Optional<RequestJob> findByIdAndUser(Long id, User user);
    Page<RequestJob> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
