pipeline {

    agent any

    stages {

        stage('Clone Repository') {
            steps {
                git 'https://github.com/lipsy454/course-registration-system.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t course-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat 'docker run -d -p 8081:8081 course-app'
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