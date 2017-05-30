package com.olx.testing

class UnitTesting implements Serializable{

    def steps

    UnitTesting(steps) {
        this.steps = steps
    }

    /**
     * Runs all the tests for the application
     * @return
     */
    def runUnitTests(){
        //Runs all the unit tests for the debug flavours
        try {
            steps.sh "./gradlew runAllUnitTests ${steps.env.CI_ENVIRONMENT_FILE}"
            steps.saveTestResultsFromPath("app/build/test-results/**/*.xml")
        }catch (Exception exception){
            //If the tests fail save the results but rethrow the exception so the build fails
            steps.saveTestResultsFromPath("app/build/test-results/**/*.xml")
            throw exception
        }
    }


}
