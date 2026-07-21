package com.anudeep.probeapi.repository;

import com.anudeep.probeapi.entity.Collection;
import com.anudeep.probeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserOrderByCreatedAtDesc(User user);
    Optional<Collection> findByIdAndUser(Long id, User user);
    void deleteByIdAndUser(Long id, User user);
}
