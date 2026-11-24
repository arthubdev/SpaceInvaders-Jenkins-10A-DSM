pipeline {
    agent any

    stages {
        stage('Compilar') {
            steps {
                bat 'call build.bat'
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'build/SpaceInvaders.jar', fingerprint: true
        }
    }
}
