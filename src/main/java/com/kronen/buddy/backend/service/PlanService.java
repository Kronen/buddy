package com.kronen.buddy.backend.service;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.common.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlanService {

    @Autowired
    private PlanRepository planRepository;


    public Optional<Plan> findByPlanId(int planId) {
        return planRepository.findById(planId);
    }

    @Transactional
    public Plan createPlan(int planId) {
        Plan plan;
        if(planId == 1) {
            plan = planRepository.save(new Plan(PlansEnum.BASIC));
        } else if(planId == 2) {
            plan = planRepository.save(new Plan(PlansEnum.PRO));
        } else {
            throw new IllegalArgumentException("Plan id " + planId + " not recognised");
        }

        return plan;
    }
}
