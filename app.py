from flask import Flask
import json
import os
import psycopg2
from flask import request

app = Flask(__name__)

@app.route('/task')
def index():
    id = request.args.get('id')
    app.logger.debug("Task with id: {}".format(id))
    conn = psycopg2.connect(host=os.environ['TYPE6_DATABASE_URL'], port=os.environ['TYPE6_DATABASE_PORT'], sslmode="verify-full", dbname="type-6-database", user=os.environ['TYPE6_DATABASE_USER'], password=os.environ['TYPE6_DATABASE_PASSWORD'])
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
