package securities;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrencyDTO {

    private Long id;
    private String name;
    private String isoCode;
    private String symbol;
    private String country;


}
