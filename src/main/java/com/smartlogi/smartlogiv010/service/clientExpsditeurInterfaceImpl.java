    package com.smartlogi.smartlogiv010.service;

    import com.smartlogi.security.dto.authDto.response.UserResponse;
    import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
    import com.smartlogi.smartlogiv010.entity.User;
    import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
    import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
    import com.smartlogi.smartlogiv010.repository.UserRepository;
    import com.smartlogi.smartlogiv010.service.interfaces.clientExpsditeurInterface;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;


    @Service
    @RequiredArgsConstructor
        public class clientExpsditeurInterfaceImpl implements clientExpsditeurInterface{

        private final UserRepository clientExpediteurRepository;
        private final SmartLogiMapper smartLogiMapper;



        @Override
        public UserResponse update(String id, ClientExpediteurUpdateRequestDto requestDto) {
            User client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            smartLogiMapper.updateEntityFromDto(requestDto, client);
            User updatedClient = clientExpediteurRepository.save(client);
            return smartLogiMapper.toSimpleResponseDto(updatedClient);
        }

        @Override
        public UserResponse getById(String id) {
            User client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            return smartLogiMapper.toSimpleResponseDto(client);
        }

        @Override
        public Page<UserResponse> getAll(Pageable pageable) {
            return clientExpediteurRepository.findAll(pageable)
                    .map(smartLogiMapper::toSimpleResponseDto);
        }

        @Override
        public UserResponse findByKeyWord(String keyword) {

            User clientExpediteur = clientExpediteurRepository.searchByKeyword(keyword);
            if (clientExpediteur == null) {
                throw new ResourceNotFoundException("le Destinataire pas trouve");
            }
            UserResponse dto = smartLogiMapper.toSimpleResponseDto(clientExpediteur);
            return dto;
        }

        @Override
        public void delete(String id) {
            User client = clientExpediteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
            clientExpediteurRepository.delete(client);
        }

        @Override
        public boolean existsById(String id) {
            return clientExpediteurRepository.existsById(id);
        }
    }