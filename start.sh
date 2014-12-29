# Production: ./nube.sh
# Run local as ./nube.sh local

if [ -z "$1" ]; then
	profile="default"
else
	profile="default,$1"
fi

echo "Starting nube using profile $profile"
mvn clean install && java -jar -Dspring.profiles.active=$profile nube-portal-server/target/*.jar
