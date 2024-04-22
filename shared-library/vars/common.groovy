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
      sh 'npm test'
    }
  }
}

def codeQuality() {
  stage('Code Quality') {
    sonaruser = sh(script: 'aws ssm get-parameter --name "sonarqube.user" --with-decryption --query="Parameter.Value"', returnStatus: true)
    sonarpass = sh(script: 'aws ssm get-parameter --name "sonarqube.pass" --with-decryption --query="Parameter.Value"', returnStatus: true)
    sh 'sonar-scanner -Dsonar.host.url=http://172.31.25.122:9000 -Dsonar.login=${sonaruser} -Dsonar.password=${sonarpass} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true'
  }
}

def codeSecurity() {
  stage('Code Security') {
    print 'Code Security'
  }
}

def release() {
  stage('Release') {
    print 'Release'
  }
}