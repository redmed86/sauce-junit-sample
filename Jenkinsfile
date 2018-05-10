node('master') {
    // Mark the code checkout 'stage'....
    stage ('Checkout'){
      checkout scm
    }

    stage('Test') {
      sauce('derek_sauce_key') {
          sh "mvn clean test"
      }
    }

    stage('Collect Results'){
      step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
      step([$class: 'SauceOnDemandTestPublisher'])
    }
}
