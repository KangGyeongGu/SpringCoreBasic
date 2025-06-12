package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 스프링 컨테이너가 올라가면서 Bean을 찾고 의존성을 주입하고 초기화하는 과정을 거치는동안 아직 Request 요청은 들어온 적이 없다.
 * 때문에 MyLogger Bean이 생성은 되지만 Request 요청이 없기 때문에 기대한 출력이 나오지 않고 에러가 발생한다.
 * ==> error creating bean with name 'myLogger':
 *      Scope 'request' is not active for the current thread;
 *      consider defining a scoped proxy for this bean if you intend to refer to it from a singleton
 *
 * ==> MyLogger Bean 생성 단계를 스프링 컨테이너의 의존성 주입 단계가 아니라, 실제 고객 요청이 들어오는 시점으로 '지연시켜야 한다.' ==> Provider
 * */
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    /**
     * Probvider를 사용하여 Bean 생성 시점을 미룰 수 있다.
     * => 첫번째 요청
     * [bae74c49-d2fd-4ff5-b5da-3215b7cdeec5] request scope bean create:hello.core.common.MyLogger@6d11f06a
     * [bae74c49-d2fd-4ff5-b5da-3215b7cdeec5][http://localhost:8080/log-demo] controller test
     * [bae74c49-d2fd-4ff5-b5da-3215b7cdeec5][http://localhost:8080/log-demo] service id = testId
     * [bae74c49-d2fd-4ff5-b5da-3215b7cdeec5] request scope bean close:hello.core.common.MyLogger@6d11f06a
     * => 두번째 요청
     * [e331343c-478d-4dcc-82e6-e322932087e9] request scope bean create:hello.core.common.MyLogger@75c79ff9
     * [e331343c-478d-4dcc-82e6-e322932087e9][http://localhost:8080/log-demo] controller test
     * [e331343c-478d-4dcc-82e6-e322932087e9][http://localhost:8080/log-demo] service id = testId
     * [e331343c-478d-4dcc-82e6-e322932087e9] request scope bean close:hello.core.common.MyLogger@75c79ff9
     * */
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        System.out.println(myLogger.getClass());

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "Ok";

    }
}
