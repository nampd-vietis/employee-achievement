package dev.vietis.nampd.employee.achievement.model.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SearchResultResponse {
    private Long resultId;
    private String capturePath;
    private String suggestions;
    private boolean isMatch;
    private LocalDate searchDate;

    // Thông tin từ SearchKeyword
    private String searchKeyword;
    private String displayKeyword;
    private String platform;
    private String device;
    private String matchPattern;

    //String foundText ->

    private List<Suggestion> suggestionList;

    @Getter
    @Setter
    public static class Suggestion {
        private String suggestText;
        private boolean isMatch;
    }

    public SearchResultResponse(SearchResult result, SearchKeyword keyword, List<Suggestion> suggestionList) {
        this.resultId = result.getId();
        this.capturePath = result.getCapturePath();
        this.suggestions = result.getSuggestions();
        this.isMatch = result.isMatch();
        this.searchDate = result.getSearchDate();

        this.searchKeyword = keyword.getSearchKeyword();
        this.displayKeyword = keyword.getDisplayKeyword();
        this.platform = keyword.getPlatform().toString();
        this.device = keyword.getDevice().toString();
        this.matchPattern = keyword.getMatchPattern().toString();

        this.suggestionList = suggestionList;
    }
}

