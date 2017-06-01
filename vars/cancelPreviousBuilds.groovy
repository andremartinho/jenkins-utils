/**
 *
 * Verifies if a job with the same name is running and if it's a lower build Number
 * stops it
 *
 * credit goes to https://github.com/cloudbees/jenkins-scripts/blob/master/cancel-builds-same-job.groovy
 *
 */
def call(steps){
    def jobName = env.JOB_NAME
    def buildNum = env.BUILD_NUMBER.toInteger()

    def job = Jenkins.instance.getItemByFullName(jobName)
    for (build in job.builds) {
        if (!build.isBuilding()) { continue }
        if (buildNum == build.getNumber().toInteger()) {
            steps.echo "Same build ignore"
            continue
        }
        if(build.getNumber().toInteger() < buildNum){
            steps.echo "Stopping old build"
            build.doStop()
        }
    }
}




