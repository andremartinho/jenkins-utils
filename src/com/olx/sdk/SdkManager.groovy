package com.olx.sdk

class SdkManager implements Serializable{

    def steps

    SdkManager(steps) {
        this.steps = steps
    }

    /**
     * Assumes that you have sdkmanager on the Path and ready to use for this call to work properly
     *
     */
    def callSdkManager() {
        //Calls the sdk manager to update all the libraries of android
        steps.sh '(while sleep 3; do echo "y"; done) | sdkmanager --update --include_obsolete'

        //Calls the sdk manager to accept all the licenses that are missing
        steps.sh '(while sleep 3; do echo "y"; done) | sdkmanager --licenses --include_obsolete'
    }
}
