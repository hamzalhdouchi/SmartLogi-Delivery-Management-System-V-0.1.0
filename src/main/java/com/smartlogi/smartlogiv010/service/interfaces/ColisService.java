package com.smartlogi.smartlogiv010.service.interfaces;

    import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
    import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDTO;
    import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDetailDTO;
    import com.smartlogi.smartlogiv010.entity.ColisProduit;
import com.smartlogi.smartlogiv010.enums.Priorite;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
import java.util.List;

public interface ColisService {

    ColisSimpleResponseDto create(ColisCreateRequestDto requestDto);
    ColisSimpleResponseDto update(String id, ColisUpdateRequestDto requestDto);
    ColisSimpleResponseDto getById(String id);
    ColisAdvancedResponseDto getByIdWithDetails(String id);
    Page<ColisSimpleResponseDto> getAll(Pageable pageable);
    public BigDecimal getPrixTotalColis(String colisId);
    boolean produitExisteDansColis(String colisId, String produitId);
    void delete(String id);
    void assignerLivreur(String colisId, String livreurId);
    void changerStatut(String colisId, StatutColis nouveauStatut, String commentaire);
    List<ColisSimpleResponseDto> getByStatut(StatutColis statut);
    List<ColisSimpleResponseDto> getByClientExpediteur(String clientExpediteurId);
    List<ColisSimpleResponseDto> getByDestinataire(String destinataireId);
    List<ColisAdvancedResponseDto> getByLivreur(String livreurId);
    List<ColisSimpleResponseDto> getByZone(String zoneId);
    List<ColisSimpleResponseDto> searchByVilleDestination(String ville);
    List<ColisSimpleResponseDto> getByPriorite(Priorite priorite);
    List<ColisSimpleResponseDto> getByPrioriteAndStatut(Priorite priorite, StatutColis statut);
    List<ColisSimpleResponseDto> getByZoneAndStatut(String zoneId, StatutColis statut);
    List<ColisSimpleResponseDto> getByVilleDestinationAndStatut(String ville, StatutColis statut);
    List<ColisProduit> getProduitsByColis(String colisId);
    void ajouterProduit(String colisId, ColisCreateRequestDto.ProduitColisDto produitDto);
    void mettreAJourQuantiteProduit(String colisId, String produitId, Integer nouvelleQuantite);
    void supprimerProduit(String colisId, String produitId);
    List<ColisSimpleResponseDto> searchByKeyword(String keyword);
    List<ColisSimpleResponseDto> getColisEnRetard();
    Page<ColisSimpleResponseDto> getByZoneId(String zoneId, Pageable pageable);
    Page<ColisSimpleResponseDto> getByStatut(StatutColis statut, Pageable pageable);
    List<PoidsParLivreurDetailDTO> getPoidsDetailParLivreur();
    long countByStatut(StatutColis statut);
    long countByZoneAndStatut(String zoneId, StatutColis statut);
    long countByLivreurAndStatut(String livreurId, StatutColis statut);

    Double calculateTotal(String colisId);
    Double calculateTotalPrix(String colisId);

}