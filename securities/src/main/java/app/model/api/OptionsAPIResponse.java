package app.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class OptionsAPIResponse {
    @JsonProperty("data")
    List<HashMap<String, String>> data;
}
