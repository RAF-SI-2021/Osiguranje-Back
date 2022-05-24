# syntax=docker/dockerfile:1

FROM python:3.8.13-slim-bullseye
LABEL org.opencontainers.image.source https://github.com/RAF-SI-2021/Osiguranje-Back


WORKDIR /app

COPY req.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY . .
CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]

# CMD ["python3","app.py"]