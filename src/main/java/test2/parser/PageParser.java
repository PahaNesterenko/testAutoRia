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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

public class PageParser {

    private AutoParser autoParser;

    public List<Auto> parse(Document doc) {
        autoParser = new AutoParser();
        //autoParser.parse();
        Elements select = doc.select(".content");

        for(Element e: select ){
            Auto d = parseContent(e);
        }

        return new ArrayList<>();

    }

    private Auto parseContent(Element e) {

        Elements select = e.select(".address");
        String year = select.get(0).textNodes().get(0).getWholeText().trim();
        String modelandmark = select.get(0).child(0).text();
        String location = e.select(".location").get(0).child(1).text();
        String priceVal = e.select("[data-currency='USD']").text().trim().replace(" ", "");
        Long prise = Try.of( () -> parseLong(priceVal) ).getOrElse(0L);
        String mileageVal = e.select(".characteristic").get(0).child(0).text().split(" ")[0] + "000";
        Long mileage = Try.of( () -> parseLong(mileageVal) ).getOrElse(0L);
        String engine = e.select(".characteristic").get(0).child(1).text();
        String engineType = null;
        FuelType fuelType = null;

        Constants co = new Constants();
        List<String> res = co.autoProducers.stream().map(s -> s.trim()).collect(Collectors.toList());

        Double engineCapacity= null;
        if(!"Не указано".equals(engine)) {
            String[] enginearr = engine.split(", ");
            engineType =  enginearr[0];
            fuelType = null;
            if("Газ/бензин".equals(engineType)){
                fuelType = FuelType.GASPETROL;
            }
            else if("Бензин".endsWith(engineType)){
                fuelType = FuelType.PETROL;
            }
            else if("Дизель".equals(engineType)){
                fuelType = FuelType.DIESEL;
            }
            else if("Электро".equals(engineType)){
                fuelType = FuelType.ELECTRIC;
            }
            String engineCapacityVal = Try.of( () -> enginearr[1].split(" ")[0] ).getOrElse("0");

            engineCapacity = Double.parseDouble(engineCapacityVal);
        }



        String transmissinvalue = e.select(".characteristic").get(0).child(2).text();
        Transmission transmission = null;
        if("Автомат".equals(transmissinvalue)){
            transmission = Transmission.AUTOMATIC;
        }
        else if("Ручная / Механика".equals(transmissinvalue)){
            transmission = Transmission.MANUAL;
        }
        else if("Вариатор".equals(transmissinvalue)){
            transmission = Transmission.VARIATOR;
        }

        Boolean custom = true;
        Boolean notInUkraine = false;
        Boolean accident = false;
        String additions = e.select("[data-state]").text();
        if(StringUtils.isNotBlank(additions)){
            if(additions.contains("Нерастаможен")){
                custom = false;
            }
            if(additions.contains("Авто не в Украине")){
                notInUkraine = true;
            }
            if(additions.contains("После ДТП")){
                accident = true;
            }

        }

        Auto auto = new Auto();

        return auto;

    }
}
