package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.Image;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ImageRepository extends ModelRepository<Image> {

    @Query(value = "select * from image im " +
            "    left join books b on im.id = b.image_id " +
            "    left join authors a on im.id = a.image_id " +
            "where b.image_id is null and a.image_id is null ", nativeQuery = true)
    List<Image> findUnusedPictures();

    @Query("select i from Image i where i.name in ?1")
    List<Image> findByNameIn(Collection<String> names);


}
