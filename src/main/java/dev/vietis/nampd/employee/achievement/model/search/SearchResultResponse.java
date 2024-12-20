package dev.vietis.nampd.employee.achievement.model.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

