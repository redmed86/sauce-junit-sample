node('master') {
    tool name: 'Maven 3.2.2', type: 'maven'

    // Mark the code checkout 'stage'....
    stage ('Checkout'){
      checkout scm
    }

    stage('Test') {
      sauce('derek_sauce_key') {
        def mvnHome = tool 'Maven 3.2.2'
        sh "${mvnHome}/bin/mvn clean test"
      }
    }

    stage('Collect Results'){
      step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
      step([$class: 'SauceOnDemandTestPublisher'])
    }
}
