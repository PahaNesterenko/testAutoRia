package test2.parser;

import javaslang.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import test2.Constants;
import test2.domain.Auto;
import test2.domain.FuelType;
import test2.domain.Transmission;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

public class PageParser {

    private AutoParser autoParser;

    public List<Auto> parse(Document doc) {
        autoParser = new AutoParser();
        Elements select = doc.select(".content");

        List<Auto> autos = new ArrayList<>();
        for (Element e : select) {
            Auto d = parseContent(e);
            autos.add(d);
        }

        return autos;

    }

    private Auto parseContent(Element e) {


        Auto auto = new Auto();
        try {
            Elements select = e.select(".address");
            String year = select.get(0).textNodes().get(0).getWholeText().trim();
            auto.setYear(Year.of(Integer.parseInt(year)));
            String modelandmark = select.get(0).child(0).text();


            Constants co = new Constants();
            List<String> producers = Arrays.asList(co.autoProducers.split("\n"));

            String[] modelandmarksplit = modelandmark.split(" ");
            String producer = "";
            String model = "";
            if (modelandmarksplit.length >= 2) {
                if (producers.contains(modelandmarksplit[0])) {
                    producer = modelandmarksplit[0];
                    if (modelandmarksplit.length >= 2) {
                        model = modelandmarksplit[1];
                    }

                } else if (producers.contains(modelandmarksplit[0] + " " + modelandmarksplit[1])) {
                    producer = modelandmarksplit[0] + " " + modelandmarksplit[1];
                    if (modelandmarksplit.length >= 3) {
                        model = modelandmarksplit[2];
                    }
                }
            }
            auto.setMake(producer);
            auto.setModel(model);

            String location = e.select(".location").get(0).child(1).text();
            auto.setLocation(location);

            String priceVal = e.select("[data-currency='USD']").text().trim().replace(" ", "");
            Long price = Try.of(() -> parseLong(priceVal)).getOrElse(0L);
            auto.setPrice(price);
            String mileageVal = e.select(".characteristic").get(0).child(0).text().split(" ")[0] + "000";
            Long mileage = Try.of(() -> parseLong(mileageVal)).getOrElse(0L);
            auto.setMileage(mileage);
            String engine = e.select(".characteristic").get(0).child(1).text();
            String engineType = null;
            FuelType fuelType = null;


            Double engineCapacity = null;
            if (!"Не указано".equals(engine)) {
                String[] enginearr = engine.split(", ");
                engineType = enginearr[0];
                fuelType = null;
                if ("Газ/бензин".equals(engineType)) {
                    fuelType = FuelType.GASPETROL;
                } else if ("Бензин".endsWith(engineType)) {
                    fuelType = FuelType.PETROL;
                } else if ("Дизель".equals(engineType)) {
                    fuelType = FuelType.DIESEL;
                } else if ("Электро".equals(engineType)) {
                    fuelType = FuelType.ELECTRIC;
                }
                auto.setFuelType(fuelType);
                String engineCapacityVal = Try.of(() -> enginearr[1].split(" ")[0]).getOrElse("0");

                engineCapacity = Double.parseDouble(engineCapacityVal);
                auto.setEngineCapacity(engineCapacity);
            }


            String transmissinvalue = e.select(".characteristic").get(0).child(2).text();
            Transmission transmission = null;
            if ("Автомат".equals(transmissinvalue)) {
                transmission = Transmission.AUTOMATIC;
            } else if ("Ручная / Механика".equals(transmissinvalue)) {
                transmission = Transmission.MANUAL;
            } else if ("Вариатор".equals(transmissinvalue)) {
                transmission = Transmission.VARIATOR;
            }
            auto.setTransmission(transmission);

            String additions = e.select("[data-state]").text();
            if (StringUtils.isNotBlank(additions)) {
                if (additions.contains("Нерастаможен")) {
                    auto.setCustomRegistered(false);
                }
                if (additions.contains("Авто не в Украине")) {
                    auto.setLocatedInUkraine(false);
                }
                if (additions.contains("После ДТП")) {
                    auto.setAccidentFree(false);
                }

            }
        }catch (Exception ex) {
            System.out.println(ex);
        }
        return auto;

    }
}
