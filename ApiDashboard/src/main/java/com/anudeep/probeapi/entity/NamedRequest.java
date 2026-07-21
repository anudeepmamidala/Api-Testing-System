package com.anudeep.probeapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "named_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NamedRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_id", nullable = true)
    private Collection collection;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false, length = 2048)
    private String url;

    @Lob
    @Column(name = "request_body", columnDefinition = "LONGTEXT")
    private String requestBody;

    @Lob
    @Column(name = "request_headers", columnDefinition = "LONGTEXT")
    private String requestHeaders;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status")
    private String status; // SUCCESS, FAILED, PENDING

    @Column(name = "response_code")
    private Integer responseCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
