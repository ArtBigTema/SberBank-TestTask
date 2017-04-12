package av.sberbank.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import av.sberbank.model.db.DBColumn;
import av.sberbank.model.db.DBColumnType;
import av.sberbank.model.db.DBTable;
import av.sberbank.model.db.Key;
import av.sberbank.utils.Constant;

/**
 * Created by Artem on 12.04.2017.
 */

@Root(name = "Valute")
@DBTable(name = "Currency")
public class Currency {
    @Key
    @DBColumn(name = "_id", type = DBColumnType.INTEGER)
    private Integer dbId;

    @Attribute(name = "ID")
    @DBColumn(name = "ID", type = DBColumnType.TEXT)
    private String id;

    @Element(name = "NumCode")
    @DBColumn(name = "NumCode", type = DBColumnType.INTEGER)
    private Integer numCode;

    @Element(name = "CharCode")
    @DBColumn(name = "CharCode", type = DBColumnType.TEXT)
    private String charCode;

    @Element(name = "Nominal")
    @DBColumn(name = "Nominal", type = DBColumnType.INTEGER)
    private Integer nominal;

    @Element(name = "Name")
    @DBColumn(name = "Name", type = DBColumnType.TEXT)
    private String name;

    @Element(name = "Value")
    @DBColumn(name = "Value", type = DBColumnType.TEXT)
    private String value;

    public Double getValue() {
        return Double.parseDouble(value.replace(',', '.'));
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getCharCodePlusName() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(getCharCode());
        sb.append(']');
        sb.append(' ');
        sb.append(getName());
        return sb.toString();
    }

    static Currency getRuble() {
        Currency rub = new Currency();

        rub.id = "0";
        rub.numCode = 0;
        rub.charCode = Constant.CHAR_CODE;
        rub.name = Constant.NAME;
        rub.nominal = 1;
        rub.value = "1";

        return rub;
    }
}