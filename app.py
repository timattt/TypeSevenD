from flask import Flask
import json
import os
import psycopg2

app = Flask(__name__)

@app.route('/task')
def index():
    print("Test endpoint!")
    conn = psycopg2.connect(host=os.environ['TYPE6_DATABASE_URL'], port=os.environ['TYPE6_DATABASE_PORT'], sslmode=verify-full, dbname="type-6-database", user=os.environ['TYPE6_DATABASE_USER'], password=os.environ['TYPE6_DATABASE_PASSWORD'], target_session_attrs=read-write)
    cursor = conn.cursor()
    cursor.execute('select * from type6user')
    res = cursor.fetchall()
    cursor.close()
    json_data = []
    row_headers = [x[0] for x in cursor.description]
    for result in res:
        json_data.append(dict(zip(row_headers, result)))
    return json.dumps(json_data)


app.run(host='0.0.0.0', port=8000)
