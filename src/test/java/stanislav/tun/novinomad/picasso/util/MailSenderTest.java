package stanislav.tun.novinomad.picasso.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.nio.file.FileSystems;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
public class MailSenderTest {

    @Autowired
    CustomMailSender customSender;

    @Test
    public void sendEmail() throws MessagingException {
        var s = FileSystems.getDefault().getSeparator();
        var path = "."+s+"picasso"+s+"picasso_logs"+s+"picasso_log.log";
        customSender.sendEmail("wong.stanislav@gmail.com", "picasso thrown an exception", "Look at the logs",
                path);
    }
}