for i in {1..10}
do
  http "localhost:8080/weather?city=London$i&days=1"
done