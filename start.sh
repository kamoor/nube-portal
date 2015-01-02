#!/bin/sh

# Production: ./nube.sh
# Run local as ./nube.sh local

##### ENVIRONMENT VARIABLES#####

#Maven Repository Location
MVN_REPO=~/.m2/repository/

#Enable Spring loaded
SPRING_LOADED_ENABLE=true


#Set Spring profiles
if [ -z "$1" ]; then
	profile="default"
else
	profile="default,$1"
fi

#More local settings
#if [ "$1" == "local" ]; then
#      if [ "$SPRING_LOADED_ENABLE" == "true" ]; then
#		echo "Enabling Spring Loaded"
#		export MAVEN_OPTS="-javaagent:$MVN_REPO/org/springframework/springloaded/1.2.1.RELEASE/springloaded-1.2.1.RELEASE.jar -noverify"
#      fi
#fi

echo "Starting nube using profile $profile"
mvn clean install && java -jar -Dspring.profiles.active=$profile nube-portal-server/target/*.jar
