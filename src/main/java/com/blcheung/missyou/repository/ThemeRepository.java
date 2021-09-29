package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Theme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("select t from Theme t where t.name in (:n)")
    List<Theme> findByNames(@Param("n") List<String> names);
    // List<Theme> findAllByNameIn(List<String> names);

    Theme findByName(String name);
}
