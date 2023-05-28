package dungnt.ptit.jiraspringboot;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.OptionalIterable;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
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

import java.util.ArrayList;
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

    public List<IssueType> getAllIssueType(){
        List<BasicProject> basicProjects = new ArrayList<>();
        List<IssueType> issueTypeList = new ArrayList<>();
//        IssueRestClient issueRestClient = restClient.getIssueClient();
        ProjectRestClient projectRestClient = restClient.getProjectClient();
        Iterator<BasicProject> projectIterator = projectRestClient.getAllProjects().claim().iterator();
        iteratorToList(basicProjects,projectIterator);

        if(!basicProjects.isEmpty()){
            BasicProject basicProjectFirst = basicProjects.get(0);
            Project project = projectRestClient.getProject(basicProjectFirst.getKey()).claim();
            final OptionalIterable<IssueType> issueTypes = project.getIssueTypes();
            if(issueTypes.isSupported()){
                final Iterator<IssueType> iterator = issueTypes.iterator();
                iteratorToList(issueTypeList,iterator);
            }
        }
        return issueTypeList;
    }

    public <T> void iteratorToList(List<T> lists, Iterator<T> iterator){
        while (iterator.hasNext()){
            lists.add(iterator.next());
        }
    }


    @Override
    public void run(String... args) throws Exception {
        final List<IssueType> allIssueType = getAllIssueType();
        allIssueType.stream().forEach(issueType -> {
            System.out.println(issueType.getName());
        });
    }
}
