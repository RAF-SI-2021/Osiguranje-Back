package app.bootstrap;

import app.Config;
import app.model.Exchange;
import app.model.Future;
import app.model.InflationRate;
import app.model.api.FutureAPIResponse;
import app.model.api.InflationRateAPIResponse;
import app.repositories.ExchangeRepository;
import app.services.FuturesService;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import app.repositories.FuturesRepository;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private final FuturesService futuresService;
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public FuturesBootstrap(FuturesRepository futuresRepository, FuturesService futuresService, ExchangeRepository exchangeRepository) {
        this.futuresRepository = futuresRepository;
        this.futuresService = futuresService;
        this.exchangeRepository = exchangeRepository;
    }

    public void loadFuturesData() throws Exception {
        List<List<String>> eurexData = new ArrayList<>();
        List<List <String>> categoryData = new ArrayList <>();

        ClassLoader classLoader = FuturesBootstrap.class.getClassLoader();
        InputStream file = new ClassPathResource(Config.getProperty("eurex_file")).getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List <String> list = Arrays.asList(values);
                if (list.size() > 5) {
                    eurexData.add(list);
                }
            }
            file = new ClassPathResource(Config.getProperty("categories_file")).getInputStream();

            try (BufferedReader brr = new BufferedReader(new InputStreamReader(file))) {
                while ((line = brr.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMETER);
                    List <String> list = Arrays.asList(values);
                    categoryData.add(list);
                }
            }
        } catch (IOException e) {}

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        futuresService.setLastupdated(date);

        SecureRandom random = new SecureRandom();
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
                        int quoteYear = calculateYear(c, year);
                        settlementDate = getDateForMonth(c, quoteYear);
                        String lastUpdated = formatter.format(date);
                        String currSymbol = symbol + c + quoteYear;
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
                            if(ask.compareTo(BigDecimal.valueOf(0)) == 0)
                                ask = price;
                            BigDecimal bid = new BigDecimal(data.get(3));
                            if(bid.compareTo(BigDecimal.valueOf(0)) == 0)
                                bid = price;
                            BigDecimal priceChange = bid.subtract(ask);
                            Long volume = (long)Double.parseDouble(data.get(5));
                            if(volume == 0)
                                volume = 5000 + (long)(random.nextDouble()*20000);

                            Future newFuture = new Future(currSymbol, description, lastUpdated, price, ask, bid, priceChange, volume, contractSize, contractUnit, maintenanceMargin, settlementDate);

                            Exchange stockExchange = exchangeRepository.findByAcronym("EUREX");
                            newFuture.setExchange(stockExchange);
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

    private int getMonth(char monthChar) {
        int month;
        switch (monthChar) {
            case 'F':
                month = 1;
                break;
            case 'G':
                month = 2;
                break;
            case 'H':
                month = 3;
                break;
            case 'J':
                month = 4;
                break;
            case 'K':
                month = 5;
                break;
            case 'M':
                month = 6;
                break;
            case 'N':
                month = 7;
                break;
            case 'Q':
                month = 8;
                break;
            case 'U':
                month = 9;
                break;
            case 'V':
                month = 10;
                break;
            case 'X':
                month = 11;
                break;
            case 'Z':
                month = 12;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + monthChar);
        }
        return month;
    }

    private int calculateYear(char monthChar, int year) throws ParseException{
        Date currDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String currMonthStr = formatter.format(currDate);
        int currMonth = Integer.parseInt(currMonthStr);
        int month = getMonth(monthChar);
        if(month <= currMonth)
            year++;
        return year;
    }

    private Date getDateForMonth(char monthChar, int year) throws ParseException {
        int month = getMonth(monthChar);
        return new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder(month, year));
    }

    private String dateFormatBuilder(int month, int year) {
        int day = 31;
        if (month == 2)
            day = 28;
        int[] shortMonths = {4, 6, 9, 11};
        List<Integer> list = Arrays.stream(shortMonths).boxed().collect(Collectors.toList());
        if(list.contains(month))
            day = 30;

        return day + "/" + month + "/" + year;
    }

//    public static BigDecimal random(int range) {
//        BigDecimal max = new BigDecimal(range);
//        SecureRandom random = new SecureRandom();
//        BigDecimal randFromDouble = BigDecimal.valueOf(random.nextInt());
//        BigDecimal actualRandomDec = randFromDouble.multiply(max);
//        actualRandomDec = actualRandomDec.setScale(2, RoundingMode.DOWN);
//        return actualRandomDec;
//    }
}
