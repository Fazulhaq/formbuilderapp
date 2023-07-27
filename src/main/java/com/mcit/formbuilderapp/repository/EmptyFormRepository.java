package com.mcit.formbuilderapp.repository;

import com.mcit.formbuilderapp.domain.EmptyForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmptyForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmptyFormRepository extends JpaRepository<EmptyForm, Long> {}
