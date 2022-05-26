# syntax=docker/dockerfile:1
FROM python:3.8.13-slim-bullseye
LABEL org.opencontainers.image.source https://github.com/RAF-SI-2021/Osiguranje-Back

WORKDIR /app
COPY req.txt requirements.txt
RUN pip3 install --no-cache-dir -r requirements.txt

COPY app.py ./app.py
CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]
