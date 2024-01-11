from flask import Flask
import json
import os

app = Flask(__name__)

@app.route('/task')
def index():
    print("Test endpoint!")
    return json.dumps({"status": "OK"})

app.run(host='0.0.0.0', port=8000)
