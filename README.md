# UTA-MAC-Facility-Maintenance-System

## A web application to report, assign and track the work orders for the activity center in my university.

I developed a full stack web application to report, assign and track the work orders for the activity center in my university. The Tech stack I used for developing this application are:

### Frontend: HTML5, JSP
### Backend: Java
### Database: MySQL
### Testing Frameworks: JUnit 4, Selenium WebDriver, Selenium IDE

- The entire application was built using the **MVC Design Pattern**.
- The view for the application was developed using HTML 5 and JSP pages.
- The Model layer consists of business logic for validating the incoming data from the controller before pushing it into the database.
- The Controller acts as the middleware which gets the request from the view and sends the request to the corresponding model or to the corresponding DAO layer. Once the data has   been validated from the Model it sends the data to the DAO layer. After processing the request the controller sends back the request to the view layer.
- I have implemented the DAO layer to separate the database operations from the Model layer.



