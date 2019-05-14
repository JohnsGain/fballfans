package bb;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangjuwa
 * @date 2019/5/7
 * @since jdk1.8
 **/
@ConfigurationProperties(prefix = "auth")
public class Aocnfig {

    private String[] permits;

    public String[] getPermits() {
        return permits;
    }

    public void setPermits(String[] permits) {
        this.permits = permits;
    }
}
