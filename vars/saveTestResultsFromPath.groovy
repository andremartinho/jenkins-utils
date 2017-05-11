#!groovy
/**
 * Tries to save the reports in the path provided if there was any problem detected prints the
 * exception
 *
 * @param path of where the specific tests to save are. Should be JUnit-style XML reports
 * ex:(build/test-results/*{@literal*}/*.xml)
 *
 * more info at https://jenkins.io/doc/pipeline/tour/tests-and-artifacts/
 *
 */
def call(String path){
    try{
        junit path
    }catch (exception) {
        sh "echo ${exception.getMessage()}"
    }
}