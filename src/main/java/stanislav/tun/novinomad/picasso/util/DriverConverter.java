package stanislav.tun.novinomad.picasso.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;

import java.util.Optional;

public class DriverConverter implements Converter<String, Optional<Driver>> {
    @Autowired
    DriverService driverService;

    @Override
    public Optional<Driver> convert(String source) {
        if(source != null ){
            try{
                Long id = Long.parseLong(source);
                return driverService.getDriver(id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
