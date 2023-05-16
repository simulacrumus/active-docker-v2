package com.example.active.data.entity.main;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;

import com.example.active.data.entity.translation.Translation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Table(name = "category")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "TITLE_TRANSLATION_ID")
    private Translation titleTranslation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CITY_ID")
    private City city;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Type> types;

    @Override
    public String toString() {
        return "Category{" +
                "titleTranslation=" + titleTranslation +
                ", city=" + city +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return titleTranslation.equals(category.titleTranslation) && city.equals(category.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), titleTranslation, city);
    }
}
