package github.jdrost1818.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class ObjectMapperUtil {

    private static final ObjectMapper oauthMapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private ObjectMapperUtil() {
        // Prevent instantiation
    }

    public static <T> T mapOauthResponse(Object objectToMap, Class<T> typeToMapTo) {
        return oauthMapper.convertValue(objectToMap, typeToMapTo);
    }

}
