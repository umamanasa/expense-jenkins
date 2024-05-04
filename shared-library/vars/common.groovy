def compile() {
  if (env.codeType == "static") {
    return "Don't need any Compilation"
  }

  stage('Compile Code') {
    if(env.codeType == "nodejs"){
      sh 'npm install'
    }
  }
}

def test() {
  stage('Test Cases') {
    if(env.codeType == "nodejs"){
  //    sh 'npm test'
      print 'OK'
    }
  }
}

def codeQuality() {
  stage('Code Quality') {
    env.sonaruser = sh (script: 'aws ssm get-parameter --name "sonarqube.user" --with-decryption --query="Parameter.Value" |xargs', returnStdout: true).trim()
    env.sonarpass = sh (script: 'aws ssm get-parameter --name "sonarqube.pass" --with-decryption --query="Parameter.Value" |xargs', returnStdout: true).trim()
    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: sonarpass]]]) {
//     sh 'sonar-scanner -Dsonar.host.url=http://172.31.25.122:9000 -Dsonar.login=${sonaruser} -Dsonar.password=${sonarpass} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true'
     print 'OK'
    }
  }
}

def codeSecurity() {
  stage('Code Security') {
    print 'Code Security'
    // we use Check Marx SAST & SCA checks for Code Security.
  }
}

//This is for Nexus Release
//def release() {
//  stage('Release') {
//    env.nexususer = sh (script: 'aws ssm get-parameter --name "nexus.username" --with-decryption --query="Parameter.Value" |xargs', returnStdout: true).trim()
//    env.nexuspass = sh (script: 'aws ssm get-parameter --name "nexus.password" --with-decryption --query="Parameter.Value" |xargs', returnStdout: true).trim()
//    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: nexuspass]]]) {
//      sh 'echo ${TAG_NAME} >VERSION'
//      if(env.codeType == "nodejs") {
//        sh 'zip -r ${component}-${TAG_NAME}.zip index.js node_modules VERSION ${schemadir}'
//      } else {
//        sh 'zip -r ${component}-${TAG_NAME}.zip *'
//      }
//
//      sh 'curl -v -u ${nexususer}:${nexuspass} --upload-file ${component}-${TAG_NAME}.zip http://172.31.81.233:8081/repository/${component}/${component}-${TAG_NAME}.zip'
//
//    }
////    print 'OK'
//  }
//}

// This is for AWS ECR
def release() {
  stage('Release') {
    sh '''
      aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 206243364202.dkr.ecr.us-east-1.amazonaws.com
      docker build -t 206243364202.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME} .
      docker push 206243364202.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME}
'''
  }
}