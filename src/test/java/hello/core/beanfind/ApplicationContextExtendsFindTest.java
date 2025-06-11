package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Configuration
    static class TestConfig {

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }

    /**
     * Rate/Fix 모두 DiscountPolicy 의 자식이므로,
     * discountPolicy 부모 타입을 조회했을 때, 스프링에서는 무조건 딸려있는 모든 자식까지 모두 조회한다.
     * */
    @Test
    @DisplayName("부모 타입으로 조회 시 자식이 둘 이상이면 중복 오류 발생")
    void findBeanByParentTypeDuplicate() {
        /**
         * getBean(DiscountPolicy.class)은 DiscountPolicy 타입에 할당 가능한 모든 Bean을 찾는데,
         * 이 과정에서 할당 가능한 구현체들이 하나 이상 발견되는 경우,
         * Spring에서는 이 중 어떤 Bean을 반환해야 하는지 결정할 수 없게된다.
         * 때문에 NoUniqueBeanDefinitionException 에러 (특정 단일 하나의 Bean 정의를 찾지 못했다)가 발생하게 된다.
         * */
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회 시 자식이 둘 이상이면 빈 이름을 지정하면 된다")
    void findBeanByParentTypeBeanName() {
        /**
         * 그러므로 Spring이 다중 후보 구현체들 중 어떤 것을 반환해야 하는지 명시적으로 선언해주어야 하며,
         * Bean의 이름을 지정하는 방식으로 Spring에게 알릴 수 있다.
         * */
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);

        /**
         * 실제 테스트 케이스 작성 시에는 출력문이 있으면 안된다.
         * 자동 통과/실패는 시스템이 결정해야 하는데 개발자가 육안으로 확인하고 있으면 안된다.
         * */
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }

    /**
     * 자바의 모든 객체는 Object 타입을 상속받기 때문에,
     * 스프링에 등록된 모든 Bean이 조회된다.
     * */
    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findAllBeanByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }


}
