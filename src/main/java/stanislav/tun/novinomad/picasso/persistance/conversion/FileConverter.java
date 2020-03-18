//package stanislav.tun.novinomad.picasso.persistance.conversion;
//
//import org.springframework.format.Formatter;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
//
//import java.text.ParseException;
//import java.util.Locale;
//
//@Component
//public class FileConverter implements Formatter<MultipartFile> {
//    @Override
//    public MultipartFile parse(String text, Locale locale) throws ParseException {
//        MultipartFile result = new MockMultipartFile(text,
//                text, "text/plain", (byte[]) null);
//        return result;
//    }
//
//    @Override
//    public String print(MultipartFile object, Locale locale) {
//        return object.getOriginalFilename();
//    }
//}
