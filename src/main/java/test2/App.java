package test2;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import test2.domain.Auto;
import test2.parser.AutoParser;
import test2.parser.PageParser;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App a = new App();
        a.doSomething();
    }

    @SneakyThrows
    private void doSomething() {

        Document doc = Jsoup.connect("https://auto.ria.com/legkovie/?page=35").get();

        //Element element = doc.select("#mp-itn b a").get(1);

        AutoParser ap = new AutoParser();
        //Auto a = ap.parse(element);

        PageParser pp = new PageParser();
        List<Auto> ao = pp.parse(doc);

    }
}
