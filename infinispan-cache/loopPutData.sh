for i in {0..6}
do
  http "localhost:8080/weather/London?days=$i"
done

for i in {0..5}
do
  http "localhost:8080/weather/Paris?days=$i"
done

for i in {0..4}
do
  http "localhost:8080/weather/Berlin?days=$i"
done

for i in {0..3}
do
  http "localhost:8080/weather?city=Madrid&days=$i"
done