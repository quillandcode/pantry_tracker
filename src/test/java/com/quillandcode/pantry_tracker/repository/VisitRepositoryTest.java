package com.quillandcode.pantry_tracker.repository;

import com.quillandcode.pantry_tracker.entity.Visit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VisitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    void whenSave_thenPersistsDataAndTriggersPrePersist() {
        Visit visit = new Visit("Ichigo", "Kurasaki", "75009", 4);

        Visit saved = visitRepository.save(visit);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertEquals("Ichigo", saved.getFirstName());
        assertEquals("Kurasaki", saved.getLastName());
    }

    @Test
    void whenQueryWithSpecification_thenReturnsFilteredResults() {
        Visit v1 = new Visit("Boaty", "McBoatface", "75009", 4);
        Visit v2 = new Visit("Flipto", "Decrypto", "90210", 2);
        entityManager.persist(v1);
        entityManager.persist(v2);
        entityManager.flush();

        Specification<Visit> zipSpec = (root, query, cb) -> cb.equal(root.get("zipCode"), "75009");

        List<Visit> results = visitRepository.findAll(zipSpec);

        assertEquals(1, results.size());
        assertEquals("Boaty", results.get(0).getFirstName());
    }
}