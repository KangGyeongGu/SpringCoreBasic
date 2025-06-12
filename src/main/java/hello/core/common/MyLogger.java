package hello.core.common;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * proxyMode = ScopedProxyMode.TARGET_CLASS
 * Provider를 쓰는 것과 똑같은 결과를 보여준다.
 * => MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP Request와 상관없이 가짜 프록시 클래스를 다른 Bean에 미리 주입해 둘 수 있다.
 * => class hello.core.common.MyLogger$$SpringCGLIB$$0
 * ==> CGLIB 이라는 라이브러리를 이용해서 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입한다. (의존관계 주입 단계에서 가짜 Proxy 객체가 주입되는 것)
 * ==> 가짜 프록시 객체는 요청이 오면 그 때 내부에서 진짜 Bean을 요청하는 위임 로직을 가지고 있다.
 * ==> 가짜 프록시 객체는 원본 클래스를 상속받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 원본인지 상관없이 동일하게 사용할 수 있다. (다형성!!)
 * */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // MyLogger -> Proxy로 생성
public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " +
                message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create:" + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close:" + this);
    }
}