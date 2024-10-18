package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import dev.vietis.nampd.employee.achievement.model.search.SearchResult;
import dev.vietis.nampd.employee.achievement.model.search.SearchResultResponse;
import dev.vietis.nampd.employee.achievement.repository.search.SearchKeywordRepository;
import dev.vietis.nampd.employee.achievement.repository.search.SearchResultRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Service
public class SearchService {
    private final SearchKeywordRepository searchKeywordRepository;
    private final SearchResultRepository searchResultRepository;

    public SearchService(SearchKeywordRepository searchKeywordRepository, SearchResultRepository searchResultRepository) {
        this.searchKeywordRepository = searchKeywordRepository;
        this.searchResultRepository = searchResultRepository;
    }

    public WebDriver setupDriver(SearchKeyword.Device device) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (device == SearchKeyword.Device.SMARTPHONE) {
            // Giả lập mobile
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Pixel 2");
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }

        return new ChromeDriver(options);
    }
    public void searchAndSaveSuggestResults(SearchKeyword searchKeyword) {
        WebDriver driver = setupDriver(searchKeyword.getDevice());
        try {
            String url = "";
            By searchBoxSelector;
            String platform = searchKeyword.getPlatform().toString();
            String keyword = searchKeyword.getSearchKeyword();
            String displayKeyword = searchKeyword.getDisplayKeyword();

            // Xác định URL và selector cho Google và Yahoo
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

            // Mở trang tìm kiếm
            driver.get(url);
            WebElement searchBox = driver.findElement(searchBoxSelector);
            searchBox.sendKeys(keyword);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox'] li")));
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul[role='listbox'] li")));

            List<WebElement> suggestions = driver.findElements(By.cssSelector("ul[role='listbox'] li"));

            String allSuggests = joinSuggestions(suggestions);
            boolean isMatch = false;

            List<SearchResultResponse.Suggestion> suggestionList = new ArrayList<>();

            for (WebElement suggestion : suggestions) {
                String suggestText = suggestion.getText();

                SearchResultResponse.Suggestion suggestionObj = new SearchResultResponse.Suggestion();
                suggestionObj.setSuggestText(suggestText);

                boolean match = checkMatchPattern(suggestText, displayKeyword, searchKeyword.getMatchPattern());
                suggestionObj.setMatch(match);

                if (match) {
                    isMatch = true;
                }

                suggestionList.add(suggestionObj);

                System.out.println(suggestionList);
            }

            String fileName = captureScreenshot(driver, keyword, platform, searchKeyword.getDevice());

            saveSearchResult(allSuggests, isMatch, fileName, searchKeyword, suggestionList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public boolean checkMatchPattern(String suggestText, String displayKeyword, SearchKeyword.MatchPattern pattern) {
        if (pattern == SearchKeyword.MatchPattern.PARTIAL && suggestText.toLowerCase().contains(displayKeyword.toLowerCase())) {
            return true;
        } else return pattern == SearchKeyword.MatchPattern.UNANIMOUS && suggestText.equalsIgnoreCase(displayKeyword);
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

    public String joinSuggestions(List<WebElement> suggestions) {
        StringBuilder allSuggests = new StringBuilder();

        for (WebElement suggestion : suggestions) {
            String suggestText = suggestion.getText();
            allSuggests.append(suggestText).append("; ");
        }

        // Xóa "; " cuối cùng
        if (!allSuggests.isEmpty()) {
            allSuggests.setLength(allSuggests.length() - 2);
        }

        return allSuggests.toString();
    }

    public void saveSearchResult(String allSuggests, boolean isMatch, String capturePath, SearchKeyword searchKeyword, List<SearchResultResponse.Suggestion> suggestionList) {
        SearchResult result = new SearchResult();

        result.setSearchKeyword(searchKeyword);

        result.setCapturePath(capturePath);
        result.setSuggestions(allSuggests);
        result.setMatch(isMatch);
        result.setSearchDate(LocalDate.now());
        result.setSuggestionList(suggestionList);

        searchResultRepository.save(result);
    }

    public List<SearchResultResponse> getSearchResults() {
        List<SearchResult> results = searchResultRepository.findAll();
        List<SearchResultResponse> responseList = new ArrayList<>();

        for (SearchResult result : results) {
            SearchKeyword keyword = result.getSearchKeyword();

            // Lấy suggestionList từ result
            List<SearchResultResponse.Suggestion> suggestionList = result.getSuggestionList();

            // Tạo SearchResultResponse với suggestionList đã xử lý
            SearchResultResponse response = new SearchResultResponse(result, keyword, suggestionList);

            responseList.add(response);
        }
        return responseList;
    }
}

