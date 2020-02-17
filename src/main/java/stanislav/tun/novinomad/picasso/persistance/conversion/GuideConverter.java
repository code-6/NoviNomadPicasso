package stanislav.tun.novinomad.picasso.persistance.conversion;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

import java.text.ParseException;
import java.util.Locale;

public class GuideConverter implements Formatter<Guide> {
    @Autowired
    GuideService guideService;

    @Override
    public Guide parse(String s, Locale locale) throws ParseException {

        return guideService.getGuide(Long.valueOf(s)).get();
    }

    @Override
    public String print(Guide guide, Locale locale) {
        return JsonPrinter.getString(guide);
    }
}
