package app.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class InflationRateAPIResponse {
    @JsonProperty("dataset")
    private Dataset dataset;

    @Getter
    public class Dataset{
        private ArrayList<ArrayList<String>> data;
    }
}
