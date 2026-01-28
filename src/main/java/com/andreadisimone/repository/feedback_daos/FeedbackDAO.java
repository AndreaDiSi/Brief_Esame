package com.andreadisimone.repository.feedback_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Feedback;
import com.andreadisimone.model.Tenant;

public interface FeedbackDAO {

    // ==================== CREATE ====================

    Feedback create(Feedback Feedback);

    // ==================== READ ====================
    List<Feedback> findAll();

    List<Feedback> findAllTenantFeedbacks(Tenant idTenant);

    Optional<Feedback> findById(Integer idFeedback);

    List<Feedback> findAllAccomodationFeedbacks(Accomodation idAccomodation);

    // ==================== UPDATE ====================

    Optional<Feedback> update(Feedback feedback);

    // ==================== DELETE ====================

    int deleteAllTenantFeedbacks(Tenant idTenant);
    
    boolean deleteById(Integer idFeedback);

    
}
