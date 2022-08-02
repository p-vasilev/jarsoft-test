# jarsoft-test
## How to build:   
### Prerequisites:  
- maven (version used: 3.8.4)
- java 17 (tested on openjdk)
- npm 8.5.5
### Process:  
- clone this repo (run `git clone https://github.com/p-vasilev/jarsoft-test`)  
- run `mvn install -Dmaven.test.skip` inside the repo

After these two steps, a jar should be located in the `target` directory  
This jar can be launched with `java -jar target/test-0.0.1-SNAPSHOT.jar`  
The app will try to connect to MySQL database named `db` at localhost:3306, so make sure that it is running  
(you can change the connection options in `application.properties`)  
**Note:** this app uses Flyway migrations, the schema will be created automatically when you launch the app. This means when you create the database you should not create any tables.  
  
If you want to run tests, you will need a docker-machine environment.  
To run tests execute `mvn test` (or drop `-Dmaven.test.skip` from the install command earlier)  

### Frontend  
To check out the frontend:
- run `npm install` inside `frontend` directory    

After that you can start the Node server by running `npm start` in the same directory (it should launch the browser, but if it doesn't, go to `http://localhost:3000` in your browser)    

Now frontend lacks searching through categories when choosing categories in banner form and error message display

