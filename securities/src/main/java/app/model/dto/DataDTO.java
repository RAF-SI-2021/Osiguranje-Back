package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataDTO {
    private List<FutureDTO> futures;
    private List<ForexDTO> forex;
    private List<StockDTO> stocks;
}
