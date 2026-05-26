pipeline {

    agent any

    stages {

        stage('Build Maven Project') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t course-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker rm -f course-container || true'
                sh 'docker run -d -p 8089:8089 --name course-container course-app'
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
