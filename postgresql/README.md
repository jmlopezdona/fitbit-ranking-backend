docker build -t eg_postgresql .

docker run --rm -p 5432:5432 --name pg_test eg_postgresql
