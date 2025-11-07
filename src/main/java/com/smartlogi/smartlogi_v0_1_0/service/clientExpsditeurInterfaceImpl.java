    package com.smartlogi.smartlogi_v0_1_0.service;

    import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
    import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
    import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.ClientExpediteurAdvancedResponseDto;
    import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
    import com.smartlogi.smartlogi_v0_1_0.entity.ClientExpediteur;
    import com.smartlogi.smartlogi_v0_1_0.exception.ResourceNotFoundException;
    import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
    import com.smartlogi.smartlogi_v0_1_0.repository.ClientExpediteurRepository;
    import com.smartlogi.smartlogi_v0_1_0.service.interfaces.clientExpsditeurInterface;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
        public class clientExpsditeurInterfaceImpl implements clientExpsditeurInterface{

        private final ClientExpediteurRepository clientExpediteurRepository;
        private final SmartLogiMapper smartLogiMapper;

        @Override
        public ClientExpediteurSimpleResponseDto create(ClientExpediteurCreateRequestDto requestDto) {
            ClientExpediteur client = smartLogiMapper.toEntity(requestDto);
            ClientExpediteur savedClient = clientExpediteurRepository.save(client);
            return smartLogiMapper.toSimpleResponseDto(savedClient);
        }

        @Override
        public ClientExpediteurSimpleResponseDto update(String id, ClientExpediteurUpdateRequestDto requestDto) {
            ClientExpediteur client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            smartLogiMapper.updateEntityFromDto(requestDto, client);
            ClientExpediteur updatedClient = clientExpediteurRepository.save(client);
            return smartLogiMapper.toSimpleResponseDto(updatedClient);
        }

        @Override
        public ClientExpediteurSimpleResponseDto getById(String id) {
            ClientExpediteur client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            return smartLogiMapper.toSimpleResponseDto(client);
        }
    //
    //    @Override
    //    public ClientExpediteurAdvancedResponseDto getByIdWithColis(String id) {
    //        ClientExpediteur client = clientExpediteurRepository.findByIdWithColis(id)
    //                .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
    //        return smartLogiMapper.toAdvancedResponseDto(client);
    //    }

        @Override
        public Page<ClientExpediteurSimpleResponseDto> getAll(Pageable pageable) {
            return clientExpediteurRepository.findAll(pageable)
                    .map(smartLogiMapper::toSimpleResponseDto);
        }

        @Override
        public List<ClientExpediteurSimpleResponseDto> searchByNom(String nom) {
            return clientExpediteurRepository.findByNomContainingIgnoreCase(nom)
                    .stream()
                    .map(smartLogiMapper::toSimpleResponseDto)
                    .collect(Collectors.toList());
        }

        @Override
        public ClientExpediteurSimpleResponseDto findByKeyWord(String keyword) {

            ClientExpediteur clientExpediteur = clientExpediteurRepository.searchByKeyword(keyword);
            if (clientExpediteur == null) {
                throw new ResourceNotFoundException("le Destinataire pas trouve");
            }
            ClientExpediteurSimpleResponseDto dto = smartLogiMapper.toSimpleResponseDto(clientExpediteur);
            return dto;
        }

        @Override
        public void delete(String id) {
            ClientExpediteur client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            clientExpediteurRepository.delete(client);
        }

        @Override
        public boolean existsById(String id) {
            return clientExpediteurRepository.existsById(id);
        }
    }