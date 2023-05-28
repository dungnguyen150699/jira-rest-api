package dungnt.ptit.jiraspringboot;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class JiraConfiguration {

    @Bean
    public JiraRestClient getJiraRestClient() throws URISyntaxException {
        return new AsynchronousJiraRestClientFactory()
            .createWithBasicHttpAuthentication(new URI("https://jiradungnt150699.atlassian.net"),
                "tiendungac99@gmail.com", "ATATT3xFfGF013HbyfsOOLEPrJB7DxXlVFiMI_1tO1acG1lxbVtUs9pxktFWgArnMW1vgeiR5WNN2l85dRozfN-W32CQqDMONOtHBFgwN65xL40p9OE4Xm_h4MUJPRJaNOS-zv6Y99gd7BwEFFyXqVLCxfyNDCX-IvsoipFCj60T8d7EZbRFysk=C8F3DEB3");
    }
}
