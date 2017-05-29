package com.olx.result

class Result implements Serializable{

    def steps

    Result(steps) {
        this.steps = steps
    }

    def finishBuild(resultStatus){
        steps.notifyBuild steps,resultStatus
    }
}
