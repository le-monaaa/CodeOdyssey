pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    // 코드 체크아웃
                    checkout scm
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // 변경된 브랜치와 머지 대상 브랜치 출력
                    // 현재 빌드 중인 브랜치명 확인
                    def currentBranch = env.BRANCH_NAME
                    echo "Merge Target Branch : \${CHANGE_TARGET}"
                    echo "Current Branch: \${currentBranch}"

                    echo "test12"
                    
                    // 여기에 빌드 스텝 추가
                }
            }
        }
    }
}
