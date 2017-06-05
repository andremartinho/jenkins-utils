package com.olx.jira

class FindTickets implements Serializable{

    def steps

    FindTickets(steps) {
        this.steps = steps
    }

    def checkTickets(branch,ticketNomenclature,transitionToMake,gitUrl){

        def tickets = getTickets(branch,ticketNomenclature)

        if(tickets.size() > 0){
            steps.jiraTransitionIssues(steps,
                    "https://naspersclassifieds.atlassian.net/rest/api/2/",
                    "${steps.env.JIRA_AUTH_TOKEN}",
                    "issue in (${tickets})",
                    "${transitionToMake}",
                    "[This was moved as part of PR #${steps.env.CHANGE_ID}|${gitUrl}${steps.env.CHANGE_ID}]")
        }else{
            steps.echo "No tickets to process. Moving forward"
        }
    }

    def checkTicketsInQA(branch,ticketNomenclature,transitionToMake){

        def tickets = getTickets(branch,ticketNomenclature)

        if(tickets.size() > 0) {
            //Transition issues that are on Ready for QA in the Project
            steps.jiraTransitionIssues(this,
                    "https://naspersclassifieds.atlassian.net/rest/api/2/",
                    "${steps.env.JIRA_AUTH_TOKEN}",
                    "project = GVRECFAND AND status = 'Ready for QA' AND issue in(${tickets})",
                    "${transitionToMake}",
                    "This was moved as part of build ${steps.env.BUILD_NUMBER}")
        }else{
            steps.echo "No tickets to process. Moving forward"
        }
    }

    private getTickets(branch, ticketNomenclature){
        return steps.sh(script: "git log origin/${branch}..HEAD" + //Gets all the commits from a branch to HEAD
                " | grep ${ticketNomenclature}" + // Gets all the references to a specific nomenclature (ex: GVRECFAND)
                " | xargs -n 1" + // Removes all the whitespaces
                " | tr '\n' ','" + // Substitutes the break lines for ","
                " | sed 's/\\(.*\\),/\\1/'" //Removes the last "," from the String
                , returnStdout: true)
    }
}
