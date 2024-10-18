package dev.vietis.nampd.employee.achievement.repository.search;

import dev.vietis.nampd.employee.achievement.model.search.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
}
