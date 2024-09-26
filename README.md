# **Product Specification: Simple Employee Feedback System**

## **Overview**
The Simple Employee Feedback System is a backend service designed to facilitate the submission, review, and response to employee feedback within an organization. 
Employees can provide feedback on their work experience, either anonymously or identified, which can then be reviewed and responded to by HR or Admin.

## **Objectives**
  - **Empower Employees:**
    - Provide a platform for employees to share their experiences and feedback securely and anonymously if desired.
      
  - **Streamline Feedback Management:**
    - Enable HR and Admin to view, filter, and respond to feedback efficiently.
      
  - **Improve Employee Satisfaction:**
    - Foster a culture of transparency and responsiveness by addressing employee concerns promptly.

## **Key Features**
  1. **Employee Feedback Submission**
     - **API Endpoint:** Provide an API endpoint for employees to submit feedback.
     - **Anonymous Submission Option:** Employees can choose to submit feedback anonymously.
     - **Basic Validation:** Implement validation for feedback content (e.g., enforce a minimum length of feedback).
       
  2. **Feedback Viewing (HR/Admin only)**
     - **API Endpoint:** Provide an API endpoint for HR/Admin to view all submitted feedback.
     - **Filtering Options:** Allow HR/Admin to filter feedback based on date, department, or anonymity status.
       
  3. **HR Response to Feedback**
     - **API Endpoint:** Allow HR to respond to feedback through an API endpoint, provided the feedback is not anonymous.
     - **Linked Responses:** Store responses so that they are linked to the original feedback entry.
       
  4. **Feedback Status Tracking**
     - **Mark as Reviewed/Unreviewed:** Allow HR to mark feedback as reviewed or unreviewed.
     - **Status Checking API:** Provide an API endpoint for employees to check the status of their submitted feedback (if not anonymous).

## **Timeline**
  - **Phase 1:** Basic API Development
      - Implement basic data persistence.
      - Implement Employee Feedback Submission API.
      - Implement Feedback Viewing API.
        
  - **Phase 2:** Advanced Features & Testing
      - Implement Feedback Filtering.
      - Implement HR Response API.
      - Implement Feedback Status Tracking.
      - Conduct unit testing and integration testing.

## **Risks**
**Data Security:** Ensuring that feedback remains confidential, especially for anonymous submissions, is critical.



-------------------------------------------------------------------------------------------------------------------------------------------------


# **HLD - Employee Feedback System**

## **Background**
This system will allow employees to provide feedback on their work experience, either anonymously or identified. In addition, admins and HR can review the feedback left and filter them by three categories. HR can also respond to feedback which isn’t anonymous.

## **Relevant docs (specs/design)**
specs

## **Architecture Overview**
![Untitled Diagram](https://github.com/user-attachments/assets/38d7596f-1169-47bd-b5cd-3a9f23059098)

## **API’s**
- **POST**
  - **Login** ✔️

  - **Submit feedback** ✔️

  - **Submit response** ✔️

- **GET**
  - **View all submitted feedback** ✔️

  - **Check status** ✔️
  
- **PUT**
  - **Mark reviewed/ unreviewed** ✔️

## **Flow**
- **POST**
  -  **Login**

  - **Submit feedback**

  - **Submit response**

- **GET**
  - **View all submitted feedback**

  - **Check status**

- **PUT**
  - **Mark reviewed/unreviewed**

## **Tables**

## **Validation**
Feedback and response length should be a minimum of 30 characters and a maximum of 500 characters.

## **Testing Strategy**
- **Service**
  - I’ll test the service during development. I’ll test each function by using a mocking framework to stimulate database interactions to check that each scenario is covered.

- **Dao**
  - I’ll add unit tests for each function in the DAO during development to make sure all the functions run as expected.

## Authentication - JWT
I’ll use JWT to authenticate and authorize an employee's action within an API. I’ll generate a token that contains the employee's information such as their employee id, company id and role. 
When an employee logs in to the system, the system will validate its credentials and generate a token which will include in the payload the employee's employee id, company id and role and extract them into the properties. For each API request, the server will make sure that the token is valid, it will check the employees information from the properties and check that the employee has permissions for the action based on its role.
If the employee has the required permissions, the server will process the request, and if not it will return a response saying the employee has no permission for this action.

## **Risks**
**Data Security:**
Ensuring that feedback remains confidential, especially for anonymous submissions.
If an employee leaves an anonymous feedback, the feedback won’t be saved with it’s employee id.
Employee A can’t review the feedback employee B left unless employee A is an admin or hr. 

## **Time table**
**Sunday**
- HLD ✔️

**Monday - Tuesday**
- Migrations - create all tables, add data to employee table ✔️
- Write data classes ✔️
- Resource - write Api’s of Employee Feedback Submission ✔️
- Resource - write Api’s of feedback Viewing ✔️
- Service - write a service file for Employee Feedback Submission + tests ✔️
- Service - write a service file for feedback Viewing + tests ✔️
- Dao - write a dao file for Employee Feedback Submission + tests ✔️
- Dao - write a dao file for feedback Viewing + tests ✔️
- Login ✔️

**Wednesday - Thursday**
- Add feedback filtering feature- Resource, service, dao, tests. ✔️
- Add HR response feature- Resource, service, dao, tests. ✔️

