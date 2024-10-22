package dev.vietis.nampd.employee.achievement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import dev.vietis.nampd.employee.achievement.service.SearchService;
import dev.vietis.nampd.employee.achievement.repository.search.SearchKeywordRepository;
import dev.vietis.nampd.employee.achievement.repository.search.SearchResultRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    private final SearchKeywordRepository searchKeywordRepository;
    private final SearchResultRepository searchResultRepository;
    private final SearchService searchService;

    public SearchController(SearchKeywordRepository searchKeywordRepository, SearchResultRepository searchResultRepository, SearchService searchService) {
        this.searchKeywordRepository = searchKeywordRepository;
        this.searchResultRepository = searchResultRepository;
        this.searchService = searchService;
    }


    @GetMapping("/new")
    public String showAddKeywordForm(Model model) {
        model.addAttribute("keyword", new SearchKeyword());
        return "suggest/add_keyword";
    }

    @PostMapping
    public String addKeyword(@ModelAttribute("keyword") SearchKeyword keyword) {
        searchKeywordRepository.save(keyword);
        return "redirect:/search/detail";
    }

    @PostMapping("/perform")
    public String performSearch(@RequestParam Long keywordId) {
        try {
            SearchKeyword keyword = searchKeywordRepository.findById(keywordId)
                    .orElseThrow(() -> new IllegalArgumentException("Keyword not found"));

            searchService.searchAndSaveSuggestResults(keyword);
            return "redirect:/search";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to perform search for this keyword";
        }

    }

    @GetMapping
    public String showSearchResults(Model model) {
        List<Map<String, Object>> groupedResults = searchService.getSearchResultsGroupedBySearchKeywordAndDate();
        model.addAttribute("groupedResults", groupedResults);
        return "suggest/suggest_results";
    }

    @GetMapping("/detail")
    public String showSearchResultsDetail(Model model) {
        List<Map<String, Object>> groupedResults = searchService.getSearchResultsGroupedBySearchKeywordAndDate();
        model.addAttribute("groupedResults", groupedResults);
        return "suggest/suggest_results_detail";
    }

    @GetMapping("/detail-json")
    @ResponseBody
    public ResponseEntity<?> showSearchResultsDetailJson() {
        List<Map<String, Object>> groupedResults = searchService.getSearchResultsGroupedBySearchKeywordAndDate();
        return ResponseEntity.ok(groupedResults);
    }
}

