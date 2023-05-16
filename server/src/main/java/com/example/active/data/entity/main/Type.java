package com.example.active.data.entity.main;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;

import com.example.active.data.entity.translation.Translation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Table(name = "type")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Type extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "TITLE_TRANSLATION_ID")
    private Translation titleTranslation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Activity> activities;

    @OneToMany(mappedBy = "type")
    private Set<FacilityType> facilityTypes;

    @Override
    public String toString() {
        return "Type{" +
                "titleTranslation=" + titleTranslation +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Type type = (Type) o;
        return category.equals(type.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }
}
