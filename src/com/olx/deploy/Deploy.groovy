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
            distributionGroups = "-PDISTRIBUTION_GROUPS=\"${steps.env.DISTRIBUTION_GROUPS}\""
        }

        if (steps.env.DISTRIBUTION_EMAILS.length() > 0) {
            distributionEmails = "-PDISTRIBUTION_EMAILS=\"${steps.env.DISTRIBUTION_EMAILS}\""
        }

        valuesToUse = "${steps.env.CI_ENVIRONMENT_FILE} " +
                "-PDISTRIBUTION_NOTES=\"${steps.env.DISTRIBUTION_NOTES}\" ${distributionGroups} ${distributionEmails}"
    }

    def deploy(weeklyDistribution,flavours,variant = "Release"){
        if(weeklyDistribution){
            deployUsingWeeklyBuilds(flavours,variant)
        }else{
            if (flavours == "All") {
                deployAll()
            }else{
                deployFlavours(flavours,variant)
            }
        }
    }

    def deployFlavours(flavoursToIterate, variant) {
        steps.echo "Flavours to iterate = ${flavoursToIterate}"

        def flavoursList = flavoursToIterate.tokenize(',')
        for (String flavour : flavoursList){
            steps.echo "Deploying flavour ${flavour}${variant}"
            steps.sh "./gradlew ${flavour}${variant}DeliverTask ${valuesToUse}"
        }
    }

    def deployUsingWeeklyBuilds(flavoursToIterate, variant){
        if(flavoursToIterate == "All"){
            flavoursToIterate = steps.sh(script:"for file in app/src/*/;\ndo\n[[ \$(basename \$file) =~ ^(androidTest|main|test)\$ ]] && continue\necho \$(basename \$file)\ndone" +
                    " | xargs -n 1" + // Removes all the whitespaces
                    " | tr '\n' ','" + // Substitutes the break lines for ","
                    " | sed 's/\\(.*\\),/\\1/'" //Removes the last ","
                    , returnStdout: true)

            steps.echo "All flavours = ${flavoursToIterate}"
        }

        def flavoursList = flavoursToIterate.tokenize(',')

        for (String flavour : flavoursList){
            steps.echo "Deploying flavour ${flavour}${variant}"
            steps.sh "./gradlew ${flavour}${variant}DeliverTask ${getDistributionGroupsFilePath(flavour)} ${valuesToUse}"
        }
    }

    def deployAll() {
        steps.echo "Deploying all Flavours of the App"
        steps.sh "./gradlew deliverAllFlavoursTask ${valuesToUse}"
    }

    def getDistributionGroupsFilePath(variant){
        def distributionGroupsFilePath = ""

        if (steps.params.USE_WEEKLY_DISTRIBUTION_FILE_PATH) {
            distributionGroupsFilePath = "-PDISTRIBUTION_GROUPS_FILE_PATH=ci_cd/weekly/${variant}/group_aliases.txt"
        }

        return distributionGroupsFilePath
    }



}
