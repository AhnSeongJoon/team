package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> ,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    // select * from item where itemNm = ? (String itemNm)
    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);
    //select * from item where price < Integer price order by desc;
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // like 뒤에 % 띄어쓰기!
    // select i from Item -> 테이블 Item 을 i 로 치환해서 출력
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail")String itemDetail);
    // String item Detail like 를 할 값 -> order by i price desc
    // Item 을 가지로 내림차순

    @Query(value = "select * from item i where i.item_Detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail")String itemDetail);


}
