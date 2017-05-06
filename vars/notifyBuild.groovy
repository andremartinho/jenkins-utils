#!groovy
/**
 * Simple information to send for a slack hook previously configured
 *
 * @param script with all the env data needed to send information
 * @param buildStatus the current build status that we are sending
 */
def call(script, String buildStatus = 'STARTED') {
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESS'

    // Default values
    summary = "${buildStatus}: Job - ${script.env.JOB_NAME} #${script.env.BUILD_NUMBER} (<${script.env.BUILD_URL}|Open>)"

    // Override default values based on build status
    if (buildStatus == 'STARTED') {
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESS') {
        colorCode = '#00FF00'
    } else {
        colorCode = '#FF0000'
    }

    // Send notifications
    slackSend (color: colorCode, message: summary)
}