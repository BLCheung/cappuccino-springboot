package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
//@Table(name = "spu_img",
//       schema = "zbl_missyou",
//       catalog = "")
@Getter
@Setter
public class SpuImg {
    @Id
    @GeneratedValue
    private Long id;
    private String img;
    private Long spuId;

}
