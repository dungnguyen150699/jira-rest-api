package dungnt.ptit.jiraspringboot;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.OptionalIterable;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
//import com.atlassian.jira.rest.client.api.AuthenticationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dungnt.ptit.jiraspringboot.Constants.ISSUE_TYPE.EPIC;

public class JiraService {

    private static final String JIRA_URL = "https://jiradungnt150699.atlassian.net";
    private static final String JIRA_USERNAME = "tiendungac99@gmail.com";
//    private static final String JIRA_USERNAME = "dungnguyen150699";
    private static final String JIRA_PASSWORD = "ATATT3xFfGF013HbyfsOOLEPrJB7DxXlVFiMI_1tO1acG1lxbVtUs9pxktFWgArnMW1vgeiR5WNN2l85dRozfN-W32CQqDMONOtHBFgwN65xL40p9OE4Xm_h4MUJPRJaNOS-zv6Y99gd7BwEFFyXqVLCxfyNDCX-IvsoipFCj60T8d7EZbRFysk=C8F3DEB3";
//    private static final String JIRA_PASSWORD = "b17dccn160";

    private static JiraRestClient jiraRestClient = new AsynchronousJiraRestClientFactory()
        .createWithBasicHttpAuthentication(URI.create(JIRA_URL), JIRA_USERNAME, JIRA_PASSWORD);
    private static final List<IssueType> allIssueType = getAllIssueType();
    private static final List<BasicProject> allBasicProject = getAllProjects();
    private static final List<User> allUser = getAllUser();

    public static List<BasicProject> getAllProjects() {
        List<BasicProject> projects = new ArrayList<>();
        try {
            Iterable<BasicProject> allProjects = jiraRestClient.getProjectClient().getAllProjects().claim();
            for (BasicProject project : allProjects) {
                projects.add(project);
            }
//            jiraRestClient.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public static BasicProject getBasicProjectByKey(String key){
        if(key==null || key.isEmpty()) return null;
        Optional<BasicProject> optionalBasicProject = allBasicProject.stream().filter(item -> key.equals(item.getKey())).findFirst();
        return optionalBasicProject.orElse(null);
    }

    public static List<IssueType> getAllIssueType(){
        List<BasicProject> basicProjects = new ArrayList<>();
        List<IssueType> issueTypeList = new ArrayList<>();
//        IssueRestClient issueRestClient = restClient.getIssueClient();
        ProjectRestClient projectRestClient = jiraRestClient.getProjectClient();
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

    public static List<User> getAllUser(){
        List<User> users = new ArrayList<>();
        UserRestClient userRestClient = jiraRestClient.getGroupClient().findGroups().claim().iterator().next().;
        Iterator<User> basicUserIterator = userRestClient.findUsers("dungnguyen150699").claim().iterator();
        iteratorToList(users,basicUserIterator);
        User user = userRestClient.getUser(URI.create("https://jiradungnt150699.atlassian.net/jira/people/712020:dc335e15-773c-4150-97f4-66957ceac1f2"))
            .claim();
        users.add(user);
        return users;
    }

    public static <T> void iteratorToList(List<T> lists, Iterator<T> iterator){
        while (iterator.hasNext()){
            lists.add(iterator.next());
        }
    }

    public static IssueType getIssueByType(Constants.ISSUE_TYPE issue_type){
        Optional<IssueType> optionalIssueType = allIssueType.stream()
            .filter(issueType -> issue_type.getCode().equals(issueType.getName()))
            .findFirst();
        return optionalIssueType.orElse(null);
    }

    public static BasicIssue createNewIssue(BasicProject basicProject, IssueType issueType, String description)
    {
        BasicIssue basicIssue = null;
        IssueRestClient issueRestClient = jiraRestClient.getIssueClient().linkIssue();
        BasicUser basicUser = getBasicUserByName("dungnguyen150699");
        if(basicUser != null){
            IssueInput input = new IssueInputBuilder(basicProject,issueType)
                .setDescription(description)
                .setDueDate(new DateTime(new Date().getTime() + 1000*60*60*24*7))
                .setAssignee(basicUser)
                .(new FieldInput())
                .build();
            basicIssue = issueRestClient.createIssue(input).claim();
        }
        return basicIssue;
    }

    public static BasicUser getBasicUserByName(String name){
        if(name==null || name.isEmpty()) return null;
        Optional<User> optionalUser = allUser.stream().filter(item -> name.equals(item.getName())).findFirst();
        return optionalUser.orElse(null);
    }

    public static void main(String[] args) {
        getAllProjects();
        BasicIssue basicIssue = createNewIssue(allBasicProject.get(0),getIssueByType(EPIC),"this is description epic");
        System.out.println(basicIssue.getKey());
        System.out.println(basicIssue.getSelf());
    }
}
