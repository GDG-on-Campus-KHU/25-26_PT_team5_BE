package com.gdg.team5.scrap.repository;

import com.gdg.team5.scrap.domain.Scrap;
import com.gdg.team5.scrap.domain.ScrapType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUserIdAndTypeAndContentId(Long userId, ScrapType type, Long contentId);

    List<Scrap> findAllByUserId(Long userId);
}
