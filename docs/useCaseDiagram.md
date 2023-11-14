```plantuml
@startuml

' actors
:visitor: as visitor

' functionalities
(register) as register
(login) as login

' relations
visitor --> register
visitor --> login

@enduml
```
