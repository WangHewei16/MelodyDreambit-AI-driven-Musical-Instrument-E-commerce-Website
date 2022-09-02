pipeline {
    agent any

    stages {
        stage('pull code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'csgit', url: 'https://csgitlab.ucd.ie/group13/staff-portal-back-end.git']]])
            }
        }
        stage('build project'){
            steps {
                sh 'mvn -Dmaven.test.failure.ignore -Dmaven.test.skip=true clean package'
            }
        }
//         stage('SonarQube代码审查') {
//             steps{
//                 script {
//                     // 引入SonarQubeScannar工具
//                     scannerHome = tool 'sonar-scanner'
//                 }
//                 // 引入SonarQube的服务器环境
//                 withSonarQubeEnv('sonarQube') {
//                     sh "${scannerHome}/bin/sonar-scanner"
//                 }
//             }
//         }
        stage('publish project'){
            steps{
                sshPublisher(publishers: [sshPublisherDesc(configName: 'http://8.130.13.122/', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''chmod -R 777 /data/jar/*
                /data/jar/run.sh''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/data/jar/', remoteDirectorySDF: false, removePrefix: 'target/', sourceFiles: 'target/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
    }
    post {
        always {
            emailext(
                subject: '构建通知：${PROJECT_NAME} - Build # ${BUILD_NUMBER} - ${BUILD_STATUS}!',
                body: '${FILE,path="email.html"}',
                to: '463216638@qq.com, jinfeng.xu@ucdconnect.ie, yunchang.liu@ucdconnect.ie, hewei.wang@ucdconnect.ie, yongfei.yan@ucdconnect.ie, ziyuan.wen@ucdconnect.ie ,kaiwen.gong@ucdconnect.ie'
            )
        }
    }
}
