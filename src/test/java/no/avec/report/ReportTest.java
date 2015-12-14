package no.avec.report;

import junit.framework.TestCase;
import no.avec.Application;
import no.avec.domain.Stock;
import no.avec.domain.StockBought;
import no.avec.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by avec on 14/12/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ReportTest extends TestCase {

    @Autowired
    private ReportService service;

   /* @Override
    public void setUp() throws Exception {
        service = new ReportService();
    }*/

    @Test
    public void testFormatStockBuyHistoryAsString() throws Exception {
        StockBought sb = new StockBought(Utils.parseDate("20151110", "yyyyMMdd"), "1.0", Arrays.asList("A", "B", "C", "D", "E"));
        StockBought sb2 = new StockBought(Utils.parseDate("20151214", "yyyyMMdd"), "-2.1", Arrays.asList("F", "G", "H", "I", "J"));

        List<StockBought> list = Arrays.asList(sb, sb2);

        String expected =
                "--------------------------------------------------------------------------------\n" +
                "| Kjøpsdato  | Retur(%) | Aksje(1) | Aksje(2) | Aksje(3) | Aksje(4) | Aksje(5) |\n" +
                "--------------------------------------------------------------------------------\n" +
                "| 10.11.2015 |      1.0 | A        | B        | C        | D        | E        |\n" +
                "| 14.12.2015 |     -2.1 | F        | G        | H        | I        | J        |\n" +
                "--------------------------------------------------------------------------------\n";

        String result = service.formatStockBuyHistoryAsString(list, "dd.MM.yyyy");
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPortfolioAsString() throws Exception {

        Stock s1 = new Stock("Tulleaksje", "123", Utils.parseDate("20151212", "yyyyMMdd"), "", "100.0", "150.0", "50.0");
        Stock s2 = new Stock("Fjolleaksje", "666", Utils.parseDate("19700101", "yyyyMMdd"), "", "100.0", "50.0", "-50.0");

        List<Stock> list = Arrays.asList(s1, s2);

        String expected =
                "------------------------------------------------------------------------------------\n" +
                "| Aksje                          | Dato inn   | Kjøpskurs | Sluttkurs | Endring(%) |\n" +
                "------------------------------------------------------------------------------------\n" +
                "| Tulleaksje                     | 12.12.2015 |     100.0 |     150.0 |       50.0 |\n" +
                "| Fjolleaksje                    | 01.01.1970 |     100.0 |      50.0 |      -50.0 |\n" +
                "------------------------------------------------------------------------------------\n";

        String result = service.formatPortfolioAsString(list, "dd.MM.yyyy");
        assertEquals(expected, result);
    }
}