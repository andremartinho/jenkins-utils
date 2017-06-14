package com.olx.build

class Assemble implements Serializable{

    def steps

    Assemble(steps) {
        this.steps = steps
    }

    def assembleReleaseBuildWithTask(task){
        steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} ${task}"
    }

    def assembleBuildsWithNames(flavours, variant){
        def flavoursList = flavours.tokenize(',')

        for (String flavour : flavoursList){
            steps.echo "Assemble ${flavour}${variant}"
            steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} assemble${flavour}${variant}"
        }
    }

    def assembleBuilds(flavours, variant = "Release"){
        if(flavours == "All"){
            assembleReleaseBuildWithTask("AssembleAllReleases")
        }else{
            assembleBuildsWithNames(flavours,variant)
        }
    }

    def archiveResults(){
        steps.archiveFilesFromPath("app/build/outputs/apk/*.apk")
    }



}
