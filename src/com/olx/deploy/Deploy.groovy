package com.olx.deploy

class Deploy implements Serializable{

    def steps
    def valuesToUse

    Deploy(steps) {
        this.steps = steps
    }

    def setValues(){
        def distributionNotes = "'Android Realestate (Version ${steps.env.BUILD_NUMBER})\n\nThis is an automated Jenkins QA Build\n\nPlease verify the issues to review on https://naspersclassifieds.atlassian.net/issues/?filter=23903\n\nItems already approved but awaiting production deployment https://naspersclassifieds.atlassian.net/issues/?filter=23902'"
        def distributionNotesProperty = "-PDISTRIBUTION_NOTES=${distributionNotes}"
        def completeBuildString = "-PBUILD_NUMBER=${steps.env.BUILD_NUMBER}"
        valuesToUse = "${steps.env.CI_ENVIRONMENT_FILE} ${completeBuildString} ${distributionNotesProperty}"
    }

    def deployTo(flavour){
        steps.sh "./gradlew ${flavour}ReleaseDeliverTask ${valuesToUse} -PDISTRIBUTION_GROUPS=ci_cd/weekly/${flavour}/group_aliases.txt"
    }

    def archiveResults(){
        steps.archiveFilesFromPath("app/build/outputs/apk/*.apk")
    }
}
