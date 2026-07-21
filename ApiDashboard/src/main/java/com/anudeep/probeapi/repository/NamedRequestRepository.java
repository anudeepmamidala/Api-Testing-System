package com.anudeep.probeapi.repository;

import com.anudeep.probeapi.entity.NamedRequest;
import com.anudeep.probeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NamedRequestRepository extends JpaRepository<NamedRequest, Long> {
    List<NamedRequest> findByUserOrderByCreatedAtDesc(User user);
    Optional<NamedRequest> findByIdAndUser(Long id, User user);
    void deleteByIdAndUser(Long id, User user);
}
