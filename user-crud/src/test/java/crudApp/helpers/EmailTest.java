package crudApp.helpers;

import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import helpers.EmailSender;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Message;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmailTest {


    @Test
    public void testSetAndGetPwd() {
        EmailSender emailSender = EmailSender.getInstance();
        String pwd = "Password";
        emailSender.setPassword(pwd);
        String ret = emailSender.getPassword();

        assertThat(ret.equals(pwd)).isTrue();
    }

    @Test
    public void testSendMail() {
        EmailSender emailSender = EmailSender.getInstance();
        GreenMail greenMail = new GreenMail(new ServerSetup[]{new ServerSetup(3026,"127.0.0.1",ServerSetup.PROTOCOL_SMTP)});

        try {
            greenMail.start();

            emailSender.setSession(greenMail.getSmtp().createSession());

            final String subject = GreenMailUtil.random();
            final String body = GreenMailUtil.random();

            emailSender.sendEmail("green@raf.rs",subject,body);

            assertThat(greenMail.waitForIncomingEmail(5000,1)).isTrue();

            Message[] messages = greenMail.getReceivedMessages();
            assertThat(messages.length).isEqualTo(1);

            assertThat(messages[0].getSubject()).isEqualTo(subject);
            assertThat(GreenMailUtil.getBody(messages[0]).trim()).isEqualTo(body);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            greenMail.stop();
        }
    }

}
