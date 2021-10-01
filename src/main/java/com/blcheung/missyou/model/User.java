package com.blcheung.missyou.model;

import com.blcheung.missyou.util.MapJSONConverter;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "delete_time is null")  // 会应用当前查询语句在当前Entity内
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                id;
    private String              openid;
    private String              nickname;
    private Long                unifyUid;
    private String              email;
    private String              password;
    private String              mobile;
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = MapJSONConverter.class)
    private Map<String, Object> wxProfile;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_coupon",
               joinColumns = @JoinColumn(name = "userId"),
               inverseJoinColumns = @JoinColumn(name = "couponId"))
    private List<Coupon> couponList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<UserAddress> addressList;
}
