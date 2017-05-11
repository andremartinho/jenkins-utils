#!groovy
/**
 * Tries to save the files in the path provided if there was any problem detected prints the
 * exception
 *
 * @param path of where the specific files to save are. ex:(build/test-results/*{@literal*}/*.xml)
 *
 * more info at https://jenkins.io/doc/pipeline/tour/tests-and-artifacts/
 *
 */
def call(String path){
    try{
        archiveArtifacts artifacts: path , fingerprint: true
    }catch (exception) {
        sh "echo ${exception.getMessage()}"
    }
}
