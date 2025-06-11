package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig의 등장으로 애플리케이션이 크게
 * (1) 사용 영역
 * (2) 객체 생성 및 구성 영역
 * 으로 분리되었다.
 * */
@Configuration
public class AppConfig {

    /**
     * @Configuration과 바이트코드 조작
     * (1) call AppConfig.memberService
     * (2) call AppConfig.memberRepository
     * (3) call AppConfig.orderService
     *
     * 스프링 컨테이너는 싱글톤 레지스트리이다. 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야 한다.
     * 하지만 스프링이 순수 자바 코드까지 조작할 수 는 없으므로, 이 때 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.
     *
     * 만약 @Configuration이 없다면, 순수 AppConfig 인스턴스가 등록된다. 이때 예상했던 대로 memberRepository가 세 번 호출되면서 싱글톤 패턴이 위반된다.
     * */

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    /**
     * 할인 정책만 변경하면 나머지 모든 코드의 변경없이 할인 정책을 변경할 수 있게 되었다.
     * */
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
