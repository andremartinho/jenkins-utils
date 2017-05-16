package com.olx.jira.response

class JiraIssue {
    String id
    String self

    JiraIssue(Object json) {
        id = json.id
        self = json.self
    }
}
