This is client/server chat application. <br />

Application supports the following chat user lifecycle
1. Registration <br />
2. Sign in <br />
3. Sending messages <br />
4. Logout <br />

Server supports multiple users (using multithreading).<br />

Users' passwords are encoded using PasswordEncoder (Spring Security).<br />

Data about users, messages is stored in db (tables must be created/exists beforehand).<br />
Interaction with db (CRUD operations) impemented using JDBC template.<br />

Server must be launched with argument <br />
--port=8081 <br />
Client must be launched with argument <br />
--server-port=8081 <br />
(you can use other ports) <br />
