package hello.core.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;

class StatefulServiceTest {

    /**
     * Spring Bean은 항상 Stateless 로 설계되어야 한다.
     * */
    @Test
    @DisplayName("싱글톤 방식에서 공유변수 필드 참조 문제 테스트")
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : userA 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : userB 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA: userA 가 주문 금액 조회 -> 20000원 출력
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}