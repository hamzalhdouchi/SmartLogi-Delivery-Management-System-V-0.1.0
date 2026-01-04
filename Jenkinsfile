pipeline {
    agent any
    
    environment {
        MAVEN_HOME = 'C:\\Program Files\\Apache\\Maven\\apache-maven-3.9.11'
        PATH = "${MAVEN_HOME}\\bin;${env.PATH}"

        SONAR_HOST_URL = 'http://127.0.0.1:9000'
        SONAR_TOKEN = credentials('sonarqube-token')

        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE = 'smartlogi/smartlogi-app'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'

        APP_NAME = 'SmartLogi_V0.1.0'
        APP_VERSION = '0.0.1-SNAPSHOT'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository from GitHub...'
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = bat(
                        script: '@git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building application with Maven...'
                bat 'mvn clean compile -DskipTests'
            }
            post {
                success {
                    echo 'Build completed successfully'
                }
                failure {
                    echo 'Build failed'
                }
            }
        }

        stage('Tests') {
            steps {
                echo 'Running unit and integration tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/test/**'
                    )
                }
                success {
                    echo 'All tests passed'
                }
                failure {
                    echo 'Some tests failed'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyzing code quality with SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    bat """
                        mvn sonar:sonar ^
                            -Dsonar.projectKey=smartlogi ^
                            -Dsonar.projectName="SmartLogi" ^
                            -Dsonar.host.url=%SONAR_HOST_URL% ^
                            -Dsonar.login=%SONAR_TOKEN% ^
                            -Dsonar.java.coveragePlugin=jacoco ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
            post {
                success {
                    echo 'SonarQube analysis completed'
                }
                failure {
                    echo 'SonarQube analysis failed'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Checking SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
            post {
                success {
                    echo 'Quality Gate passed - Code meets quality standards'
                }
                failure {
                    echo 'Quality Gate failed - Code does not meet quality standards'
                    error('Pipeline aborted due to quality gate failure')
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                script {
                    bat 'mvn package -DskipTests'

                    docker.build("${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}")
                    docker.build("${DOCKER_IMAGE}:latest")
                }
            }
            post {
                success {
                    echo "Docker image built: ${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}"
                }
                failure {
                    echo 'Docker build failed'
                }
            }
        }

        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                echo 'Pushing Docker image to registry...'
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIALS_ID) {
                        docker.image("${DOCKER_IMAGE}:${GIT_COMMIT_SHORT}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
            post {
                success {
                    echo "Docker image pushed to ${DOCKER_REGISTRY}"
                }
                failure {
                    echo 'Docker push failed'
                }
            }
        }
    }

    post {
        success {
            echo '''
            ========================================
            PIPELINE COMPLETED SUCCESSFULLY
            ========================================
            '''
        }
        failure {
            echo '''
            ========================================
            PIPELINE FAILED
            ========================================
            '''
        }
    }
}
