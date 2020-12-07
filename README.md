# Project-Team_Alpha
Project-team_alpha created by GitHub Classroom

## Sample Login
<b>Admin</b> <br>
Account: admin, Password: 123admin456 <br>
<br>
<b>Employee1</b> <br>
Account: employee1, Password: 123456 <br>
<br>
<b>customer1</b> <br>
Account: customer1, Password: 123456 <br>

## New Update
- Create functions for admin (remove users, add service, edit service and remove service)
- New Class structure and new Screen 

## Project Description 
This time we have successfully install the database and have our users information and service information kept within the firebase database. We did have some pre-created admin, employee and customer account inside the database already. You should be able to create an account and immidately login with it. There is no unit testing as the professor said we don't need to build it yet. 

## UML Diagram
We created our UML diagram based on our current version (as requested from the assignment guideline) which there is still many additional funtions and class to implement later on.

## Class
- UserModel (all user, including email, username, password)
- Admin, so far we just created this class, but haven't create additional functinos for the class yet
- Employee (hours, emp_id, branch_id)
- Customer (Address), basic user without any additional function yet
- Address
- Branch, branch infomation
- NovLocalDate, store year, month and date
- NovLocalTime, store hour and mintues for start and end time
- NovService, service that offer by the admin
- UserHelperClass, a class that help with the database (firebase), it has two boolean attribute to know if the users is admin or employee or customer
- UserList, for the listview of users
- NovServiceList, for the listview of service
- ServiceDoc, store mandatory application information

## Screen
- Home Screen (Pre login)
- Sign Up Page
- Welcome Page for Admin
- Admin Manage Service Page
- Admin Manage User Page
- Admin Create Service Page
- Welcome Page for Employee
- Welcome Page for Customer (Users)

## Key Functions
- Sign up (Create a branch employee account) <br>
- Sign up (Create a customer account) <br>
- Login 
- Admin Create/Remove/Edit Service
- Admin Remove Users

## Team Member (Authors)
- Tsoi Yuen Lau 8429907<br>
- Avneesh Chaudhary 300120325<br>
- Jonathan Ao 300137907<br>
- Angus Gunn 300077083<br>
- Sean Kim 300056691 <br>
- Liam Prieditis 300062876 <br>
