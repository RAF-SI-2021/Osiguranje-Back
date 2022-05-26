package app.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class AlphaVantageQuoteAPIResponse extends HashMap<String, Map<String, String>> {
}
