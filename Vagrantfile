# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrant file for event service, using ansible for provisioning.
# Server is provided as a private network, ip: 10.10.10.20
# NOTE: provisioning necessitates installation of a jdk, and this Vagrant file
#       uses a download URL that points to the host machine (http://10.0.2.2/packages/jdk-8u45-linux-x64.tar.gz)
#       Therefore, to use this, you should have a locally running webserver with the jdk tar packages available
#       in the directory described by the URL.

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

ANSIBLE_TAGS=ENV['ANSIBLE_TAGS']
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "nrel/CentOS-6.6-x86_64"
  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "provisioning/pipeline.yml"
    ansible.tags = ANSIBLE_TAGS
    ansible.groups = {
      "ci-server" => ["default"]
    }
  end

 config.vm.provision "shell", inline: "/etc/init.d/iptables $1", args: "stop"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network :private_network, ip: "10.10.10.20"

 config.vm.provision "shell", inline: "/etc/init.d/iptables $1", args: "stop"


  # If true, then any SSH connections made will enable agent forwarding.
  # Default value: false
  config.ssh.forward_agent = true

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  # config.vm.provider :virtualbox do |vb|
  #   # Don't boot with headless mode
  #   vb.gui = true
  #
  #   # Use VBoxManage to customize the VM. For example to change memory:
  #   vb.customize ["modifyvm", :id, "--memory", "1024"]
  # end
  #
end
