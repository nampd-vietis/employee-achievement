package dev.vietis.nampd.employee.achievement.model.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class SearchResultResponse {
    private String capturePath;
    private boolean isMatch;
    private LocalDate searchDate;

    private String searchKeyword;
    private String displayKeyword;
    private String platform;
    private String device;
    private String matchPattern;

    private List<Suggestion> suggestionList;

    @Getter
    @Setter
    public static class Suggestion {
        private String suggestText;
        private boolean isMatch;
    }
}

