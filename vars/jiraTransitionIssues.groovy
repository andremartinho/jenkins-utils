#!groovy
import com.olx.jira.JiraApi

/**
 * Calls a specific instance of Jira and performs a transition of issues
 * depending on the search provided.
 *
 * @param apiUrl Url for the Api we want to consult (for example: https://jira.atlassian.com/rest/api/)
 * @param authorisation a base64 encoded string with your authorization
 * @param searchQuery a jqlQuery to make to the Api previously used
 * don't need to url encode it because it's already done on the code
 * @param transitionId to apply to the ticket
 * @param comment that we want to add to the transition for the items.
 * The default is not having any comment
 *
 */
def call(pipeline,String apiUrl,String authorisation,String searchQuery, String transitionId, String comment = "") {

    JiraApi jiraApi = new JiraApi(apiUrl,authorisation)

    jiraApi.transitionIssuesUsingSearch(pipeline,searchQuery, transitionId, comment)
}