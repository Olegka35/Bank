pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['test', 'prod'], description: 'Environment')
    }

    environment {
        COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        SERVICES = "account-service cash-service transfer-service frontend gateway notification-service"
    }

    stages {

        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('build') {
            steps {
                sh "./gradlew clean build"
            }
        }

        stage('tag') {
            steps {
                script {
                    SERVICES.split().each { svc ->
                        sh """
                          docker build -t oleg1997/$svc:$COMMIT_HASH $svc
                          docker push oleg1997/$svc:$COMMIT_HASH
                        """
                    }
                }
            }
        }

        stage('deploy') {
            steps {
                sh "helm upgrade bank-chart ./bank-chart --install --namespace $ENV"
            }
        }
    }
}