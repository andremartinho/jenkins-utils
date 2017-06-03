package com.olx.deploy

class Deploy implements Serializable{

    def steps
    def valuesToUse

    Deploy(steps) {
        this.steps = steps
    }

    def setValues(){
        def distributionGroupsFilePath = ""
        def distributionGroups = ""
        def distributionEmails = ""

        if(steps.env.DISTRIBUTION_GROUPS_FILE_PATH.length() > 0){
            distributionGroupsFilePath = "-PDISTRIBUTION_GROUPS_FILE_PATH='${steps.env.DISTRIBUTION_GROUPS_FILE_PATH}'"
        }

        if(steps.env.DISTRIBUTION_GROUPS.length() > 0){
            distributionGroups = "-PDISTRIBUTION_GROUPS='${steps.env.DISTRIBUTION_GROUPS}'"
        }

        if(steps.env.DISTRIBUTION_EMAILS.length() > 0){
            distributionEmails = "-DISTRIBUTION_EMAILS='${steps.env.DISTRIBUTION_EMAILS}'"
        }

        valuesToUse = "${steps.env.CI_ENVIRONMENT_FILE} -PDISTRIBUTION_NOTES='${steps.env.DISTRIBUTION_NOTES}' ${distributionGroupsFilePath} ${distributionGroups} ${distributionEmails}"
    }

    def deployTo(flavour, variant = "Release"){
        steps.sh "./gradlew ${flavour}${variant}DeliverTask ${valuesToUse}"
    }

    def deployAll(){
        steps.sh ".gradlew deliverAllFlavoursTask ${valuesToUse}"
    }

}
