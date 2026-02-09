pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['test', 'prod'], description: 'Environment')
    }

    environment {
        DOCKER_REPO = 'oleg1997'
        SERVICES = "account-service cash-service transfer-service frontend gateway notification-service"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Set commit hash') {
            steps {
                script {
                    env.COMMIT_HASH = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Build') {
            steps {
                sh "./gradlew clean build"
            }
        }

        stage('Docker build & push') {
            steps {
                script {
                    SERVICES.split().each { svc ->
                        sh """
                          docker build -t ${DOCKER_REPO}/${svc}:${COMMIT_HASH} ${svc}
                          docker push ${DOCKER_REPO}/${svc}:${COMMIT_HASH}
                        """
                    }
                }
            }
        }

        stage('Prod approval') {
            when {
                expression { params.ENV == 'prod' }
            }
            steps {
                input message: 'Deploy to PROD environment?', ok: 'Yes, deploy'
            }
        }

        stage('Deploy') {
            steps {
                sh """
                  helm upgrade --install bank-chart ./bank-chart \
                    --namespace ${params.ENV} \
                    --set image.tag=${COMMIT_HASH}
                """
            }
        }
    }

    post {
        success {
            echo "Deployment to ${params.ENV} completed successfully"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
