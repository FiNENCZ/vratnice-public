package cz.diamo.vratnice_public.csvRepresentation;

import com.opencsv.bean.AbstractBeanField;

public class LokalitaSplitConverter extends AbstractBeanField<String[], String>  {

    @Override
    protected String[] convert(String value) {
        if (value == null || value.isEmpty()) {
            return new String[0];
        }
        return value.split("\\|");
    }
}
