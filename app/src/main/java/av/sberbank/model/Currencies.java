package av.sberbank.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 12.04.2017.
 */

@Root
public class Currencies {
    @Attribute(name = "Date")
    private String date;

    @Attribute
    private String name;

    @ElementList(inline = true)
    private List<Currency> currencies = new ArrayList<>();
}