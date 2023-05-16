<!-- Author: Emrah Kinay -->
<a name="readme-top"></a>
<div align="center">
  <a href="https://apps.apple.com/ca/app/active/id6445869038">
        <h3 align="center">Active</h3>
        <p align="center">Drop-In Activities at City of Ottawa Recreation Facilities</p>
  </a>
</div>

## About The Project
Active is a mobile application that allows users to browse and search drop-in activities at City of Ottawa Recreation Facilities.

There is no API provided by the city for the drop-in activities and the data for the application is collected using Python web scraper and published using Spring Boot REST API Server on Docker.


Download the app on [AppStore](https://apps.apple.com/ca/app/active/id6445869038).
<div align="left">
  <a href="https://apps.apple.com/ca/app/active/id6445869038">
    <img src="https://raw.githubusercontent.com/simulacrumus/active-docker/b34e74ed74f9a552ceac620087c1eb40eb67a312/Download_on_the_App_Store_Badge_US-UK_RGB_blk_092917.svg" alt="AppStore Download">
  </a>
</div>

### Built With

* Java
* Spring Boot
* Python
* MySQL
* REST API
* Docker
* Beautiful Soup

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

Please follow the instructions below to get started with Active.

### Prerequisites
* Docker `20.10.20` or newer version

### Installation
* Clone the repo
    ```sh
    git clone https://github.com/simulacrumus/active-docker
    ```
* Change your directory
    ```sh
    cd active-docker
    ```
* Enter your keys in `.env` file
    ```conf
    MYSQL_DATABASE=<YOUR_DATABASE_NAME>
    MYSQL_USER=<YOUR_DATABASE_USERNAME>
    MYSQL_PASSWORD=<YOUR_DATABSE_USER_PASSWORD>
    MYSQL_ROOT_PASSWORD=<YOUR_DATABSE_ROOT_PASSWORD>
    MYSQL_PORT=<YOUR_DATABASE_PORT_NUMBER>
    SERVER_PORT=<YOUR_API_SERVER_PORT_NUMBER>
    API_KEY=<YOUR_API_KEY>
    TIME_ZONE=<YOUR_TIME_ZONE>
    ```
* Run Docker
    ```sh
   docker-compose up -d
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

Emrah Kinay - [@emrahkinay](https://www.linkedin.com/in/emrahkinay/)

Project Link: [https://github.com/simulacrumus/active-docker-v2](https://github.com/simulacrumus/active-docker-v2)

<p align="right">(<a href="#readme-top">back to top</a>)</p>