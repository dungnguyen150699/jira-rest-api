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
            .createWithBasicHttpAuthentication(new URI("https://jiradungnt150699.atlassian.net/"),
                "dungnguyen150699", "b17dccn160");
    }
}
