package dev.vietis.nampd.employee.achievement.model.search;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class SearchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_keyword_id", nullable = false)
    private SearchKeyword searchKeyword;
    private String capturePath;
    @Column(columnDefinition = "TEXT")
    private String suggestions;
    private boolean isMatch;
    private LocalDate searchDate;

    @Transient
    private List<SearchResultResponse.Suggestion> suggestionList;
}

