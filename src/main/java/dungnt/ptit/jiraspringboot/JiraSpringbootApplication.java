package dungnt.ptit.jiraspringboot;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.google.common.collect.Lists;
import io.atlassian.util.concurrent.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JiraSpringbootApplication implements CommandLineRunner {
    @Autowired
    private JiraRestClient restClient;

    public List<Status> getStatuses() {
        try {
            final Iterable<Status> statuses = restClient.getMetadataClient().getStatuses()
                .get(5000, TimeUnit.SECONDS);
            return Lists.newArrayList(statuses);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(JiraSpringbootApplication.class, args);
    }

    public String createIssue(String projectKey, Long issueType, String issueSummary) {
        IssueRestClient issueClient = restClient.getIssueClient();
        IssueInput newIssue = new IssueInputBuilder(
            projectKey, issueType, issueSummary).build();
        return issueClient.createIssue(newIssue).claim().getKey();
    }

    public Issue getIssue(String issueKey) {
        return restClient.getIssueClient()
            .getIssue(issueKey)
            .claim();
    }

    public String searchJql(){
        SearchRestClient searchClient = restClient.getSearchClient();
        Promise<SearchResult> resultPromise = searchClient.searchJql("project=SCRUM");
        return resultPromise + "";
    }

    public void getUser(){
        // Invoke the JRJC Client
        Promise<User> promise = restClient.getUserClient().getUser("dungnguyen150699");
        User user = promise.claim();
        // Print the result
        System.out.println(String.format("Your user's email address is: %s\r\n", user.getEmailAddress()));

    }

    @Override
    public void run(String... args) throws Exception {
        ProjectRestClient projectClient = restClient.getProjectClient();
        Promise<Iterable<BasicProject>> allProjects = projectClient.getAllProjects();
        allProjects.done(item -> item.forEach(i-> System.out.println(i.getName())));

        Iterable<BasicProject> basicProjects = allProjects.get();
        Iterator<BasicProject> basicProjectIterator = basicProjects.iterator();
        while (basicProjectIterator.hasNext()){
            System.out.println(basicProjectIterator.next().getName());
        }
//        createIssue("SCRUM",1l,"xx");
//        Issue issue = getIssue("ST-3");
//        System.out.println(issue.getKey());
//        System.out.println(issue.getAttachments());
//        searchJql();
//        getStatuses();
//        getUser();
    }
}
