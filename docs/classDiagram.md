# Class diagram

<!-- ================================================================================================= -->
<!-- ================================================================================================= -->

## Controller

```plantuml
@startuml

class AuthController {
	- authService : AuthService

	+ AuthController(authService : AuthService)
	+ register(authAttemptDTO : AuthAttempt) : ResponseEntity
	+ login(authAttemptDTO: AuthAttempt) : ResponseEntity
}

@enduml
```

<!-- ================================================================================================= -->
<!-- ================================================================================================= -->

## Service

<!-- ================================================================================================= -->

```plantuml
@startuml

class AuthService {
	- userRepository : UserRepository

	+ AuthService(userRepository : UserRepository)
	+ register(authAttemptDTO : AuthAttempt) : User
	+ login(authAttemptDTO : AuthAttempt) : User
}

@enduml
```

<!-- ================================================================================================= -->
<!-- ================================================================================================= -->

## Repository

<!-- ================================================================================================= -->

```plantuml
@startuml

interface UserRepository extends JpaRepository {
	+ findUserByUsername(username : String)
}

@enduml
```
