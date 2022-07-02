package buyingmarket.mappers;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class SetToLongConverter implements AttributeConverter<Set<Long>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Long> longs) {
        return longs == null ? null :
                longs.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public Set<Long> convertToEntityAttribute(String data) {
        return data == null ? Collections.emptySet() :
                Arrays.stream(data.split(",")).map(Long::parseLong).collect(Collectors.toSet());
    }
}
