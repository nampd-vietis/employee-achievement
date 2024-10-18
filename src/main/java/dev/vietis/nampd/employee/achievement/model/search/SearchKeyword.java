package dev.vietis.nampd.employee.achievement.model.search;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String searchKeyword;
    private String displayKeyword;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Enumerated(EnumType.STRING)
    private MatchPattern matchPattern;
    @Enumerated(EnumType.STRING)
    private Device device;
    private String remarks;

    public enum MatchPattern {
        PARTIAL,
        UNANIMOUS
    }

    public enum Platform {
        GOOGLE,
        YAHOO
    }

    public enum Device {
        PC,
        SMARTPHONE
    }
}

