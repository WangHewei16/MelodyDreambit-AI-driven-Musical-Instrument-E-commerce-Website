## Gold-Melody-Musical-Instrument-Website-Platform
/badge/<license>-<MIT>-<blue>
/static/v1?label=<LABEL>&message=<MESSAGE>&color=<COLOR>
#### 1. Background of developing this website
Due to the pandemic, many off-line stores of a local musical instrument had been closed. Before the outbreak of the COVID-19, all of their business is from the walk-in retail customers. However, People need to keep their distance and reduce contact outdoors, which undoubtedly affects the business of offline stores now. Hence, the shop wants to transfer the products sold in their stores to an online store to keep their business. 


At the same time, this may also be a good opportunity to use the mall website to increase economic income. People have to spend more time at home because of the epidemic. They may choose to play or learn to play a musical instrument to enrich their daily lives. Therefore, the demand for musical instruments may increase.


#### 2. Overall Objective
This project aims to build a web-based online shopping platform for musical instrument store. Through this platform, musical instrument stores can achieve management and sales information. In the context of COVID-19, online stores can help businesses maintain normal sales and facilitate the normal needs of customers. Customers can browse, select and purchase products and communicate with employees in the same way they would in an offline store. In addition, our team designed a blog feature for customers to share Musical Instruments they received when they purchased the product. Other customers can view these blogs and interact with buyers.


#### 3. Functions
##### 3.1 System
* The website allows users to search instrument.
* The website allows users to use filter condition to find products.
* The website provides separative customer portal and staff portal, which allows users to go shopping and manage data respectively.
* The website allows customers and staffs to switch language between English and Chinese.
* The website provides interface that can be adapted to pages on different devices such as PC and mobile phone.
* The website provides quality communication between customers and staffs.

##### 3.2 Customer
* Customer can browse the company and members information.
* Customer can add personal address or upload avatar in setting page.
* Customer can communicate with staffs.
* Customer can choose certain order and price range to browse product.
* Customer can grade the product.
* Customer can add product to shopping carts.
* Customer can choose address and receiving method to place order.
* Customer can cancel order or modify address by submitting request and chatting with staff.
* Customer can post a blog for a product and make comments for a blog.
* Customer can click like for comments of blog and product.

##### 3.3 Staff
* Staff can view and manage user information.
* Staff can view, add, remove, edit product information.
* Staff can view and manage orders.
* Staff can view and manage blogs.
* Staff can communicate with customers.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/user%20case%20diagram.png" width="750"/></div>

#### 4. Service Architecture
* Staff Portal uses front and back-end separation technology. The back-end interacts with the database and provides the interface to the front-end. The front-end renders the pages.
* MySQL is used for data storage, `AliyunOSS` for file storage, and `Redis-Cluster` for temporary data storage. `MyCAT` is also used for database migration and synchronization.
* Since University College Dublin (UCD) only provides a separate external port. So we additionally provide the reverse proxy service, adopt `NodeJS` service as a gateway to provide the service. And run the front end through NodeJS as a service.
* We also use AliyunOSS to provide file object storage and `WeChat + OAuth2` for third party authorization.

Figure below shows the diagram of our project's service architecture.
<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/system%20service%20architecture.png" width="700"/></div>

#### 5. CI/CD Pipeline
A mature project should have a test environment for integrated development and deployment, and we use `Jenkins` to build a `CI/CD pipeline`. Code quality review with SonarCube. Package the front-end and back-end code separately and build it into a Docker image for release and deployment on the test server. 
This can greatly improve the efficiency of code deployment and save time.

* Notify Jenkins through hook for code submitted to GitLab repository.
* Jenkins pulls the front-end and back-end code of the GitLab repository separately, then sends the code to the sonarqube server for quality checking.
* Jenkins uses `Maven` to package the back-end code into a jar package, and uses `Webpack` to package front-end code.
* Jenkins builds the dist folder and jar package into a docker image, and Jenkins pushes the built docker image to the `Docker` registry.
* The test server pulls the image from the docker registry and runs in the background.

Figure below shows the flowchart of the pipeline.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/CI:CD%20pipeline.png" width="700"/></div>

#### 6. Novel Functions
##### Data Visualization
* We use `ECharts`, a JavaScript-based data visualization charting library that provides intuitive, vivid, interactive and customize data visualization charts, to process data stored in databases and turn it into charts. It allows staff to see changes in data more visually.
* To retrieve the data at regular intervals, we used `Quartz`, an open source project that allows program developers to schedule jobs based on time intervals, in order to extract and store the data in the database.
    
In the left navigation bar of the staff-portal website page, click "Sta" to choose "Data Analysis", then user will enter the data analysis page shown as below.

* `Line Chart` - `total number of registrants & product views & product purchases & blog`: Staff can select the data to be analyzed, and the time period to generate a line graph of the total change in that data over the specified time period. The date allows the window to slide. 

* `Pie Chart` - `total numbers of different types of products in shopping cart`: Staff can select a date and analyze the total number of items added to the shopping cart for each type of product on that date, and generate the corresponding pie chart. shows the pie chart.

