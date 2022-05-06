package com.blcheung.cappuccino.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
//@Table(name = "spu_detail_img",
//       schema = "zbl_missyou",
//       catalog = "")
@Getter
@Setter
public class SpuDetailImg extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String img;
    private Long spuId;
    private Long index;


}
