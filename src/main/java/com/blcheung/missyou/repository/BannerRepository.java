package com.blcheung.missyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// repository：用于操作数据库的仓储层
// JpaRepository<需要进行仓储的模型, 这个模型主键的数据类型>
public interface BannerRepository extends JpaRepository<String, Long> {
//    Banner findOneById(Long id);
//
//    Banner findOneByName(String name);
}
