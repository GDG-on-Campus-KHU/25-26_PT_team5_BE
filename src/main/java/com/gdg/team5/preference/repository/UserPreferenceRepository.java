package com.gdg.team5.preference.repository;

import com.gdg.team5.preference.domain.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserPreference up WHERE up.user.id = :userId")
    void deleteByUserId(@Param("userId") Long id);

    @Query("SELECT up FROM UserPreference up JOIN FETCH up.preference WHERE up.user.id = :userId")
    List<UserPreference> findAllByUserIdWithPreference(@Param("userId") Long userId);
}
