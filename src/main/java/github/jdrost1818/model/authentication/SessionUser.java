package github.jdrost1818.model.authentication;

import lombok.Data;
import lombok.experimental.Delegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionUser implements Serializable {

    @Delegate
    private User currentUser;

}
