package com.olx.testing

class UnitTesting implements Serializable{

    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    /**
     * Defaults to run all the release unit tests for the application
     * but you can choose to build only specific flavours and build types
     */
    def runUnitTests(flavours = "All",variant = "Release"){

        try {
            if(flavours == "All"){
                runAllUnitTests()
            }else{
                runSpecificFlavours(flavours,variant)
            }
            steps.saveTestResultsFromPath("app/build/test-results/**/*.xml")
        }catch (Exception exception){
            //If the tests fail save the results but rethrow the exception so the build fails
            steps.saveTestResultsFromPath("app/build/test-results/**/*.xml")
            throw exception
        }
    }

    def runAllUnitTests(){
        steps.sh "./gradlew runAllUnitTests ${steps.env.CI_ENVIRONMENT_FILE}"
    }

    def runSpecificFlavours(flavours,variant){
        def flavoursList = flavours.tokenize(',')

        for (String flavour : flavoursList){
            steps.echo "Running unitTests for ${flavour}${variant}"
            steps.sh "./gradlew test${flavour}${variant}UnitTest ${steps.env.CI_ENVIRONMENT_FILE}"
        }
    }


}
