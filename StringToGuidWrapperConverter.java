import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
/**
 * This converter will convert the UUID passed in the url as a path variable string to a GuidWrapper object
 */
public class StringToGuidWrapperConverter implements Converter<String, GuidWrapper> {
       @Override
        public GuidWrapper convert(String source) {
            return GuidWrapper.fromString(source);
        }
}

