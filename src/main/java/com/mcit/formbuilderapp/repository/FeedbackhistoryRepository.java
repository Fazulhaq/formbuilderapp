package com.mcit.formbuilderapp.repository;

import com.mcit.formbuilderapp.domain.Feedbackhistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Feedbackhistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackhistoryRepository extends JpaRepository<Feedbackhistory, Long> {}
