package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataDTO {
    private List<Future> futures;
    private List<Forex> forex;
    private List<Stock> stock;
}
