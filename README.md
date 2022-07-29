# jarsoft-test
## How to build:
**Note: this process is only for backend, frontend is not yet in this repo...**
### Prerequisites:
- maven (version used: 3.8.4)
- java 17 (tested on openjdk)
### Process:
- clone this repo (run `git clone https://github.com/p-vasilev/jarsoft-test`)
- run `mvn install -Dmaven.test.skip` inside the repo

After these two steps, a jar should be located in the `target` directory  
This jar can be launched with `java -jar target/test-0.0.1-SNAPSHOT.jar`  
The app will try to connect to MySQL database named `db` at localhost:3306, so make sure that it is running
(you can change the connection options in `application.properties`)

If you want to run tests, you will need a docker-machine environment.  
To run tests execute `mvn test` (or drop `-Dmaven.test.skip` from the install command earlier)
