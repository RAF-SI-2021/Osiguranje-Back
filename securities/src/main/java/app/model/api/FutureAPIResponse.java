package app.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class FutureAPIResponse {
    @JsonProperty("dataset")
    private Dataset dataset;

    @Getter
    public class Dataset{
        private ArrayList<ArrayList<String>> data;
    }
}
