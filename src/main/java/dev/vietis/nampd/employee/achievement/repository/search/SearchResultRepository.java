package dev.vietis.nampd.employee.achievement.repository.search;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import dev.vietis.nampd.employee.achievement.model.search.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    @Query("SELECT sr FROM SearchResult sr WHERE sr.searchKeyword = :searchKeyword AND sr.searchDate = :searchDate")
    Optional<SearchResult> findBySearchKeywordAndSearchDate(@Param("searchKeyword") SearchKeyword searchKeyword,
                                                                       @Param("searchDate") LocalDate searchDate);
}
