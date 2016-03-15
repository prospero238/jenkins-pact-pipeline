# Demo set up for pact pipeline
---

Supplies jenkins with pact pipeline setup at "http://10.10.10.20:8080"


before running "vagrant up", there are a couple things you'll have to do:

* Start pact_broker on port 8383
* run  'ansible-galaxy install -r requirements.yml' (in directory of Vagrantfile)


