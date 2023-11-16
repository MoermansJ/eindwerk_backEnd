# Sequence Diagram

<!-- ================================================================================================= -->

## validateToken

```plantuml
@startuml
Boundary FrontEnd as frontend
Control AuthController as authcontroller
Control AuthService as authservice
Control JwtUtil as jwtutil
Control JwtParser as jwtparser

frontend -> authcontroller : POST /auth/validateToken : ResponseEntity
activate authcontroller

authcontroller -> authservice : validateToken(token: String) : boolean
activate authservice

authservice -> jwtutil : validateClaims(token : String) : boolean
activate jwtutil

jwtutil -> jwtutil : parseJwtClaims(token : String) : Claims

jwtutil -> jwtparser : parseClaimsJws(String token) : Claims
activate jwtparser

jwtparser --> jwtutil : Claims
deactivate jwtparser

jwtutil -> jwtutil : validateClaims(claims : Claims) : boolean

jwtutil --> authservice: boolean
deactivate jwtutil

authservice --> authcontroller : boolean
deactivate authservice

authcontroller --> frontend : ReponseEntity
deactivate authcontroller

@enduml
```

<!-- ================================================================================================= -->

## register

```plantuml
@startuml
Boundary FrontEnd as frontend
Control AuthController as authcontroller
Control AuthService as authservice
Control UserRepository as userrepository

frontend -> authcontroller : POST /auth/register : ResponseEntity
activate authcontroller

authcontroller -> authservice : register(authAttemptDTO: AuthAttemptDTO) : User
activate authservice

authservice -> userrepository : findUserByUsername(username : String) : Opt<User>
activate userrepository

userrepository --> authservice : Opt<User>

alt Opt<User> is present
	authservice --> authcontroller : Exception

else Opt<User> is empty
	alt password is invalid
		authservice --> authcontroller : Exception

	else password is valid
		authservice -> userrepository : save(user : User) : User

		userrepository --> authservice : User
		deactivate userrepository

		authservice --> authcontroller : User
		deactivate authservice
	end
end

authcontroller --> frontend : ResponseEntity
deactivate authcontroller

@enduml
```

<!-- ============================================================ -->

## login

```plantuml
@startuml
Boundary FrontEnd as frontend
Control AuthController as authcontroller
Control AuthService as authservice
Control UserRepository as userrepository
Control AuthenticationManager as authenticationmanager
Control JwtUtil as jwtutil

frontend -> authcontroller : POST /auth/login : ResponseEntity
activate authcontroller

authcontroller -> authservice : login(authAttemptDTO : AuthAttemptDTO) : AuthTokenDTO
activate authservice

authservice -> userrepository : findUserByUsername(username : String) : Opt<User>
activate userrepository

userrepository --> authservice : Opt<User>
deactivate userrepository

alt Opt<User> is empty
	authservice --> authcontroller : Exception

else Opt<User> is present
	alt	password is incorrect
		authservice --> authcontroller : Exception

	else password is correct
		authservice -> authenticationmanager : authenticate(authentication : Authentication) : Authentication
		activate authenticationmanager

		authenticationmanager --> authservice : Authentication
		deactivate authenticationmanager

		authservice -> jwtutil : createToken(user : User, role : String) : String
		activate jwtutil

		jwtutil --> authservice : String
		deactivate jwtutil

		authservice --> authcontroller : AuthTokenDTO
		deactivate authservice
	end
end

authcontroller --> frontend : ResponseEntity
deactivate authcontroller

@enduml
```
