http  localhost:8080/increments
http post localhost:8080/increments key="e1" value=23
http post localhost:8080/increments key="e2" value=18
http  localhost:8080/increments
http  localhost:8080/increments/e2
http put "localhost:8080/increments/e2?value=2"
http delete localhost:8080/increments/e1
http  localhost:8080/increments