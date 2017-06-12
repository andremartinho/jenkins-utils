package com.olx.preparation

class Preparation implements Serializable{

    def steps

    Preparation(steps) {
        this.steps = steps
    }

    def prepareBuild(boolean shouldRefreshDependencies = true){

        checkoutScm()

        notifyBuildStarted()

        prepareEnvVariables()

        if(shouldRefreshDependencies){
            cleanAndRefreshDependencies()
        }else{
            clean()
        }
    }

    def clean() {
        steps.sh "./gradlew clean ${steps.env.CI_ENVIRONMENT_FILE}"
    }

    def checkoutScm(){
        steps.checkout steps.scm
    }

    def notifyBuildStarted(){
        steps.notifyBuild steps,"STARTED"
    }

    def prepareEnvVariables(){

        // Setting Java version
        steps.env.JAVA_HOME = "${steps.tool 'jdk-8u112'}"
        steps.env.PATH = "${steps.env.JAVA_HOME}/bin:${steps.env.PATH}"

        // Setting Android Path
        //Adding tools,platform-tools and tools/bin to PATH
        steps.env.PATH = "${steps.env.ANDROID_HOME}:${steps.env.ANDROID_HOME}/tools:${steps.env.ANDROID_HOME}/platform-tools:${steps.env.ANDROID_HOME}/tools/bin:${steps.env.PATH}"

        //Setting path for Keystore
        steps.env.CI_ENVIRONMENT_FILE="-PCI_ENVIRONMENT_FILE=${steps.env.KEYSTORES}keystore_cd.gradle"

        //Add execute permission to gradlew
        steps.sh 'chmod +x gradlew'
    }

    def cleanAndRefreshDependencies(){
        //Cleans the build and the dependencies
        steps.sh "./gradlew clean --refresh-dependencies ${steps.env.CI_ENVIRONMENT_FILE}"
    }


}
