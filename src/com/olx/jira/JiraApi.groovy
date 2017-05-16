package com.olx.jira

import com.olx.jira.json.Body
import com.olx.jira.response.JiraIssue
import com.olx.jira.validator.JiraValidator
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import com.olx.jira.json.CommentAdd
import com.olx.jira.json.Transition
import com.olx.jira.json.UpdateJson

import groovy.json.internal.LazyMap
import sun.net.www.protocol.https.HttpsURLConnectionImpl

import javax.net.ssl.HttpsURLConnection

class JiraApi implements Serializable{

    public static final String SEARCH = "search"
    public static final String TRANSITIONS = "/transitions"

    public String authorization
    public String jiraUrl

    JiraValidator jiraValidator

    JiraApi(jiraUrl,authorization) {
        jiraValidator = new JiraValidator()
        this.authorization = authorization
        this.jiraUrl = jiraUrl
    }

    void transitionIssuesUsingSearch(pipeline,String queryParameters,String transitionToImplement,String comment = "") throws Exception {

        jiraValidator.validateJiraObject(jiraUrl,authorization)

        String request = jiraUrl + SEARCH + getQueryParameters(queryParameters)

        pipeline.echo("Doing request " + request)

        HttpsURLConnectionImpl connection = new URL(request).openConnection() as HttpsURLConnectionImpl

        connection.addRequestProperty("Accept","application/json")
        connection.addRequestProperty("Authorization","Basic " + authorization)

        int connectionCode = connection.getResponseCode()

        if(connectionCode >= 200 && connectionCode < 300){
            InputStream inputStream = connection.getInputStream()
            Object object = new JsonSlurper().parse(inputStream)

            pipeline.echo("Result = " + object)

            List<LazyMap> jiraIssueList = object.issues

            String updateComment = createJsonObject(transitionToImplement,comment)

            for (int ii = 0; ii < jiraIssueList.size(); ii++){
                JiraIssue jiraIssue = jiraIssueList[ii] as JiraIssue
                String newRequest = jiraIssue.self + TRANSITIONS

                pipeline.echo("Doing new Request " + newRequest + " with transition = " + transitionToImplement)

                HttpsURLConnection newConnection = new URL(newRequest).openConnection() as HttpsURLConnection

                newConnection.addRequestProperty("Accept","application/json")
                newConnection.addRequestProperty("Content-Type","application/json")
                newConnection.addRequestProperty("Authorization","Basic " + authorization)

                newConnection.setRequestMethod("POST")
                newConnection.setUseCaches(false)
                newConnection.setDoOutput(true)
                newConnection.setDoInput(true)
                DataOutputStream outStream = new DataOutputStream(newConnection.getOutputStream())
                outStream.writeBytes(updateComment)
                outStream.flush()
                outStream.close()

                newConnection.connect()
                if(newConnection.responseCode != 204){
                    InputStream newInputStream = newConnection.getInputStream()
                    pipeline.echo("Error Occurred " + new JsonSlurper().parse(newInputStream))
                    newInputStream.close()
                }else{
                    pipeline.echo("Ticket " + jiraIssue.id  + " transitioned successfully")
                }
                newConnection.disconnect()
            }
            inputStream.close()
        }else{
            throw new Exception(connection.getResponseCode() + " " + connection.getResponseMessage())
        }

        connection.disconnect()

    }

    private static String getQueryParameters(String searchQuery) {
        if(searchQuery != null){

            String query = "?jql=" + URLEncoder.encode(searchQuery,"UTF-8")

            query += "&fields=self&maxResults=200"

            return query
        }

        return ""
    }

    private static String createJsonObject(transitionToImplement, comment){

        UpdateJson updateJson = new UpdateJson()

        if(comment != ""){
            ArrayList<CommentAdd> commentAdds = new ArrayList<>()
            commentAdds.add(new CommentAdd(add: new Body(body: comment)))
            updateJson.metaClass.update = [comment:commentAdds]
        }

        updateJson.transition = new Transition(id:transitionToImplement)

        return JsonOutput.toJson(updateJson)
    }
}
