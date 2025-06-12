package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class PrototypeTest {

    /**
     * Prototype Scope Bean : 스프링 컨테이너에서 Bean을 '조회할 때 생성'되고, 초기화 메서드도 실행된다.
     * 매 조회 마다 완전히 다른 (새로운) 스프링 빈이 생성된다. + 초기화도 두 번 실행 되었다.
     * SingletonBean => 스프링 컨테이너가 전체 생명 주기를 관리하기 때문에 소멸메서드까지 호출하지만,
     * PrototypeBean => 스프링 컨테이너가 생성/의존관계 주입/초기화 까지만 관여하므로 소멺메서드가 호출되지 않았다.
     * */

    /**
     * find prototypeBean1
     * PrototypeBean.init =>
     * find prototypeBean2
     * PrototypeBean.init => 초기화 메서드가 두 번의 조회에 대해 두 번 생성되었다. 즉, 매 조회 요청마다 새로운 Bean을 생성하고 있다.
     * prototypeBean1 = hello.core.scope.PrototypeTest$PrototypeBean@1bae316d
     * prototypeBean2 = hello.core.scope.PrototypeTest$PrototypeBean@147a5d08
     * */
    @Test
    void prototypeBeanTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);

        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
