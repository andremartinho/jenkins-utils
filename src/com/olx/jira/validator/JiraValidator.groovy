package com.olx.jira.validator

class JiraValidator {


    private static validateJiraUrl(String url) {
        URL urlObject = null

        try {
            urlObject = new URL(url)
        } catch (MalformedURLException exception) {
            throw new Exception("Badly formated URL (" + exception.message + ")")
        }

        try {
            urlObject.toURI()
        } catch (URISyntaxException exception) {
            throw new Exception("URI syntax (" + exception.message + ")")
        }
    }

    private static validateAuthorization(String authorization){

        Base64.Decoder decoder = Base64.getDecoder()

        try {
            decoder.decode(authorization)
        }catch (IllegalArgumentException exception){
            throw new Exception("The Authorization key is not a valid base64 encoded String (" + exception.message + ")")
        }

    }

    def static validateJiraObject(String jiraUrl,String jiraAuthorization){

        validateJiraUrl(jiraUrl)

        validateAuthorization(jiraAuthorization)

    }
}
