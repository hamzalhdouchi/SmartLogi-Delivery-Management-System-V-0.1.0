package com.smartlogi.smartlogiv010.serviceTest;

import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.HistoriqueLivraisonCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.HistoriqueLivraisonUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogiv010.entity.Colis;
import com.smartlogi.smartlogiv010.entity.HistoriqueLivraison;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.ColisRepository;
import com.smartlogi.smartlogiv010.repository.HistoriqueLivraisonRepository;
import com.smartlogi.smartlogiv010.service.HistoriqueLivraisonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistoriqueLivraison Service Implementation - Complete Test Suite")
class HistoriqueLivraisonServiceImplTest {

    @Mock
    private HistoriqueLivraisonRepository historiqueLivraisonRepository;

    @Mock
    private ColisRepository colisRepository;

    @Mock
    private SmartLogiMapper smartLogiMapper;

    @InjectMocks
    private HistoriqueLivraisonServiceImpl historiqueLivraisonService;

    private HistoriqueLivraison historique;
    private Colis colis;
    private HistoriqueLivraisonCreateRequestDto createRequestDto;
    private HistoriqueLivraisonUpdateRequestDto updateRequestDto;
    private HistoriqueLivraisonResponseDto responseDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        colis = new Colis();
        colis.setId("colis-123");

        historique = new HistoriqueLivraison();
        historique.setId("historique-123");
        historique.setColis(colis);
        historique.setStatut(StatutColis.EN_TRANSIT);
        historique.setDateChangement(now);
        historique.setCommentaire("Colis en transit");
        historique.setDateCreation(now);

        createRequestDto = new HistoriqueLivraisonCreateRequestDto();
        createRequestDto.setColisId("colis-123");
        createRequestDto.setStatut(StatutColis.EN_TRANSIT);
        createRequestDto.setDateChangement(now);
        createRequestDto.setCommentaire("Test");

        updateRequestDto = new HistoriqueLivraisonUpdateRequestDto();
        updateRequestDto.setId("historique-123");
        updateRequestDto.setStatut(StatutColis.LIVRE);
        updateRequestDto.setDateChangement(now);
        updateRequestDto.setCommentaire("Livré");

