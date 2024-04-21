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
    print 'Code Quality'
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