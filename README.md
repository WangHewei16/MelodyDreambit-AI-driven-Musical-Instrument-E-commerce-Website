## Gold-Melody-Musical-Instrument-Website-Platform
#### 1. Background of this website
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
* MySQL is used for data storage, `AliyunOSS` for file storage, and Redis-Cluster for temporary data storage. `MyCAT` is also used for database migration and synchronization.
* Since UCD only provides a separate external port. So we additionally provide the reverse proxy service, Use `NodeJS` service as a gateway to provide the service. And run the front end through NodeJS as a service.
* We also use AliyunOSS to provide file object storage and `WeChat + OAuth2` for third party authorization.

Figure below shows the diagram of our project's service architecture.
<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/system%20service%20architecture.png" width="600"/></div>

#### 5. CI/CD Pipeline
A mature project should have a test environment for integrated development and deployment, and we use `Jenkins` to build a `CI/CD pipeline`. Code quality review with SonarCube. Package the front-end and back-end code separately and build it into a Docker image for release and deployment on the test server. 
This can greatly improve the efficiency of code deployment and save time.

Figure below shows the flowchart of the pipeline.

* Notify Jenkins through hook for code submitted to GitLab repository.
* Jenkins pulls the front-end and back-end code of the GitLab repository separately, then sends the code to the sonarqube server for quality checking.
* Jenkins uses `Maven` to package the back-end code into a jar package, and uses `Webpack` to package front-end code.
* Jenkins builds the dist folder and jar package into a docker image, and Jenkins pushes the built docker image to the `Docker` registry.
* The test server pulls the image from the docker registry and runs in the background.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/CI:CD%20pipeline.png" width="500"/></div>

#### 6. Novel Functions
##### Data Visualization
<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/line%20chart.png" width="350"/> &emsp;&emsp;<img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/pie%20chart.png" width="330"/></div>
<div> &emsp;&emsp;&emsp;&emsp; </div>
<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/histogram1.png" width="350"/>&emsp;&emsp; <img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/histogram2.png" width="350"/></div>


#### 7. Swagger+Druid
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

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/system%20database.jpg" width="600"/></div>

#### 10. Project Testing
##### 10.1 Interface Testing

Interface testing is a type of testing that tests the interfaces between system components. Interface testing is used to detect the interaction points between external systems and systems and between internal subsystems. The focus of testing is to check the exchange of data, the transfer and control management processes, and the inter-logical dependencies between systems. We embedded `Swagger2` with the code. The entire interface is tested by swagger. Simulation tests at large data levels are also performed using `Apifox`. Ensure the availability and reliability of the interfaces. In the left navigation bar of the staff-portal website page, user can click the `Swagger` to jump to `API Interface Test Page` to simulate tests for all functional interfaces. Figure below shows this page.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/API%20Interface%20Test.png" width="500"/></div>

##### 10.2 Code Quality Testing
We built a test environment on the team's personal server from the beginning of the project. We also built the corresponding CI/CD pipeline. The pipeline uses `SonarQube` to perform quality testing for each version of the code. SonarQube is mainly used for quality assessment in terms of reliability, security, maintainability, coverage, and repetition rate. We can optimize and improve the code quality in the next release based on the results of SonarQube's code evaluation. Figure below shows the process of SonarQube.

<div align=center><img src="https://github.com/WangHewei16/GoldMelody-Musical-Instrument-Website-Platform/blob/main/images/SonarQube.png" width="600"/></div>





