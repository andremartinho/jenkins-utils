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

    def runAndArchive(flavours = "All",variant = "Release",String path = "app/build/reports/lint-results.xml"){
        def timedException = null
        try {
            if(flavours == "All"){
                runAnalysis()
            }else{
                runFlavours(flavours,variant)
            }
        }catch (exception){
            timedException = exception
        }finally{
            archiveLintAnalysis(path)
            if(timedException != null)
                throw timedException
        }
    }

    def runFlavours(flavours, variant) {
        def flavoursList = flavours.tokenize(',')

        for (String flavour : flavoursList){
            steps.echo "Running lint task for ${flavour}${variant}"
            steps.sh "./gradlew lint${flavour}${variant} ${steps.env.CI_ENVIRONMENT_FILE}"
        }
    }
}
