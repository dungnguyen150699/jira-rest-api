package dungnt.ptit.jiraspringboot;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.springframework.stereotype.Service;
//import com.atlassian.jira.rest.client.api.AuthenticationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JiraService {

    private static final String JIRA_URL = "https://jiradungnt150699.atlassian.net";
    private static final String JIRA_USERNAME = "tiendungac99@gmail.com";
//    private static final String JIRA_USERNAME = "dungnguyen150699";
    private static final String JIRA_PASSWORD = "ATATT3xFfGF04Kw7kqY09AaquGF1RCYEbQMh53xBTY5qOTXejuMMpgU5RcEM0qtzhArFZiAcUg1CWGpB8UOtMuceKDFOjaaQZjfrOYkNCpRgkFTkw7HmA2hkuLIAvmKu7FX76INAZuhfYbcT1yhFMZuo4K1b6UBlnZAsv-JfkeK5_f5Jsok0Bhk=671CF2F1";
//    private static final String JIRA_PASSWORD = "b17dccn160";

    public static List<BasicProject> getAllProjects() {
        List<BasicProject> projects = new ArrayList<>();
        try {
            URI jiraServerUri = new URI(JIRA_URL);
            JiraRestClient restClient = new AsynchronousJiraRestClientFactory()
                    .createWithBasicHttpAuthentication(jiraServerUri, JIRA_USERNAME, JIRA_PASSWORD);
            Iterable<BasicProject> allProjects = restClient.getProjectClient().getAllProjects().claim();
            for (BasicProject project : allProjects) {
                projects.add(project);
            }
            restClient.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public static void main(String[] args) {
        getAllProjects();
    }
}
