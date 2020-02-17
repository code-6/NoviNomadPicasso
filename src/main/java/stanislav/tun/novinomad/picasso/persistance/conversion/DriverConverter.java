package stanislav.tun.novinomad.picasso.persistance.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class DriverConverter implements Formatter< Driver> {

    @Autowired
    DriverService service;

    @Override
    public Driver parse(String text, Locale locale) throws ParseException {
        long driverId = Long.valueOf(text);
        return this.service.getDriver(driverId).get();
    }

    @Override
    public String print(Driver object, Locale locale) {
        return JsonPrinter.getString(object);
    }
}
