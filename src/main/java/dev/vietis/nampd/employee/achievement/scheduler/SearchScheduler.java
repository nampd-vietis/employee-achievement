package dev.vietis.nampd.employee.achievement.scheduler;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import dev.vietis.nampd.employee.achievement.repository.search.SearchKeywordRepository;
import dev.vietis.nampd.employee.achievement.service.SearchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchScheduler {
    private final SearchService searchService;
    private final SearchKeywordRepository searchKeywordRepository;

    public SearchScheduler(SearchService searchService, SearchKeywordRepository searchKeywordRepository) {
        this.searchService = searchService;
        this.searchKeywordRepository = searchKeywordRepository;
    }

    // Lên lịch chạy mỗi ngày lúc 9h sáng
//    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(cron = "0 */3 * * * *")
    public void scheduleDailySearch()
    {
        System.out.println("Scheduled Search Running...");

        // Lấy tất cả từ khóa từ cơ sở dữ liệu
        List<SearchKeyword> keywords = searchKeywordRepository.findAll();

        if (keywords.isEmpty()) {
            System.out.println("No keywords found in the database.");
        }

        for (SearchKeyword keyword : keywords) {
            try {
                searchService.searchAndSaveSuggestResults(keyword);
                System.out.println("Search completed for keyword: " + keyword.getSearchKeyword());
            } catch (Exception e) {
                System.out.println("Failed to perform search for keyword: " + keyword.getSearchKeyword());
                e.printStackTrace();
            }
        }
    }
}

