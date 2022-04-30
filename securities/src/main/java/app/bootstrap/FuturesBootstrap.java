package app.bootstrap;

import app.Config;
import app.model.Future;
import app.model.InflationRate;
import app.model.api.FutureAPIResponse;
import app.model.api.InflationRateAPIResponse;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import app.repositories.FuturesRepository;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FuturesBootstrap {

    private static final String COMMA_DELIMETER = ",";
    private static final int EUREX_SYMBOL = 0;
    private static final int EUREX_NAME = 2;
    private static final int EUREX_SETTLEMENT_MONTHS = 3;
    private static final int EUREX_CATEGORY = 5;
    private static final int CATEGORY_NAME = 0;
    private static final int CONTRACT_SIZE = 1;
    private static final int CONTRACT_UNIT = 2;
    private static final int MAINTENANCE_MARGIN = 3;

    private final FuturesRepository futuresRepository;

    @Autowired
    public FuturesBootstrap(FuturesRepository futuresRepository) {
        this.futuresRepository = futuresRepository;
    }

    public void loadFuturesData() throws Exception {
        List<List<String>> eurexData = new ArrayList<>();
        List<List <String>> categoryData = new ArrayList <>();

        ClassLoader classLoader = FuturesBootstrap.class.getClassLoader();
        File file = new File(classLoader.getResource(Config.getProperty("eurex_file")).getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List <String> list = Arrays.asList(values);
                if (list.size() > 5) {
                    eurexData.add(list);
                }
            }
            file = new File(classLoader.getResource(Config.getProperty("categories_file")).getFile());

            try (BufferedReader brr = new BufferedReader(new FileReader(file))) {
                while ((line = brr.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMETER);
                    List <String> list = Arrays.asList(values);
                    categoryData.add(list);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (List<String> eurexList : eurexData) {
            for (List <String> categoryList : categoryData) {
                if (eurexList.get(EUREX_CATEGORY).equals(categoryList.get(CATEGORY_NAME))) {
                    String symbol = eurexList.get(EUREX_SYMBOL);
                    String description = eurexList.get(EUREX_NAME);
                    Integer contractSize = Integer.parseInt(categoryList.get(CONTRACT_SIZE));
                    String contractUnit = categoryList.get(CONTRACT_UNIT);
                    Integer maintenanceMargin = Integer.parseInt(categoryList.get(MAINTENANCE_MARGIN));
                    String settlementDates = eurexList.get(EUREX_SETTLEMENT_MONTHS);
                    char[] months = new char[settlementDates.length()];
                    for (int i = 0; i < settlementDates.length(); i++) {
                        months[i] = settlementDates.toUpperCase().charAt(i);
                    }
                    int year = 2022;
                    Date settlementDate;
                    for (char c : months) {
                        settlementDate = getDateForMonth(c, year);

                        String lastUpdated = formatter.format(date);

                        String currSymbol = symbol + c + year;
                        try{
                            RestTemplate rest = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            HttpEntity<FutureAPIResponse> entity = new HttpEntity <>(headers);
                            System.out.println(Config.getProperty("nasdaq_futures_url") + currSymbol + "?start_date=2022-04-20" + "&end_date=2022-04-20" + "&api_key=" + Config.getProperty("nasdaq_api_key"));
                            ResponseEntity<FutureAPIResponse> response = rest.exchange(Config.getProperty("nasdaq_futures_url") + currSymbol + "?start_date=2022-04-20" + "&end_date=2022-04-20" + "&api_key=" + Config.getProperty("nasdaq_api_key"), HttpMethod.GET, entity, FutureAPIResponse.class);
                            ArrayList<ArrayList<String>> dataWrapper = Objects.requireNonNull(response.getBody()).getDataset().getData();
                            if(dataWrapper.isEmpty())
                                continue;
                            ArrayList<String> data = dataWrapper.get(0);
                            if(data.isEmpty())
                                continue;
                            BigDecimal price = new BigDecimal(data.get(4));
                            BigDecimal ask = new BigDecimal(data.get(2));
                            BigDecimal bid = new BigDecimal(data.get(3));
                            BigDecimal priceChange = bid.subtract(ask);
                            Long volume = (long)Double.parseDouble(data.get(5));

                            Future newFuture = new Future(currSymbol, description, lastUpdated, price, ask, bid, priceChange, volume, contractSize, contractUnit, maintenanceMargin, settlementDate);
                            newFuture.setSecurityHistory(null);
                            futuresRepository.save(newFuture);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                       // year++;       // todo
                    }
                }
            }
        }
    }

    private Date getDateForMonth(char month, int year) throws ParseException {
        Date settlementDate;
        switch (month) {
            case 'F':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("01", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("01", year));
                }
                break;
            case 'G':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("02", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("02", year));
                }
                break;
            case 'H':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("03", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("03", year));
                }
                break;
            case 'J':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("04", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("04", year));
                }
                break;
            case 'K':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("05", year));
                break;
            case 'M':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("06", year));
                break;
            case 'N':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("07", year));
                break;
            case 'Q':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("08", year));
                break;
            case 'U':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("09", year));
                break;
            case 'V':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("10", year));
                break;
            case 'X':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("11", year));
                break;
            case 'Z':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("12", year));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        return settlementDate;
    }

    private String dateFormatBuilder(String month, int year) {
        return "01/" + month + "/" + year;
    }

    public static BigDecimal random(int range) {
        BigDecimal max = new BigDecimal(range);
        SecureRandom random = new SecureRandom();
        BigDecimal randFromDouble = BigDecimal.valueOf(random.nextInt());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        actualRandomDec = actualRandomDec.setScale(2, RoundingMode.DOWN);
        return actualRandomDec;
    }
}
