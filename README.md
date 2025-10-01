In IntelliJ set .env:

run->Edit configurations->env variables-> "fronted/.env"

Open Run → Edit Configurations…
Select your BackendApplication run config.
In the field VM options, add: (modify options, if no vm options showing)
-Dspring.profiles.active=dev