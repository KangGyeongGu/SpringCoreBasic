package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    /**
     * lombok => @RequiredArgsConstructor 를 사용하면, final이 붙은 필드들을 모두 모아서 자동으로 생성자를 만들어준다.
     * 필드주입/생성자 주입 방식에서 명시적으로 @Autowired 하는 코드를 작성하지 않아도 된다.
     *
     * @MainDiscountPolicy: 커스텀 어노테이션을 작성한 후 아래 파라미터 형식과 같이 사용할 수 있다.
     * */
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
