package futures;

import futures.model.FuturesContract;
import futures.repositories.FuturesContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {
    private static final String COMMA_DELIMETER = ",";
    private static final int EUREX_SYMBOL = 0;
    private static final int EUREX_NAME = 2;
    private static final int EUREX_SETTLEMENT_MONTHS = 3;
    private static final int EUREX_CATEGORY = 5;
    private static final int CATEGORY_NAME = 0;
    private static final int CONTRACT_SIZE = 1;
    private static final int CONTRACT_UNIT = 2;
    private static final int MAINTENANCE_MARGIN = 3;

    private final FuturesContractRepository futuresContractRepository;

    @Autowired
    public DataLoader(FuturesContractRepository futuresContractRepository) {
        this.futuresContractRepository = futuresContractRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<List<String>> eurexData = new ArrayList<>();
        List<List<String>> categoryData = new ArrayList<>();
        File file = new File(Config.getProperty("eurex_file"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file.getCanonicalPath()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List<String> list = Arrays.stream(values).toList();
                if (list.size() > 5) {
                    eurexData.add(list);
                }
            }
            file = new File(Config.getProperty("categories_file"));
            br = new BufferedReader(new FileReader(file.getCanonicalPath()));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List<String> list = Arrays.stream(values).toList();
                categoryData.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert br != null;
            br.close();
        }
        for (List<String> eurexList : eurexData) {
            for (List<String> categoryList : categoryData) {
                if (eurexList.get(EUREX_CATEGORY).equals(categoryList.get(CATEGORY_NAME))) {
                    String symbol = eurexList.get(EUREX_SYMBOL);
                    String productName = eurexList.get(EUREX_NAME);
                    Integer contractSize = Integer.parseInt(categoryList.get(CONTRACT_SIZE));
                    String contractUnit = categoryList.get(CONTRACT_UNIT);
                    Integer maintenanceMargin = Integer.parseInt(categoryList.get(MAINTENANCE_MARGIN));
                    String settlementDates = eurexList.get(EUREX_SETTLEMENT_MONTHS);
                    System.out.println(settlementDates);
                    char[] months = new char[settlementDates.length()];
                    for (int i = 0; i < settlementDates.length(); i++) {
                        months[i] = settlementDates.toUpperCase().charAt(i);
                    }
                    int year = 2022;
                    Date settlementDate;
                    for (char c : months) {
                        settlementDate = getDateForMonth(c, year);
                        FuturesContract futuresContract = new FuturesContract(symbol, productName, contractSize, contractUnit, maintenanceMargin, settlementDate);
                        System.out.println(futuresContract);
                        futuresContractRepository.save(futuresContract);
                        year++;
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
}
