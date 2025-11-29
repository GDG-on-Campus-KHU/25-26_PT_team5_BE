package com.gdg.team5.jobposting.repository;

import com.gdg.team5.jobposting.domain.JobPostings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingsRepository extends JpaRepository<JobPostings, Long> {
    Optional<JobPostings> findBySourceAndExternalId(String source, String externalId);

    List<JobPostings> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTechStackContainingIgnoreCase(
        String titleKeyword,
        String contentKeyword,
        String techStackKeyword
    );
}
