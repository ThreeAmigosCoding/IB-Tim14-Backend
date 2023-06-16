# X509 Certificates Management
## Overview
This project is a web application designed for X509 certificate management.

## User Roles
- Unauthenticated user: Can register on the system to obtain a standard account. If they already own an account, they can log in.
- Authenticated user: A user logged into their profile can view and download all certificates, request issuance of certificates to be signed by others, request revocation of certain certificates, and execute certificate validation. Users can be owners of only intermediate or end certificates.
- Admin: A special user who manages root certificates and approves requests for certificates signed by root certificates. They can perform all the actions an authenticated user can.

## System Components
- Local file storage
- Database
- [Backend](https://github.com/ThreeAmigosCoding/IB-Tim14-Backend)
- [Frontend](https://github.com/ThreeAmigosCoding/IB-Tim14-Frontend)

## Functionalities
- User profile registration, confirmation, and system login.
- Password recovery and rotation mechanisms.
- Overview and download of all certificates.
- Certificate validity check based on identifiers or uploaded copies.
- Certificate request creation, overview, approval, or denial.
- Certificate revocation by authenticated users or admins.
- HTTPS communication for the application and between the database and backend.
- Two-factor authentication during user login.
- ReCAPTCHA for forms to protect from spamming.
- Implementation of OAuth protocol for delegated access.
 
- Data validation: Injection attacks prevention, XSS attacks prevention, path traversal attacks prevention, data validation, and file type and size check for uploads.
- Data protection: Sensitive data handling, encrypted data storage, hashed passwords, and secret keys storage.

## Authors
- [Miloš Čuturić](https://github.com/cuturic01)
- [Luka Đorđević](https://github.com/lukaDjordjevic01)
- [Marko Janošević](https://github.com/janosevicsm)

## Academic Context
This project was developed for the purposes of the course [Information Security](http://www.ftn.uns.ac.rs/1604112244/informaciona-bezbednost).
### Course Assistant
- Nikola Milosavljević
### Course Professor
- [Goran Sladić](https://www.linkedin.com/in/goran/)
