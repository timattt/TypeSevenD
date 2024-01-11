FROM spittet/python-mysql:latest
LABEL authors="timat"

WORKDIR /app

COPY ./app.py /app/app.py

RUN pip install flask
RUN pip install psycopg2

EXPOSE 8000

ENTRYPOINT ["python", "app.py"]
