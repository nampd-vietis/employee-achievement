package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import dev.vietis.nampd.employee.achievement.model.search.SearchResult;
import dev.vietis.nampd.employee.achievement.model.search.SearchResultResponse;
import dev.vietis.nampd.employee.achievement.repository.search.SearchKeywordRepository;
import dev.vietis.nampd.employee.achievement.repository.search.SearchResultRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class SearchService {
    private final WebDriverService webDriverService;
    private final SearchKeywordRepository searchKeywordRepository;
    private final SearchResultRepository searchResultRepository;

    public SearchService(WebDriverService webDriverService, SearchKeywordRepository searchKeywordRepository, SearchResultRepository searchResultRepository) {
        this.webDriverService = webDriverService;
        this.searchKeywordRepository = searchKeywordRepository;
        this.searchResultRepository = searchResultRepository;
    }

    public void searchAndSaveSuggestResults(SearchKeyword searchKeyword) {
        WebDriver driver = webDriverService.setupDriver(searchKeyword.getDevice());
        try {
            String url;
            By searchBoxSelector;

            switch (searchKeyword.getPlatform()) {
                case GOOGLE:
                    url = "https://www.google.com/";
                    searchBoxSelector = By.name("q");
                    break;
                case YAHOO:
                    url = "https://www.yahoo.com/";
                    searchBoxSelector = By.name("p");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported platform: " + searchKeyword.getPlatform());
            }

            driver.get(url);
            List<WebElement> suggestions = webDriverService.getSuggestions(driver, searchBoxSelector, searchKeyword.getSearchKeyword());
            String allSuggests = joinSuggestions(suggestions);

            boolean isMatch = false;
            for (WebElement suggestion : suggestions) {
                if (checkMatchPattern(suggestion.getText(), searchKeyword.getDisplayKeyword(), searchKeyword.getMatchPattern())) {
                    isMatch = true;
                    break;
                }
            }

            String fileName = captureScreenshot(driver, searchKeyword.getSearchKeyword(), searchKeyword.getPlatform().toString(), searchKeyword.getDevice());
            saveSearchResult(allSuggests, isMatch, fileName, searchKeyword);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriverService.closeDriver(driver);
        }
    }

    public boolean checkMatchPattern(String suggestText, String displayKeyword, SearchKeyword.MatchPattern pattern) {
        if (pattern == SearchKeyword.MatchPattern.PARTIAL && suggestText.toLowerCase().contains(displayKeyword.toLowerCase())) {
            return true;
        } else return pattern == SearchKeyword.MatchPattern.UNANIMOUS && suggestText.equalsIgnoreCase(displayKeyword);
    }

    public String joinSuggestions(List<WebElement> suggestions) {
        StringBuilder allSuggests = new StringBuilder();

        for (WebElement suggestion : suggestions) {
            String suggestText = suggestion.getText();
            allSuggests.append(suggestText).append("; ");
        }

        if (!allSuggests.isEmpty()) {
            allSuggests.setLength(allSuggests.length() - 2); // Xóa "; " cuối cùng
        }

        return allSuggests.toString();
    }

    public void saveSearchResult(String allSuggests, boolean isMatch, String capturePath, SearchKeyword searchKeyword) {
        Optional<SearchResult> existingResult = searchResultRepository.findBySearchKeywordAndSearchDate(searchKeyword, LocalDate.now());

        SearchResult result;

        if (existingResult.isPresent()) {
            result = existingResult.get();
            result.setSuggestions(allSuggests);
            result.setMatch(isMatch);
            result.setCapturePath(capturePath);
        } else {
            result = new SearchResult();
            result.setSearchKeyword(searchKeyword);
            result.setCapturePath(capturePath);
            result.setSuggestions(allSuggests);
            result.setMatch(isMatch);
            result.setSearchDate(LocalDate.now());
        }

        searchResultRepository.save(result);
    }

    public List<Map<String, Object>> getSearchResultsGroupedBySearchKeywordAndDate() {
        List<SearchResult> results = searchResultRepository.findAll();
        Map<SearchKeyword, Map<LocalDate, List<SearchResultResponse>>> groupedResults = new HashMap<>();

        for (SearchResult result : results) {
            SearchKeyword searchKeywordObj = result.getSearchKeyword();
            LocalDate searchDate = result.getSearchDate();

            SearchResultResponse response = new SearchResultResponse();
            response.setCapturePath(result.getCapturePath());
            response.setSearchDate(result.getSearchDate());
            response.setMatch(result.isMatch());

            List<SearchResultResponse.Suggestion> suggestionList = parseSuggestions(result.getSuggestions(), searchKeywordObj.getDisplayKeyword(), searchKeywordObj.getMatchPattern());
            response.setSuggestionList(suggestionList);

            groupedResults.computeIfAbsent(searchKeywordObj, k -> new HashMap<>())
                    .computeIfAbsent(searchDate, k -> new ArrayList<>())
                    .add(response);
        }

        List<Map<String, Object>> groupedResponseList = new ArrayList<>();
        for (Map.Entry<SearchKeyword, Map<LocalDate, List<SearchResultResponse>>> keywordEntry : groupedResults.entrySet()) {
            SearchKeyword searchKeywordObj = keywordEntry.getKey();
            Map<String, Object> keywordGroup = new HashMap<>();

            keywordGroup.put("searchKeyword", searchKeywordObj.getSearchKeyword());
            keywordGroup.put("displayKeyword", searchKeywordObj.getDisplayKeyword());
            keywordGroup.put("platform", searchKeywordObj.getPlatform().toString());
            keywordGroup.put("matchPattern", searchKeywordObj.getMatchPattern().toString());
            keywordGroup.put("device", searchKeywordObj.getDevice().toString());

            Map<String, List<SearchResultResponse>> resultsByDay = new HashMap<>();
            for (Map.Entry<LocalDate, List<SearchResultResponse>> dateEntry : keywordEntry.getValue().entrySet()) {
                resultsByDay.put(String.valueOf(dateEntry.getKey().getDayOfMonth()), dateEntry.getValue());
            }

            keywordGroup.put("resultsByDay", resultsByDay);
            groupedResponseList.add(keywordGroup);
        }

        return groupedResponseList;
    }

    private List<SearchResultResponse.Suggestion> parseSuggestions(String suggestions, String displayKeyword, SearchKeyword.MatchPattern pattern) {
        List<SearchResultResponse.Suggestion> suggestionList = new ArrayList<>();
        if (suggestions == null || suggestions.isEmpty()) {
            return suggestionList;
        }

        String[] suggestArray = suggestions.split(";");

        for (String suggest : suggestArray) {
            SearchResultResponse.Suggestion suggestion = new SearchResultResponse.Suggestion();
            suggestion.setSuggestText(suggest.trim());

            boolean isMatch = checkMatchPattern(suggest.trim(), displayKeyword, pattern);
            suggestion.setMatch(isMatch);

            suggestionList.add(suggestion);
        }

        return suggestionList;
    }
    public String captureScreenshot(WebDriver driver, String keyword, String platform, SearchKeyword.Device device) throws Exception {
        String destinationPath = "src/main/resources/static/capture/";
        String deviceType = device == SearchKeyword.Device.SMARTPHONE ? "mobile" : "pc";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = keyword + "_" + platform + "_" + deviceType + "_suggest_" + timestamp + ".png";

        String filePath = destinationPath + fileName;

        Screenshot screenshot = new AShot().takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "PNG", new File(filePath));

        return fileName;
    }
}
