package com.olx.analysis

class Lint implements Serializable{

    def steps

    Lint(steps) {
        this.steps = steps
    }

    def runAnalysis(){
        //Runs the Android Lint analysis
        steps.sh "./gradlew lintTask ${steps.env.CI_ENVIRONMENT_FILE}"
    }

    def archiveLintAnalysis(String path){
        steps.androidLint canRunOnFailed: true, defaultEncoding: '', healthy: '', pattern: path, unHealthy: ''
    }

    def runAndArchive(String path){
        def timedException = null
        try {
            runAnalysis()
        }catch (exception){
            timedException = exception
        }finally{
            archiveLintAnalysis(path)
            if(timedException != null)
                throw timedException
        }
    }

}
