package dev.vietis.nampd.employee.achievement.repository.search;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
}
