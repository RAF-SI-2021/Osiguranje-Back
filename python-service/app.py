from cmath import isnan
from flask import Flask, request, jsonify, make_response
from flask_restful import Resource, Api
from yahoo_fin import options
import pandas as pd
import yahoo_fin.stock_info as si
import exchange_calendars as xcals


app = Flask(__name__)
api = Api(app)


@app.route("/stock", methods=["POST"])
def stock_price_data():
    data = request.get_json()
    result = None
    try:
        if 'start_date' in data and 'end_date' in data:
            result = si.get_data(data['name'], start_date=data['start_date'], end_date=data['end_date'])
        else:
            result = si.get_data(data['name'])
    except Exception as e:
        print(e)
        return jsonify({'message' : f'invalid arguments {data.keys()}'}), 500
    return result.to_json(orient="index", date_format='iso')


@app.route("/stock/quote", methods=["POST"])
def stock_live_quote_data():
    data = request.get_json()
    return jsonify(si.get_quote_table(data["name"]))


@app.route("/stock/dow", methods=["POST"])
def stock_dow():
    return jsonify(si.tickers_dow())


@app.route("/stock/nasdaq", methods=["POST"])
def stock_nasdaq():
    return jsonify(si.tickers_nasdaq())


@app.route("/calendar/live", methods=["POST"])
def is_calendar_live():
    data = request.get_json()
    print(type(data), data)
    try:
        calendar = xcals.get_calendar(data["calendar"])
    except:
        return jsonify({'message': f'Calendar does\'t exist {data["calendar"]}'}), 500
    return jsonify(calendar.is_trading_minute(data["time"]))


@app.route("/calendar/next", methods=["POST"])
def next_session():
    data = request.get_json()
    try:
        calendar = xcals.get_calendar(data["calendar"])
    except:
        return jsonify({'message': f'Calendar does\'t exist {data["calendar"]}'}), 500
    return jsonify(calendar.date_to_session(data["date"], direction="next"))


@app.route("/future/all", methods=["POST"])
def get_all_futures():
    dic = si.get_futures().fillna('').T.to_dict()
    return jsonify(list(dic.values()))


@app.route("/future", methods=["POST"])
def get_sym_futures():
    data = request.get_json()
    return jsonify(si.get_quote_data(data["symbol"]))


@app.route("/options/calls", methods=["POST"])
def get_calls_options():
    symbol = request.args.get('symbol')
    try:
        chain = options.get_options_chain(symbol)
        return chain['calls'].to_json(orient="table", date_format='iso')
    except Exception as e:
        print(e)
        return f'Unknown symbol {symbol} for options calls', 500


@app.route("/options/puts", methods=["POST"])
def get_puts_options():
    symbol = request.args.get('symbol')
    try:
        chain = options.get_options_chain(symbol)
        return chain['puts'].to_json(orient="table", date_format='iso')
    except Exception as e:
        print(e)
        return f'Unknown symbol {symbol} for options puts', 500


@app.route("/options/quote", methods=["POST"])
def get_quote_options():
    symbol = request.args.get('symbol')
    try:
        return si.get_quote_data(symbol)
    except Exception as e:
        print(e)
        return f'Unknown symbol {symbol} for options price', 500


if __name__ == '__main__':
    address = 'localhost'
    pport = 9999
    print(f"Running on {address}:{pport}")
    app.run(host=address, port=pport)


