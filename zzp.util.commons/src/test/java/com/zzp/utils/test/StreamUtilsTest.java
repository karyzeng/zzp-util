package com.zzp.utils.test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 测试lambda
 * @Author Garyzeng
 * @since 2022.01.18
 **/
public class StreamUtilsTest {

    public static void main(String[] args) {
        List<Goods> goodsList = new ArrayList<Goods>();
        goodsList.add(new Goods(1L, new BigDecimal("12.3")));
        goodsList.add(new Goods(2L, new BigDecimal("12.88")));
        goodsList.add(new Goods(3L, null));

        List<Goods> filter = goodsList.stream().filter(goods -> goods.getNumber() != null).collect(Collectors.toList());
        BigDecimal totalNumber = filter.stream().map(Goods::getNumber).reduce(new BigDecimal("0"), BigDecimal::add);
        System.out.println(totalNumber);
    }

    static class Goods{

        private Long id;

        private BigDecimal number;

        public Goods(Long id, BigDecimal number) {
            this.id = id;
            this.number = number;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public BigDecimal getNumber() {
            return number;
        }

        public void setNumber(BigDecimal number) {
            this.number = number;
        }
    }

}
