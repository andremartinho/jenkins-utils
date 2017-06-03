package com.olx.build

class Assemble {

    def steps

    Assemble(steps) {
        this.steps = steps
    }

    def assembleBuilds(variant = "Release"){
        steps.sh "./gradlew ${steps.env.CI_ENVIRONMENT_FILE} -PBUILD_NUMBER=${steps.env.BUILD_NUMBER} assemble${variant}"
    }

    def archiveResults(){
        steps.archiveFilesFromPath("app/build/outputs/apk/*.apk")
    }
}
