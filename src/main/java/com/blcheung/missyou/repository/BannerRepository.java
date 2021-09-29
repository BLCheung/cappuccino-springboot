package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository：用于操作数据库的仓储层
// JpaRepository<需要进行仓储的模型, 这个模型主键的数据类型>
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    /**
     * 通过id找到Banner
     * @param id
     * @return
     */
    Banner findOneById(Long id);

    /**
     * 通过name找到Banner
     * @param name
     * @return
     */
    Banner findOneByName(String name);
}