        responseDto = new HistoriqueLivraisonResponseDto();
        responseDto.setId("historique-123");
        responseDto.setColisId("colis-123");
        responseDto.setStatut(StatutColis.EN_TRANSIT);
        responseDto.setDateChangement(now);
        responseDto.setCommentaire("Colis en transit");
        responseDto.setDateCreation(now);
    }

    @Test
    @DisplayName("GetById - Should get historique by id successfully")
    void testGetById_Success() {
        when(historiqueLivraisonRepository.findById("historique-123")).thenReturn(Optional.of(historique));
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        HistoriqueLivraisonResponseDto result = historiqueLivraisonService.getById("historique-123");

        assertThat(result.getId()).isEqualTo("historique-123");
        assertThat(result.getColisId()).isEqualTo("colis-123");
        assertThat(result.getStatut()).isEqualTo(StatutColis.EN_TRANSIT);

        verify(historiqueLivraisonRepository, times(1)).findById("historique-123");
        verify(smartLogiMapper, times(1)).toResponseDto(historique);
    }

    @Test
    @DisplayName("GetById - Should throw exception when historique not found")
    void testGetById_NotFound() {
        when(historiqueLivraisonRepository.findById("historique-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historiqueLivraisonService.getById("historique-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Historique non trouvé");

        verify(historiqueLivraisonRepository, times(1)).findById("historique-999");
        verify(smartLogiMapper, never()).toResponseDto((HistoriqueLivraison) any());
    }

    @Test
    @DisplayName("GetAll - Should get all historiques with pagination")
    void testGetAllWithPagination_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        Page<HistoriqueLivraison> historiquePage = new PageImpl<>(historiques, pageable, historiques.size());

        when(historiqueLivraisonRepository.findAll(any(Pageable.class))).thenReturn(historiquePage);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        Page<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll(pageable);

        assertThat(result).isNotNull()
                .hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(historiqueLivraisonRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAll - Should return empty page when no historiques exist")
    void testGetAllWithPagination_EmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<HistoriqueLivraison> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(historiqueLivraisonRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll(pageable);

        assertThat(result).isNotNull()
                .isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(historiqueLivraisonRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAll - Should handle multiple pages")
    void testGetAllWithPagination_MultiplePages() {
        Pageable pageable = PageRequest.of(1, 5);
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        Page<HistoriqueLivraison> historiquePage = new PageImpl<>(historiques, pageable, 25);

        when(historiqueLivraisonRepository.findAll(any(Pageable.class))).thenReturn(historiquePage);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        Page<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll(pageable);

        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        verify(historiqueLivraisonRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAll - Should get all historiques without pagination")
    void testGetAllList_Success() {
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        when(historiqueLivraisonRepository.findAll()).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll();

        assertThat(result).isNotNull()
                .hasSize(1);
        verify(historiqueLivraisonRepository, times(1)).findAll();
        verify(smartLogiMapper, times(1)).toResponseDto(historique);
    }

    @Test
    @DisplayName("GetAll - Should return empty list when no historiques exist")
    void testGetAllList_EmptyList() {
        when(historiqueLivraisonRepository.findAll()).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll();

        assertThat(result).isNotNull()
                .isEmpty();
        verify(historiqueLivraisonRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAll - Should handle multiple historiques")
    void testGetAllList_MultipleHistoriques() {
        HistoriqueLivraison historique2 = new HistoriqueLivraison();
        historique2.setId("historique-456");
        historique2.setStatut(StatutColis.LIVRE);

        HistoriqueLivraisonResponseDto dto2 = new HistoriqueLivraisonResponseDto();
        dto2.setId("historique-456");
        dto2.setStatut(StatutColis.LIVRE);

        List<HistoriqueLivraison> historiques = Arrays.asList(historique, historique2);
        when(historiqueLivraisonRepository.findAll()).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(historique)).thenReturn(responseDto);
        when(smartLogiMapper.toResponseDto(historique2)).thenReturn(dto2);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getAll();

        assertThat(result).hasSize(2);
        verify(smartLogiMapper, times(2)).toResponseDto(any(HistoriqueLivraison.class));
    }

    @Test
    @DisplayName("GetByColis - Should get historiques by colis successfully")
    void testGetByColis_Success() {
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColis(colis)).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColis("colis-123");

        assertThat(result).isNotNull()
                .hasSize(1);
        verify(colisRepository, times(1)).findById("colis-123");
        verify(historiqueLivraisonRepository, times(1)).findByColis(colis);
    }

    @Test
    @DisplayName("GetByColis - Should throw exception when colis not found")
    void testGetByColis_ColisNotFound() {
        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historiqueLivraisonService.getByColis("colis-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Colis non trouvé");

        verify(colisRepository, times(1)).findById("colis-999");
        verify(historiqueLivraisonRepository, never()).findByColis(any());
    }

    @Test
    @DisplayName("GetByColis - Should return empty list when colis has no historiques")
    void testGetByColis_EmptyList() {
        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColis(colis)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColis("colis-123");

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1)).findByColis(colis);
    }

    @Test
    @DisplayName("GetByColisOrderByDateAsc - Should get historiques ordered ascending")
    void testGetByColisOrderByDateAsc_Success() {
        HistoriqueLivraison historique1 = new HistoriqueLivraison();
        historique1.setDateChangement(now.minusDays(2));

        HistoriqueLivraison historique2 = new HistoriqueLivraison();
        historique2.setDateChangement(now.minusDays(1));

        List<HistoriqueLivraison> historiques = Arrays.asList(historique1, historique2);

        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColisOrderByDateChangementAsc(colis)).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColisOrderByDateAsc("colis-123");

        assertThat(result).isNotNull()
                .hasSize(2);
        verify(colisRepository, times(1)).findById("colis-123");
        verify(historiqueLivraisonRepository, times(1)).findByColisOrderByDateChangementAsc(colis);
    }

    @Test
    @DisplayName("GetByColisOrderByDateAsc - Should throw exception when colis not found")
    void testGetByColisOrderByDateAsc_ColisNotFound() {
        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historiqueLivraisonService.getByColisOrderByDateAsc("colis-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Colis non trouvé");

        verify(colisRepository, times(1)).findById("colis-999");
        verify(historiqueLivraisonRepository, never()).findByColisOrderByDateChangementAsc(any());
    }

    @Test
    @DisplayName("GetByColisOrderByDateAsc - Should return empty list")
    void testGetByColisOrderByDateAsc_EmptyList() {
        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColisOrderByDateChangementAsc(colis)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColisOrderByDateAsc("colis-123");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("GetByColisOrderByDateDesc - Should get historiques ordered descending")
    void testGetByColisOrderByDateDesc_Success() {
        HistoriqueLivraison historique1 = new HistoriqueLivraison();
        historique1.setDateChangement(now);

        HistoriqueLivraison historique2 = new HistoriqueLivraison();
        historique2.setDateChangement(now.minusDays(1));

        List<HistoriqueLivraison> historiques = Arrays.asList(historique1, historique2);

        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColisOrderByDateChangementDesc(colis)).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColisOrderByDateDesc("colis-123");

        assertThat(result).isNotNull()
                .hasSize(2);
        verify(colisRepository, times(1)).findById("colis-123");
        verify(historiqueLivraisonRepository, times(1)).findByColisOrderByDateChangementDesc(colis);
    }

    @Test
    @DisplayName("GetByColisOrderByDateDesc - Should throw exception when colis not found")
    void testGetByColisOrderByDateDesc_ColisNotFound() {
        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historiqueLivraisonService.getByColisOrderByDateDesc("colis-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Colis non trouvé");

        verify(colisRepository, times(1)).findById("colis-999");
        verify(historiqueLivraisonRepository, never()).findByColisOrderByDateChangementDesc(any());
    }

    @Test
    @DisplayName("GetByColisOrderByDateDesc - Should return empty list")
    void testGetByColisOrderByDateDesc_EmptyList() {
        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
        when(historiqueLivraisonRepository.findByColisOrderByDateChangementDesc(colis)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByColisOrderByDateDesc("colis-123");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("GetByStatut - Should get historiques by statut CREE")
    void testGetByStatut_CREE() {
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        when(historiqueLivraisonRepository.findByStatut(StatutColis.CREE)).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByStatut(StatutColis.CREE);

        assertThat(result).isNotNull()
                .hasSize(1);
        verify(historiqueLivraisonRepository, times(1)).findByStatut(StatutColis.CREE);
    }

    @Test
    @DisplayName("GetByStatut - Should get historiques by statut COLLECTE")
    void testGetByStatut_COLLECTE() {
        when(historiqueLivraisonRepository.findByStatut(StatutColis.COLLECTE)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByStatut(StatutColis.COLLECTE);

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1)).findByStatut(StatutColis.COLLECTE);
    }

    @Test
    @DisplayName("GetByStatut - Should get historiques by statut EN_STOCK")
    void testGetByStatut_EN_STOCK() {
        when(historiqueLivraisonRepository.findByStatut(StatutColis.EN_STOCK)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByStatut(StatutColis.EN_STOCK);

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1)).findByStatut(StatutColis.EN_STOCK);
    }

    @Test
    @DisplayName("GetByStatut - Should get historiques by statut EN_TRANSIT")
    void testGetByStatut_EN_TRANSIT() {
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        when(historiqueLivraisonRepository.findByStatut(StatutColis.EN_TRANSIT)).thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByStatut(StatutColis.EN_TRANSIT);

        assertThat(result).hasSize(1);
        verify(historiqueLivraisonRepository, times(1)).findByStatut(StatutColis.EN_TRANSIT);
    }

    @Test
    @DisplayName("GetByStatut - Should get historiques by statut LIVRE")
    void testGetByStatut_LIVRE() {
        when(historiqueLivraisonRepository.findByStatut(StatutColis.LIVRE)).thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService.getByStatut(StatutColis.LIVRE);

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1)).findByStatut(StatutColis.LIVRE);
    }

    @Test
    @DisplayName("GetByColisIdAndStatut - Should get historiques by colis and statut")
    void testGetByColisIdAndStatut_Success() {
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);
        when(historiqueLivraisonRepository.findByColisIdAndStatut("colis-123", StatutColis.EN_TRANSIT))
                .thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByColisIdAndStatut("colis-123", StatutColis.EN_TRANSIT);

        assertThat(result).isNotNull()
                .hasSize(1);
        verify(historiqueLivraisonRepository, times(1))
                .findByColisIdAndStatut("colis-123", StatutColis.EN_TRANSIT);
    }

    @Test
    @DisplayName("GetByColisIdAndStatut - Should return empty list when no match")
    void testGetByColisIdAndStatut_EmptyList() {
        when(historiqueLivraisonRepository.findByColisIdAndStatut("colis-123", StatutColis.LIVRE))
                .thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByColisIdAndStatut("colis-123", StatutColis.LIVRE);

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1))
                .findByColisIdAndStatut("colis-123", StatutColis.LIVRE);
    }

    @Test
    @DisplayName("GetByColisIdAndStatut - Should handle non-existent colis")
    void testGetByColisIdAndStatut_NonExistentColis() {
        when(historiqueLivraisonRepository.findByColisIdAndStatut("colis-999", StatutColis.EN_TRANSIT))
                .thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByColisIdAndStatut("colis-999", StatutColis.EN_TRANSIT);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should get historiques in date range")
    void testGetByDateChangementBetween_Success() {
        LocalDateTime startDate = now.minusDays(7);
        LocalDateTime endDate = now;
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);

        when(historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate))
                .thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(startDate, endDate);

        assertThat(result).isNotNull()
                .hasSize(1);
        verify(historiqueLivraisonRepository, times(1))
                .findByDateChangementBetween(startDate, endDate);
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should return empty list when no historiques in range")
    void testGetByDateChangementBetween_EmptyList() {
        LocalDateTime startDate = now.minusDays(30);
        LocalDateTime endDate = now.minusDays(20);

        when(historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate))
                .thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(startDate, endDate);

        assertThat(result).isEmpty();
        verify(historiqueLivraisonRepository, times(1))
                .findByDateChangementBetween(startDate, endDate);
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should handle same start and end date")
    void testGetByDateChangementBetween_SameDate() {
        LocalDateTime sameDate = now;
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);

        when(historiqueLivraisonRepository.findByDateChangementBetween(sameDate, sameDate))
                .thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(sameDate, sameDate);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should handle large date range")
    void testGetByDateChangementBetween_LargeDateRange() {
        LocalDateTime startDate = now.minusYears(1);
        LocalDateTime endDate = now.plusDays(1);
        List<HistoriqueLivraison> historiques = Arrays.asList(historique);

        when(historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate))
                .thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(startDate, endDate);

        assertThat(result).hasSize(1);
        verify(historiqueLivraisonRepository, times(1))
                .findByDateChangementBetween(startDate, endDate);
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should handle future dates")
    void testGetByDateChangementBetween_FutureDates() {
        LocalDateTime startDate = now.plusDays(1);
        LocalDateTime endDate = now.plusDays(7);

        when(historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate))
                .thenReturn(new ArrayList<>());

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(startDate, endDate);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("GetByDateChangementBetween - Should handle multiple results")
    void testGetByDateChangementBetween_MultipleResults() {
        LocalDateTime startDate = now.minusDays(7);
        LocalDateTime endDate = now;

        HistoriqueLivraison hist1 = new HistoriqueLivraison();
        hist1.setId("hist-1");
        hist1.setDateChangement(now.minusDays(5));

        HistoriqueLivraison hist2 = new HistoriqueLivraison();
        hist2.setId("hist-2");
        hist2.setDateChangement(now.minusDays(3));

        HistoriqueLivraison hist3 = new HistoriqueLivraison();
        hist3.setId("hist-3");
        hist3.setDateChangement(now.minusDays(1));

        List<HistoriqueLivraison> historiques = Arrays.asList(hist1, hist2, hist3);

        when(historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate))
                .thenReturn(historiques);
        when(smartLogiMapper.toResponseDto(any(HistoriqueLivraison.class))).thenReturn(responseDto);

        List<HistoriqueLivraisonResponseDto> result = historiqueLivraisonService
                .getByDateChangementBetween(startDate, endDate);

        assertThat(result).hasSize(3);
        verify(smartLogiMapper, times(3)).toResponseDto(any(HistoriqueLivraison.class));
    }
}
