package com.mflores.telecomapp.tests.plan.repository;

import com.mflores.telecomapp.model.Money;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.repository.PlanRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Test
    void shouldCreatePlanSuccessfully() {
        Plan plan = new Plan();
        plan.setPlanName("Unlimited Ultimate");
        plan.setDescription("Unlimited 5G UW data, 25 GB of roaming, 200GB of hotspot");
        plan.setActive(true);
        plan.setDataLimitMb(null);
        plan.setRoamingLimitMb(25600L);
        plan.setHotspotLimitMb(204800L);
        Money money = new Money(10000, Currency.getInstance("USD"));
        plan.setPrice(money);

        Plan savedPlan = planRepository.saveAndFlush(plan);
        //That forces Hibernate to actually execute the INSERT before the assertions.
        // This becomes especially important when you're testing database constraints.

        assertEquals("Unlimited Ultimate",  savedPlan.getPlanName());
        assertEquals("Unlimited 5G UW data, 25 GB of roaming, 200GB of hotspot",  savedPlan.getDescription());
        assertTrue(savedPlan.isActive());
        assertNull(savedPlan.getDataLimitMb());
        assertEquals(25600L, savedPlan.getRoamingLimitMb());
        assertEquals(204800L, savedPlan.getHotspotLimitMb());
        assertEquals(10000, savedPlan.getPrice().amountInCents());
        assertEquals(Currency.getInstance("USD"), savedPlan.getPrice().currency());
    }

    @Test
    void shouldNotAllowNullPlanName() {
        Plan plan = new Plan();
        plan.setPlanName(null);

        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowNullDescription() {
        Plan plan = new Plan();
        plan.setDescription(null);

        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowNegativeDataLimitMb() {

        Plan plan = new Plan();
        plan.setDataLimitMb(-10L);

        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowNegativeRoamingLimitMb() {
        Plan plan = new Plan();
        plan.setRoamingLimitMb(-10L);
        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowNegativeHotspotLimitMb() {
        Plan plan = new Plan();
        plan.setHotspotLimitMb(-10L);
        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowNullPrice() {
        Plan plan = new Plan();
        plan.setPrice(null);
        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }

    @Test
    void shouldNotAllowDuplicatePlanName() {
        Plan plan = new Plan();
        plan.setPlanName("Unlimited Ultimate");
        plan.setDescription("Unlimited 5G UW data, 25 GB of roaming, 200GB of hotspot");
        plan.setActive(true);
        plan.setDataLimitMb(null);
        plan.setRoamingLimitMb(25600L);
        plan.setHotspotLimitMb(204800L);
        Money money = new Money(10000, Currency.getInstance("USD"));
        plan.setPrice(money);
        planRepository.saveAndFlush(plan);

        Plan plan2 = new Plan();
        plan2.setPlanName("Unlimited Ultimate");
        plan2.setDescription("Unlimited 5G UW data, 25 GB of roaming, 200GB of hotspot");
        plan2.setActive(true);
        plan2.setDataLimitMb(null);
        plan2.setRoamingLimitMb(25600L);
        plan2.setHotspotLimitMb(204800L);
        Money money2 = new Money(10000, Currency.getInstance("USD"));
        plan2.setPrice(money2);

        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan2));
    }

    @Test
    void shouldNotAllowZeroPrice() {
        Plan plan = new Plan();

        plan.setPrice(new Money(0, Currency.getInstance("USD")));

        assertThrows(DataIntegrityViolationException.class, () -> planRepository.saveAndFlush(plan));
    }
}
