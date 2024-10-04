package dev.vietis.nampd.employee.achievement.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private Byte type;

    @NotNull
    @Lob
    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "date_recorded")
    private LocalDate dateRecorded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}