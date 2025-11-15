package com.smartlogi.smartlogiv010.integration;

import com.smartlogi.smartlogiv010.entity.*;
import com.smartlogi.smartlogiv010.enums.Priorite;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import com.smartlogi.smartlogiv010.repository.ColisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("ColisRepository Integration Tests")
class ColisRepositoryTest {

    @Autowired
    private ColisRepository colisRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ClientExpediteur client;
    private Destinataire destinataire;
    private Livreur livreur;
    private Zone zone;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setNom("Zone Test");
        zone = entityManager.persistAndFlush(zone);

        client = new ClientExpediteur();
        client.setNom("Client Test");
        client.setEmail("client@test.com");
        client.setTelephone("+212600000001");
        client = entityManager.persistAndFlush(client);

        destinataire = new Destinataire();
        destinataire.setNom("Destinataire Test");
        destinataire.setPrenom("Test");
        destinataire.setEmail("dest@test.com");
        destinataire.setTelephone("+212600000002");
        destinataire.setAdresse("Adresse Test");
        destinataire = entityManager.persistAndFlush(destinataire);

        livreur = new Livreur();
        livreur.setNom("Livreur Test");
        livreur.setPrenom("Test");
        livreur.setTelephone("+212600000003");
        livreur.setZone(zone);
        livreur = entityManager.persistAndFlush(livreur);
    }

    @Test
    @DisplayName("Test findByStatut - Should return colis with specific status")
    void testFindByStatut() {

        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        Colis colis2 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);
        Colis colis3 = createColis(StatutColis.CREE, Priorite.BASSE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByStatut(StatutColis.CREE);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Colis::getStatut)
                .containsOnly(StatutColis.CREE);
    }

    @Test
    @DisplayName("Test findByPriorite - Should filter by priority")
    void testFindByPriorite() {

        Colis colis1 = createColis(StatutColis.CREE, Priorite.HAUTE);
        Colis colis2 = createColis(StatutColis.CREE, Priorite.NORMALE);
        Colis colis3 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByPriorite(Priorite.HAUTE);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Colis::getPriorite)
                .containsOnly(Priorite.HAUTE);
    }

    @Test
    @DisplayName("Test findByPrioriteAndStatut - Should filter by priority and status")
    void testFindByPrioriteAndStatut() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.HAUTE);
        Colis colis2 = createColis(StatutColis.CREE, Priorite.NORMALE);
        Colis colis3 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByPrioriteAndStatut(
                Priorite.HAUTE, StatutColis.CREE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriorite()).isEqualTo(Priorite.HAUTE);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutColis.CREE);
    }

    @Test
    @DisplayName("Test findByLivreur - Should return all colis for a delivery person")
    void testFindByLivreur() {
        // Arrange
        Colis colis1 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);
        Colis colis2 = createColis(StatutColis.LIVRE, Priorite.HAUTE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.flush();

        List<Colis> result = colisRepository.findByLivreur(livreur);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getLivreur().getId().equals(livreur.getId()));
    }

    @Test
    @DisplayName("Test findByLivreurAndStatut - Should filter by delivery person and status")
    void testFindByLivreurAndStatut() {
        // Arrange
        Colis colis1 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);
        Colis colis2 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);
        Colis colis3 = createColis(StatutColis.LIVRE, Priorite.NORMALE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByLivreurAndStatut(livreur, StatutColis.EN_TRANSIT);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getStatut().equals(StatutColis.EN_TRANSIT));
    }

    @Test
    @DisplayName("Test findByZone - Should return colis for specific zone")
    void testFindByZone() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        Colis colis2 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.flush();

        List<Colis> result = colisRepository.findByZone(zone);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getZone().getId().equals(zone.getId()));
    }

    @Test
    @DisplayName("Test findByZoneAndStatut - Should filter by zone and status")
    void testFindByZoneAndStatut() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        Colis colis2 = createColis(StatutColis.CREE, Priorite.HAUTE);
        Colis colis3 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByZoneAndStatut(zone, StatutColis.CREE);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getStatut().equals(StatutColis.CREE));
    }

    @Test
    @DisplayName("Test findByVilleDestination - Should filter by destination city")
    void testFindByVilleDestination() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis1.setVilleDestination("Casablanca");

        Colis colis2 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis2.setVilleDestination("Rabat");

        Colis colis3 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis3.setVilleDestination("Casablanca");

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByVilleDestination("Casablanca");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Colis::getVilleDestination)
                .containsOnly("Casablanca");
    }

    @Test
    @DisplayName("Test findByVilleDestinationAndStatut - Should filter by city and status")
    void testFindByVilleDestinationAndStatut() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis1.setVilleDestination("Casablanca");

        Colis colis2 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);
        colis2.setVilleDestination("Casablanca");

        Colis colis3 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis3.setVilleDestination("Rabat");

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Colis> result = colisRepository.findByVilleDestinationAndStatut("Casablanca", StatutColis.CREE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getVilleDestination()).isEqualTo("Casablanca");
        assertThat(result.get(0).getStatut()).isEqualTo(StatutColis.CREE);
    }

    @Test
    @DisplayName("Test searchByKeyword - Should search across multiple fields")
    void testSearchByKeyword() {
        // Arrange
        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis1.setDescription("Livraison urgente");

        Colis colis2 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis2.setDescription("Colis normal");

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.flush();

        List<Colis> result = colisRepository.searchByKeyword("urgente");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains("urgente");
    }

    @Test
    @DisplayName("Test countByStatut - Should count colis by status")
    void testCountByStatut() {
        // Arrange
        entityManager.persist(createColis(StatutColis.CREE, Priorite.NORMALE));
        entityManager.persist(createColis(StatutColis.CREE, Priorite.HAUTE));
        entityManager.persist(createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE));
        entityManager.flush();

        long count = colisRepository.countByStatut(StatutColis.CREE);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Test countByZoneAndStatut - Should count by zone and status")
    void testCountByZoneAndStatut() {
        // Arrange
        entityManager.persist(createColis(StatutColis.CREE, Priorite.NORMALE));
        entityManager.persist(createColis(StatutColis.CREE, Priorite.HAUTE));
        entityManager.persist(createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE));
        entityManager.flush();

        long count = colisRepository.countByZoneAndStatut(zone.getId(), StatutColis.CREE);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Test countByLivreurAndStatut - Should count by delivery person and status")
    void testCountByLivreurAndStatut() {
        // Arrange
        entityManager.persist(createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE));
        entityManager.persist(createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE));
        entityManager.persist(createColis(StatutColis.LIVRE, Priorite.NORMALE));
        entityManager.flush();

        long count = colisRepository.countByLivreurAndStatut(livreur.getId(), StatutColis.EN_TRANSIT);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Test findByDateCreationBetween - Should filter by date range")
    void testFindByDateCreationBetween() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime oldDate = now.minusDays(5);  // Bien avant yesterday

        Colis colis1 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis1.setDateCreation(now);

        Colis colis2 = createColis(StatutColis.CREE, Priorite.NORMALE);
        colis2.setDateCreation(oldDate);  // Date bien en dehors de la plage

        entityManager.persist(colis1);
        entityManager.flush();

        List<Colis> result = colisRepository.findByDateCreationBetween(yesterday, tomorrow);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDateCreation()).isAfterOrEqualTo(yesterday);
        assertThat(result.get(0).getDateCreation()).isBeforeOrEqualTo(tomorrow);
    }


    @Test
    @DisplayName("Test findAll with Pageable - Should return paginated results")
    void testFindAllWithPageable() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            entityManager.persist(createColis(StatutColis.CREE, Priorite.NORMALE));
        }
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);
        Page<Colis> page = colisRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test findByStatut with Pageable - Should return paginated filtered results")
    void testFindByStatutWithPageable() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            entityManager.persist(createColis(StatutColis.CREE, Priorite.NORMALE));
        }
        entityManager.persist(createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE));
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);
        Page<Colis> page = colisRepository.findByStatut(StatutColis.CREE, pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    @DisplayName("Test findByZoneId with Pageable - Should return paginated zone results")
    void testFindByZoneIdWithPageable() {
        // Arrange
        for (int i = 1; i <= 3; i++) {
            entityManager.persist(createColis(StatutColis.CREE, Priorite.NORMALE));
        }
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);
        Page<Colis> page = colisRepository.findByZoneId(zone.getId(), pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test findPoidsTotalParLivreur - Should calculate total weight per delivery person")
    void testFindPoidsTotalParLivreur() {
        // Arrange
        Colis colis1 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);
        colis1.setPoids(new BigDecimal("10.50"));

        Colis colis2 = createColis(StatutColis.LIVRE, Priorite.NORMALE);
        colis2.setPoids(new BigDecimal("5.50"));

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.flush();

        List<Object[]> result = colisRepository.findPoidsTotalParLivreur();

        assertThat(result).isNotEmpty();
        Object[] firstRow = result.get(0);
        assertThat(firstRow[0]).isEqualTo(livreur);
        assertThat(firstRow[1]).isEqualTo(new BigDecimal("16.00"));
    }

    @Test
    @DisplayName("Test findPoidsTotalParLivreurAvecStatut - Should calculate weight by status")
    void testFindPoidsTotalParLivreurAvecStatut() {
        // Arrange
        Colis colis1 = createColis(StatutColis.EN_TRANSIT, Priorite.NORMALE);
        colis1.setPoids(new BigDecimal("10.00"));

        Colis colis2 = createColis(StatutColis.EN_TRANSIT, Priorite.HAUTE);
        colis2.setPoids(new BigDecimal("5.00"));

        Colis colis3 = createColis(StatutColis.LIVRE, Priorite.NORMALE);
        colis3.setPoids(new BigDecimal("3.00"));

        entityManager.persist(colis1);
        entityManager.persist(colis2);
        entityManager.persist(colis3);
        entityManager.flush();

        List<Object[]> result = colisRepository.findPoidsTotalParLivreurAvecStatut(
                Arrays.asList(StatutColis.EN_TRANSIT));

        assertThat(result).isNotEmpty();
        Object[] firstRow = result.get(0);
        assertThat(firstRow[1]).isEqualTo(new BigDecimal("15.00"));
    }


    private Colis createColis(StatutColis statut, Priorite priorite) {
        Colis colis = new Colis();
        colis.setDescription("Test colis");
        colis.setPoids(new BigDecimal("5.0"));
        colis.setStatut(statut);
        colis.setPriorite(priorite);
        colis.setVilleDestination("Casablanca");
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setLivreur(livreur);
        colis.setZone(zone);
        colis.setHistorique(new ArrayList<>());
        return colis;
    }
}
