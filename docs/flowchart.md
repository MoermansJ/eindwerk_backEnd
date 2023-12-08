# Flowchart diagrams

<!-- ================================================================================================= -->

<!-- ## validateToken

```plantuml
@startuml
start

:Parse token claims;
if (Evaluate token expiration) then (expired)
:return false;
stop
else(not expired)
:return true;
stop
@enduml
``` -->

<!-- ================================================================================================= -->

## register

```plantuml
@startuml
start

if (username + password validation) then (fail)
:throw exception;
stop
else(pass)

:Hash password + add user to DB;

stop
@enduml
```

<!-- ================================================================================================= -->

## log in

```plantuml
@startuml
start

if (username + password validation) then (fail)
:throw exception;
stop
else(pass)

:Create & return token;

stop
@enduml
```

<!-- ================================================================================================= -->

## view profile

```plantuml
@startuml
start

:view profile info (highscore, ...);

stop
@enduml
```

<!-- ================================================================================================= -->

## view leaderboard

```plantuml
@startuml
start

:view top10 highest scores;

stop
@enduml
```

<!-- ================================================================================================= -->

## play game

```plantuml
@startuml
start

:play game;

stop
@enduml
```
