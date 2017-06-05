package com.olx.deploy

class Deploy implements Serializable {

    def steps
    def valuesToUse

    Deploy(steps) {
        this.steps = steps
    }

    def setValues() {
        def distributionGroups = ""
        def distributionEmails = ""

        if (steps.env.DISTRIBUTION_GROUPS.length() > 0) {
            distributionGroups = "-PDISTRIBUTION_GROUPS='${steps.env.DISTRIBUTION_GROUPS}'"
        }

        if (steps.env.DISTRIBUTION_EMAILS.length() > 0) {
            distributionEmails = "-PDISTRIBUTION_EMAILS='${steps.env.DISTRIBUTION_EMAILS}'"
        }

        valuesToUse = "${steps.env.CI_ENVIRONMENT_FILE} -PDISTRIBUTION_NOTES='${steps.env.DISTRIBUTION_NOTES}' ${distributionGroups} ${distributionEmails}"
    }

    def deployFlavours(flavoursToIterate, variant = "Release") {
        for (String flavour : flavoursToIterate){
            steps.sh "./gradlew ${flavour}${variant}DeliverTask ${valuesToUse}"
        }
    }

    def deployUsingWeeklyBuilds(flavoursToIterate, variant = "Release"){
        if(flavoursToIterate == "All"){
            flavoursToIterate = steps.sh(script:"for file in app/src/*/; do [[ \$(basename \$file) =~ ^(androidTest|main|test)\$ ]] && continue echo \$(basename \$file) done",returnStdout: true).tokenize(',')
        }

        for (String flavour : flavoursToIterate){
            steps.sh "./gradlew ${flavour}${variant}DeliverTask ${getDistributionGroupsFilePath(flavour)} ${valuesToUse}"
        }
    }

    def deployAll() {
        steps.sh "./gradlew deliverAllFlavoursTask ${valuesToUse}"
    }

    def getDistributionGroupsFilePath(variant){
        def distributionGroupsFilePath = ""

        if (steps.env.USE_WEEKLY_DISTRIBUTION_FILE_PATH) {
            distributionGroupsFilePath = "-PDISTRIBUTION_GROUPS_FILE_PATH=ci_cd/weekly/${variant}/group_aliases.txt"
        }

        return distributionGroupsFilePath
    }



}
