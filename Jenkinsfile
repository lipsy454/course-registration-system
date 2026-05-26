pipeline {

    agent any

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                url: 'https://github.com/lipsy454/course-registration-system.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                bat 'chmod +x mvnw'
                bat './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t course-app .'
            }
        }

        stage('Stop Old Container') {
            steps {
                bat 'docker stop course-container || true'
                bat 'docker rm course-container || true'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat 'docker run -d -p 8081:8081 --name course-container course-app'
            }
        }
    }

    post {

        success {
            echo 'Pipeline Executed Successfully'
        }

        failure {
            echo 'Pipeline Failed'
        }
    }
}
