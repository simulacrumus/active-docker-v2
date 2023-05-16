package com.example.active.data.entity.translation;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "language")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language{

    @Id
    private String id;

    @Column
    private String title;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

//    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
//    private Set<LanguageTranslation> translations;
}