    
def branches=[:]
def mvn_command="bin/mvn clean verify -s /vagrant/settings.xml"
//def java_home='/vagrant/jdk1.8.0_71'
def java_home='/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.71-1.b15.el6_7.x86_64'
def consumer_git_url='file:///vagrant/consumer-and-provider'

def stage_per_downstream_provider=true
  node {
    
     def git_module_provider_map = [
        "shape-provider": [ 
            name: "shape-provider", 
            git_url:'file:///vagrant/consumer-and-provider',
            maven_args: "-Pshape-provider"

            ]
        //,"color-provider": [ name: "color-provider", git_url:'keith@10.0.2.2:/home/keith/projects/color-provider']
     ]     

    def projects=[]

    git url: 'file:///vagrant/consumer-and-provider'    
   
    
    // Get the maven tool.
    // ** NOTE: This 'M3' maven tool must be configured
    // **       in the global configuration.           
   def mvnHome = tool 'M3'

   stage 'Consumer Build & Verify'
   
    // Run the maven build
    withEnv(["JAVA_HOME=${java_home}"]) {
        sh "${mvnHome}/${mvn_command} -Pconsumer"
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
         echo "creating forked build for ${current_provider}"
         def args=git_module_provider_map[current_provider].maven_args
         branches["${i}-${current_provider}"] = {
            node {
               
                stage "${current_provider} verification"
               
                git url: git_module_provider_map[current_provider].git_url
                // Run the maven build
                withEnv(["JAVA_HOME=${java_home}"]) {
                    sh "${mvnHome}/${mvn_command} ${args}"
                }
            }
         }

     }
   }
  
}
stage "Verify Downstream Providers"
parallel branches  
