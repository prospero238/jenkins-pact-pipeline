
def branches=[:]
def mvn_command="bin/mvn clean verify -s /vagrant/settings.xml"
def java_home='/vagrant/jdk1.8.0_71'
def stage_per_downstream_provider=true
  node {
    
     def git_module_provider_map = [
        "shape-provider": [ name: "shape-provider", git_url:'keith@10.0.2.2:/home/keith/projects/shape-provider']
        //,"color-provider": [ name: "color-provider", git_url:'keith@10.0.2.2:/home/keith/projects/color-provider']
     ]     

    def projects=[]

    git credentialsId: 'fromfile', url: 'keith@10.0.2.2:/home/keith/projects/consumer-pact-example'
    // Get the maven tool.
    // ** NOTE: This 'M3' maven tool must be configured
    // **       in the global configuration.           
   def mvnHome = tool 'M3'

   stage 'Consumer Build & Verify'
   
    // Run the maven build
    withEnv(["JAVA_HOME=${java_home}"]) {
        sh "${mvnHome}/${mvn_command}"
        step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
    }
       
   archive 'target/pacts/**/*.json'
   def providers=[]
   def pact_files=findFiles glob: 'target/pacts/**/*.json'

   for (int i = 0; i < pact_files.size(); i++) {
        def json = new groovy.json.JsonSlurper().parseText(readFile(pact_files[i].path))  
        providers << json.provider.name
   }
   
   json=null
   pact_files=null   
   for (int i = 0; i < providers.size(); i++) {
     def current_provider=providers.get(i)
     if(git_module_provider_map.containsKey(current_provider)) {
         echo "creating branch for ${current_provider}"
         branches["${i}-${current_provider}"] = {
            node {
                if(stage_per_downstream_provider) {
                    stage "${current_provider} verification"
                }
                git credentialsId: 'fromfile', url: git_module_provider_map[current_provider].git_url
                // Run the maven build
                withEnv(['JAVA_HOME=/vagrant/jdk1.8.0_71']) {
                    sh "${mvnHome}/${mvn_command}"
                }
            }
         }

     }
   }
  
}
stage "Verify Downstream Providers"
parallel branches  
