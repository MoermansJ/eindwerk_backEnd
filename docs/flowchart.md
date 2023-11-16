# Flowchart diagrams

<!-- ================================================================================================= -->

## validateToken

```plantuml
@startuml
start

:Token gets sent to the backend;
:Parse claims;
if (Evaluate token expiration) then (expired)
:return false;
stop
else(not expired)
:return true;
stop
@enduml
```

<!-- ================================================================================================= -->

## register

```plantuml
@startuml
start

:register attempt;
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

:log in attempt;
if (username + password validation) then (fail)
:throw exception;
stop
else(pass)

:Create & return token;

stop
@enduml
```
