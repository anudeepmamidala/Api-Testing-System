package com.anudeep.probeapi.controller;

import com.anudeep.probeapi.dto.CollectionDTO;
import com.anudeep.probeapi.dto.NamedRequestDTO;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage")
@Slf4j
public class StorageController {

    @Autowired
    private StorageService storageService;

    // Saved Requests
    @PostMapping("/saved-requests")
    public ResponseEntity<NamedRequestDTO> saveRequest(
            @RequestBody NamedRequestDTO request,
            @RequestAttribute("user") User user) {
        log.info("Saving request '{}' for user {}", request.getName(), user.getUsername());
        NamedRequestDTO saved = storageService.saveRequest(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/saved-requests")
    public ResponseEntity<List<NamedRequestDTO>> getSavedRequests(@RequestAttribute("user") User user) {
        List<NamedRequestDTO> requests = storageService.getSavedRequests(user);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/saved-requests/{id}")
    public ResponseEntity<NamedRequestDTO> getSavedRequest(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        NamedRequestDTO request = storageService.getSavedRequest(id, user);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/saved-requests/{id}")
    public ResponseEntity<NamedRequestDTO> updateSavedRequest(
            @PathVariable Long id,
            @RequestBody NamedRequestDTO request,
            @RequestAttribute("user") User user) {
        NamedRequestDTO updated = storageService.updateSavedRequest(id, request, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/saved-requests/{id}")
    public ResponseEntity<Void> deleteSavedRequest(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        storageService.deleteSavedRequest(id, user);
        return ResponseEntity.noContent().build();
    }

    // Collections
    @PostMapping("/collections")
    public ResponseEntity<CollectionDTO> createCollection(
            @RequestBody CollectionDTO collection,
            @RequestAttribute("user") User user) {
        log.info("Creating collection '{}' for user {}", collection.getName(), user.getUsername());
        CollectionDTO created = storageService.createCollection(collection, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/collections")
    public ResponseEntity<List<CollectionDTO>> getCollections(@RequestAttribute("user") User user) {
        List<CollectionDTO> collections = storageService.getCollections(user);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<CollectionDTO> getCollection(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        CollectionDTO collection = storageService.getCollection(id, user);
        return ResponseEntity.ok(collection);
    }

    @PutMapping("/collections/{id}")
    public ResponseEntity<CollectionDTO> updateCollection(
            @PathVariable Long id,
            @RequestBody CollectionDTO collection,
            @RequestAttribute("user") User user) {
        CollectionDTO updated = storageService.updateCollection(id, collection, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/collections/{id}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        storageService.deleteCollection(id, user);
        return ResponseEntity.noContent().build();
    }

}
