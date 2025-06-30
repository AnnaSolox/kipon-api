# KIPON API

> [Versión en Español / Spanish version](README_ES.md)

This application is part of the backend development for the final project of the DAM superior grade.
It allows the frontend to perform actions on the database through REST endpoints.

Developed in `Java` and `Spring Boot`

---
## Technologies and libraries

- **Java**
- **PostgreSQL**: Relational database management system.
- **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework that simplifies the development of Spring applications by enabling minimal configuration and standalone execution.
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: Persistence abstraction that facilitates access to relational databases using JPA and automatic interfaces.
- **[Spring Security](https://spring.io/projects/spring-security)**: Framework for managing security in Spring applications, including authentication and authorization.
- **[Swagger](https://swagger.io/tools/swagger-ui/)**: Tool for documenting and testing RESTful APIs through an automatically generated interactive interface.
- **[MapStruct](https://mapstruct.org/)**: Code generator for mapping between Java objects, facilitating the conversion between DTOs and entities.
- **[Swagger](https://swagger.io/)**: Documentation generator.

---

## AWS Infrastructure
- **RDS**: Database hosting.
- **S3**: Image storage.
- **Elastic beanstalk**: Application deployment.

---

## CI/CD
`Automated deployment` to Elastic Beanstalk via the [deploy.yml](.github/workflows/deploy.yml)

---

## Mobile APP
You can check out the source code of the mobile application developed to interact with this API at the following link:
[KIPON APP](https://github.com/AnnaSolox/Kipon)

---

## Licencia
Designed and developed by AnnaSolox in 2025.

Este proyecto está bajo la licencia [Apache 2.0](/LICENSE).
You may not use this file except in compliance with the License.
You may obtain a copy of the License at:

https://opensource.org/license/apache-2-0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on 
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
