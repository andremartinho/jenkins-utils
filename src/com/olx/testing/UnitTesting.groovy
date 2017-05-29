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
        steps.sh "./gradlew runAllUnitTests ${steps.env.CI_ENVIRONMENT_FILE}"

        steps.saveTestResultsFromPath("app/build/test-results/**/*.xml")
    }


}
