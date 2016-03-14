#!/bin/bash


cd /vagrant/pipeline && git init && git add pipeline.groovy && git commit -m "demo"


cd /vagrant/consumer-and-provider && git init && git add pom.xml && git add src && git commit -m "demo"
