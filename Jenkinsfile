pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    tools {
        maven 'maven'
        jdk 'jdk21'
    }
    stages {
        stage('Verify') {
            steps {
                sh 'mvn --version'
                sh 'java --version'
            }
        }

		stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests --no-transfer-progress'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deliver') {
            steps {
                sh './jenkins/scripts/deliver.sh'
            }
        }
    }
}