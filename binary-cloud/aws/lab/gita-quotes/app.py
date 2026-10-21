"""Gita Quotes API: a tiny Flask + MySQL app for the single-instance EC2 labs.

Routes (all under /api so the web server can forward /api/* unchanged):
  GET  /api/health   -> {"status": "ok", "db": "ok" | "<error>"}
  GET  /api/quotes   -> list of quotes, newest first
  POST /api/quotes   -> add a quote: {"verse": "2.47", "text": "...", "meaning": "..."}

GET / serves static/index.html, so the app can also be tried alone (python app.py)
without nginx or IIS in front.

Database settings come from environment variables, never from code:
  DB_HOST (default 127.0.0.1), DB_PORT (3306), DB_NAME (gita), DB_USER (gita_app), DB_PASSWORD
"""

import os

import pymysql
from flask import Flask, jsonify, request, send_from_directory

STATIC_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "static")

app = Flask(__name__)


def get_conn():
    return pymysql.connect(
        host=os.environ.get("DB_HOST", "127.0.0.1"),
        port=int(os.environ.get("DB_PORT", "3306")),
        user=os.environ.get("DB_USER", "gita_app"),
        password=os.environ.get("DB_PASSWORD", ""),
        database=os.environ.get("DB_NAME", "gita"),
        cursorclass=pymysql.cursors.DictCursor,
        connect_timeout=5,
    )


@app.get("/")
def index():
    return send_from_directory(STATIC_DIR, "index.html")


@app.get("/api/health")
def health():
    try:
        conn = get_conn()
        with conn.cursor() as cur:
            cur.execute("SELECT 1")
        conn.close()
        return jsonify(status="ok", db="ok")
    except Exception as e:  # report the DB problem instead of crashing: this is the first thing to check
        return jsonify(status="ok", db=str(e)), 503


@app.get("/api/quotes")
def list_quotes():
    conn = get_conn()
    try:
        with conn.cursor() as cur:
            cur.execute(
                "SELECT id, verse, text, meaning, created_at FROM quotes ORDER BY id DESC"
            )
            rows = cur.fetchall()
    finally:
        conn.close()
    for row in rows:
        row["created_at"] = row["created_at"].isoformat()
    return jsonify(rows)


@app.post("/api/quotes")
def add_quote():
    body = request.get_json(silent=True) or {}
    verse = (body.get("verse") or "").strip()
    text = (body.get("text") or "").strip()
    meaning = (body.get("meaning") or "").strip()
    if not verse or not text:
        return jsonify(error="verse and text are required"), 400
    if len(verse) > 20 or len(text) > 1000 or len(meaning) > 1000:
        return jsonify(error="too long"), 400

    conn = get_conn()
    try:
        with conn.cursor() as cur:
            # Parameterised query: the driver escapes values, so user input cannot inject SQL
            cur.execute(
                "INSERT INTO quotes (verse, text, meaning) VALUES (%s, %s, %s)",
                (verse, text, meaning),
            )
            new_id = cur.lastrowid
        conn.commit()
    finally:
        conn.close()
    return jsonify(id=new_id, verse=verse, text=text, meaning=meaning), 201


if __name__ == "__main__":
    # Local try-out only. On the server, gunicorn (Linux) or waitress (Windows) runs the app.
    app.run(host="127.0.0.1", port=5000, debug=False)
