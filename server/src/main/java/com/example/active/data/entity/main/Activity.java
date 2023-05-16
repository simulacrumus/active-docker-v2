package com.example.active.data.entity.main;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;

import com.example.active.data.entity.translation.Translation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Table(name = "activity")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "TITLE_TRANSLATION_ID")
    private Translation titleTranslation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TYPE_ID")
    private Type type;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private List<Availability> availabilities;
}
