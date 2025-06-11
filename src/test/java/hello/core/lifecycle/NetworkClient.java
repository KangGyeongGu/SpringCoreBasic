package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

//public class NetworkClient implements InitializingBean, DisposableBean {
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 Url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출 메서드
    public void connect() {
        System.out.println("connect : " + url);
    }

    public void call(String message) {
        System.out.println("call : " + url + " message : " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close : " + url);
    }


    /**
     * (1) 고전적인 방식
     * implements InitializingBean, DisposableBean 스프링 내장 기능 사용
     * 메서드 네이밍 변경 불가, 외부 API Bean 등록 시 커스텀 어려움 등의 문제로 이제는 잘 사용하지 않음
     * */
//    /**
//     * Bean LifeCycle에서 '의존관계 주입 단계'가 끝나면 호출되는 메서드
//     * */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("Call -> NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//    /**
//     * Bean LifeCycle에서 '빈 소멸 단계'에서 호출되는 메서드
//     * */
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("Call -> NetworkClient.destroy");
//       disconnect();
//    }


    /**
     * (2) 설정 정보 사용
     * @Bean(initMethod = "init", destroyMethod = "close")
     * - 스프링 빈이 코드에 의존하지 않는다.
     * - 코드가 아닌 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러에도 초기화/종료 메서드를 사용할 수 있다.
     *
     * destroyMethod -> default: "(inferred)" 로 등록되어 있어서, 'close', 'shutdown' 등 메서드를 자동 호출해준다. =""  공백 지정 시 가능하다.
     * */
//    public void init() {
//        System.out.println("NetworkClient.init");
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//    public void close() {
//        System.out.println("NetworkClient.close");
//       disconnect();
//    }

    /**
     * (3) 스프링 권고: @PostConstruct, @PreDestroy
     * - 유일한 단점 -> 외부 라이브러리에 적용할 수 가 없다. => 외부 라이브러리 초기화/종료 로직 추가가 필요하면 @Bean의 (2) 기능을 사용하자.
     * */
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
       disconnect();
    }

}