* `Histogram` - `daily views of different types of products`: Staff can select a time period and generate a bar chart of the total number of daily views for different categories of products during that time period. 

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/line%20chart.png" width="350"/> &emsp;&emsp;<img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/pie%20chart.png" width="330"/></div>
<div> &emsp;&emsp;&emsp;&emsp; </div>
<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/histogram1.png" width="350"/>&emsp;&emsp; <img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/histogram2.png" width="350"/></div>


#### 7. Swagger+Druid
A complete backend system also monitors data persistence information and interface availability. We use Druid to monitor the database, Swagger to test the interface. In the staff portal.	

* Build tests for all API and embed Swagger in `SpringBoot`.
* Add SQL monitoring and logging, block and filter built-in common.js advertisements, integrate Druid into SpringBoot.
* Identifying and processing information, then add the processed information to the database.

        
        
#### 8. Deployment Process
Our Staff Portal and Customer Portal use different technologies. Staff Portal uses front and back-end separation technology. Customer Portal uses Flask And Staff Portal uses `SpringBoot + Vue`. Hence, the single port deployment relies on the following components with restricted permissions.

* `Gateway`: Because we can only use one externally exposed interface. So in the deployment, we use to provide a reverse proxy similar to the gateway. The gateway is implemented through `Express + Http-Proxy`, which implements load balancing through simple random number logic.
* `Customer Portal`: Flask project is used for the simplest runtime environment deployment. Keeping session through Screen.
* `Staff Portal Back End`: Jenkins automatically pulls projects from GitLab and performs quality checks. Maven is used for packaging, and the corresponding Jar package is transferred to the UCD server via ssh to run automatically in the background via sh script.
* `Staff Portal Front End`: Jenkins automatically pulls projects from GitLab and performs quality checks. Packaged via `Webpack`, the corresponding dist file is run as a service in the background via `Express`.
* `MySQL`: Since UCD's server, MySQL only allows localhost access. So we use another cloud database for the development/testing environment. The test environment database is synchronized to the UCD server via `MyCAT`.
* `Redis-Cluster`: Since part of the project uses Redis as cache, the server cannot deploy Redis, so we build a Redis high availability sentinel cluster on another server with `CDN service`. This ensures the availability of the Redis service.

Figure below shows the operation logic of the `reverse proxy`.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/deployment%20diagram.png" width="300"/></div>

#### 9. Database Design
`Logical Foreign Keys`: One of our database design decision is to use logical foreign keys instead of physical foreign keys.

* Physical foreign key means the fields of a table using a foreign key to associate with another table or field. They use a restricted engine called `InnoDB` to create connections between forms. They have performance problems. They need the database to maintain the internal management zof foreign keys. That causes some unintended sequences. For example, when we do some add, delete or update operations involving foreign key fields, the relevant operations need to be triggered which consumes a lot of resources.
* In addition, in that case, all tables must be InnoDB type and can not be temporary tables. Index prefixing of foreign key columns is not supported. One consequence of this is that BLOB and TEXT columns are not included in a foreign key, because indexes on these columns must always contain a prefix length. InnoDB does not check foreign key constraints for those foreign keys or referenced keys that contain NULL columns. If the volume of data goes up, using physical foreign keys can significantly degrade performance, especially in the case of deadlocks.
* Due to the problems mentioned above, therefore, we use logical foreign keys for database association to ensure the final consistency of data through transactions
* The logical foreign key is a technique in which thing foreign key but do not use foreign keys, the use of syntax (code) to produce a logical association resulting in a foreign key.
* We avoid large transactions and try to split large transactions into multiple smaller transactions to deal with them, which also have less chance of lock conflicts. When using physical foreign keys, at least 2 tables need to be updated at the same time. In this case, it is necessary to lock at least two tables at the same time. If split, the two locks are not synchronized at the same time. The probability of deadlock is smaller. Figure below shows the structure of the database. In practice, we implement it through logical foreign keys.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/system%20database.jpg" width="600"/></div>

#### 10. Project Testing
##### 10.1 Interface Testing

Interface testing is a type of testing that tests the interfaces between system components. Interface testing is used to detect the interaction points between external systems and systems and between internal subsystems. The focus of testing is to check the exchange of data, the transfer and control management processes, and the inter-logical dependencies between systems. We embedded `Swagger2` with the code. The entire interface is tested by swagger. Simulation tests at large data levels are also performed using `Apifox`. Ensure the availability and reliability of the interfaces. In the left navigation bar of the staff-portal website page, user can click the `Swagger` to jump to `API Interface Test Page` to simulate tests for all functional interfaces. Figure below shows this page.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/API%20Interface%20Test.png" width="500"/></div>

##### 10.2 Code Quality Testing
We built a test environment on the team's personal server from the beginning of the project. We also built the corresponding CI/CD pipeline. The pipeline uses `SonarQube` to perform quality testing for each version of the code. SonarQube is mainly used for quality assessment in terms of reliability, security, maintainability, coverage, and repetition rate. We can optimize and improve the code quality in the next release based on the results of SonarQube's code evaluation. Figure below shows the process of SonarQube.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/SonarQube.png" width="600"/></div>





