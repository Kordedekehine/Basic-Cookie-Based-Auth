# **API DOCUMENTATION**

 * ZEESHAN ADIL(https://medium.com/spring-boot/cookie-based-jwt-authentication-with-spring-security-756f70664673) MODIFIED VERSION

 *COOKIE BASED AUTHENTICATION*

# **sign up request -- POST** 
(http://localhost:8080/api/v1/signUp)

{
"username": "ADEX",
"email": "ade@gmail.com",
"confirmPassword": "123456",
"password": "123456",
"roles": [
{
"id": 1,
"name": "ROLE_USER"
}
]
}

# *sign up response*

{
"username": "ADEX",
"email": "ade@gmail.com",
"password": "$2a$10$YBHXgbleYZw7J9qPPPCyM.Yc/WnfNC6LxVJh7cx3ivA6/PBIxelhC",
"roles": [
{
"id": 1,
"name": "ROLE_USER"
}
]
}

# **LOGIN request -- POST** 
(http://localhost:8080/api/v1/login)

{
"usernameOrMail": "ADEX",
"password": "123456"
}

# **LOGIN response ** 

{
"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBREVYIiwiaWF0IjoxNzE0OTA1MTg4LCJleHAiOjE3MTQ5MDUyNDh9.kWpXrWixCdcsClCgDGQujmW0phMB87a5fSrDFFVwZfk",
"token": "4f771fed-88e8-4304-afad-9d603f844040",
"expiresIn": 3600000
}



# **REFRESH TOKEN request -- POST** 
(http://localhost:8080/api/v1/refreshToken)

{
"token": "711ec032-93d0-46ef-8a95-3106fae63555"
}


# **REFRESH TOKEN response ** 

{
"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKSUJFWCIsImlhdCI6MTcxNDkwNDAwMiwiZXhwIjoxNzE0OTA0MDYyfQ.CSK3fE6bbTHYOFUvDwdy_pGhDO-RxQRQlzQxFtkTC4Q",
"token": "711ec032-93d0-46ef-8a95-3106fae63555",
"expiresIn": 1714905072244
}


# ** SECURE ENDPOINT TEST request -- POST** 
(http://localhost:8080/api/v1/ping)

NOTE: YOU DON'T NEED TO WORRY ABOUT BEARER TOKEN THE COOKIE IS ACTIVATED


# ** SECURE ENDPOINT TEST response** 

Welcome



# ** LOG OUT request -- POST**
(http://localhost:8080/api/v1/logOut)


# ** LOG OUT request -- POST**