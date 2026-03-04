pipeline {
    agent any

    environment {
        DOCKER_USERNAME = credentials('docker-hub-username')
        TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Push Images') {
            parallel {
                stage('UserService') {
                    steps {
                        script {
                            docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                def img = docker.build("${DOCKER_USERNAME}/tradebook-user:${TAG}", "-f UserService/Dockerfile .")
                                img.push()
                                img.push('latest')
                            }
                        }
                    }
                }
                stage('OrderService') {
                    steps {
                        script {
                            docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                def img = docker.build("${DOCKER_USERNAME}/tradebook-order:${TAG}", "-f OrderService/Dockerfile .")
                                img.push()
                                img.push('latest')
                            }
                        }
                    }
                }
                stage('TradeService') {
                    steps {
                        script {
                            docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                def img = docker.build("${DOCKER_USERNAME}/tradebook-trade:${TAG}", "-f TradeService/Dockerfile .")
                                img.push()
                                img.push('latest')
                            }
                        }
                    }
                }
                stage('ExchangeService') {
                    steps {
                        script {
                            docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                def img = docker.build("${DOCKER_USERNAME}/tradebook-exchange:${TAG}", "-f ExchangeService/Dockerfile .")
                                img.push()
                                img.push('latest')
                            }
                        }
                    }
                }
                stage('Frontend') {
                    steps {
                        script {
                            docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                def img = docker.build("${DOCKER_USERNAME}/tradebook-frontend:${TAG}", "-f frontend/Dockerfile ./frontend")
                                img.push()
                                img.push('latest')
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    export DOCKER_USERNAME=${DOCKER_USERNAME}
                    export TAG=${TAG}
                    docker compose pull
                    docker compose up -d
                """
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed'
        }
        success {
            echo "Deployed version ${TAG}"
        }
    }
}