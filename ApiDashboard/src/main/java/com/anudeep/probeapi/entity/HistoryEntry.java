package com.anudeep.probeapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(name = "response_status")
    private Integer responseStatus;

    @Lob
@Column(name = "response_body", columnDefinition = "LONGTEXT")
private String responseBody;

@Lob
@Column(name = "response_headers", columnDefinition = "LONGTEXT")
private String responseHeaders;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
