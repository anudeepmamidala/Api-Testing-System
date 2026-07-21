package com.anudeep.probeapi.service;

import com.anudeep.probeapi.dto.CollectionDTO;
import com.anudeep.probeapi.dto.NamedRequestDTO;
import com.anudeep.probeapi.entity.Collection;
import com.anudeep.probeapi.entity.NamedRequest;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.exception.CustomException;
import com.anudeep.probeapi.repository.CollectionRepository;
import com.anudeep.probeapi.repository.NamedRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageService {

    @Autowired
    private NamedRequestRepository namedRequestRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Named Request Methods
    public NamedRequestDTO saveRequest(NamedRequestDTO dto, User user) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new CustomException("Request name cannot be empty", "INVALID_INPUT", 400);
        }

        Collection collection = null;
        if (dto.getCollectionId() != null) {
            collection = collectionRepository.findByIdAndUser(dto.getCollectionId(), user)
                    .orElseThrow(() -> new CustomException("Collection not found", "NOT_FOUND", 404));
        }

        NamedRequest request = NamedRequest.builder()
                .user(user)
                .collection(collection)
                .name(dto.getName())
                .method(dto.getMethod())
                .url(dto.getUrl())
                .requestBody(dto.getBody())
                .requestHeaders(serializeMap(dto.getHeaders()))
                .description(dto.getDescription())
                .status(dto.getStatus())
                .responseCode(dto.getResponseCode())
                .build();

        request = namedRequestRepository.save(request);
        log.info("Saved request '{}' for user {} in collection {}", 
            dto.getName(), user.getUsername(), 
            collection != null ? collection.getName() : "None");

        return mapNamedRequestToDTO(request);
    }

    public List<NamedRequestDTO> getSavedRequests(User user) {
        List<NamedRequest> requests = namedRequestRepository.findByUserOrderByCreatedAtDesc(user);
        return requests.stream()
                .map(this::mapNamedRequestToDTO)
                .collect(Collectors.toList());
    }

    public NamedRequestDTO getSavedRequest(Long id, User user) {
        NamedRequest request = namedRequestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Request not found", "NOT_FOUND", 404));
        return mapNamedRequestToDTO(request);
    }

    public NamedRequestDTO updateSavedRequest(Long id, NamedRequestDTO dto, User user) {
        NamedRequest request = namedRequestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Request not found", "NOT_FOUND", 404));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            request.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            request.setDescription(dto.getDescription());
        }
        if (dto.getMethod() != null && !dto.getMethod().isEmpty()) {
            request.setMethod(dto.getMethod());
        }
        if (dto.getUrl() != null && !dto.getUrl().isEmpty()) {
            request.setUrl(dto.getUrl());
        }
        if (dto.getBody() != null) {
            request.setRequestBody(dto.getBody());
        }
        if (dto.getHeaders() != null) {
            request.setRequestHeaders(serializeMap(dto.getHeaders()));
        }
        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }
        if (dto.getResponseCode() != null) {
            request.setResponseCode(dto.getResponseCode());
        }

        if (dto.getCollectionId() != null) {
            Collection collection = collectionRepository.findByIdAndUser(dto.getCollectionId(), user)
                    .orElseThrow(() -> new CustomException("Collection not found", "NOT_FOUND", 404));
            request.setCollection(collection);
        }

        request = namedRequestRepository.save(request);
        log.info("Updated request '{}' for user {}", request.getName(), user.getUsername());
        return mapNamedRequestToDTO(request);
    }

    public void deleteSavedRequest(Long id, User user) {
        NamedRequest request = namedRequestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Request not found", "NOT_FOUND", 404));
        
        namedRequestRepository.delete(request);
        log.info("Deleted request '{}' for user {}", request.getName(), user.getUsername());
    }

    // Collection Methods
    public CollectionDTO createCollection(CollectionDTO dto, User user) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new CustomException("Collection name cannot be empty", "INVALID_INPUT", 400);
        }

        Collection collection = Collection.builder()
                .user(user)
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        collection = collectionRepository.save(collection);
        log.info("Created collection '{}' for user {}", dto.getName(), user.getUsername());

        return mapCollectionToDTO(collection);
    }

    public List<CollectionDTO> getCollections(User user) {
        List<Collection> collections = collectionRepository.findByUserOrderByCreatedAtDesc(user);
        return collections.stream()
                .map(this::mapCollectionToDTO)
                .collect(Collectors.toList());
    }

    public CollectionDTO getCollection(Long id, User user) {
        Collection collection = collectionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Collection not found", "NOT_FOUND", 404));
        return mapCollectionToDTO(collection);
    }

    public CollectionDTO updateCollection(Long id, CollectionDTO dto, User user) {
        Collection collection = collectionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Collection not found", "NOT_FOUND", 404));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            collection.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            collection.setDescription(dto.getDescription());
        }

        collection = collectionRepository.save(collection);
        log.info("Updated collection '{}' for user {}", collection.getName(), user.getUsername());

        return mapCollectionToDTO(collection);
    }

    public void deleteCollection(Long id, User user) {
        Collection collection = collectionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("Collection not found", "NOT_FOUND", 404));
        
        collectionRepository.delete(collection);
        log.info("Deleted collection '{}' for user {}", collection.getName(), user.getUsername());
    }

    // Helper methods
    private NamedRequestDTO mapNamedRequestToDTO(NamedRequest entity) {
        return NamedRequestDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .method(entity.getMethod())
                .url(entity.getUrl())
                .body(entity.getRequestBody())
                .headers(deserializeMap(entity.getRequestHeaders()))
                .collectionId(entity.getCollection() != null ? entity.getCollection().getId() : null)
                .status(entity.getStatus())
                .responseCode(entity.getResponseCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CollectionDTO mapCollectionToDTO(Collection entity) {
        Long requestCount = 0L;
        List<NamedRequestDTO> requestDTOs = new ArrayList<>();
        
        if (entity.getRequests() != null) {
            requestCount = (long) entity.getRequests().size();
            requestDTOs = entity.getRequests().stream()
                    .map(this::mapNamedRequestToDTO)
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return CollectionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .requestCount(requestCount)
                .requests(requestDTOs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private String serializeMap(Map<String, String> map) {
        try {
            if (map == null) {
                return "{}";
            }
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("Error serializing map: {}", e.getMessage());
            return "{}";
        }
    }

    private Map<String, String> deserializeMap(String json) {
        try {
            if (json == null || json.isEmpty() || json.equals("{}")) {
                return new java.util.HashMap<>();
            }
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Error deserializing map: {}", e.getMessage());
            return new java.util.HashMap<>();
        }
    }

}
