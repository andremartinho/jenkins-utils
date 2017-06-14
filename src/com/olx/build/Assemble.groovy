package com.olx.build

class Assemble implements Serializable{

    def steps

    Assemble(steps) {
        this.steps = steps
    }

    def assembleBuildsWithVariant(variant = "Release"){
        steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} assemble${variant}"
    }

    def assembleReleaseBuildWithTask(task){
        steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} ${task}"
    }

    def assembleBuildsWithNames(flavours, variant = "Release"){
        def flavoursList = flavours.tokenize(',')

        for (String flavour : flavoursList){
            steps.echo "Assemble ${flavour}${variant}"
            steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} assemble${flavour}${variant}"
        }
    }

    def archiveResults(){
        steps.archiveFilesFromPath("app/build/outputs/apk/*.apk")
    }
}
